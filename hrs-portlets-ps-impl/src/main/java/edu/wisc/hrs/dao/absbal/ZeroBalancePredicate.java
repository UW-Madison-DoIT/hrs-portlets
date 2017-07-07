/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package edu.wisc.hrs.dao.absbal;

import com.google.common.base.Predicate;
import edu.wisc.hr.dm.absbal.AbsenceBalance;

import java.math.BigDecimal;

/**
 * Predicate over AbsenceBalance s returning true when the balance for the entitlement is zero.
 */
public class ZeroBalancePredicate
    implements Predicate {

    @Override
    public boolean apply(final Object input) {

        if (null == input) {
            throw new NullPointerException("ZeroBalancePredicate ill defined over null input.");
        }

        final AbsenceBalance absenceBalance = (AbsenceBalance) input;

        final BigDecimal balance = absenceBalance.getBalance();

        return (BigDecimal.ZERO.equals(balance));

    }
}
