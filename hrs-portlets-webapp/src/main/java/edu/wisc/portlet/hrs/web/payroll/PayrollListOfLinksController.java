package edu.wisc.portlet.hrs.web.payroll;

import edu.wisc.hr.dao.roles.HrsRolesDao;
import edu.wisc.hr.dao.url.HrsUrlDao;
import edu.wisc.portlet.hrs.web.HrsControllerBase;
import edu.wisc.portlet.hrs.web.listoflinks.Link;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletRequest;
import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class PayrollListOfLinksController
  extends HrsControllerBase {

  public static final String MADISON_FNAME = "earnings-statement";
  public static final String SYSTEM_FNAME = "uw-system-earnings-statement";

  public static final String DIRECT_DEPOSIT_PDF_URL =
      "https://uwservice.wisconsin.edu/docs/forms/pay-direct-deposit.pdf";

  public static final String WITHHOLDINGS_PDF_URL =
      "https://uwservice.wisconsin.edu/docs/forms/pay-employee-withholding.pdf";

  public static final String ROLE_SELF_SERVICE_DIRECT_DEPOSIT = "ROLE_VIEW_DIRECT_DEPOSIT";

  private HrsRolesDao hrsRolesDao;



  /**
   * Model is "content" --> "links" --> Link[],
   * suitable for (in JSON representation) use as the remote source for dynamic list-of-links
   * widget in uPortal-app-framework.
   * @param modelMap Spring PortletMVC model map
   * @param request PortletRequest
   * @return String representing view
   */
  @ResourceMapping("listOfLinks")
  public String listOfLinks(ModelMap modelMap, PortletRequest request) {

    String fname = fname(request);

    Link earningsStatementsLink = new Link();
    earningsStatementsLink.setTitle("Earnings Statements");
    earningsStatementsLink.setHref("/web/exclusive/" + fname);
    earningsStatementsLink.setIcon("attach_money");

    Link directDepositLink = directDepositLink();

    Link taxStatementsLink = new Link();
    taxStatementsLink.setTitle("Tax Statements");
    taxStatementsLink.setHref("/portal/p/" + fname + "?pP_requestedContent=Tax%20Statements");
    taxStatementsLink.setIcon("toll");

    Link withholdingsLink = new Link();
    withholdingsLink.setTitle("Update W4");
    withholdingsLink.setHref(WITHHOLDINGS_PDF_URL);
    withholdingsLink.setTarget("_blank");
    withholdingsLink.setIcon("picture_as_pdf");

    final List<Link> linkList = new ArrayList<Link>();
    linkList.add(earningsStatementsLink);
    linkList.add(directDepositLink);
    linkList.add(taxStatementsLink);
    linkList.add(withholdingsLink);

    final Map<String, Object[]> content = new HashMap<String, Object[]>();
    content.put("links", linkList.toArray());

    modelMap.put("content", content);

    return "contentAttrJsonView";

  }

  /**
   * Returns the Payroll Information portlet-definition fname to use for links for the viewing user.
   * @param request PortletRequest
   * @return MADISON_FNAME or SYSTEM_FNAME
   */
  private String fname(PortletRequest request) {
    String fname = MADISON_FNAME;

    String remoteUser = request.getRemoteUser();

    if (remoteUser != null && remoteUser.contains("@")) {
      fname = SYSTEM_FNAME;
    }
    return fname;
  }

  /**
   * Returns the correct Direct Deposit link for this user,
   * honoring the self-service direct deposit role iff HRS has provided that HRS self-service URL,
   * otherwise using the static PDF URL.
   *
   * @return Direct Deposit link appropriate for the viewing user
   */
  private Link directDepositLink() {
    Link directDepositLink = new Link();
    directDepositLink.setTitle("Update Direct Deposit");
    directDepositLink.setTarget("_blank");

    // if the employee has the self-service direct deposit role
    // and the self-service direct deposit URL is set
    // then use this HRS self-service URL, otherwise use the URL to the PDF

    final String emplId = PrimaryAttributeUtils.getPrimaryId();
    Set<String> roles = this.hrsRolesDao.getHrsRoles(emplId);
    Map<String, String> urls = this.getHrsUrls();

    if (roles.contains(ROLE_SELF_SERVICE_DIRECT_DEPOSIT) &&
        (null != urls.get(HrsUrlDao.SELF_SERVICE_DIRECT_DEPOSIT_KEY))) {
      directDepositLink.setHref(urls.get(HrsUrlDao.SELF_SERVICE_DIRECT_DEPOSIT_KEY));
      directDepositLink.setIcon("account_balance_wallet");
    } else {
      directDepositLink.setHref(DIRECT_DEPOSIT_PDF_URL);
      directDepositLink.setIcon("picture_as_pdf");
    }
    return directDepositLink;
  }

  @Autowired
  public void setRolesDao(HrsRolesDao hrsRolesDao) {
    this.hrsRolesDao = hrsRolesDao;
  }

}
