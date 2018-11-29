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

package edu.wisc.portlet.hrs.web.payroll;

import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dm.ernstmt.EarningStatementDateComparator;
import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.portlet.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.jasig.springframework.web.client.PortletResourceProxyResponse;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.ernstmt.EarningStatementDao;
import edu.wisc.hr.dm.ernstmt.EarningStatement;
import edu.wisc.hr.dm.ernstmt.EarningStatements;
import edu.wisc.portlet.hrs.util.HrsDownloadControllerUtils;

import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

/**
 * 
 * 
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class EarningStatementDataController {

  /**
   * Legacy Cypress-specific DAO.
   */
  private EarningStatementDao earningStatementDao;



    private Set<String> ignoredProxyHeaders;
    
    @Resource(name="ignoredProxyHeaders")
    public void setIgnoredProxyHeaders(Set<String> ignoredProxyHeaders) {
        this.ignoredProxyHeaders = ignoredProxyHeaders;
    }
    
    @Autowired
    public void setEarningStatementDao(EarningStatementDao earningStatementDao) {
        this.earningStatementDao = earningStatementDao;
    }

    /**
     * Legacy, Cypress-only earnings statements.
     *
     * NB The term is canonically "earnings statements", not "earning statements", the key and
     * method name here notwithstanding.
     *
     * @param modelMap
     * @return
     */
    @ResourceMapping("earningStatements")
    public String getEarningStatements(ModelMap modelMap) {
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        final EarningStatements earningStatements = this.earningStatementDao.getEarningStatements(emplid);
        
        final List<EarningStatement> statements = earningStatements.getEarningStatements();
        modelMap.addAttribute("report", statements);
        
        return "reportAttrJsonView";
    }

    //Server
    //
    @ResourceMapping("earning_statement.pdf")
    public void getEarningsStatement(
            @RequestParam("docId") String docId, 
            ResourceResponse response) {
        
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        HrsDownloadControllerUtils.setResponseHeaderForDownload(response, "earning_statement", "PDF");
        this.earningStatementDao.getEarningStatement(emplid, docId, new PortletResourceProxyResponse(response, ignoredProxyHeaders));
    }

    @ResourceMapping("latest_earnings_statement.pdf")
    public void latestEarningsStatement(final ResourceResponse response) {
        final String emplid = PrimaryAttributeUtils.getPrimaryId();

        // earningStatements because that's the domain model here, but N.B. term is consistently
        // "earnings statements" or "Earnings Statements" in employee-facing content on Service
        // Center website
        final EarningStatements earningStatements =
            this.earningStatementDao.getEarningStatements(emplid);

        final List<EarningStatement> statements = earningStatements.getEarningStatements();

        if (statements.isEmpty()) {
            response.setProperty(ResourceResponse.HTTP_STATUS_CODE, "404");
        } else {
            Collections.sort(statements, new EarningStatementDateComparator());
            Collections.reverse(statements);

            EarningStatement latestStatement = statements.get(0);
            BigInteger docIdBigInt = latestStatement.getDocId();

            HrsDownloadControllerUtils.setResponseHeaderForDownload(response,
                "latest_earnings_statement", "PDF");
            this.earningStatementDao.getEarningStatement(emplid, docIdBigInt.toString(),
                new PortletResourceProxyResponse(response, ignoredProxyHeaders));
        }
    }
}
