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
package edu.wisc.hrs.dao.tlpayable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dao.tlpayable.TimeSheetDao;
import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hr.dm.tlpayable.TimeSheet;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.tlpaybl.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.tlpaybl.req.GetCompIntfcUWPORTAL1TLPAYBL;
import edu.wisc.hrs.xdm.tlpaybl.res.GetCompIntfcUWPORTAL1TLPAYBLResponse;
import edu.wisc.hrs.xdm.tlpaybl.res.UwTlPayablVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 */
@Repository("soapTimeLeavePayableDao")
public class SoapTimeLeavePayableDao extends BaseHrsSoapDao implements TimeSheetDao {
    private static final class TimeSheetComparator implements Comparator<TimeSheet> {
        public static final TimeSheetComparator INSTANCE = new TimeSheetComparator();
        
        @Override
        public int compare(TimeSheet o1, TimeSheet o2) {
            final DateMidnight d1 = o1.getDate();
            final DateMidnight d2 = o2.getDate();
            if (d1 == d2) {
                return 0;
            }
            if (d1 == null) {
                return -1;
            }
            return d1.compareTo(d2) * -1;
        }
    }

    private WebServiceOperations webServiceOperations;
    private ContactInfoDao contactInfoDao;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("timeLeavePayableWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Autowired
    public void setContactInfoDao(ContactInfoDao contactInfoDao) {
        this.contactInfoDao = contactInfoDao;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
	
    @Override
	@Cacheable(cacheName="timeSheets", exceptionCacheName="hrsUnknownExceptionCache")
    public List<TimeSheet> getTimeSheets(String emplId) {
	    final GetCompIntfcUWPORTAL1TLPAYBL request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1TLPAYBLResponse response = this.internalInvoke(request);
        
        final PersonInformation personalData = this.contactInfoDao.getPersonalData(emplId);
        final Map<Integer, Job> jobs;
        if (personalData == null) {
            jobs = Collections.emptyMap();
        }
        else {
            jobs = personalData.getJobMap();
        }
	    
	    return this.convertTimeSheets(response, jobs);
    }

    protected GetCompIntfcUWPORTAL1TLPAYBL createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1TLPAYBL request = new GetCompIntfcUWPORTAL1TLPAYBL();
	    request.setEmplid(value);

	    return request;
    }

    protected List<TimeSheet> convertTimeSheets(GetCompIntfcUWPORTAL1TLPAYBLResponse response, Map<Integer, Job> jobs) {
        if (response == null) {
            return Collections.emptyList();
        }
        
        final List<UwTlPayablVwTypeShape> uwTlPayablVws = response.getUwTlPayablVws();
        
        final List<TimeSheet> timeSheetList = new ArrayList<TimeSheet>(uwTlPayablVws.size());
        
        for (final UwTlPayablVwTypeShape uwTlPayablVwTypeShape : uwTlPayablVws) {
            final TimeSheet timeSheet = new TimeSheet();
            
            final DateMidnight date = HrsUtils.getValue(uwTlPayablVwTypeShape.getDur());
            timeSheet.setDate(date);
            
            timeSheet.setStatus((String)HrsUtils.getValue(uwTlPayablVwTypeShape.getXlatLongName()));
            timeSheet.setTotal((BigDecimal)HrsUtils.getValue(uwTlPayablVwTypeShape.getTlQuantity()));
            timeSheet.setType((String)HrsUtils.getValue(uwTlPayablVwTypeShape.getDescr()));
            
            final Integer jobId = HrsUtils.getValue(uwTlPayablVwTypeShape.getEmplRcd());
            final Job job = jobs.get(jobId);
            timeSheet.setJob(job);
            
            timeSheetList.add(timeSheet);
        }
        
        Collections.sort(timeSheetList, TimeSheetComparator.INSTANCE);
        
        return timeSheetList;
    }
}
