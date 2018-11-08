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
package edu.wisc.hrs.dao.roles;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Set;

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

import edu.wisc.hrs.xdm.roles.res.GetCompIntfcUWPORTAL1ROLESResponse;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockPeopleSoftTestContext.xml")
public class SoapHrsRolesDaoTest extends SoapHrsRolesDaoIT {
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
    public void testGetRoles() throws Exception {
        final WebServiceMessage webServiceMessage = setupWebServiceMessageSender();
        
        when(webServiceMessage.getPayloadSource())
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/roles.xml")));

        super.testGetRoles();
    }
    
    @Test
    public void testGetNullRoles() throws Exception {
        setupWebServiceMessageSender();
        
        final Set<String> hrsRoles = client.getHrsRoles("00481400");
        assertNotNull(hrsRoles);
        assertEquals(0, hrsRoles.size());
    }

    @Override
    public void testGetMissingRoles() throws Exception {
        throw new WebServiceFaultException("CANT FIGURE OUT HOW TO MOCK A FAULT");
    }
   
    @Test
    public void testDataMapping() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/roles.xml");
        assertNotNull(xmlStream);
        
        final GetCompIntfcUWPORTAL1ROLESResponse response = (GetCompIntfcUWPORTAL1ROLESResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final Set<String> roles = client.convertRoles(response);
        verifyMappedData(roles);
    }
}
