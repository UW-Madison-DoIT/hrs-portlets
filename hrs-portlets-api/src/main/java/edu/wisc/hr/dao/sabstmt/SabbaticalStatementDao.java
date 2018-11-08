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

package edu.wisc.hr.dao.sabstmt;

import org.jasig.springframework.web.client.ExtendedRestOperations.ProxyResponse;

import edu.wisc.hr.dm.sabstmt.SabbaticalReports;

/**
 * @author Eric Dalquist
 */
public interface SabbaticalStatementDao {
    /**
     * Get the sabbatical reports for the specified emplid
     */
    public SabbaticalReports getSabbaticalReports(String emplid);

    public void getSabbaticalReport(String emplid, String docId, ProxyResponse proxyResponse);
}
