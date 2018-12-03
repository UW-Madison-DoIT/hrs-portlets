package edu.wisc.hr.service.ernstmt;

import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dao.url.HrsUrlDao;
import edu.wisc.hr.dm.ernstmt.RetrievedEarningsStatements;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Merging earnings statement service that collects earnings statements from both HRS and Cypress,
 * merging these together into a single bundle of earnings statements.
 *
 * Both DAOs are optional. Turn off HRS or Cypress as earnings statement source by not setting that
 * DAO. Additionally, will not query HRS earnings statements if HrsUrlDao.EARNINGS_STATEMENT_KEY is
 * not set, so HRS earnings statement support can be held back by holding back setting that URL.
 */
public class MergingEarningsStatementService
  implements EarningsStatementService {

  private SimpleEarningsStatementDao hrsEarningsStatementDao;

  private SimpleEarningsStatementDao cypressEarningsStatementDao;

  private HrsUrlDao urlDao;

  @Override
  public RetrievedEarningsStatements statementsForEmplid(String emplid) {

    final RetrievedEarningsStatements retrievedEarningsStatements =
        new RetrievedEarningsStatements();

    // only try to get HRS earnings statements if the HRS "Earning Statement" URL is configured.

    String hrsEarningsStatementUrl = urlDao.getHrsUrls().get(HrsUrlDao.EARNINGS_STATEMENT_KEY);

    if (null != hrsEarningsStatementDao && StringUtils.hasText(hrsEarningsStatementUrl)) {
      try {
        retrievedEarningsStatements.addStatements(
            hrsEarningsStatementDao.statementsForEmployee(emplid));
      } catch (Exception e) {
        retrievedEarningsStatements.registerError(e);
      }
    }

    if (null != cypressEarningsStatementDao) {
      try {
        retrievedEarningsStatements.addStatements(
            cypressEarningsStatementDao.statementsForEmployee(emplid));
      } catch (Exception e) {
        retrievedEarningsStatements.registerError(e);
      }
    }

    return retrievedEarningsStatements;
  }

  public void setHrsEarningsStatementDao(
      SimpleEarningsStatementDao hrsEarningsStatementDao) {
    this.hrsEarningsStatementDao = hrsEarningsStatementDao;
  }

  public void setCypressEarningsStatementDao(
      SimpleEarningsStatementDao cypressEarningsStatementDao) {
    this.cypressEarningsStatementDao = cypressEarningsStatementDao;
  }

  @Autowired
  public void setUrlDao(HrsUrlDao urlDao) {
    this.urlDao = urlDao;
  }

}
