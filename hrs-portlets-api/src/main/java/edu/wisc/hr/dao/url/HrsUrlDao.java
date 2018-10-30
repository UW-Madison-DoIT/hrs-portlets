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
 * @version $Revision: 1.1 $
 */
public interface HrsUrlDao {
    public Map<String, String> getHrsUrls();

    /**
     * The name of the key the value of which is the deep link into HRS to approve payable time.
     * NB: Approve and Payable are capitalized; time isn't
     */
    public static String APPROVE_PAYABLE_TIME_KEY = "Approve Payable time";

    /**
     * The name of the key the value of which is the deep link to HRS for an employee to view the
     * garnishments on their own earnings.
     */
    String SELF_SERVICE_VIEW_GARNISHMENTS_KEY = "Fluid Garnishments";
}
