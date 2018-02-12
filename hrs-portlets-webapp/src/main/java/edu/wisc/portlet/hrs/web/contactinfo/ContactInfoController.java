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

package edu.wisc.portlet.hrs.web.contactinfo;

import java.util.Arrays;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.handler.PortletRequestMethodNotSupportedException;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateDao;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.bnsemail.PreferredEmail;
import edu.wisc.hr.dm.person.PersonInformation;
import edu.wisc.hr.dm.prefname.PreferredName;
import edu.wisc.hr.dm.prefname.PreferredNameExtended;
import edu.wisc.hr.dm.prefname.PreferredNameValidator;
import edu.wisc.hr.service.PreferredNameService;

import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

import edu.wisc.portlet.hrs.web.HrsControllerBase;

/**
 * @author Eric Dalquist
 * @version $Revision: 1.3 $
 */
@Controller
@RequestMapping("VIEW")
public class ContactInfoController extends HrsControllerBase {
	private final String PVI_ATTR = "wiscedupvi";
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private static final String HR_CONTACT_URL_PREFERENCE_NAME="humanResourceContactUrl";
    
    private String businessEmailRolesPreferences = "businessEmailRoles";
    private ContactInfoDao contactInfoDao;
    private BusinessEmailUpdateDao businessEmailUpdateDao;
    private PreferredNameService preferredNameService;
    
    public void setBusinessEmailRolesPreferences(String businessEmailRolesPreferences) {
        this.businessEmailRolesPreferences = businessEmailRolesPreferences;
    }

    @Autowired
    public void setBusinessEmailUpdateDao(BusinessEmailUpdateDao businessEmailUpdateDao) {
        this.businessEmailUpdateDao = businessEmailUpdateDao;
    }
    
    @Autowired
    public void setPreferredNameService(PreferredNameService service) {
    	this.preferredNameService = service;
    }

    @Autowired
    public void setContactInfoDao(ContactInfoDao contactInfoDao) {
        this.contactInfoDao = contactInfoDao;
    }
    
    /**
     * Populate the ModelMap with the link of humanResourceOffice
     * @param request
     * @return
     */
    @ModelAttribute("humanResourceOfficeContactUrl")
    public final String getHumanResourceOfficeContainLink(PortletRequest request) {
        final PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(HR_CONTACT_URL_PREFERENCE_NAME, null);
    }

    @RenderMapping
    public String viewContactInfo(ModelMap model, PortletRequest request) {
        final String emplId = PrimaryAttributeUtils.getPrimaryId();
        
        final PersonInformation contactInformation = this.contactInfoDao.getPersonalData(emplId);
        model.addAttribute("contactInformation", contactInformation);
        
        final boolean showBusinessEmail = showBusinessEmail(request);
        model.addAttribute("showBusinessEmail", showBusinessEmail);
        if (showBusinessEmail) {
            final PreferredEmail preferredEmail = this.businessEmailUpdateDao.getPreferedEmail(emplId);
            model.addAttribute("preferredEmail", preferredEmail);
        }
        
        setupPreferredName(model, request);
        
        
        
        return "contactInfo";
    }
    
    private void setupPreferredName(ModelMap modelMap, PortletRequest request) {
    	@SuppressWarnings("unchecked")
		Map<String, String> userInfo = (Map <String, String>) request.getAttribute(PortletRequest.USER_INFO);
    	
    	modelMap.addAttribute("displayName",userInfo.get("displayName"));//for system
        modelMap.addAttribute("legalName",userInfo.get("wiscEduSORName")); // for uw-madison
        boolean isPreferredNameEnabled = !StringUtils.isBlank(userInfo.get("wiscEduSORName"));
        modelMap.addAttribute("isPreferredNameEnabled", isPreferredNameEnabled);
        
        if(isPreferredNameEnabled) {
    		PreferredName preferredName = preferredNameService.getPreferredName(getPvi(request));
    		
    		String currentFirstName = userInfo.get("wiscedupreferredfirstname");
    		String currentMiddleName = userInfo.get("wiscedupreferredmiddlename");
    		String currentLastName = userInfo.get("wiscedupreferredlastname");
    		
    		if(preferredName != null) {
    			modelMap.addAttribute("firstName", preferredName.getFirstName());
    			modelMap.addAttribute("middleName", preferredName.getMiddleName());
    			modelMap.addAttribute("lastName", preferredName.getLastName());
    		}
    		
    		modelMap.addAttribute("pendingStatus",preferredNameService.getStatus(new PreferredName(currentFirstName, currentMiddleName,currentLastName,getPvi(request)),preferredNameService.getPreferredName(getPvi(request))));
    		modelMap.addAttribute("sirName",userInfo.get("sn"));
    		
    		//edit setup
    		if(!modelMap.containsKey("preferredName")) {
    		
    			if(preferredName != null) {
    				modelMap.addAttribute("preferredName", preferredName);
    			} else {
    				modelMap.addAttribute("preferredName", new PreferredName());
    			}
    		}
    		
    		if(request.getParameter("therewasanerror") != null) {
    			modelMap.addAttribute("therewasanerror","true");
    		}
        }
	}

    //TODO switch to spring-sec role check?
    protected boolean showBusinessEmail(PortletRequest request) {
        final PortletPreferences preferences = request.getPreferences();
        final String[] roles = preferences.getValues(this.businessEmailRolesPreferences, new String[0]);
        
        for (final String businessEmailRole : roles) {
            if (request.isUserInRole(businessEmailRole)) {
                this.logger.debug("Showing business email info, user is in role: {}", businessEmailRole);
                return true;
            }
        }
        
        this.logger.debug("Hiding business email info, user is not in any of the roles: {}", Arrays.asList(roles));
        
        return false;
    }
    
    @ResourceMapping("businessEmailAddress")
    public String updateBusinessEmailAddress(
            @RequestParam("email") String email,
            ResourceRequest request,
            ModelMap modelMap) throws PortletRequestMethodNotSupportedException {
        
        //TODO can I move this into the annotations?
        //TODO add method support to portlet annotations in portlet contrib
        if (!"POST".equals(request.getMethod())) {
            throw new PortletRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" });
        }
        
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        
        email = email.trim();
        
        if (!StringUtils.isEmpty(email)) {
            if (emailValidator.isValid(email)) {
                this.businessEmailUpdateDao.updatePreferedEmail(emplid, email);
            }
            else {
                logger.warn("User {} submitted invalid email address {} the change request will be ignored.", emplid, email);
            }
        }
        
        return this.getBusinessEmailAddress(modelMap);
    }
    
    @ActionMapping(params="action=savePreferredName")
	public void submitEdit(ActionRequest request, ActionResponse response, PreferredName preferredName, BindingResult bindingResult) throws PortletModeException {
      
        @SuppressWarnings("unchecked")
        Map<String, String> userInfo = (Map <String, String>) request.getAttribute(PortletRequest.USER_INFO);
      
        PreferredNameExtended pne = new PreferredNameExtended(preferredName, userInfo.get("sn"));
		//validation
		ValidationUtils.invokeValidator(new PreferredNameValidator(), pne, bindingResult);
		if(!bindingResult.hasErrors()) {
			//submit changes to DAO
			preferredName.setPvi(getPvi(request));
			
			preferredNameService.setPreferredName(preferredName.getPvi(), preferredName);
			//redirect to view page on success
			response.setPortletMode(PortletMode.VIEW);
		} else {
			//fail back to edit mode with flag set
			response.setRenderParameter("therewasanerror", "true");
			response.setPortletMode(PortletMode.VIEW);
		}
	}
    
    @ActionMapping(params="action=deletePreferredName") 
	public void submitDelete(ActionRequest request, ActionResponse response) throws PortletModeException {
		final String pvi = getPvi(request);
		preferredNameService.deletePreferredName(pvi);
		response.setPortletMode(PortletMode.VIEW);
	}
    
    protected String getBusinessEmailAddress( ModelMap modelMap) {
        
        final String emplid = PrimaryAttributeUtils.getPrimaryId();
        
        final PreferredEmail preferredEmail = this.businessEmailUpdateDao.getPreferedEmail(emplid);
        final String updatedEmail = preferredEmail.getEmail();
        modelMap.addAttribute("email", updatedEmail);
        
        return "jsonView";
    }
    
    private String getPvi(PortletRequest request) {
    	@SuppressWarnings("unchecked")
		Map<String, String> userInfo = (Map <String, String>) request.getAttribute(PortletRequest.USER_INFO);
    	
    	final String pvi = userInfo.get(PVI_ATTR);
    	return pvi;
    }
}
