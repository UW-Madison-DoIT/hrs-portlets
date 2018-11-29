package edu.wisc.portlet.hrs.web.troubleshoot;

import edu.wisc.hr.dao.ernstmt.EarningStatementDao;
import edu.wisc.hr.dao.ernstmt.SimpleEarningsStatementDao;
import edu.wisc.hr.dao.roles.HrsRolesDao;
import edu.wisc.hr.dm.ernstmt.RetrievedEarningsStatements;
import edu.wisc.hr.dm.ernstmt.SimpleEarningsStatement;
import edu.wisc.hr.service.ernstmt.EarningsStatementService;
import edu.wisc.portlet.hrs.web.HrsControllerBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("VIEW")
public class TroubleshootingController
    extends HrsControllerBase {

  private HrsRolesDao rolesDao;

  private EarningsStatementService earningsStatementService;

  @Autowired
  public void setEarningsStatementService(
      EarningsStatementService earningsStatementService) {
    this.earningsStatementService = earningsStatementService;
  }

  public HrsRolesDao getRolesDao() {
    return rolesDao;
  }

  @Autowired
  public void setRolesDao(HrsRolesDao rolesDao) {
    this.rolesDao = rolesDao;
  }

  @RequestMapping
  public String renderView(ModelMap modelMap, @RequestParam(required = false) String queriedEmplId) {

    if (null != queriedEmplId) {

      this.logger.warn(
          "AUDIT LOG: TROUBLESHOOTER: Queried about emplid " + queriedEmplId);

      modelMap.put("queriedEmplId", queriedEmplId);

      final Set<String> roles = this.rolesDao.getHrsRoles(queriedEmplId);

      List<String> sortedRoles = new ArrayList<String>(roles);
      Collections.sort(sortedRoles);

      modelMap.put("roles", sortedRoles);

      final Set<String> rawRoles =
          this.rolesDao.rawHrsRolesForEmplid(queriedEmplId);

      List<String> sortedRawRoles = new ArrayList<String>(rawRoles);
      Collections.sort(sortedRawRoles);

      modelMap.put("rawRoles", sortedRawRoles);

      List<SimpleEarningsStatement> earningsStatements = new ArrayList<SimpleEarningsStatement>();

      RetrievedEarningsStatements retrievedEarningsStatements =
          this.earningsStatementService.statementsForEmplid(queriedEmplId);

      modelMap.put("earningsStatements",
          retrievedEarningsStatements.getStatements());

      if (retrievedEarningsStatements.isErrored()) {
        Exception firstError = retrievedEarningsStatements.getErrors().get(0);
        modelMap.put("earningsStatementsError", firstError.getMessage());
      }


    }

    final Map<String, Set<String>> hrsRoleMappings =
        this.rolesDao.getHrsRoleMappings();
    
    final List<HrsRoleMappingRule> rules =
        new ArrayList<HrsRoleMappingRule>();

    List<String> ruleKeysAlphabetically =
        new ArrayList<String>(hrsRoleMappings.keySet());
    Collections.sort(ruleKeysAlphabetically);

    for (final String hrsRole : ruleKeysAlphabetically) {

      Set<String> portletRoles = hrsRoleMappings.get(hrsRole);

      HrsRoleMappingRule rule = new HrsRoleMappingRule(hrsRole, portletRoles);

      rules.add(rule);

    }

    modelMap.put("rules", rules);

    return "troubleshooting";
  }

}
