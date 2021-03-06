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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.wisc.hr.dm.bnsemail.PreferredEmail;

/**
 * @author Eric Dalquist
 */
final class PreferredEmailRowMapper implements RowMapper {
    public static final PreferredEmailRowMapper INTANCE = new PreferredEmailRowMapper();
    
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        final PreferredEmail preferredEmail = new PreferredEmail();
        
        preferredEmail.setName(rs.getString("name"));
        preferredEmail.setEmail(rs.getString("email"));
        preferredEmail.setEmplid(rs.getString("emplid"));
        preferredEmail.setMessage(rs.getString("message"));
        
        return preferredEmail;
    }
}