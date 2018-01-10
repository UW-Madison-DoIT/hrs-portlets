/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
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
package edu.wisc.portlet.hrs.web.benefits;

import com.google.common.collect.Lists;
import edu.wisc.hr.dm.benstmt.BenefitStatement;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class BenefitStatementDataControllerTest {

  /**
   * Test of sortStatements method, of class BenefitStatementDataController.
   */
  @Test
  public void testSortStatements() {
    List<BenefitStatement> actual = Lists.newArrayList(example);
    BenefitStatementDataController.sortStatements(actual);
    
    assertArrayEquals(expected.toArray(), actual.toArray());
  }

  private static BenefitStatement newBenefitStatement(String fullTitle, String name, String docType, int docId, int year) {
    BenefitStatement result = new BenefitStatement();

    result.setFullTitle(fullTitle);
    result.setName(name);
    result.setDocType(docType);
    result.setDocId(BigInteger.valueOf(docId));
    result.setYear(BigInteger.valueOf(year));

    return result;
  }

  private static List<BenefitStatement> example = Collections.unmodifiableList(Lists.<BenefitStatement>newArrayList(
          newBenefitStatement(
                  "2012 ETF Annual Statement of Benefits (WRS) Issued 2013",
                  "ETF Annual Statement of Benefits (WRS) Issued 2013",
                  "etf",
                  17086030,
                  2012
          ),
          newBenefitStatement(
                  "2013 ETF Annual Statement of Benefits (WRS) Issued 2014",
                  "ETF Annual Statement of Benefits (WRS) Issued 2014",
                  "etf",
                  18721487,
                  2013
          ),
          newBenefitStatement(
                  "2017 Benefit Enrollment Confirmation Issued Nov 22, 2016",
                  "2017 Benefit Enrollment Confirmation Issued Nov 22, 2016",
                  "2017 Benefit Confirmation",
                  23288193,
                  2016
          ),
          newBenefitStatement(
                  "2015 ETF Annual Statement of Benefits (WRS) Issued 2016",
                  "ETF Annual Statement of Benefits (WRS) Issued 2016",
                  "etf",
                  22293873,
                  2015
          ),
          newBenefitStatement(
                  "2016 ETF Annual Statement of Benefits (WRS) Issued 2017",
                  "ETF Annual Statement of Benefits (WRS) Issued 2017",
                  "etf",
                  24302943,
                  2016
          ),
          newBenefitStatement(
                  "2015 Benefit Enrollment Confirmation Issued Oct 22, 2014",
                  "2015 Benefit Enrollment Confirmation Issued Oct 22, 2014",
                  "2015 Benefit Confirmation",
                  19510689,
                  2014
          ),
          newBenefitStatement(
                  "2016 Benefit Enrollment Confirmation Issued Oct 31, 2015",
                  "2016 Benefit Enrollment Confirmation Issued Oct 31, 2015",
                  "2016 Benefit Confirmation",
                  21287191,
                  2015
          ),
          newBenefitStatement(
                  "2014 ETF Annual Statement of Benefits (WRS) Issued 2015",
                  "ETF Annual Statement of Benefits (WRS) Issued 2015",
                  "etf",
                  20457984,
                  2014
          ),
          newBenefitStatement(
                  "2017 Benefit Enrollment Confirmation Issued Nov 12, 2016",
                  "2017 Benefit Enrollment Confirmation Issued Nov 12, 2016",
                  "2017 Benefit Confirmation",
                  23246929,
                  2016
          ),
          newBenefitStatement(
                  "2018 Benefit Enrollment Confirmation Issued Oct 30, 2017",
                  "2018 Benefit Enrollment Confirmation Issued Oct 30, 2017",
                  "2018 Benefit Confirmation",
                  25253981,
                  2017
          ),
          newBenefitStatement(
                  "2018 Benefit Enrollment Confirmation Issued Nov 30, 2017",
                  "2018 Benefit Enrollment Confirmation Issued Nov 30, 2017",
                  "2018 Benefit Confirmation",
                  25416532,
                  2017
          )
  ));

  private static List<BenefitStatement> expected = Collections.unmodifiableList(Lists.<BenefitStatement>newArrayList(
          newBenefitStatement(
                  "2018 Benefit Enrollment Confirmation Issued Nov 30, 2017",
                  "2018 Benefit Enrollment Confirmation Issued Nov 30, 2017",
                  "2018 Benefit Confirmation",
                  25416532,
                  2017
          ),
          newBenefitStatement(
                  "2018 Benefit Enrollment Confirmation Issued Oct 30, 2017",
                  "2018 Benefit Enrollment Confirmation Issued Oct 30, 2017",
                  "2018 Benefit Confirmation",
                  25253981,
                  2017
          ),
          newBenefitStatement(
                  "2017 Benefit Enrollment Confirmation Issued Nov 22, 2016",
                  "2017 Benefit Enrollment Confirmation Issued Nov 22, 2016",
                  "2017 Benefit Confirmation",
                  23288193,
                  2016
          ),
          newBenefitStatement(
                  "2017 Benefit Enrollment Confirmation Issued Nov 12, 2016",
                  "2017 Benefit Enrollment Confirmation Issued Nov 12, 2016",
                  "2017 Benefit Confirmation",
                  23246929,
                  2016
          ),
          newBenefitStatement(
                  "2016 Benefit Enrollment Confirmation Issued Oct 31, 2015",
                  "2016 Benefit Enrollment Confirmation Issued Oct 31, 2015",
                  "2016 Benefit Confirmation",
                  21287191,
                  2015
          ),
          newBenefitStatement(
                  "2015 Benefit Enrollment Confirmation Issued Oct 22, 2014",
                  "2015 Benefit Enrollment Confirmation Issued Oct 22, 2014",
                  "2015 Benefit Confirmation",
                  19510689,
                  2014
          ),
          newBenefitStatement(
                  "2016 ETF Annual Statement of Benefits (WRS) Issued 2017",
                  "ETF Annual Statement of Benefits (WRS) Issued 2017",
                  "etf",
                  24302943,
                  2016
          ),
          newBenefitStatement(
                  "2015 ETF Annual Statement of Benefits (WRS) Issued 2016",
                  "ETF Annual Statement of Benefits (WRS) Issued 2016",
                  "etf",
                  22293873,
                  2015
          ),
          newBenefitStatement(
                  "2014 ETF Annual Statement of Benefits (WRS) Issued 2015",
                  "ETF Annual Statement of Benefits (WRS) Issued 2015",
                  "etf",
                  20457984,
                  2014
          ),
          newBenefitStatement(
                  "2013 ETF Annual Statement of Benefits (WRS) Issued 2014",
                  "ETF Annual Statement of Benefits (WRS) Issued 2014",
                  "etf",
                  18721487,
                  2013
          ),
          newBenefitStatement(
                  "2012 ETF Annual Statement of Benefits (WRS) Issued 2013",
                  "ETF Annual Statement of Benefits (WRS) Issued 2013",
                  "etf",
                  17086030,
                  2012
          )
  ));
}
