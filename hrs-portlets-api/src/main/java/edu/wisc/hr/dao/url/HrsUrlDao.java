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

package edu.wisc.hr.dao.url;

import java.util.Map;

/**
 * Gets a set of deep-links into the HRS system
 *
 * @author Eric Dalquist
 */
public interface HrsUrlDao {
    public Map<String, String> getHrsUrls();

    /**
     * The key the value of which is the deep link to a specific earnings statement.
     * While earnings statements are canonically `earnings` statements, this key is nonetheless
     * sic `Earning Statement`.
     *
     * Optionally append ?paycheck_nbr={integer} to specify which earnings statement.
     *
     * Omitting the parameter gets the latest earnings statement.
     */
    public static String EARNINGS_STATEMENT_KEY = "Earning Statement";

    /**
     * The name of the key the value of which is the deep link into HRS to approve payable time.
     * NB: Approve and Payable are capitalized; time isn't
     */
    public static String APPROVE_PAYABLE_TIME_KEY = "Approve Payable time";

    public static String SELF_SERVICE_DIRECT_DEPOSIT_KEY = "Direct Deposit";

    /**
     * The name of the key the value of which is the deep link to HRS for an
     * employee to view the garnishments on their own earnings.
     */
    String SELF_SERVICE_VIEW_GARNISHMENTS_KEY = "Fluid Garnishments";

    /**
     * The name of the key the value of which is the deep link to HRS for
     * Fluid > Employee Self Service (Home Page) > Time
     */
    String SELF_SERIVCE_FLUID_TIME_KEY = "Fluid Time";

    /**
     * The name of the key the value of which is the deep link into HRS for
     * managers to manage others performance.
     */
    public static String OTHERS_PERFORMANCE_KEY = "Manager ePerf";

    /**
     * The name of the key the value of which is the deep link into HRS for
     * employees to manage their own performance.
     */
    public static String SELF_PERFORMANCE_KEY = "Employee ePerf";

    public static String TIME_ABSENCE_DASHBOARD_KEY = "Time/Absence Dashboard";
}
