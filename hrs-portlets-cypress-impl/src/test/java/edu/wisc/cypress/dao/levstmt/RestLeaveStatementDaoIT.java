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
package edu.wisc.cypress.dao.levstmt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.wisc.cypress.dm.MockProxyResponse;
import edu.wisc.hr.dao.levstmt.StatementType;
import edu.wisc.hr.dm.levstmt.SummarizedLeaveStatement;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/integrationCypressTestContext.xml")
public class RestLeaveStatementDaoIT {
    @Autowired
    private RestLeaveStatementDao client;
    
    @Test
    public void testStatement() throws Exception {
        MockProxyResponse response = new MockProxyResponse();
        client.getLeaveStatement("00282835", "8421736", StatementType.LEAVE, response);
        
        final byte[] content = response.getContentAsByteArray();
        assertNotNull(content);
        assertEquals(4367, content.length);
    }
    
    @Test
    public void testGetStatements() throws Exception {
        final SummarizedLeaveStatement leaveStatements = client.getLeaveStatements("00282835");
        assertNotNull(leaveStatements);
        assertEquals(3, leaveStatements.getLeavePayPeriodReports().size());
    }
    
    @Test
    public void testGetMultipleStatements() throws Exception {
        final SummarizedLeaveStatement leaveStatements = client.getLeaveStatements("12345678");
        assertNotNull(leaveStatements);
        assertEquals(8, leaveStatements.getLeavePayPeriodReports().size());
    }
    
    @Test
    public void testNoStatements() throws Exception {
        final SummarizedLeaveStatement leaveStatements = client.getLeaveStatements("00000000");
        assertNotNull(leaveStatements);
        assertEquals(0, leaveStatements.getLeavePayPeriodReports().size());
    }
    
    @Test
    public void testBadEmplId() throws Exception {
        final SummarizedLeaveStatement leaveStatements = client.getLeaveStatements("");
        assertNotNull(leaveStatements);
        assertEquals(0, leaveStatements.getLeavePayPeriodReports().size());
    }
//    
//    @Test
//    public void testSummarizeStatements() throws Exception {
//        final InputStream xmlStream = this.getClass().getResourceAsStream("/sampleData/leave-statements.xml");
//        assertNotNull(xmlStream);
//
//        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        final JAXBElement<LeaveStatements> jaxbElement = unmarshaller.unmarshal(new StreamSource(xmlStream), LeaveStatements.class);
//        final LeaveStatements response = jaxbElement.getValue();
//
//        final Collection<SummarizedLeaveStatement> leaveStatements = client.summarizeLeaveStatements(response);
//        assertNotNull(leaveStatements);
//        assertEquals(4, leaveStatements.size());
//    }
//    
//    @Test
//    public void testSummarizeMultipleStatements() throws Exception {
//        final InputStream xmlStream = this.getClass().getResourceAsStream("/sampleData/leave-multi-statements.xml");
//        assertNotNull(xmlStream);
//
//        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        final JAXBElement<LeaveStatements> jaxbElement = unmarshaller.unmarshal(new StreamSource(xmlStream), LeaveStatements.class);
//        final LeaveStatements response = jaxbElement.getValue();
//
//        final Collection<SummarizedLeaveStatement> leaveStatements = client.summarizeLeaveStatements(response);
//        assertNotNull(leaveStatements);
//        assertEquals(9, leaveStatements.size());
//    }
}
