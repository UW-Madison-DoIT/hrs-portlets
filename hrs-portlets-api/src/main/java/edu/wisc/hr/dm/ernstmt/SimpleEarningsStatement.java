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
   * ISO representation of date check was cut, e.g. `2018-08-06`,
   * or `unknown` if date unknown. Never null.
   */
  private String isoDateOfCheck = "unknown";

  public String getUsDateOfCheck() {
    return usDateOfCheck;
  }

  public void setUsDateOfCheck(String usDateOfCheck) {
    this.usDateOfCheck = usDateOfCheck;
  }

  /**
   * Display representation of date check was cut, e.g. `08/06/2018`.
   * Included because the front end currently assumes this format, including in
   * front-end sorting.
   *
   * Or `unknown` if date unknown. Never null.
   */
  private String usDateOfCheck = "unknown";

  /**
   * Decimal representation of net pay in dollars, with currency symbol,
   * e.g. `$1234.56`
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

  public String getIsoDateOfCheck() {
    return isoDateOfCheck;
  }

  public void setIsoDateOfCheck(String isoDateOfCheck) {
    this.isoDateOfCheck = isoDateOfCheck;
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
