package edu.wisc.cypress.dao.ernstmt;

import static org.junit.Assert.*;

import edu.wisc.hr.dm.ernstmt.EarningStatement;
import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import java.math.BigInteger;
import org.joda.time.LocalDate;
import org.junit.Test;


public class CypressEarningStatementToSimpleEarningStatementConverterTest {

  @Test(expected = NullPointerException.class)
  public void convertingNullStatementThrowsNullPointerException() {

    CypressEarningStatementToSimpleEarningStatementConverter converter =
        new CypressEarningStatementToSimpleEarningStatementConverter("someFName");

    converter.convertToSimpleEarningsStatement(null);
  }

  @Test
  public void convertsSampleCypressEarningsStatement() {

    CypressEarningStatementToSimpleEarningStatementConverter converter =
        new CypressEarningStatementToSimpleEarningStatementConverter("someFName");

    EarningStatement sampleCypressStatement = new EarningStatement();
    // example data evolved from
    // /hrs-portlets-cypress-impl/test/resources/sampleData/earning-statements.xml
    sampleCypressStatement.setAmount("$4,625.12");
    sampleCypressStatement.setDocId(BigInteger.valueOf(8421763));
    sampleCypressStatement.setEarned("OCT 10");
    sampleCypressStatement.setFullTitle(
        "PAID:11/01/10_EARNED:EARNED OCT 10_Net: $4,625.12");
    sampleCypressStatement.setPaid("11/01/2010");

    SimpleEarningsStatement convertedStatement =
        converter.convertToSimpleEarningsStatement(sampleCypressStatement);

    sampleCypressStatement = null; // ensure test doesn't reference input directly

    assertNull(sampleCypressStatement);

    assertNotNull(convertedStatement);

    assertEquals("$4,625.12", convertedStatement.getAmount());

    assertEquals("OCT 10", convertedStatement.getEarned());

    assertEquals(new LocalDate("2010-11-01"), convertedStatement.getDateOfCheck());
    assertEquals("2010-11-01", convertedStatement.getIsoDateOfCheck());

    assertEquals("/portal/p/someFName/exclusive/earning_statement.pdf.resource.uP?pP_docId=8421763",
        convertedStatement.getUrl());
  }

  @Test
  public void failsGracefullyOnBogusPaidDate() {
    CypressEarningStatementToSimpleEarningStatementConverter converter =
        new CypressEarningStatementToSimpleEarningStatementConverter("someFName");

    EarningStatement sampleCypressStatement = new EarningStatement();
    // example data from
    // /hrs-portlets-cypress-impl/test/resources/sampleData/earning-statements.xml
    sampleCypressStatement.setAmount(" $4,625.12");
    sampleCypressStatement.setDocId(BigInteger.valueOf(8421763));
    sampleCypressStatement.setEarned(" OCT 10");
    sampleCypressStatement.setFullTitle(
        "PAID:11/01/10_EARNED:EARNED OCT 10_Net: $4,625.12");
    sampleCypressStatement.setPaid("garbage-in");

    SimpleEarningsStatement convertedStatement =
        converter.convertToSimpleEarningsStatement(sampleCypressStatement);

    assertNotNull(convertedStatement);
    assertEquals("unknown", convertedStatement.getIsoDateOfCheck());
    assertEquals("unknown", convertedStatement.getPaid());
  }

}
