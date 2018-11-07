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

package edu.wisc.cypress.dao.ernstmt;



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

import edu.wisc.cypress.xdm.ernstmt.XmlEarningStatement;
import edu.wisc.cypress.xdm.ernstmt.XmlEarningStatements;
import edu.wisc.hr.dao.ernstmt.EarningStatementDao;
import edu.wisc.hr.dm.ernstmt.EarningStatement;
import edu.wisc.hr.dm.ernstmt.EarningStatements;

/**
 * @author Eric Dalquist
 */
@Repository("restEarningStatementDao")
public class RestEarningStatementDao implements EarningStatementDao {

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

  @Cacheable(cacheName = "earningStatement", exceptionCacheName = "cypressUnknownExceptionCache")
  @Override
  public EarningStatements getEarningStatements(String emplid) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("HRID", emplid);

    final XmlEarningStatements xmlEarningStatements =
        this.restOperations
            .getForObject(this.statementsUrl, XmlEarningStatements.class,
                httpHeaders, emplid);

    return this.mapEarningStatements(xmlEarningStatements);
  }

  @Override
  public void getEarningStatement(String emplid, String docId,
      ProxyResponse proxyResponse) {
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.set("HRID", emplid);
    this.restOperations
        .proxyRequest(proxyResponse, this.statementUrl, HttpMethod.GET,
            httpHeaders, docId);
  }

  protected EarningStatements mapEarningStatements(
      XmlEarningStatements xmlEarningStatements) {
    final List<XmlEarningStatement> xmlEarningStatementList = xmlEarningStatements
        .getEarningStatements();

    final EarningStatements earningStatements = new EarningStatements();
    final List<EarningStatement> earningStatementsList = earningStatements
        .getEarningStatements();

    for (final XmlEarningStatement input : xmlEarningStatementList) {
      final EarningStatement earningStatement = new EarningStatement();

      earningStatement.setAmount(input.getAmount());
      earningStatement.setDocId(input.getDocId());
      earningStatement.setEarned(input.getEarned());
      earningStatement.setFullTitle(input.getFullTitle());
      earningStatement.setPaid(input.getPaid());

      earningStatementsList.add(earningStatement);
    }

    return earningStatements;
  }
}
