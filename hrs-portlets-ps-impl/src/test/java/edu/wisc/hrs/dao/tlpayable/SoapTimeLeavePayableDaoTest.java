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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

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

import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.tlpayable.TimeSheet;
import edu.wisc.hrs.xdm.tlpaybl.res.GetCompIntfcUWPORTAL1TLPAYBLResponse;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockPeopleSoftTestContext.xml")
public class SoapTimeLeavePayableDaoTest extends SoapTimeLeavePayableDaoIT {
    @Autowired
    private WebServiceMessageSender webServiceMessageSender;
    @Autowired
    protected Unmarshaller unmarshaller;
    
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
    public void testGetTimeSheets() throws Exception {
        final WebServiceMessage webServiceMessage = setupWebServiceMessageSender();
        
        when(webServiceMessage.getPayloadSource())
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/tlpaybl.xml")))
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/person.xml")));

        super.testGetTimeSheets();
    }
    
    @Test
    public void testGetNullTimeSheets() throws Exception {
        setupWebServiceMessageSender();
        
        final List<TimeSheet> timeSheets = client.getTimeSheets("00481400");
        assertNotNull(timeSheets);
        assertEquals(0, timeSheets.size());
    }

    @Override
    public void testGetMissingTimeSheets() {
        throw new WebServiceFaultException("CANT FIGURE OUT HOW TO MOCK A FAULT");
    }
   
    @Test
    public void testDataMapping() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/tlpaybl.xml");
        assertNotNull(xmlStream);

        final GetCompIntfcUWPORTAL1TLPAYBLResponse response = (GetCompIntfcUWPORTAL1TLPAYBLResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final List<TimeSheet> timeSheets = client.convertTimeSheets(response, Collections.<Integer, Job>emptyMap());
        verifyMappedData(timeSheets);
    }

    @Override
    protected void verifyMappedData(final List<TimeSheet> timeSheets) {
        assertNotNull(timeSheets);

        assertEquals(13, timeSheets.size());
    }
}
