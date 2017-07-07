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

import static org.junit.Assert.*;


public class ClassifiedFurloughAllocatedPredicateTest {

    private final Predicate classifiedFurloughAllocatedPredicate =
        new ClassifiedFurloughAllocatedPredicate();


    @Test
    public void trueForClassifiedFurloughAllocated() {

        final AbsenceBalance classifiedFurloughAllocatedAbsenceBalance = new AbsenceBalance();
        classifiedFurloughAllocatedAbsenceBalance.setEntitlement("Classified Furlough Allocated");

        assertTrue(classifiedFurloughAllocatedPredicate.
            apply(classifiedFurloughAllocatedAbsenceBalance));
    }

    /**
     * The predicate will not inadvertently match hypothetical future absence balances that are
     * near to but not exactly ClassifiedFurloughAllocated.
     */
    @Test
    public void falseForVariousOtherEntitlements() {

        final AbsenceBalance notQuiteClassifiedFurloughAllocated = new AbsenceBalance();

        notQuiteClassifiedFurloughAllocated.setEntitlement("Classified Furlough Unallocated");
        assertFalse(classifiedFurloughAllocatedPredicate.apply
            (notQuiteClassifiedFurloughAllocated));

        notQuiteClassifiedFurloughAllocated.setEntitlement("Academic Furlough Allocated");
        assertFalse(classifiedFurloughAllocatedPredicate.apply
            (notQuiteClassifiedFurloughAllocated));

        notQuiteClassifiedFurloughAllocated.setEntitlement("University Staff Furlough Allocated");
        assertFalse(classifiedFurloughAllocatedPredicate.apply
            (notQuiteClassifiedFurloughAllocated));

        notQuiteClassifiedFurloughAllocated.setEntitlement("Academic Staff Furlough Allocated");
        assertFalse(classifiedFurloughAllocatedPredicate.apply
            (notQuiteClassifiedFurloughAllocated));

        notQuiteClassifiedFurloughAllocated.setEntitlement("Some other hypothetical furlough");
        assertFalse(classifiedFurloughAllocatedPredicate.apply
            (notQuiteClassifiedFurloughAllocated));
    }

    @Test(expected=NullPointerException.class)
    public void throwsNullPointerExceptionOnNullInput() {

        classifiedFurloughAllocatedPredicate.apply(null);

    }

}
