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

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateDao;
import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateNotifier;
import edu.wisc.hr.dao.person.ContactInfoDao;
import edu.wisc.hr.dm.bnsemail.PreferredEmail;
import edu.wisc.hr.dm.person.PersonInformation;

/**
 * @author Eric Dalquist
 */
@Repository
public class JdbcBusinessEmailUpdateDao implements BusinessEmailUpdateDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private BusinessEmailUpdateNotifier businessEmailUpdateNotifier;
    private NamedParameterJdbcOperations jdbcTemplate;
    private UpdatePreferredEmailProcedure updatePreferredEmail;
    private ContactInfoDao contactInfoDao;

    @Autowired
    public void setUpdatePreferredEmail(UpdatePreferredEmailProcedure updatePreferredEmail) {
        this.updatePreferredEmail = updatePreferredEmail;
    }

    @Autowired
	public void setContactInfoDao(ContactInfoDao contactInfoDao) {
		this.contactInfoDao = contactInfoDao;
	}

	@Autowired
    public void setBusinessEmailUpdateNotifier(BusinessEmailUpdateNotifier businessEmailUpdateNotifier) {
        this.businessEmailUpdateNotifier = businessEmailUpdateNotifier;
    }

	@Autowired
    public void setJdbcTemplate(@Qualifier("bnsemail") NamedParameterJdbcOperations jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /* (non-Javadoc)
     * @see edu.wisc.bnsemail.dao.BusinessEmailUpdateDao#getPreferedEmail(java.lang.String)
     */
    @Override
    public PreferredEmail getPreferedEmail(final String emplId) {
        final Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("pvi", null);
        args.put("emplid", emplId);
        
        return (PreferredEmail)this.jdbcTemplate.queryForObject(
                "select name, email, emplid, message from table(MSNHREMAIL.GET_PREFERRED_EMAIL(:pvi, :emplid))", 
                args, 
                PreferredEmailRowMapper.INTANCE);
    }
    
    /* (non-Javadoc)
     * @see edu.wisc.bnsemail.dao.BusinessEmailUpdateDao#updatePreferedEmail(java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public void updatePreferedEmail(String emplId, String email) {
        final PreferredEmail preferedEmail = this.getPreferedEmail(emplId);
        
        final String oldAddress;
        if (preferedEmail != null) {
        	oldAddress = preferedEmail.getEmail();
        }
        else {
        	final PersonInformation personalData = this.contactInfoDao.getPersonalData(emplId);
        	oldAddress = personalData.getEmail();
        }
        
        //If the email addresses do not match do the update and send an update notification
		if (preferedEmail == null || !email.equals(oldAddress)) {
            this.logger.info("Changing email address for {} from {} to {}", new Object[] { emplId, oldAddress, email });
            
            this.updatePreferredEmail.updatePreferredEmailByEmplId(emplId, email);
            this.businessEmailUpdateNotifier.notifyEmailUpdated(oldAddress, email);
        }
    }
}

