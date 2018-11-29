package edu.wisc.hr.service.ernstmt;

import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dm.ernstmt.RetrievedEarningsStatements;
import java.util.List;

public class MergingEarningsStatementService
  implements EarningsStatementService {

  private List<SimpleEarningsStatementDao> daos;

  @Override
  public RetrievedEarningsStatements statementsForEmplid(String emplid) {

    final RetrievedEarningsStatements retrievedEarningsStatements =
        new RetrievedEarningsStatements();

    for (SimpleEarningsStatementDao dao : daos) {
      try {
        retrievedEarningsStatements.addStatements(dao.statementsForEmployee(emplid));
      } catch (Exception e) {
        retrievedEarningsStatements.registerError(e);
      }
    }

    return retrievedEarningsStatements;
  }

  public List<SimpleEarningsStatementDao> getDaos() {
    return daos;
  }

  public void setDaos(List<SimpleEarningsStatementDao> daos) {
    this.daos = daos;
  }
}
