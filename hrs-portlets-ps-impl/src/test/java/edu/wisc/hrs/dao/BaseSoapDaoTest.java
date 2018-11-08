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

package edu.wisc.hrs.dao;

import static org.mockito.Matchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.WebServiceMessageSender;

/**
 * Base class for SOAP dao tests (integration and mock)
 * 
 * @author Eric Dalquist
 */
public class BaseSoapDaoTest {
    protected WebServiceMessageSender getWebServiceMessageSender() {
        throw new UnsupportedOperationException();
    }

    protected WebServiceMessage setupWebServiceMessageSender() throws IOException {
        final WebServiceMessageSender webServiceMessageSender = this.getWebServiceMessageSender();
        
        final WebServiceConnection webServiceConnection = mock(WebServiceConnection.class);
        when(webServiceMessageSender.supports(any(URI.class))).thenReturn(true);
        when(webServiceMessageSender.createConnection(any(URI.class))).thenReturn(webServiceConnection);
        
        final WebServiceMessage webServiceMessage = mock(WebServiceMessage.class);
        
        when(webServiceConnection.receive(any(WebServiceMessageFactory.class))).thenReturn(webServiceMessage);
        
        return webServiceMessage;
    }
}
