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
package edu.wisc.hrs.dao.mssabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

import edu.wisc.hr.dao.mssabs.ManagerAbsenceDao;
import edu.wisc.hr.dm.mssabs.ManagedAbsence;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.mssabs.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.mssabs.req.GetCompIntfcUWPORTAL1MSSABS;
import edu.wisc.hrs.xdm.mssabs.res.GetCompIntfcUWPORTAL1MSSABSResponse;
import edu.wisc.hrs.xdm.mssabs.res.UwAmMssAbsVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 */
@Repository("soapManagerAbsenceDao")
public class SoapManagerAbsenceDao extends BaseHrsSoapDao implements ManagerAbsenceDao {
    private WebServiceOperations webServiceOperations;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("managerAbsenceWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
	
    @Override
	@Cacheable(cacheName="managedAbsences", exceptionCacheName="hrsUnknownExceptionCache")
    public List<ManagedAbsence> getManagedAbsences(String emplId) {
	    final GetCompIntfcUWPORTAL1MSSABS request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1MSSABSResponse response = this.internalInvoke(request);
	    
	    return this.convertManagedAbsences(response);
    }
    
    @Override
    @TriggersRemove(cacheName="managedAbsences")
    public void refreshManagedAbsences(String emplId) {
        //Noop
    }

    protected GetCompIntfcUWPORTAL1MSSABS createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1MSSABS request = new GetCompIntfcUWPORTAL1MSSABS();
	    request.setEmplid(value);

	    return request;
    }

    protected List<ManagedAbsence> convertManagedAbsences(final GetCompIntfcUWPORTAL1MSSABSResponse response) {
        if (response == null) {
            return Collections.emptyList();
        }
        
        final List<UwAmMssAbsVwTypeShape> uwAmMssAbsVws = response.getUwAmMssAbsVws();
        
        final List<ManagedAbsence> managedAbsences = new ArrayList<ManagedAbsence>(uwAmMssAbsVws.size());
        
        for (final UwAmMssAbsVwTypeShape uwAmMssAbsVwTypeShape : uwAmMssAbsVws) {
            final ManagedAbsence managedAbsence = new ManagedAbsence();
            
            managedAbsence.setName((String)HrsUtils.getValue(uwAmMssAbsVwTypeShape.getName()));
            managedAbsence.setStatus((String)HrsUtils.getValue(uwAmMssAbsVwTypeShape.getXlatLongName()));
            
            managedAbsences.add(managedAbsence);
        }
        
        return managedAbsences;
    }
}
