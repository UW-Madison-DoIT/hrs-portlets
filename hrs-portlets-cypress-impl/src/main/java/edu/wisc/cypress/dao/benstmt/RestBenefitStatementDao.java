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

package edu.wisc.cypress.dao.benstmt;



import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.jasig.springframework.web.client.ExtendedRestOperations;
import org.jasig.springframework.web.client.ExtendedRestOperations.ProxyResponse;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.cypress.xdm.benstmt.XmlBenefitStatement;
import edu.wisc.cypress.xdm.benstmt.XmlBenefitStatements;
import edu.wisc.hr.dao.benstmt.BenefitStatementDao;
import edu.wisc.hr.dm.benstmt.BenefitStatement;
import edu.wisc.hr.dm.benstmt.BenefitStatements;

/**
 * @author Eric Dalquist
 */
@Repository("restBenefitStatementDao")
public class RestBenefitStatementDao implements BenefitStatementDao {
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

    @Cacheable(cacheName="benefitStatement", exceptionCacheName="cypressUnknownExceptionCache")
    @Override
    public BenefitStatements getBenefitStatements(String emplid) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        
        final XmlBenefitStatements xmlBenefitStatements = 
                this.restOperations.getForObject(this.statementsUrl, XmlBenefitStatements.class, httpHeaders, emplid);

        final BenefitStatements benefitStatements = mapBenefitStatements(xmlBenefitStatements);

        logger.debug("Got statements [{}] for emplid [{}] (and updating portlet-local cache).",
                benefitStatements, emplid);

        return benefitStatements;
    }
    
    @Override
    public void getBenefitStatement(String emplid, int year, String docId, String mode, ProxyResponse proxyResponse) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        
        final String yearStr = Integer.toString(year);
        final String yearCode;
        if (yearStr.length() > 2) {
            yearCode = yearStr.substring(2);
        }
        else if (yearStr.length() < 2) {
            yearCode = StringUtils.leftPad(yearStr, 2, '0');
        }
        else {
            yearCode = yearStr;
        }
        this.restOperations.proxyRequest(proxyResponse, this.statementUrl, HttpMethod.GET, httpHeaders, yearCode, docId, mode);
    }
    
    protected BenefitStatements mapBenefitStatements(XmlBenefitStatements xmlBenefitStatements) {
        final List<XmlBenefitStatement> xmlBenefitStatementList = xmlBenefitStatements.getBenefitStatements();
        
        final BenefitStatements benefitStatements = new BenefitStatements();
        final List<BenefitStatement> benefitStatementList = benefitStatements.getBenefitStatements();
        
        for (final XmlBenefitStatement input : xmlBenefitStatementList) {
            final BenefitStatement benefitStatement = new BenefitStatement();
            
            benefitStatement.setDocId(input.getDocId());
            benefitStatement.setDocType(input.getDocType());
            benefitStatement.setFullTitle(input.getFullTitle());
            benefitStatement.setName(input.getName());
            benefitStatement.setYear(input.getYear());
            
            benefitStatementList.add(benefitStatement);
        }
        
        return benefitStatements;
    }
}
