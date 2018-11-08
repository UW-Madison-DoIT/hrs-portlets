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
package edu.wisc.hrs.dao.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Map;

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

import edu.wisc.hr.dm.person.HomeAddress;
import edu.wisc.hr.dm.person.Job;
import edu.wisc.hr.dm.person.OfficeAddress;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hrs.xdm.person.res.GetCompIntfcUWPORTAL1PERSONResponse;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockPeopleSoftTestContext.xml")
public class SoapContactInfoDaoTest extends SoapContactInfoDaoIT {
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
    public void testGetPersonInformation() throws Exception {
        final WebServiceMessage webServiceMessage = setupWebServiceMessageSender();
        
        when(webServiceMessage.getPayloadSource())
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/person.xml")));

        super.testGetPersonInformation();
    }
    
    @Test
    public void testGetNullPersonInformation() throws Exception {
        setupWebServiceMessageSender();
        
        final PersonInformation personalData = client.getPersonalData("00481400");
        assertNull(personalData);
    }
    
    @Override
    public void testGetMissingPersonInformation() throws Exception {
        throw new WebServiceFaultException("CANT FIGURE OUT HOW TO MOCK A FAULT");
    }
   
    @Test
    public void testDataMapping() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/person.xml");
        assertNotNull(xmlStream);
        
        final GetCompIntfcUWPORTAL1PERSONResponse response = (GetCompIntfcUWPORTAL1PERSONResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final PersonInformation contactInformation = client.mapPerson(response);
        verifyMappedData(contactInformation);
    }
   
    @Test
    public void testDataMapping2() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/person2.xml");
        assertNotNull(xmlStream);
        
        final GetCompIntfcUWPORTAL1PERSONResponse response = (GetCompIntfcUWPORTAL1PERSONResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final PersonInformation contactInformation = client.mapPerson(response);
        assertNotNull(contactInformation);

        assertEquals("JANE SMITH", contactInformation.getName());
        final Map<Integer, Job> jobs = contactInformation.getJobMap();
        assertNotNull(jobs);
        assertEquals(2, jobs.size());
        assertEquals("GEOGRAPHY", jobs.get(0).getDepartmentName());
        assertEquals("ASSOCIATE PROFESSOR", jobs.get(0).getTitle());
        assertEquals("WSH/DEANS OFFICE", jobs.get(1).getDepartmentName());
        assertEquals("CAMPUS DEAN/UWC", jobs.get(1).getTitle());
        
        final Job primaryJob = contactInformation.getPrimaryJob();
        assertNotNull(primaryJob);
        assertEquals(1, primaryJob.getId());
        assertEquals("WSH/DEANS OFFICE", primaryJob.getDepartmentName());
        assertEquals("CAMPUS DEAN/UWC", primaryJob.getTitle());
        
        
        assertEquals(false, contactInformation.isOnVisa());
        assertEquals(false, contactInformation.isMadisonEmpl());

        final HomeAddress homeAddress = contactInformation.getHomeAddress();
        assertNotNull(homeAddress);
        
        final OfficeAddress officeAddress = contactInformation.getOfficeAddress();
        assertNotNull(officeAddress);

        final String email = contactInformation.getEmail();
        assertEquals("NOTACTIVE@DONOTREPLY.COM", email);
    }
    
    @Override
    protected void verifyMappedData(final PersonInformation contactInformation) {
        assertNotNull(contactInformation);

        assertEquals("JOHN DOE", contactInformation.getName());
        final Map<Integer, Job> jobs = contactInformation.getJobMap();
        assertNotNull(jobs);
        assertEquals(1, jobs.size());
        assertEquals("DOIT/NTRPRZ NET/IIAT", jobs.get(0).getDepartmentName());
        
        assertEquals(false, contactInformation.isOnVisa());
        assertEquals(true, contactInformation.isMadisonEmpl());

        final HomeAddress homeAddress = contactInformation.getHomeAddress();
        assertNotNull(homeAddress);
        
        final OfficeAddress officeAddress = contactInformation.getOfficeAddress();
        assertNotNull(officeAddress);

        final String email = contactInformation.getEmail();
        assertEquals("NOTACTIVE@DONOTREPLY.COM", email);
    }
}

