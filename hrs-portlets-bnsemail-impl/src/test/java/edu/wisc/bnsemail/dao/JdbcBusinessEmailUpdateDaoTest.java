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

package edu.wisc.bnsemail.dao;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateDao;
import edu.wisc.hr.dm.bnsemail.PreferredEmail;

/**
 * @author Eric Dalquist
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/testBuisnessEmailUpdateContext.xml")
public class JdbcBusinessEmailUpdateDaoTest {
    @Autowired
    private BusinessEmailUpdateDao businessEmailUpdateDao;
    
    @Test
    public void testGetCurrentBuisnessEmail() throws Exception {
        final PreferredEmail email = businessEmailUpdateDao.getPreferedEmail("00481400");
        
        assertNotNull(email);
        assertEquals("ERIC B DALQUIST", email.getName());
        assertEquals("ERIC.DALQUIST@DOIT.WISC.EDU", email.getEmail());
        assertEquals("00481400", email.getEmplid());
        assertNull(email.getMessage());
    }
    
    @Test
    public void testUpdateCurrentBuisnessEmail() throws Exception {
        businessEmailUpdateDao.updatePreferedEmail("00481400", "ERIC.DALQUIST@DOIT.WISC.EDU");
    }
}
