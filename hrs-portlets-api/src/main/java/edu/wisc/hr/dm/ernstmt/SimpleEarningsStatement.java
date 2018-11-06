package edu.wisc.hr.dm.ernstmt;

/**
 * JavaBean for a simplified cross-source (both HRS and Cypress) model for an
 * earnings statement.
 *
 * A conceptual successor to the `EarningStatement`, which was generated from an
 * XSD and included Cypress-specific assumptions.
 *
 */
public class SimpleEarningsStatement {

  /**
   * ISO representation of date check was cut, e.g. `2018-11-06`
   */
  private String dateOfCheck;

  /**
   * Decimal representation of net pay in dollars, e.g. `1234.56`
   */
  private String amountNetPay;

  /**
   * ISO representation of begin date of earnings period for which this is a
   * statement. e.g. `2018-10-10`
   */
  private String dateEarnedStart;


  /**
   * ISO representation of end date of earnings period for which this is a
   * statement. e.g. `2018-10-24`.
   */
  private String dateEarnedEnd;

  /**
   * The URL at which an authorized user would access the statement.
   */
  private String url;

  public String getDateOfCheck() {
    return dateOfCheck;
  }

  public void setDateOfCheck(String dateOfCheck) {
    this.dateOfCheck = dateOfCheck;
  }

  public String getAmountNetPay() {
    return amountNetPay;
  }

  public void setAmountNetPay(String amountNetPay) {
    this.amountNetPay = amountNetPay;
  }

  public String getDateEarnedStart() {
    return dateEarnedStart;
  }

  public void setDateEarnedStart(String dateEarnedStart) {
    this.dateEarnedStart = dateEarnedStart;
  }

  public String getDateEarnedEnd() {
    return dateEarnedEnd;
  }

  public void setDateEarnedEnd(String dateEarnedEnd) {
    this.dateEarnedEnd = dateEarnedEnd;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
