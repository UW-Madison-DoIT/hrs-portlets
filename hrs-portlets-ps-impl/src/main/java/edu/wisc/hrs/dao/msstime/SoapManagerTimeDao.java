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
package edu.wisc.hrs.dao.msstime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;

import edu.wisc.hr.dao.msstime.ManagerTimeDao;
import edu.wisc.hr.dm.msstime.ManagedTime;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.msstime.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.msstime.req.GetCompIntfcUWPORTAL1MSSTIME;
import edu.wisc.hrs.xdm.msstime.res.GetCompIntfcUWPORTAL1MSSTIMEResponse;
import edu.wisc.hrs.xdm.msstime.res.UwTlEmpDtlVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 */
@Repository("soapManagerTimeDao")
public class SoapManagerTimeDao extends BaseHrsSoapDao implements ManagerTimeDao {
    private WebServiceOperations webServiceOperations;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("managerTimeWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
	
    @Override
	@Cacheable(cacheName="managedTimes", exceptionCacheName="hrsUnknownExceptionCache")
    public List<ManagedTime> getManagedTimes(String emplId) {
	    final GetCompIntfcUWPORTAL1MSSTIME request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1MSSTIMEResponse response = this.internalInvoke(request);
	    
	    return this.convertManagedTimes(response);
    }
    
    @Override
    @TriggersRemove(cacheName="managedTimes")
    public void refreshManagedTimes(String emplId) {
        //Noop
    }

    protected GetCompIntfcUWPORTAL1MSSTIME createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1MSSTIME request = new GetCompIntfcUWPORTAL1MSSTIME();
	    request.setEmplid(value);

	    return request;
    }

    protected List<ManagedTime> convertManagedTimes(final GetCompIntfcUWPORTAL1MSSTIMEResponse response) {
        if (response == null) {
            return Collections.emptyList();
        }
        
        final List<UwTlEmpDtlVwTypeShape> uwAmMssAbsVws = response.getUwTlEmpDtlVws();
        
        final List<ManagedTime> managedTimes = new ArrayList<ManagedTime>(uwAmMssAbsVws.size());
        
        for (final UwTlEmpDtlVwTypeShape uwAmMssAbsVwTypeShape : uwAmMssAbsVws) {
            final ManagedTime managedTime = new ManagedTime();
            
            managedTime.setName((String)HrsUtils.getValue(uwAmMssAbsVwTypeShape.getName()));
            managedTime.setStatus((String)HrsUtils.getValue(uwAmMssAbsVwTypeShape.getXlatLongName()));
            
            managedTimes.add(managedTime);
        }
        
        return managedTimes;
    }
}
