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

package edu.wisc.portlet.hrs.web.bnsemail;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateDao;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.bnsemail.PreferredEmail;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.portlet.hrs.web.HrsControllerBase;

/**
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class BusinessEmailAdminController extends HrsControllerBase {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private ContactInfoDao contactInfoDao;
    private BusinessEmailUpdateDao businessEmailUpdateDao;
    
    @Autowired
    public void setBusinessEmailUpdateDao(BusinessEmailUpdateDao businessEmailUpdateDao) {
        this.businessEmailUpdateDao = businessEmailUpdateDao;
    }
    
    @Autowired
    public void setContactInfoDao(ContactInfoDao contactInfoDao) {
        this.contactInfoDao = contactInfoDao;
    }

    @RequestMapping
    public String viewEmailSearchForm(ModelMap model, PortletRequest request) {
        return "emailSearchForm";
    }

    @RequestMapping(params = "action=lookupPerson")
    public String lookupEmail(
            @RequestParam("userEmplId") String usersEmplId,
            @RequestParam(value = "emailUpdateSuccess", required = false) Boolean emailUpdateSuccess,
            ModelMap model) {
    	
    	usersEmplId = usersEmplId.trim();
        
        final PersonInformation contactInformation;
        try {
            contactInformation = this.contactInfoDao.getPersonalData(usersEmplId);
        }
        catch (RuntimeException e) {
            //JaxbUnmarshallingFailureException
            //The wonderful exception thrown due to HRS not doing SOAP Faults correctly
            model.addAttribute("searchMessage", "HRS could not find a user with id: " + usersEmplId);
            return "emailSearchForm";
        }
        
        //No HRS user info found, return a message about the lack of data
        if (contactInformation == null) {
            model.addAttribute("searchMessage", "HRS could not find a user with id: " + usersEmplId);
            return "emailSearchForm";
        }
        
        final PreferredEmail preferedEmail = this.businessEmailUpdateDao.getPreferedEmail(usersEmplId);
        //No Middleware user info found, return a message about the lack of data
        if (preferedEmail == null) {
            model.addAttribute("searchMessage", "The email change service could not find a user with id: " + usersEmplId);
            return "emailSearchForm";
        }
        if (preferedEmail.getName() == null) {
            model.addAttribute("searchMessage", "The email change service could not find a user with id: " + usersEmplId);
            model.addAttribute("searchSubMessage", preferedEmail.getMessage());
            return "emailSearchForm";
        }
        
        //A message implies an error, return to the search form with the message
        final String message = preferedEmail.getMessage();
        if (message != null) {
            logger.warn("A message was returned from the email change service for user {}: {}", usersEmplId, message);
        }
            
        //Return the found user email info
        model.addAttribute("emailUpdateSuccess", emailUpdateSuccess);
        model.addAttribute("contactInformation", contactInformation);
        model.addAttribute("preferredEmail", preferedEmail);
        return "emailUpdateForm";
    }
    
    
    @RequestMapping(params = "action=updateEmail")
    public void updateUsersEmail(ActionRequest request, ActionResponse response,                
            @RequestParam("userEmplId") String userEmplId,
            @RequestParam("email1") String email) {
    	
    	userEmplId = userEmplId.trim();
    	email = email.trim();
        
        if (emailValidator.isValid(email)) {
            this.businessEmailUpdateDao.updatePreferedEmail(userEmplId, email);
            response.setRenderParameter("emailUpdateSuccess", Boolean.TRUE.toString());
        }
        else {
            logger.warn("Admin user {} submitted invalid email address {} for {} the change request will be ignored.", new Object[] {request.getRemoteUser(), userEmplId, email});
            response.setRenderParameter("emailUpdateSuccess", Boolean.FALSE.toString());
        }
        
        response.setRenderParameter("action", "lookupPerson");
        response.setRenderParameter("userEmplId", userEmplId);
    }
}
