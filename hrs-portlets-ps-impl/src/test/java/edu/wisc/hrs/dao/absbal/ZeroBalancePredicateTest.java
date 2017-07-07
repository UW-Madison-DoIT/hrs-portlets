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
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;


public class ZeroBalancePredicateTest {

    private Predicate zeroBalancePredicate = new ZeroBalancePredicate();

    /**
     * Test that predicate returns true for a zero-balance input.
     */
    @Test
    public void trueOnZeroBalanceInput() {

        final AbsenceBalance zeroBalanceEntitlement = new AbsenceBalance();

        zeroBalanceEntitlement.setEntitlement("Parental leave");
        zeroBalanceEntitlement.setBalance(BigDecimal.ZERO);

        assertTrue(zeroBalancePredicate.apply(zeroBalanceEntitlement));
    }

    /**
     * Test that predicate returns false for a non-zero-balance input.
     */
    @Test
    public void falseOnNonzeroBalanceInput() {

        final AbsenceBalance oneBalanceEntitlement = new AbsenceBalance();

        oneBalanceEntitlement.setEntitlement("Jury duty");
        oneBalanceEntitlement.setBalance(BigDecimal.ONE);

        assertFalse(zeroBalancePredicate.apply(oneBalanceEntitlement));

    }

    /**
     * Test that throws NPE on null input,
     * as specified by Predicate.apply() API.
     */
    @Test(expected=NullPointerException.class)
    public void throwsNullPointerExceptionOnNullInput() {

        zeroBalancePredicate.apply(null);

    }

}
