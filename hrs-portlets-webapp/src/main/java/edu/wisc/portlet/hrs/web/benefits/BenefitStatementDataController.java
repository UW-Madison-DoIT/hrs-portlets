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

package edu.wisc.portlet.hrs.web.benefits;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.portlet.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.jasig.springframework.web.client.PortletResourceProxyResponse;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.benstmt.BenefitStatementDao;
import edu.wisc.hr.dm.benstmt.BenefitStatement;
import edu.wisc.hr.dm.benstmt.BenefitStatements;
import edu.wisc.portlet.hrs.util.HrsDownloadControllerUtils;
import java.text.DateFormat;
import java.text.ParseException;

import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

import java.util.Date;

/**
 * 
 * 
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class BenefitStatementDataController {
    private BenefitStatementDao benefitStatementDao;
    private Set<String> ignoredProxyHeaders;
    
    @Resource(name="ignoredProxyHeaders")
    public void setIgnoredProxyHeaders(Set<String> ignoredProxyHeaders) {
        this.ignoredProxyHeaders = ignoredProxyHeaders;
    }

    @Autowired
    public void setBenefitStatementDao(BenefitStatementDao benefitStatementDao) {
        this.benefitStatementDao = benefitStatementDao;
    }
    
    protected static void sortStatements(List<BenefitStatement> statements) {
        ComparatorChain chainSort = new ComparatorChain();
        chainSort.addComparator(new BenefitStatementIssuedComparator(), true);
        chainSort.addComparator(new BenefitStatementNameComparator());
        chainSort.addComparator(new BenefitStatementYearComparator(), true);
        
        Collections.sort(statements,chainSort);
    }
    
    @ResourceMapping("benefitStatements")
    public String getBenefitStatements(ModelMap modelMap) {
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        final BenefitStatements benefitStatements = this.benefitStatementDao.getBenefitStatements(emplid);

        final List<BenefitStatement> statements = benefitStatements.getBenefitStatements();
        sortStatements(statements);
        modelMap.addAttribute("report", statements);
        
        return "reportAttrJsonView";
    }

    @ResourceMapping("benefits.pdf")
    public void getBenefitStatement(
            @RequestParam("mode") String mode,
            @RequestParam("docId") String docId, 
            @RequestParam("year") int year,
            ResourceResponse response) {

        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        HrsDownloadControllerUtils.setResponseHeaderForDownload(response, "benefits", "PDF");
        this.benefitStatementDao.getBenefitStatement(emplid, year, docId, mode, new PortletResourceProxyResponse(response, ignoredProxyHeaders));
    }
    
    private static class BenefitStatementIssuedComparator implements Comparator<BenefitStatement> {
      private static final int GREATER_THAN = 1;
      private static final int LESS_THAN = -1;
      private static final int NOT_FOUND = -1;
      private static final String KEYWORD = "Issued ";
      private final DateFormat dateFormatter;

      public BenefitStatementIssuedComparator() {
        dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
        dateFormatter.setLenient(false);
      }
      
      private Date getIssuedDate(String name) {
        Date result = null;
        
        int issuedStart = name.indexOf(KEYWORD);
        if (issuedStart > NOT_FOUND) {
          String dateString = name.substring(issuedStart + KEYWORD.length());
          try {
            result = dateFormatter.parse(dateString);
          } catch (ParseException e) {
            // swallow
          }
        }
        
        return result;
      }
      
      @Override
      public int compare(BenefitStatement o1, BenefitStatement o2) {
        int result = 0;

        Date leftDate = getIssuedDate(o1.getName());
        Date rightDate = getIssuedDate(o2.getName());
        if (null != leftDate && null != rightDate) {
          result = leftDate.compareTo(rightDate);
        } else if (null != leftDate) {
          result = GREATER_THAN;
        } else if (null != rightDate) {
          result = LESS_THAN;
        }
        
        return result;
      }
    }
    
    private static class BenefitStatementYearComparator implements Comparator<BenefitStatement> {

		@Override
		public int compare(BenefitStatement o1, BenefitStatement o2) {
			return o1.getYear().compareTo(o2.getYear());
		}
    	
    }
    
    private static class BenefitStatementNameComparator implements Comparator<BenefitStatement> {

		@Override
		public int compare(BenefitStatement o1, BenefitStatement o2) {
			String o1Type = o1.getName().substring(0,o1.getName().indexOf(" "));
			String o2Type = o2.getName().substring(0,o2.getName().indexOf(" "));
			return o1Type.compareTo(o2Type);
		}
    	
    }
}
