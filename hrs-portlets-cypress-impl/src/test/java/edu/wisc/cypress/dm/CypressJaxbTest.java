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

package edu.wisc.cypress.dm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import edu.wisc.cypress.xdm.benstmt.XmlBenefitStatement;
import edu.wisc.cypress.xdm.benstmt.XmlBenefitStatements;
import edu.wisc.cypress.xdm.ernstmt.XmlEarningStatements;
import edu.wisc.cypress.xdm.levstmt.XmlLeaveStatements;
import edu.wisc.cypress.xdm.sabstmt.XmlSabbaticalReports;
import edu.wisc.cypress.xdm.taxstmt.XmlTaxStatements;

/**
 * @author Eric Dalquist
 */
public class CypressJaxbTest {
    @Test
    public void testBenefitStatementsUnmarshall() throws Exception {
        final XmlBenefitStatements statements = unmarshall("/sampleData/benefit-statements.xml", XmlBenefitStatements.class);
        assertNotNull(statements);
        final List<XmlBenefitStatement> benefitStatements = statements.getBenefitStatements();
        assertNotNull(benefitStatements);
        assertEquals(3, benefitStatements.size());
    }
    
    @Test
    public void testBenefitStatements2Unmarshall() throws Exception {
        final XmlBenefitStatements statements = unmarshall("/sampleData/benefit-statements_2.xml", XmlBenefitStatements.class);
        assertNotNull(statements);
        final List<XmlBenefitStatement> benefitStatements = statements.getBenefitStatements();
        assertNotNull(benefitStatements);
        assertEquals(6, benefitStatements.size());
    }
    
    @Test
    public void testEarningStatementsUnmarshall() throws Exception {
        final XmlEarningStatements statements = unmarshall("/sampleData/earning-statements.xml", XmlEarningStatements.class);
        assertNotNull(statements);
    }
    
    @Test
    public void testLeaveMultiStatementsUnmarshall() throws Exception {
        final XmlLeaveStatements statements = unmarshall("/sampleData/leave-multi-statements.xml", XmlLeaveStatements.class);
        assertNotNull(statements);
    }
    
    @Test
    public void testLeaveStatementsUnmarshall() throws Exception {
        final XmlLeaveStatements statements = unmarshall("/sampleData/leave-statements.xml", XmlLeaveStatements.class);
        assertNotNull(statements);
    }
    
    @Test
    public void testSabbaticalReportsUnmarshall() throws Exception {
        final XmlSabbaticalReports statements = unmarshall("/sampleData/sabbatical-reports.xml", XmlSabbaticalReports.class);
        assertNotNull(statements);
    }
    
    @Test
    public void testTaxStatementsUnmarshall() throws Exception {
        final XmlTaxStatements statements = unmarshall("/sampleData/tax-statements.xml", XmlTaxStatements.class);
        assertNotNull(statements);
    }
    
    private <T> T unmarshall(String resource, Class<T> clazz) throws Exception {
        final JAXBContext context = JAXBContext.newInstance(clazz.getPackage().getName());
        
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        
        final InputStream xmlStream = this.getClass().getResourceAsStream(resource);
        try {
            assertNotNull(xmlStream);
            final JAXBElement<T> statements = unmarshaller.unmarshal(new StreamSource(xmlStream), clazz);
        
            return statements.getValue();
        }
        finally {
            IOUtils.closeQuietly(xmlStream);
        }
    }
}
