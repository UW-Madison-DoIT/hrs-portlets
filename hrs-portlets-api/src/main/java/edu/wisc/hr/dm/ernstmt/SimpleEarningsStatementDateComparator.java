package edu.wisc.hr.dm.ernstmt;

import java.util.Comparator;

public class SimpleEarningsStatementDateComparator
  implements Comparator<SimpleEarningsStatement> {

  @Override
  public int compare(SimpleEarningsStatement o1, SimpleEarningsStatement o2) {

    if (o1 == null || o2 == null) {
      throw new NullPointerException(
          "Cannot compare null earnings statement by date.");
    }

    if (null == o1.getDateOfCheck() || null == o2.getDateOfCheck()) {
      throw new NullPointerException(
          "Cannot compare by date earnings statements with null date of check.");
    }

    return o1.getDateOfCheck().compareTo(o2.getDateOfCheck());
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof SimpleEarningsStatementDateComparator;
  }
}
