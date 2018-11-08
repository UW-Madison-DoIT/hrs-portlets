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
package edu.wisc.hrs.dao.abshis;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.client.WebServiceFaultException;

import edu.wisc.hr.dm.abshis.AbsenceHistory;
import edu.wisc.hrs.dao.BaseSoapDaoTest;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/integrationPeopleSoftTestContext.xml")
public class SoapAbsenceHistoryDaoIT extends BaseSoapDaoTest {
    @Autowired
    protected SoapAbsenceHistoryDao client;
    
    @Test
    public void testAbsenceHistory() throws Exception {
        final List<AbsenceHistory> absenceHistories = client.getAbsenceHistory("00176463");
        this.verifyMappedData(absenceHistories);
    }
    
    @Test(expected=WebServiceFaultException.class)
    public void testGetMissingInfo() throws Exception {
        client.getAbsenceHistory("00000000");
    }

    protected void verifyMappedData(List<AbsenceHistory> absenceHistories) {
        assertNotNull(absenceHistories);
        assertEquals(0, absenceHistories.size());
    }
}
