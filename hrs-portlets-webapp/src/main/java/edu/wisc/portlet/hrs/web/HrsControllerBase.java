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

package edu.wisc.portlet.hrs.web;

import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.ModelMap;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.url.HrsUrlDao;

/**
 * Common functions and dependencies for HRS controllers
 *
 * @author Eric Dalquist
 * @version $Revision: 1.2 $
 */
public class HrsControllerBase {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String notificationPreferences = "notification";
    protected static final String helpUrlPreferences = "helpUrl";
    private static final String GENERIC_ERROR_MESSAGE_PREFERENCE_NAME = "genericErrorMessage";
    private static final String DEFAULT_ERROR_MESSAGE = "Sorry! MyUW was unable to load your information. Please try again later or <a href=\\\"https://kb.wisc.edu/myuw/page.php?id=50338\\\" target=\\\"_blank\\\">get help</a>.";
    private HrsUrlDao hrsUrlDao;

    public void setNotificationPreferences(String notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    @Autowired
    public void setHrsUrlDao(HrsUrlDao hrsUrlDao) {
        this.hrsUrlDao = hrsUrlDao;
    }

    /**
     * Populate the ModelMap with the navigation links from the portlet preferences
     */
    @ModelAttribute("helpUrl")
    public final String getNavLinks(PortletRequest request) {
        final PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(helpUrlPreferences, "#");
    }

    /**
     * The generic Message if you want to override the default in
     * messages.properties
     */
    @ModelAttribute("genericErrorMessage")
    public final String getGenericErrorMessage(PortletRequest request){
        final PortletPreferences preferences = request.getPreferences();
        return preferences.getValue(GENERIC_ERROR_MESSAGE_PREFERENCE_NAME, DEFAULT_ERROR_MESSAGE);
    }

    /**
     * Populate the ModelMap with the notification message
     */
    @ModelAttribute("notification")
    public final String[] getNotification(PortletRequest request) {
        final PortletPreferences preferences = request.getPreferences();

        return preferences.getValues(this.notificationPreferences, null);
    }

    /**
     * Populate the ModelMap with the HRS Urls
     */
    @ModelAttribute("hrsUrls")
    public final Map<String, String> getHrsUrls() {
        return this.hrsUrlDao.getHrsUrls();
    }

    @ResourceMapping("getHrsUrlsJson")
    public String getURLRestService(ModelMap modelMap) {
      modelMap.addAttribute("report", this.hrsUrlDao.getHrsUrls());
      return "reportAttrJsonView";
    }

}
