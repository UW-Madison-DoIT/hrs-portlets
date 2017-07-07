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
package edu.wisc.hrs.dao.absbal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.hr.dao.absbal.AbsenceBalanceDao;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.absbal.AbsenceBalance;
import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.absbal.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.absbal.req.GetCompIntfcUWPORTAL1ABSBAL;
import edu.wisc.hrs.xdm.absbal.res.GetCompIntfcUWPORTAL1ABSBALResponse;
import edu.wisc.hrs.xdm.absbal.res.UwGpAbsBalVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 * @version $Id: SoapAbsenceBalanceDao.java,v 1.3 2012/08/14 21:18:04 dalquist Exp $
 */
@Repository("soapAbsenceBalanceDao")
public class SoapAbsenceBalanceDao extends BaseHrsSoapDao implements AbsenceBalanceDao {
    private WebServiceOperations webServiceOperations;
    private ContactInfoDao contactInfoDao;

    /**
     * Predicate matching absence balances that are
     * of type ClassifiedFurloughAllocated AND have zero balance.
     */
    protected final Predicate degenerateFurloughPredicate =
        Predicates.and(new ClassifiedFurloughAllocatedPredicate(), new ZeroBalancePredicate());
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("absenceBalanceWebServiceTemplate") WebServiceOperations webServiceOperations) {
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
	@Cacheable(cacheName="absenceBalance", exceptionCacheName="hrsUnknownExceptionCache")
    public List<AbsenceBalance> getAbsenceBalance(String emplId) {
	    final GetCompIntfcUWPORTAL1ABSBAL request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1ABSBALResponse response = this.internalInvoke(request);
        
        final PersonInformation personalData = this.contactInfoDao.getPersonalData(emplId);
        final Map<Integer, Job> jobMap;
        if (personalData == null) {
            jobMap = Collections.emptyMap();
        }
        else {
            jobMap = personalData.getJobMap();
        }
	    
	    return this.convertAbsenceBalance(response, jobMap);
    }

    protected GetCompIntfcUWPORTAL1ABSBAL createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1ABSBAL request = new GetCompIntfcUWPORTAL1ABSBAL();
	    request.setEmplid(value);

	    return request;
    }

    protected List<AbsenceBalance> convertAbsenceBalance(GetCompIntfcUWPORTAL1ABSBALResponse response, Map<Integer, Job> jobs) {
        if (response == null) {
            return Collections.emptyList();
        }
        
        final List<UwGpAbsBalVwTypeShape> uwGpAbsBalVws = response.getUwGpAbsBalVws();

        final List<AbsenceBalance> absenceBalances = new ArrayList<AbsenceBalance>(uwGpAbsBalVws.size());
        
        for (final UwGpAbsBalVwTypeShape uwGpAbsBalVwTypeShape : uwGpAbsBalVws) {
            final AbsenceBalance absenceBalance = new AbsenceBalance();
            
            absenceBalance.setEntitlement((String)HrsUtils.getValue(uwGpAbsBalVwTypeShape.getDescr()));
            absenceBalance.setBalance((BigDecimal)HrsUtils.getValue(uwGpAbsBalVwTypeShape.getCalcRsltVal()));

            final Integer jobId = HrsUtils.getValue(uwGpAbsBalVwTypeShape.getEmplRcd());
            final Job job = jobs.get(jobId);
            absenceBalance.setJob(job);

            // filter out not-currently-relevant furlough leave
            if (! degenerateFurloughPredicate.apply(absenceBalance)) {
                absenceBalances.add(absenceBalance);
            } else {
                logger.trace("Filtered out classified furlough " + absenceBalance + " .");
            }
        }
        
        return absenceBalances;
    }
}
