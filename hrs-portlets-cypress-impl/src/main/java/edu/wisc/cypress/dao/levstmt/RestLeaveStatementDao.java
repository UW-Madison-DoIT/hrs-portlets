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



import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.jasig.springframework.web.client.ExtendedRestOperations;
import org.jasig.springframework.web.client.ExtendedRestOperations.ProxyResponse;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.cypress.xdm.levstmt.XmlLeaveStatement;
import edu.wisc.cypress.xdm.levstmt.XmlLeaveStatements;
import edu.wisc.hr.dao.levstmt.LeaveStatementDao;
import edu.wisc.hr.dao.levstmt.StatementType;
import edu.wisc.hr.dm.levstmt.PayPeriodLeaveReport;
import edu.wisc.hr.dm.levstmt.Report;
import edu.wisc.hr.dm.levstmt.SummarizedLeaveStatement;

/**
 * @author Eric Dalquist
 */
@Repository("restLeaveStatementDao")
public class RestLeaveStatementDao implements LeaveStatementDao {
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
    
    @Cacheable(cacheName="leaveStatement", exceptionCacheName="cypressUnknownExceptionCache")
    @Override
    public SummarizedLeaveStatement getLeaveStatements(String emplid) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        final XmlLeaveStatements leaveStatements = this.restOperations.getForObject(this.statementsUrl, XmlLeaveStatements.class, httpHeaders, emplid);
        return this.summarizeLeaveStatements(leaveStatements);
    }
    
    @Override
    public void getLeaveStatement(String emplid, String docId, StatementType type, ProxyResponse proxyResponse) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        this.restOperations.proxyRequest(proxyResponse, this.statementUrl, HttpMethod.GET, httpHeaders, docId, type.getKey());
    }
    
    protected SummarizedLeaveStatement summarizeLeaveStatements(XmlLeaveStatements leaveStatements) {

    	SummarizedLeaveStatement summary = new SummarizedLeaveStatement();
        final Map<String, PayPeriodLeaveReport> payPeriodLeaveReports = new LinkedHashMap<String, PayPeriodLeaveReport>();
        
        for (final XmlLeaveStatement statement : leaveStatements.getLeaveStatements()) {
            final String type = statement.getType();
            final StatementType statmentType = StatementType.getStatmentTypeByName(type);
            switch (statmentType) {
                case LEAVE: {
                    //Not needed anymore, should tell cypress to stop sending them eventually
                    break;
                }
                case REPORT: {
                    String payPeriod = statement.getPayPeriod();
                    PayPeriodLeaveReport pplr = payPeriodLeaveReports.get(payPeriod);
                	if(pplr == null) {
                		pplr = new PayPeriodLeaveReport();
                		pplr.setPayPeriod(payPeriod);
                		payPeriodLeaveReports.put(payPeriod,pplr);
                	}
                	final Report report = new Report();
                    report.setTitle(statement.getReportTitle());
                    report.setDocId(statement.getDocId());
                    
                    pplr.getLeaveReports().add(report);
                    break;
                }
                case MISSING: {
                    final Report report = new Report();
                    report.setTitle(statement.getReportTitle());
                    report.setDocId(statement.getDocId());
                    
                    summary.setMissingReport(report);
                    break;
                }
                default: {
                    logger.warn("Unknown LeaveStatment type: {}", statement);
                }
            }
        }
        summary.getLeavePayPeriodReports().addAll(payPeriodLeaveReports.values());
        
        return summary;
    }
}
