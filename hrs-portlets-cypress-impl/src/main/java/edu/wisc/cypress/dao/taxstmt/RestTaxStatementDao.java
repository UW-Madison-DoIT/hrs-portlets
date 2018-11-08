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



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.jasig.springframework.web.client.ExtendedRestOperations;
import org.jasig.springframework.web.client.ExtendedRestOperations.ProxyResponse;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.cypress.xdm.taxstmt.XmlTaxStatement;
import edu.wisc.cypress.xdm.taxstmt.XmlTaxStatements;
import edu.wisc.hr.dao.taxstmt.TaxStatementDao;
import edu.wisc.hr.dm.taxstmt.TaxStatement;
import edu.wisc.hr.dm.taxstmt.TaxStatements;

/**
 * @author Eric Dalquist
 */
@Repository("restTaxStatementDao")
public class RestTaxStatementDao implements TaxStatementDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private ExtendedRestOperations restOperations;
    private String statementsUrl;
    private String statementUrl;
    
    @Autowired
    public void setRestTemplate(ExtendedRestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public void setStatementsUrl(String statementsUrl) {
        this.statementsUrl = statementsUrl;
    }
    public void setStatementUrl(String statementUrl) {
        this.statementUrl = statementUrl;
    }
    
    @Cacheable(cacheName="taxStatement", exceptionCacheName="cypressUnknownExceptionCache")
    @Override
    public TaxStatements getTaxStatements(String emplid) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        final XmlTaxStatements xmlTaxStatements = this.restOperations.getForObject(this.statementsUrl, XmlTaxStatements.class, httpHeaders, emplid);
        return mapTaxStatements(xmlTaxStatements);
    }
    
    @Override
    public void getTaxStatement(String emplid, String docId, ProxyResponse proxyResponse) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        this.restOperations.proxyRequest(proxyResponse, this.statementUrl, HttpMethod.GET, httpHeaders, docId);
    }
    
    protected TaxStatements mapTaxStatements(XmlTaxStatements xmlTaxStatements) {
        final List<XmlTaxStatement> xmlTaxStatementList = xmlTaxStatements.getTaxStatements();
        
        final TaxStatements earningStatements = new TaxStatements();
        final List<TaxStatement> taxStatementsList = earningStatements.getTaxStatements();
        
        for (final XmlTaxStatement input : xmlTaxStatementList) {
            final TaxStatement taxStatement = new TaxStatement();
            
            taxStatement.setDocId(input.getDocId());
            taxStatement.setFullTitle(input.getFullTitle());
            taxStatement.setName(input.getName());
            taxStatement.setYear(input.getYear());
            
            taxStatementsList.add(taxStatement);
        }

        return earningStatements;
    }
}
