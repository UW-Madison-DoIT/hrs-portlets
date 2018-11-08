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

package edu.wisc.cypress.dao.taxstmt;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Eric Dalquist
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/mockCypressTestContext.xml")
public class RestTaxStatementDaoTest extends RestTaxStatementDaoIT {
    @Autowired
    private HttpClient httpClient;
    
    @Before
    public void setup() {
        Mockito.reset(httpClient);
    }

    @Test
    @Override
    public void testStatement() throws Exception {
        final InputStream taxStatementsStream = this.getClass().getResourceAsStream("/sampleData/tax-statements.xml");
        assertNotNull(taxStatementsStream);
        
        setupHttpClient(taxStatementsStream, "application/octet-stream");
        
        super.testStatement();
    }

    @Test
    @Override
    public void testGetStatements() throws Exception {
        final InputStream taxStatementsStream = this.getClass().getResourceAsStream("/sampleData/tax-statements.xml");
        assertNotNull(taxStatementsStream);
        
        setupHttpClient(taxStatementsStream, "application/xml");
        
        super.testGetStatements();
    }

    @Test
    @Override
    public void testNoStatements() throws Exception {
        final InputStream taxStatementsStream = this.getClass().getResourceAsStream("/sampleData/tax-statements_empty.xml");
        assertNotNull(taxStatementsStream);
        
        setupHttpClient(taxStatementsStream, "application/xml");
        
        super.testNoStatements();
    }

    @Test
    @Override
    public void testBadEmplId() throws Exception {
        final InputStream taxStatementsStream = this.getClass().getResourceAsStream("/sampleData/tax-statements_empty.xml");
        assertNotNull(taxStatementsStream);
        
        setupHttpClient(taxStatementsStream, "application/xml");
        
        super.testBadEmplId();
    }

    protected HttpResponse setupHttpClient(InputStream taxStatementsStream, String contentType) throws IOException,
            ClientProtocolException {
        final HttpResponse httpResponse = mock(HttpResponse.class);
        when(httpClient.execute(any(HttpUriRequest.class), any(HttpContext.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(new BasicStatusLine(HttpVersion.HTTP_1_1, HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase()));
        when(httpResponse.getAllHeaders()).thenReturn(new Header[] {
                new BasicHeader("Content-Type", contentType)
        });
        when(httpResponse.getEntity()).thenReturn(new InputStreamEntity(taxStatementsStream, -1));
        
        return httpResponse;
    }

}
