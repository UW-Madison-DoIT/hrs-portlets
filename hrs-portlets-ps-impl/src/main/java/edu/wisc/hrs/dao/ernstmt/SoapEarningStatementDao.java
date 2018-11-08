
package edu.wisc.hrs.dao.ernstmt;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;
import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

@Repository("soapEarningStatementDao")
public class SoapEarningStatementDao
  extends BaseHrsSoapDao
  implements SimpleEarningsStatementDao {


  private WebServiceOperations webServiceOperations;

  @Autowired
  public void setWebServiceOperations(
      @Qualifier("earningsStatementsWebServiceTemplate")
          WebServiceOperations webServiceOperations) {
    this.webServiceOperations = webServiceOperations;
  }

  @Override
  protected WebServiceOperations getWebServiceOperations() {
    return this.webServiceOperations;
  }

  @Override
  @Cacheable(cacheName="peopleSoftEarningsStatement",
      decoratedCacheType= DecoratedCacheType.SELF_POPULATING_CACHE,
      selfPopulatingTimeout=20000,
      exceptionCacheName="hrsUnknownExceptionCache")
  public List<SimpleEarningsStatement> statementsForEmployee(String emplid) {

    if (null == emplid) {
      throw new NullPointerException(
          "Cannot query earnings statements for null emplid.");
    }



    return null;
  }

  protected List<SimpleEarningsStatement> convertSoapResponseToDataTransferObjects(final GetCompIntfcUWPYGETERNSTLSResponse response) {


  }

}
