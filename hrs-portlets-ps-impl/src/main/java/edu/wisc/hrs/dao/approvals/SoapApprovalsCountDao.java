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

package edu.wisc.hrs.dao.approvals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;

import edu.wisc.hr.dao.approvals.ApprovalsCountDao;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.apprcnt.req.EMPLIDTypeShape;
import edu.wisc.hrs.xdm.apprcnt.req.GetCompIntfcUWPTAPPRCNTCI;
import edu.wisc.hrs.xdm.apprcnt.res.APPROVALCOUNTTypeShape;
import edu.wisc.hrs.xdm.apprcnt.res.GetCompIntfcUWPTAPPRCNTCIResponse;

@Repository("soapApprovalsCountDao")
public class SoapApprovalsCountDao extends BaseHrsSoapDao implements ApprovalsCountDao{
    
    @Autowired
    @Qualifier("approvalsCountWebServiceTemplate")
    private WebServiceOperations webServiceOperations;

    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }

    @Override
    @Cacheable(cacheName="approvalsCount", exceptionCacheName="hrsUnknownExceptionCache")
    public int getApprovalsCount(String emplId) {
        EMPLIDTypeShape value = HrsUtils.createValue(EMPLIDTypeShape.class, emplId);
        final GetCompIntfcUWPTAPPRCNTCI request = new GetCompIntfcUWPTAPPRCNTCI();
        request.setEMPLID(value);
        final GetCompIntfcUWPTAPPRCNTCIResponse response = this.internalInvoke(request);
        final APPROVALCOUNTTypeShape approvalCount = response.getAPPROVALCOUNT();
        return approvalCount.getValue();
    }

}
