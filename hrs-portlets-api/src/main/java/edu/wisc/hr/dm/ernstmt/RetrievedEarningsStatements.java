package edu.wisc.hr.dm.ernstmt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RetrievedEarningsStatements {

  private List<Exception> errors = new ArrayList<Exception>();

  private List<SimpleEarningsStatement> statements = new ArrayList<SimpleEarningsStatement>();

  public void registerError(final Exception error) {
    this.errors.add(error);
  }

  public void addStatements(final Collection<SimpleEarningsStatement> statementsToAdd) {
    if (null != statementsToAdd) {
      this.statements.addAll(statementsToAdd);

      Collections.sort(this.statements, new SimpleEarningsStatementDateComparator());
      // keep the statements sorted in reverse chronological order
      Collections.reverse(this.statements);
    }
  }

  public boolean isErrored() {
    return ! this.errors.isEmpty();
  }

  public List<Exception> getErrors() {
    return errors;
  }

  /**
   * Get the statements in reverse chronological order.
   * @return
   */
  public List<SimpleEarningsStatement> getStatements() {
    return statements;
  }

}
