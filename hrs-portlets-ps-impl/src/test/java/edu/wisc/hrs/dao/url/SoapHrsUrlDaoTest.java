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
package edu.wisc.hrs.dao.url;

import static junit.framework.Assert.assertNotNull;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import edu.wisc.hr.dao.roles.HrsRolesDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.transport.WebServiceMessageSender;

import edu.wisc.hrs.xdm.url.res.GetCompIntfcUWPORTAL1URLResponse;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockPeopleSoftTestContext.xml")
public class SoapHrsUrlDaoTest extends SoapHrsUrlDaoIT {
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
    public void testGetUrls() throws Exception {
        final WebServiceMessage webServiceMessage = setupWebServiceMessageSender();
        
        when(webServiceMessage.getPayloadSource())
            .thenReturn(new StreamSource(this.getClass().getResourceAsStream("/hrs/url.xml")));

        super.testGetUrls();
    }
   
    @Test
    public void testDataMapping() throws Exception {
        final InputStream xmlStream = this.getClass().getResourceAsStream("/hrs/url.xml");
        assertNotNull(xmlStream);
        
        final GetCompIntfcUWPORTAL1URLResponse response = (GetCompIntfcUWPORTAL1URLResponse)this.unmarshaller.unmarshal(new StreamSource(xmlStream));
        
        final Map<String, String> urlMap = client.convertUrlMap(response);
        verifyMappedData(urlMap);
    }

    /**
     * Test that the log message computation utility method composes messages as expected.
     */
    @Test
    public void testUrlMapLogMessageComputation() {

        final Map<String, String> someKeyValuePairs = new HashMap<String, String>();
        someKeyValuePairs.put("key1", "value1");

        System.out.println(SoapHrsUrlDao.computeUrlMapLogMessage(someKeyValuePairs));

        final String expected =
                "Retrieved URL map (and updating the URL map cache) with these name : value pairs: \n" +
                "  key1 : value1\n";

        assertEquals(expected, SoapHrsUrlDao.computeUrlMapLogMessage(someKeyValuePairs));

    }
}
