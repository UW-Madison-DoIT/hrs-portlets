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
package edu.wisc.hrs.dao.url;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;

import edu.wisc.hr.dao.url.HrsUrlDao;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.url.req.GetCompIntfcUWPORTAL1URL;
import edu.wisc.hrs.xdm.url.res.GetCompIntfcUWPORTAL1URLResponse;
import edu.wisc.hrs.xdm.url.res.InstallationTypeShape;

/**
 * Spring {@link WebServiceOperations} backed implementation of {@link BaseHrsSoapDao}.
 * 
 */
@Repository("soapHrsUrlDao")
public class SoapHrsUrlDao extends BaseHrsSoapDao implements HrsUrlDao {
    private WebServiceOperations webServiceOperations;
    
    @Autowired
    public void setWebServiceOperations(@Qualifier("urlWebServiceTemplate") WebServiceOperations webServiceOperations) {
        this.webServiceOperations = webServiceOperations;
    }
    
    @Override
    protected WebServiceOperations getWebServiceOperations() {
        return this.webServiceOperations;
    }
	
	@Override
	@Cacheable(cacheName="hrsUrls", decoratedCacheType=DecoratedCacheType.SELF_POPULATING_CACHE, selfPopulatingTimeout=10000, exceptionCacheName="hrsUnknownExceptionCache")
    public Map<String, String> getHrsUrls() {
	    final GetCompIntfcUWPORTAL1URL request = new GetCompIntfcUWPORTAL1URL();
	    
	    final GetCompIntfcUWPORTAL1URLResponse response = this.internalInvoke(request);
	    
	    Map<String, String> urlMap = this.convertUrlMap(response);

        if (logger.isInfoEnabled()) {
            logger.info(computeUrlMapLogMessage(urlMap));
        }

        return urlMap;
    }

    protected Map<String, String> convertUrlMap(final GetCompIntfcUWPORTAL1URLResponse response) {
        final List<InstallationTypeShape> installations = response.getInstallations();
	    final Map<String, String> hrsUrls = new LinkedHashMap<String, String>(installations.size());
	    
	    for (final InstallationTypeShape installationTypeShape : installations) {
	        final String description = HrsUtils.getValue(installationTypeShape.getDescr());
	        final String url = HrsUtils.getValue(installationTypeShape.getUrl());
	        
	        hrsUrls.put(description, url);
        }
	    
        return hrsUrls;
    }

    /**
     * Compute a loggable String describing the retrieved URL map, to enable reasonable logging.
     * This method is non-private only to enable unit testing.
     * This method is not part of the exposed API of this class.
     * @param urlMap non-null Map from String keys to String values
     * @return a String suitable for logging.
     */
    static String computeUrlMapLogMessage(final Map<String, String> urlMap) {

        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Retrieved URL map (and updating the URL map cache) with these name : value pairs: \n");

        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            logMessage.append("  ");
            logMessage.append(entry.getKey());
            logMessage.append(" : ");
            logMessage.append(entry.getValue());
            logMessage.append("\n");
        }

        return logMessage.toString();

    }
}
