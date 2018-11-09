package edu.wisc.hr.dm.ernstmt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.joda.time.LocalDate;

/**
 * JavaBean for a simplified cross-source (both HRS and Cypress) model for an
 * earnings statement.
 *
 * A conceptual successor to the `EarningStatement`, which was generated from an
 * XSD and included Cypress-specific assumptions.
 *
 */
public class SimpleEarningsStatement {

  private DateFormat unitedStatesDateFormat = new SimpleDateFormat("MM/dd/yyyy");

  public LocalDate getDateOfCheck() {
    return dateOfCheck;
  }

  public void setDateOfCheck(LocalDate dateOfCheck) {
    this.dateOfCheck = dateOfCheck;
  }

  private LocalDate dateOfCheck;


  /**
   * Display representation of date check was cut, e.g. `08/06/2018`.
   * Included because the front end currently assumes this format, including in
   * front-end sorting.
   *
   * Or `unknown` if date unknown. Never null.
   *
   * Named `paid` because that is what the front end expected this property to be named in the
   * predecessor `EarningStatement` data model.
   */
  public String getPaid() {
    if (null == dateOfCheck) {
      return "unknown";
    } else {
      return unitedStatesDateFormat.format(this.dateOfCheck.toDate());
    }
  }

  /**
   * Decimal representation of net pay in dollars, with currency symbol,
   * e.g. `$1234.56`
   *
   * Named `amount` because that was the name in the predecessor data model and so the front end
   * expects it.
   */
  private String amount;

  /**
   * Description of the period during which the earnings were earned.
   *
   * Named `earned` because that was the name in the predecessor data model so the front end expects
   * this.
   */
  public String getEarned() {
    return earned;
  }

  public void setEarned(String earned) {
    this.earned = earned;
  }


  private String earned;

  /**
   * The URL at which an authorized user would access the statement.
   */
  private String url;

  /**
   * ISO representation of date check was cut, e.g. `2018-08-06`,
   * or `unknown` if date unknown. Never null.
   * Useful for chronologically sorting earnings statements.
   */
  public String getIsoDateOfCheck() {
    if (null == this.dateOfCheck) {
      return "unknown";
    } else {
      return this.dateOfCheck.toString();
    }
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
