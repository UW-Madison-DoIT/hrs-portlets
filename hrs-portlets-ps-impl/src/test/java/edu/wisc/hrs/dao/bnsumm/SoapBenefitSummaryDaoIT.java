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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.WebServiceFaultException;

import edu.wisc.hr.dm.bnsumm.Benefit;
import edu.wisc.hr.dm.bnsumm.BenefitSummary;
import edu.wisc.hr.dm.bnsumm.Dependent;
import edu.wisc.hrs.dao.BaseSoapDaoTest;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/integrationPeopleSoftTestContext.xml")
public class SoapBenefitSummaryDaoIT extends BaseSoapDaoTest {
    @Autowired
    protected SoapBenefitSummaryDao client;
    
    @Test
    public void testGetBenefitSummary() throws Exception {
        final BenefitSummary benefitSummary = client.getBenefitSummary("00481400");
        
        verifyMappedData(benefitSummary);
    }
    
    @Test(expected=WebServiceFaultException.class)
    public void testGetMissingInfo() throws Exception {
        client.getBenefitSummary("00000000");
    }
    
    protected void verifyMappedData(final BenefitSummary benefitSummary) {
        assertNotNull(benefitSummary);
        
        assertEquals("Z", benefitSummary.getEnrollmentFlag());
        
        final List<Benefit> benefits = benefitSummary.getBenefits();
        assertNotNull(benefits);
        assertEquals(19, benefits.size());
        
        final List<Dependent> dependents = benefitSummary.getDependents();
        assertNotNull(dependents);
        assertEquals(2, dependents.size());
    }
}
