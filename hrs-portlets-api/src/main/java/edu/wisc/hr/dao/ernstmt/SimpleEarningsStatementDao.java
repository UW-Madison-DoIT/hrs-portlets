package edu.wisc.hr.dao.ernstmt;

import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import java.util.List;

public interface SimpleEarningsStatementDao {

  /**
   * Get the potentially empty non-null List of SimpleEarningsStatement s for
   * an employee specified by emplid.
   *
   * List MUST be ordered by date in reverse chronological order; that is, the
   * first earnings statement in the List is the most recent.
   *
   * @param emplid
   * @return non-null potentially empty List
   */
  public List<SimpleEarningsStatement> statementsForEmployee(String emplid);

}
