package edu.wisc.hr.dm.ernstmt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.joda.time.DateMidnight;

public class EarningsPeriod {

  private DateFormat unitedStatesFormat = new SimpleDateFormat("MM/dd/yyyy");

  private DateMidnight earningsPeriodStart;

  private DateMidnight earningsPeriodEnd;

  public EarningsPeriod(final DateMidnight start, final DateMidnight end) {

    if (null == start) {
      throw new NullPointerException("Earnings period start date cannot be null.");
    }

    if (null == end ) {
      throw new NullPointerException("Earnings period end date cannot be null.");
    }

    this.earningsPeriodStart = start;
    this.earningsPeriodEnd = end;
  }

  /*
   * "10/28/2018 - 11/10/2018"
   */
  @Override
  public String toString() {

    return unitedStatesFormat.format(earningsPeriodStart.toDate()) + " - "
        + unitedStatesFormat.format(earningsPeriodEnd.toDate());

  }

}
