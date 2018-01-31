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

package edu.wisc.portlet.hrs.web.approvals;

import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.approvals.ApprovalsCountDao;
import edu.wisc.portlet.hrs.web.HrsControllerBase;

@Controller
@RequestMapping("VIEW")
public class ApprovalsCountController extends HrsControllerBase{
    @Autowired
    private ApprovalsCountDao approvalsCountDao;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResourceMapping("approvalCounts")
    public String getApprovalsCount(ModelMap modelMap){
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        int approvalsCount = approvalsCountDao.getApprovalsCount(emplid);
        modelMap.addAttribute("quantity", approvalsCount);
        return "countAttrJsonView";
    }

    @RequestMapping
    public String viewApprovals(){
        return "approvals";
    }

}
