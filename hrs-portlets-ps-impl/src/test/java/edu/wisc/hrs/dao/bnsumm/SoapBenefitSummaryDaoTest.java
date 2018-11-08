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

import static junit.framework.Assert.assertNotNull;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNull;

import java.io.InputStream;

import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceFaultException;
import org.springframework.ws.transport.WebServiceMessageSender;

import edu.wisc.hr.dm.bnsumm.BenefitSummary;
import edu.wisc.hrs.xdm.bnsumm.res.GetCompIntfcUWPORTAL1BNSUMMResponse;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockPeopleSoftTestContext.xml")
public class SoapBenefitSummaryDaoTest extends SoapBenefitSummaryDaoIT {
    @Autowired
    private WebServiceMessageSender webServiceMessageSender;
    @Autowired
    private Unmarshaller unmarshaller;
    
    @Override
    protected WebServiceMessageSender getWebServiceMessageSender() {
        return this.webServiceMessageSender;
    }
    
    @Before
    public void setup() {
        reset(this.webServiceMessageSender);
    }
    
    @Test
    @Override
    public void testGetBenefitSummary() throws Exception {
        final WebServiceMessage webServiceMessage = setupWebServiceMessageSender();
        
        when(webServiceMessage.getPayloadSource())
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/bnsumm.xml")));

        super.testGetBenefitSummary();
    }
    
    @Test
    public void testGetNullBenefitSummary() throws Exception {
        setupWebServiceMessageSender();

        final BenefitSummary benefitSummary = client.getBenefitSummary("00481400");
        assertNull(benefitSummary);
    }
    
    //PS is returning HTTP 200 for faults right now so can't test this
    @Override
    public void testGetMissingInfo() {
        throw new WebServiceFaultException("CANT FIGURE OUT HOW TO MOCK A FAULT");
    }
   
    @Test
    public void testDataMapping() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/bnsumm.xml");
        assertNotNull(xmlStream);

        final GetCompIntfcUWPORTAL1BNSUMMResponse response = (GetCompIntfcUWPORTAL1BNSUMMResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final BenefitSummary benefitSummary = client.convertBenefitSummary(response);
        verifyMappedData(benefitSummary);
    }
}
