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

import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * 
msnhremail.UPDATE_FROM_PORTAL(p_email_i IN msn_hr_email.email_address%TYPE
                             , p_pvi_i in VARCHAR2 DEFAULT NULL
                             , p_skv_i in msn_hr_email.source_key_value%TYPE DEFAULT NULL
                                             )

 * @author Eric Dalquist
 */
public class UpdatePreferredEmailProcedure extends StoredProcedure {

    public UpdatePreferredEmailProcedure(DataSource dataSource) {
        super(dataSource, "msnhremail.UPDATE_FROM_PORTAL");
        
        this.declareParameter(new SqlParameter("email", Types.VARCHAR));
        this.declareParameter(new SqlParameter("pvi", Types.VARCHAR));
        this.declareParameter(new SqlParameter("emplid", Types.VARCHAR));
        
        this.compile();
    }

    public void updatePreferredEmailByEmplId(String emplId, String email) {
        this.updatePrefferedEmail(null, emplId, email);
    }
    
    public void updatePreferredEmailByPvi(String pvi, String email) {
        this.updatePrefferedEmail(pvi, null, email);
    }
    
    private void updatePrefferedEmail(String pvi, String emplId, String email) {
        final Map<String, String> args = new LinkedHashMap<String, String>();
        args.put("email", email);
        args.put("pvi", pvi);
        args.put("emplid", emplId);
        
        this.execute(args);
    }
}
