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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.bnsumm.BenefitSummaryDao;
import edu.wisc.hr.dm.bnsumm.BenefitSummary;
import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

/**
 * 
 * 
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class BenefitSummaryDataController {
    private BenefitSummaryDao benefitSummaryDao;

    @Autowired
    public void setBenefitSummaryDao(BenefitSummaryDao benefitSummaryDao) {
        this.benefitSummaryDao = benefitSummaryDao;
    }
    
    @ResourceMapping("benefitSummary")
    public String getBenefitSummary(ModelMap modelMap) {
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        
        final BenefitSummary benefitSummary = this.benefitSummaryDao.getBenefitSummary(emplid);
        modelMap.addAttribute("benefits", benefitSummary.getBenefits());
        modelMap.addAttribute("dependents", benefitSummary.getDependents());
        modelMap.addAttribute(
          "enrollmentFlag", benefitSummary.getEnrollmentFlag())

        return "jsonView";
    }
}
