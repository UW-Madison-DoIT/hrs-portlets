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

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import edu.wisc.hr.dao.benstmt.BenefitStatementDao;
import edu.wisc.hr.dm.benstmt.BenefitStatement;
import edu.wisc.hr.dm.benstmt.BenefitStatementIssuedComparator;
import edu.wisc.hr.dm.benstmt.BenefitStatementYearComparator;
import edu.wisc.hr.dm.benstmt.BenefitStatements;
import edu.wisc.hr.dm.benstmt.BenefitEnrollmentConfirmationPredicate;
import edu.wisc.hr.dm.benstmt.EtfAnnualStatementOfBenefitsPredicate;
import edu.wisc.hr.dm.statement.NameYearUrl;
import edu.wisc.hr.service.benefits.AnnualBenefitEnrollmentDatesService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.bnsumm.BenefitSummaryDao;
import edu.wisc.hr.dao.roles.HrsRolesDao;
import edu.wisc.hr.dm.bnsumm.BenefitSummary;

import org.apache.commons.lang.StringUtils;
import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

import edu.wisc.portlet.hrs.web.HrsControllerBase;

/**
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class BenefitInformationController extends HrsControllerBase {
    private BenefitSummaryDao benefitSummaryDao;
  private BenefitStatementDao benefitStatementDao;
    private HrsRolesDao hrsRolesDao;

    private BenefitsLearnMoreLinkGenerator learnMoreLinker =
      new BenefitsLearnMoreLinkGenerator();

    private AnnualBenefitEnrollmentDatesService abeDates =
        new AnnualBenefitEnrollmentDatesService();

    @Autowired
    public void setBenefitSummaryDao(BenefitSummaryDao benefitSummaryDao) {
        this.benefitSummaryDao = benefitSummaryDao;
    }

  @Autowired
  public void setBenefitStatementDao(BenefitStatementDao benefitStatementDao) {
    this.benefitStatementDao = benefitStatementDao;
  }

    @Autowired
    public void setHrsRolesDao(HrsRolesDao hrsRolesDao) {
        this.hrsRolesDao = hrsRolesDao;
    }

    @RequestMapping
    public String viewBenefitInfo(ModelMap model, PortletRequest request) {
      @SuppressWarnings("unchecked")
      Map<String, String> userInfo = (Map <String, String>) request.getAttribute(PortletRequest.USER_INFO);
      final String emplId = PrimaryAttributeUtils.getPrimaryId();
      final String etfMemberId = (String) userInfo.get("eduWisconsinETFMemberID");

      final BenefitSummary benefitSummary = this.benefitSummaryDao.getBenefitSummary(emplId);
      model.addAttribute("enrollmentFlag", benefitSummary.getEnrollmentFlag());

      boolean isMadisonUser = !StringUtils.isBlank(userInfo.get("wiscEduHRSEmplid"));
      model.addAttribute("isMadisonUser", isMadisonUser);
      final PortletPreferences preferences = request.getPreferences();
      model.addAttribute("learnMoreEBenefitGuide", preferences.getValue("ebenefitguidetext", null));

      String fname = preferences.getValue("fname", null);
      if (fname == null) {
        logger.error("Missing portlet-preference 'fname', required to generate URLs to benefit statements.");
        model.addAttribute("statementError", true);
        return "benefitInformation";
      }

      Set<String> roles = hrsRolesDao.getHrsRoles(emplId);
      model.addAttribute("learnMoreLink",
        learnMoreLinker.learnMoreLinkFor(roles, isMadisonUser));

      try {
        final BenefitStatements benefitStatements = this.benefitStatementDao.getBenefitStatements(emplId, etfMemberId);

        // split benefit statements between types of statements

        List<BenefitStatement> etfStatements =
          new ArrayList<BenefitStatement>(
            Collections2.filter(benefitStatements.getBenefitStatements(),
              new EtfAnnualStatementOfBenefitsPredicate()));

        List<BenefitStatement> enrollmentStatements =
          new ArrayList<BenefitStatement>(
            Collections2.filter(benefitStatements.getBenefitStatements(),
              new BenefitEnrollmentConfirmationPredicate()));

        // sort the lists

        Collections.sort(etfStatements, new BenefitStatementYearComparator());
        Collections.reverse(etfStatements);

        Collections.sort(enrollmentStatements, new BenefitStatementIssuedComparator());
        Collections.reverse(enrollmentStatements);

        // transform to lists of reasonable JavaBeans suitable for rendering in the JSP

        Function<BenefitStatement, NameYearUrl> transformFunction =  new StatementToBeanFunction(fname);

        List<NameYearUrl> eftStatementsAsNameYearUrlBeans = Lists.transform(etfStatements, transformFunction);
        List<NameYearUrl> enrollmentStatementsAsNameYearUrlBeans = Lists.transform(enrollmentStatements, transformFunction);

        model.addAttribute("etfStatements", eftStatementsAsNameYearUrlBeans);
        model.addAttribute("enrollmentStatements", enrollmentStatementsAsNameYearUrlBeans);

      } catch (Exception e) {
        logger.warn("Error loading or transforming benefit statements.", e);
        model.addAttribute("statementError", true);
      }

      return "benefitInformation";
    }

    @ResourceMapping("benefitInformationWidget")
    public String benefitInformationWidget(
      ModelMap model, PortletRequest request) {

      final String emplId = PrimaryAttributeUtils.getPrimaryId();

      // same model as the portlet view JSP
      viewBenefitInfo(model, request);


      Set<String> roles = hrsRolesDao.getHrsRoles(emplId);
      LocalDate today = new LocalDate();

      if (roles.contains("ROLE_VIEW_NEW_HIRE_BENEFITS")) {
        return "benefitInformationWidgetPersonalEnrollmentEvent";

      } else if (abeDates.foreshadowAnnualBenefitsEnrollment(today)) {
        return "benefitInformationWidgetAnnualEnrollmentForeshadowing";

      } else if (roles.contains("ROLE_VIEW_OPEN_ENROLL_BENEFITS")) {
        if (abeDates.lastDayOfAnnualBenefitEnrollment(today)) {
          return "benefitInformationWidgetAnnualEnrollmentLastDay";
        }

        model.addAttribute("abeDaysRemaining",
            abeDates.daysRemainingInAnnualBenefitsEnrollment(today));
        return "benefitInformationWidgetAnnualEnrollmentDuringCountdown";

      } else if (abeDates.feedbackPeriod(today)) {
        return "benefitInformationWidgetAnnualEnrollmentFeedback";
      }

      return "benefitInformationWidget";

    }


    private class StatementToBeanFunction implements Function<BenefitStatement, NameYearUrl> {

      private BenefitStatementToNameYearUrlConverter converter;

      public StatementToBeanFunction(String fname) {
        this.converter = new BenefitStatementToNameYearUrlConverter(fname);
      }

      @Override
      public NameYearUrl apply(BenefitStatement input) {
        return converter.convert(input);
      }
    }
}
