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

package edu.wisc.cypress.dao.sabstmt;



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

import edu.wisc.cypress.xdm.sabstmt.XmlSabbaticalReport;
import edu.wisc.cypress.xdm.sabstmt.XmlSabbaticalReports;
import edu.wisc.hr.dao.sabstmt.SabbaticalStatementDao;
import edu.wisc.hr.dm.sabstmt.SabbaticalReport;
import edu.wisc.hr.dm.sabstmt.SabbaticalReports;

/**
 * @author Eric Dalquist
 */
@Repository("restSabbaticalStatementDao")
public class RestSabbaticalStatementDao implements SabbaticalStatementDao {
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
    
    @Cacheable(cacheName="sabbaticalReports", exceptionCacheName="cypressUnknownExceptionCache")
    @Override
    public SabbaticalReports getSabbaticalReports(String emplid) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        final XmlSabbaticalReports xmlSabbaticalReports = this.restOperations.getForObject(this.statementsUrl, XmlSabbaticalReports.class, httpHeaders, emplid);
        return mapSabbaticalReports(xmlSabbaticalReports);
    }
    
    @Override
    public void getSabbaticalReport(String emplid, String docId, ProxyResponse proxyResponse) {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("HRID", emplid);
        this.restOperations.proxyRequest(proxyResponse, this.statementUrl, HttpMethod.GET, httpHeaders, docId);
    }
    
    protected SabbaticalReports mapSabbaticalReports(XmlSabbaticalReports xmlSabbaticalReports) {
        final List<XmlSabbaticalReport> xmlSabbaticalReportList = xmlSabbaticalReports.getSabbaticalReports();
        
        final SabbaticalReports sabbaticalReports = new SabbaticalReports();
        final List<SabbaticalReport> sabbaticalReportsList = sabbaticalReports.getSabbaticalReports();
        
        for (final XmlSabbaticalReport input : xmlSabbaticalReportList) {
            final SabbaticalReport sabbaticalReport = new SabbaticalReport();
            
            sabbaticalReport.setDocId(input.getDocId());
            sabbaticalReport.setDocType(input.getDocType());
            sabbaticalReport.setFullTitle(input.getFullTitle());
            sabbaticalReport.setName(input.getName());
            sabbaticalReport.setYear(input.getYear());

            sabbaticalReportsList.add(sabbaticalReport);
        }

        return sabbaticalReports;
    }
}
