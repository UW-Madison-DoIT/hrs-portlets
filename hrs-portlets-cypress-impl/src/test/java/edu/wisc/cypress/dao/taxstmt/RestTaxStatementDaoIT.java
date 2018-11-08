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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.wisc.cypress.dm.MockProxyResponse;
import edu.wisc.hr.dm.taxstmt.TaxStatement;
import edu.wisc.hr.dm.taxstmt.TaxStatements;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/integrationCypressTestContext.xml")
public class RestTaxStatementDaoIT {
    @Autowired
    private RestTaxStatementDao client;
    
    @Test
    public void testStatement() throws Exception {
        MockProxyResponse response = new MockProxyResponse();
        
        client.getTaxStatement("00282835", "8421960", response);
    }
    
    @Test
    public void testGetStatements() throws Exception {
        final TaxStatements TaxStatements = client.getTaxStatements("00282835");
        assertNotNull(TaxStatements);
        final List<TaxStatement> statements = TaxStatements.getTaxStatements();
        assertNotNull(statements);
        assertEquals(3, statements.size());
    }
    
    @Test
    public void testNoStatements() throws Exception {
        final TaxStatements TaxStatements = client.getTaxStatements("00000000");
        assertNotNull(TaxStatements);
        final List<TaxStatement> statements = TaxStatements.getTaxStatements();
        assertNotNull(statements);
        assertEquals(0, statements.size());
    }
    
    @Test
    public void testBadEmplId() throws Exception {
        final TaxStatements TaxStatements = client.getTaxStatements("");
        assertNotNull(TaxStatements);
        final List<TaxStatement> statements = TaxStatements.getTaxStatements();
        assertNotNull(statements);
        assertEquals(0, statements.size());
    }
}
