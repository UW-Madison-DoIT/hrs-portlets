package edu.wisc.cypress.dao.ernstmt;

import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.joda.time.LocalDate;

public class CypressEarningStatementToSimpleEarningStatementConverter {

  private final String payrollInformationPortletFname;

  public CypressEarningStatementToSimpleEarningStatementConverter(
      String payrollInformationPortletFname) {
    this.payrollInformationPortletFname = payrollInformationPortletFname;
  }

  public SimpleEarningsStatement
    convertToSimpleEarningsStatement(EarningStatement cypressEarningStatement) {

    if (null == cypressEarningStatement) {
      throw new NullPointerException("Cannot convert a null statement.");
    }

    SimpleEarningsStatement convertedStatement = new SimpleEarningsStatement();

    try {
      String mmddyyyyPaid = cypressEarningStatement.getPaid();

      SimpleDateFormat mmddyyyyFormat = new SimpleDateFormat("mm/dd/yyyy");

      Date datePaid = mmddyyyyFormat.parse(mmddyyyyPaid);

      convertedStatement.setDateOfCheck(LocalDate.fromDateFields(datePaid));

    } catch (Exception e) {
      // leave the statement dateless
    }

    convertedStatement.setAmount(cypressEarningStatement.getAmount());
    convertedStatement.setEarned(cypressEarningStatement.getEarned());

    // https://my.wisc.edu/portal/f/u360303l1s4/p/earnings-statement.u360303l1n9/exclusive/earning_statement.pdf.resource.uP?pP_docId=27404180

    String statementUrl = "/portal/p/" + payrollInformationPortletFname +
        "/exclusive/earning_statement.pdf.resource.uP?pP_docId=" +
        cypressEarningStatement.getDocId();

    convertedStatement.setUrl(statementUrl);

    return convertedStatement;
  }

}
