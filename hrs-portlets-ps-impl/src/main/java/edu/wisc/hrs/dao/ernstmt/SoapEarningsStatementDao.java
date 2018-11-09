
package edu.wisc.hrs.dao.ernstmt;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.DecoratedCacheType;
import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dao.url.HrsUrlDao;
import edu.wisc.hr.dm.ernstmt.EarningsPeriod;
import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import edu.wisc.hrs.dao.BaseHrsSoapDao;
import edu.wisc.hrs.dao.HrsUtils;
import edu.wisc.hrs.xdm.ernstmt.req.EMPLIDTypeShape;
import edu.wisc.hrs.xdm.ernstmt.req.GetCompIntfcUWPYGETERNSTLSCI;
import edu.wisc.hrs.xdm.ernstmt.res.CHECKDTTypeShape;
import edu.wisc.hrs.xdm.ernstmt.res.GetCompIntfcUWPYGETERNSTLSCIResponse;
import edu.wisc.hrs.xdm.ernstmt.res.NETPAYTypeShape;
import edu.wisc.hrs.xdm.ernstmt.res.PAYBEGINDTTypeShape;
import edu.wisc.hrs.xdm.ernstmt.res.PAYCHECKNBRTypeShape;
import edu.wisc.hrs.xdm.ernstmt.res.PAYENDDTTypeShape;
import edu.wisc.hrs.xdm.ernstmt.res.UWPYERNSTLSPCComplexTypeShape;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.ws.client.core.WebServiceOperations;

@Repository("soapEarningsStatementDao")
public class SoapEarningsStatementDao
  extends BaseHrsSoapDao
  implements SimpleEarningsStatementDao {

  public HrsUrlDao getHrsUrlDao() {
    return hrsUrlDao;
  }

  @Autowired
  public void setHrsUrlDao(HrsUrlDao hrsUrlDao) {
    this.hrsUrlDao = hrsUrlDao;
  }

  private HrsUrlDao hrsUrlDao;


  private WebServiceOperations webServiceOperations;

  @Autowired
  public void setWebServiceOperations(
      @Qualifier("earningsStatementWebServiceTemplate")
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
  public List<SimpleEarningsStatement> statementsForEmployee(final String emplid) {

    if (null == emplid) {
      throw new NullPointerException(
          "Cannot query earnings statements for null emplid.");
    }

    final EMPLIDTypeShape emplidInTypeShape = HrsUtils.createValue(EMPLIDTypeShape.class, emplid);

    final GetCompIntfcUWPYGETERNSTLSCI request = new GetCompIntfcUWPYGETERNSTLSCI();
    request.setEMPLID(emplidInTypeShape);

    final GetCompIntfcUWPYGETERNSTLSCIResponse response = this.internalInvoke(request);

    return this.convertSoapResponseToDataTransferObjects(response);
  }

  protected List<SimpleEarningsStatement> convertSoapResponseToDataTransferObjects(
      final GetCompIntfcUWPYGETERNSTLSCIResponse response) {

    final List<UWPYERNSTLSPCComplexTypeShape> soapEarningsStatements = response.getUWPYERNSTLSPCS();

    final List<SimpleEarningsStatement> simpleEarningsStatements =
        new ArrayList<SimpleEarningsStatement>();

    for( final UWPYERNSTLSPCComplexTypeShape soapEarningsStatement : soapEarningsStatements) {

      final SimpleEarningsStatement simpleEarningsStatement = new SimpleEarningsStatement();

      final CHECKDTTypeShape checkDate = soapEarningsStatement.getCHECKDT();
      final DateMidnight checkDateMidnight = checkDate.getValue();
      simpleEarningsStatement.setDateOfCheck(checkDateMidnight.toLocalDate());

      final NETPAYTypeShape soapNetPay = soapEarningsStatement.getNETPAY();
      simpleEarningsStatement.setAmount( "$" + soapNetPay.getValue().toString());

      final PAYBEGINDTTypeShape soapPayPeriodBeginDate = soapEarningsStatement.getPAYBEGINDT();
      final PAYENDDTTypeShape soapPayPeriodEndDate = soapEarningsStatement.getPAYENDDT();
      final EarningsPeriod earningsPeriod =
          new EarningsPeriod(soapPayPeriodBeginDate.getValue(), soapPayPeriodEndDate.getValue());
      simpleEarningsStatement.setEarned(earningsPeriod.toString());

      final String baseUrl = this.hrsUrlDao.getHrsUrls().get(HrsUrlDao.EARNINGS_STATEMENT_KEY);
      final PAYCHECKNBRTypeShape soapPaycheckNumber = soapEarningsStatement.getPAYCHECKNBR();
      final int paycheckNumber = soapPaycheckNumber.getValue();
      simpleEarningsStatement.setUrl(baseUrl + "?paycheck_nbr=" + paycheckNumber);

      simpleEarningsStatements.add(simpleEarningsStatement);

    }

    return simpleEarningsStatements;

  }

}
