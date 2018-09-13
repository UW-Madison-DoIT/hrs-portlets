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

package edu.wisc.portlet.hrs.web.timeabs;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import edu.wisc.hr.dao.tlpayable.TimeSheetDao;
import edu.wisc.hr.dm.tlpayable.TimeSheet;

import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;

/**
 *
 *
 * @author Eric Dalquist
 */
@Controller
@RequestMapping("VIEW")
public class TimeSheetDataController {
    private TimeSheetDao timeSheetDao;

    @Autowired
    public void setTimeSheetDao(TimeSheetDao timeSheetDao) {
        this.timeSheetDao = timeSheetDao;
    }

    /**
     * Gets Time sheets - limited to most recent 80 results for the requesting user
     * @param modelMap
     * @return the most recent timesheets, limited to 80
     */
    @Secured({"ROLE_VIEW_TIME_ENTRY_HISTORY"})
    @ResourceMapping("timeSheets")
    public String getTimeSheets(ModelMap modelMap) {
        final String emplid = PrimaryAttributeUtils.getPrimaryId();

        final List<TimeSheet> timeSheets = this.timeSheetDao.getTimeSheets(emplid);

        //sorts the list from most recent time sheets to oldest time sheets
        Collections.sort(timeSheets, new Comparator<TimeSheet>(){
            @Override
            public int compare(TimeSheet o1, TimeSheet o2) {
                if(o1 == null || o1.getDate() == null){
                    return -1;
                }else if(o2 == null || o2.getDate() == null){
                    return 1;
                }
                return(o1.getDate().compareTo(o2.getDate())*-1);
            }
        });
        //Limit to 80 is due to shortcomings in the datalist.js library used in portlet
        modelMap.addAttribute("report", timeSheets.subList(0, timeSheets.size()>80 ? 80:timeSheets.size()));
        return "reportAttrJsonView";
    }
}
