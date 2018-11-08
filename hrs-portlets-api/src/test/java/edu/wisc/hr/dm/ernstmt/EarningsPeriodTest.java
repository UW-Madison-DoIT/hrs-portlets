package edu.wisc.hr.dm.ernstmt;

import static org.junit.Assert.*;

import org.joda.time.DateMidnight;
import org.junit.Test;

public class EarningsPeriodTest {

  @Test
  public void toStringPrintsPrettyRepresenationOfPeriod() {
    DateMidnight start = DateMidnight.parse("2018-10-28");
    DateMidnight end = DateMidnight.parse("2018-11-10");

    EarningsPeriod period = new EarningsPeriod(start, end);

    assertEquals("10/28/2018 - 11/10/2018", period.toString());

  }

  @Test(expected = NullPointerException.class)
  public void nullStartDateThrowsNullPointerExceptionFromConstructor() {

    new EarningsPeriod(null, DateMidnight.now());
  }

  @Test(expected = NullPointerException.class)
  public void nullEndDateThrowsNullPointerExceptionFromConstructor() {

    new EarningsPeriod(DateMidnight.now(), null);
  }

}
