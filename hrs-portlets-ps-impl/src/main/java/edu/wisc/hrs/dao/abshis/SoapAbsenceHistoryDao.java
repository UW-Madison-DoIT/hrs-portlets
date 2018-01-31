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
package edu.wisc.hrs.dao.abshis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.hr.dao.abshis.AbsenceHistoryDao;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.abshis.AbsenceHistory;
import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.abshis.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.abshis.req.GetCompIntfcUWPORTAL1ABSHIS;
import edu.wisc.hrs.xdm.abshis.res.GetCompIntfcUWPORTAL1ABSHISResponse;
import edu.wisc.hrs.xdm.abshis.res.UwGpAbsHisVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 * @version $Id: SoapAbsenceHistoryDao.java,v 1.3 2012/08/14 21:18:04 dalquist Exp $
 */
@Repository("soapAbsenceHistoryDao")
public class SoapAbsenceHistoryDao extends BaseHrsSoapDao implements AbsenceHistoryDao {
    private WebServiceOperations webServiceOperations;
    private ContactInfoDao contactInfoDao;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("absenceHistoryWebServiceTemplate") WebServiceOperations webServiceOperations) {
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
	@Cacheable(cacheName="absenceHistory", exceptionCacheName="hrsUnknownExceptionCache")
    public List<AbsenceHistory> getAbsenceHistory(String emplId) {
	    final GetCompIntfcUWPORTAL1ABSHIS request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1ABSHISResponse response = this.internalInvoke(request);
	    
	    final PersonInformation personalData = this.contactInfoDao.getPersonalData(emplId);
	    final Map<Integer, Job> jobs;
        if (personalData == null) {
            jobs = Collections.emptyMap();
        }
        else {
            jobs = personalData.getJobMap();
        }
	    
	    return this.convertAbsenceHistory(response, jobs);
    }

    protected GetCompIntfcUWPORTAL1ABSHIS createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1ABSHIS request = new GetCompIntfcUWPORTAL1ABSHIS();
	    request.setEmplid(value);

	    return request;
    }

    protected List<AbsenceHistory> convertAbsenceHistory(GetCompIntfcUWPORTAL1ABSHISResponse response, Map<Integer, Job> jobs) {
        if (response == null) {
            return Collections.emptyList();
        }

        final List<UwGpAbsHisVwTypeShape> uwGpAbsHisVws = response.getUwGpAbsHisVws();

        final List<AbsenceHistory> absenceHistories = new ArrayList<AbsenceHistory>(uwGpAbsHisVws.size());

        for (final UwGpAbsHisVwTypeShape uwGpAbsHisVwTypeShape : uwGpAbsHisVws) {
            final AbsenceHistory absenceBalance = new AbsenceHistory();

            absenceBalance.setName((String)HrsUtils.getValue(uwGpAbsHisVwTypeShape.getDescr30()));
            absenceBalance.setStatus((String)HrsUtils.getValue(uwGpAbsHisVwTypeShape.getXlatLongName()));

            final DateMidnight start = HrsUtils.getValue(uwGpAbsHisVwTypeShape.getOrigBeginDt());
            absenceBalance.setStart(start);

            final DateMidnight end = HrsUtils.getValue(uwGpAbsHisVwTypeShape.getOrigEndDt());
            absenceBalance.setEnd(end);

            absenceBalance.setTotal((BigDecimal)HrsUtils.getValue(uwGpAbsHisVwTypeShape.getDurationAbs()));

            final Integer jobId = HrsUtils.getValue(uwGpAbsHisVwTypeShape.getEmplRcd());
            final Job job = jobs.get(jobId);
            absenceBalance.setJob(job);

            absenceHistories.add(absenceBalance);
        }

        return absenceHistories;
    }
}
