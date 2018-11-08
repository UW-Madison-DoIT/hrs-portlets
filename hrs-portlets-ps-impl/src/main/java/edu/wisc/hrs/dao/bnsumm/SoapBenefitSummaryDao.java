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
package edu.wisc.hrs.dao.bnsumm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;

import edu.wisc.hr.dao.bnsumm.BenefitSummaryDao;
import edu.wisc.hr.dm.bnsumm.Benefit;
import edu.wisc.hr.dm.bnsumm.BenefitSummary;
import edu.wisc.hr.dm.bnsumm.Dependent;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.bnsumm.req.EmplidTypeShape;
import edu.wisc.hrs.xdm.bnsumm.req.GetCompIntfcUWPORTAL1BNSUMM;
import edu.wisc.hrs.xdm.bnsumm.res.GetCompIntfcUWPORTAL1BNSUMMResponse;
import edu.wisc.hrs.xdm.bnsumm.res.UwBnBnSmryVwTypeShape;
import edu.wisc.hrs.xdm.bnsumm.res.UwBnDpndntVwTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 */
@Repository("soapBenefitSummaryDao")
public class SoapBenefitSummaryDao extends BaseHrsSoapDao implements BenefitSummaryDao {
    private WebServiceOperations webServiceOperations;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("benefitSummaryWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
	
    @Override
	@Cacheable(cacheName="benefitSummary", decoratedCacheType=DecoratedCacheType.SELF_POPULATING_CACHE, selfPopulatingTimeout=20000, exceptionCacheName="hrsUnknownExceptionCache")
    public BenefitSummary getBenefitSummary(String emplId) {
	    final GetCompIntfcUWPORTAL1BNSUMM request = this.createRequest(emplId);
	    
	    final GetCompIntfcUWPORTAL1BNSUMMResponse response = this.internalInvoke(request);
	    
	    return this.convertBenefitSummary(response);
    }

    protected GetCompIntfcUWPORTAL1BNSUMM createRequest(String emplId) {
        EmplidTypeShape value = HrsUtils.createValue(EmplidTypeShape.class, emplId);
	    
	    final GetCompIntfcUWPORTAL1BNSUMM request = new GetCompIntfcUWPORTAL1BNSUMM();
	    request.setEmplid(value);

	    return request;
    }

    protected BenefitSummary convertBenefitSummary(final GetCompIntfcUWPORTAL1BNSUMMResponse response) {
        if (response == null) {
            return null;
        }
        
        final BenefitSummary benefitSummary = new BenefitSummary();
        
        benefitSummary.setEnrollmentFlag((String)HrsUtils.getValue(response.getFlag()));
        
        final List<Benefit> benefits = benefitSummary.getBenefits();
        this.convertBenefits(response, benefits);
        
        final List<Dependent> dependents = benefitSummary.getDependents();
        this.convertDependents(response, dependents);
        
        return benefitSummary;
    }

    protected void convertBenefits(final GetCompIntfcUWPORTAL1BNSUMMResponse response, List<Benefit> benefits) {
        final List<UwBnBnSmryVwTypeShape> uwBnBnSmryVws = response.getUwBnBnSmryVws();
        for (final UwBnBnSmryVwTypeShape uwBnBnSmryVwTypeShape : uwBnBnSmryVws) {
            final Benefit benefit = new Benefit();
            
            final String name = HrsUtils.getValue(uwBnBnSmryVwTypeShape.getXlatLongName());
            benefit.setName(name);
            
            final String coverage = HrsUtils.getValue(uwBnBnSmryVwTypeShape.getDescr3());
            benefit.setCoverage(coverage);
            
            benefits.add(benefit);
        }
    }

    protected void convertDependents(final GetCompIntfcUWPORTAL1BNSUMMResponse response, List<Dependent> dependents) {
        final List<UwBnDpndntVwTypeShape> uwBnDpndntVws = response.getUwBnDpndntVws();
        for (final UwBnDpndntVwTypeShape uwBnBnSmryVwTypeShape : uwBnDpndntVws) {
            final Dependent dependent = new Dependent();
            
            final String name = HrsUtils.getValue(uwBnBnSmryVwTypeShape.getName());
            dependent.setName(name);
            
            final String relationship = HrsUtils.getValue(uwBnBnSmryVwTypeShape.getXlatLongName1());
            dependent.setRelationship(relationship);

            logger.trace("Noted dependent [" + name 
              + "] with relationship [" + relationship + "]");

            dependents.add(dependent);
        }
    }
}
