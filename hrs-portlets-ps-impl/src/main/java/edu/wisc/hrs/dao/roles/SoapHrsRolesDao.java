/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.wisc.hrs.dao.roles;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;
import edu.wisc.hr.dao.roles.HrsRolesDao;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.roles.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.roles.req.GetCompIntfcUWPORTAL1ROLES;
import edu.wisc.hrs.xdm.roles.res.GetCompIntfcUWPORTAL1ROLESResponse;
import edu.wisc.hrs.xdm.roles.res.UwHrRolUsrVwTypeShape;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 *
 */
@Repository("soapHrsRolesDao")
public class SoapHrsRolesDao extends BaseHrsSoapDao implements HrsRolesDao {

  private WebServiceOperations webServiceOperations;
  private Map<String, Set<String>> hrsRolesMappings = Collections.emptyMap();

  @Autowired
  public void setWebServiceOperations(
      @Qualifier("rolesWebServiceTemplate") WebServiceOperations webServiceOperations) {
    this.webServiceOperations = webServiceOperations;
  }

  @Resource(name = "hrsRolesMapping")
  public void setHrsRoleMappings(Map<String, Set<String>> hrsRolesMappings) {
    this.hrsRolesMappings = hrsRolesMappings;
  }

  @Override
  public Map<String, Set<String>> getHrsRoleMappings() {
    return this.hrsRolesMappings;
  }

  @Override
  protected WebServiceOperations getWebServiceOperations() {
    return this.webServiceOperations;
  }

  @Override
  @Cacheable(cacheName = "hrsRoles", decoratedCacheType = DecoratedCacheType.SELF_POPULATING_CACHE, selfPopulatingTimeout = 20000, exceptionCacheName = "hrsUnknownExceptionCache")
  public Set<String> getHrsRoles(String emplId) {
    final GetCompIntfcUWPORTAL1ROLES request = this.createRequest(emplId);

    final GetCompIntfcUWPORTAL1ROLESResponse response = this.internalInvoke(request);

    return this.convertRoles(response);
  }

  @Override
  public Set<String> rawHrsRolesForEmplid(String emplId) {

    final GetCompIntfcUWPORTAL1ROLES request = this.createRequest(emplId);

    final GetCompIntfcUWPORTAL1ROLESResponse response = this.internalInvoke(request);

    if (response == null) {
      logger.warn("Converted null roles web service SOAP response to empty role set.");
      return Collections.emptySet();
    }

    final List<UwHrRolUsrVwTypeShape> uwHrRolUsrVws = response.getUwHrRolUsrVws();

    final Set<String> roles = new HashSet<String>(uwHrRolUsrVws.size());

    for (final UwHrRolUsrVwTypeShape uwHrRolUsrVwTypeShape : uwHrRolUsrVws) {
      final String hrsRoleName = (String) HrsUtils.getValue(uwHrRolUsrVwTypeShape.getRoleName());
      roles.add(hrsRoleName);
    }

    return roles;
  }

  protected GetCompIntfcUWPORTAL1ROLES createRequest(String emplId) {
    EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);

    final GetCompIntfcUWPORTAL1ROLES request = new GetCompIntfcUWPORTAL1ROLES();
    request.setEmplid(value);

    return request;
  }

  protected Set<String> convertRoles(final GetCompIntfcUWPORTAL1ROLESResponse response) {
    if (response == null) {
      logger.warn("Converted null roles web service SOAP response to empty role set.");
      return Collections.emptySet();
    }

    final List<UwHrRolUsrVwTypeShape> uwHrRolUsrVws = response.getUwHrRolUsrVws();

    final Set<String> roles = new LinkedHashSet<String>(uwHrRolUsrVws.size());

    for (final UwHrRolUsrVwTypeShape uwHrRolUsrVwTypeShape : uwHrRolUsrVws) {
      final String hrsRoleName = (String) HrsUtils.getValue(uwHrRolUsrVwTypeShape.getRoleName());
      final Set<String> mappedRoleNames = this.hrsRolesMappings.get(hrsRoleName);
      if (mappedRoleNames != null) {
        roles.addAll(mappedRoleNames);
      }
    }

    return roles;
  }
}
