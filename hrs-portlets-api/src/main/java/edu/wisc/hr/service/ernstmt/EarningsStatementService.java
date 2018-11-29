package edu.wisc.hr.service.ernstmt;

import edu.wisc.hr.dm.ernstmt.RetrievedEarningsStatements;

public interface EarningsStatementService {

  RetrievedEarningsStatements statementsForEmplid(String emplid);

}
