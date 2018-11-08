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

package edu.wisc.portlet.hrs.ws.dao.soap;

import static junit.framework.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

/**
 * @author Eric Dalquist
 */
public class UrIModificationTest {
    @Test
    public void testUriModification() throws Exception {
        final URI baseUri = new URI("https://www.hrs.wisconsin.edu/hrs-ib/PSIGW/PeopleSoftServiceListeningConnector");
        assertEquals("https://www.hrs.wisconsin.edu/hrs-ib/PSIGW/PeopleSoftServiceListeningConnector", baseUri.toString());
        
        final URI newUri = new URI(baseUri.getScheme(), baseUri.getUserInfo(), baseUri.getHost(), 444, baseUri.getPath(), baseUri.getQuery(), baseUri.getFragment());
        assertEquals("https://www.hrs.wisconsin.edu:444/hrs-ib/PSIGW/PeopleSoftServiceListeningConnector", newUri.toString());
    }
}
