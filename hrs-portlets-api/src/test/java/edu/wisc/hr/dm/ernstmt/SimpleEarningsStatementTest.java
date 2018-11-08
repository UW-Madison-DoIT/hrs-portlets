package edu.wisc.hr.dm.ernstmt;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;

public class SimpleEarningsStatementTest {
  
  @Test
  public void exposesCheckDateThreeWays() {
    LocalDate date = LocalDate.parse("2018-11-08");

    SimpleEarningsStatement statement = new SimpleEarningsStatement();
    statement.setDateOfCheck(date);

    assertEquals(date, statement.getDateOfCheck());
    assertEquals("2018-11-08", statement.getIsoDateOfCheck());
    assertEquals("11/08/2018", statement.getUsDateOfCheck());

  }

  @Test
  public void isoCheckDateDefaultsToUnknown() {
    assertEquals("unknown", new SimpleEarningsStatement().getIsoDateOfCheck());
  }

  @Test
  public void usCheckDateDefaultsToUnknown() {
    assertEquals("unknown", new SimpleEarningsStatement().getUsDateOfCheck());
  }

}
