package edu.wisc.portlet.hrs.web.roles;

import edu.wisc.hr.dao.roles.HrsRolesDao;
import edu.wisc.portlet.hrs.web.HrsControllerBase;
import edu.wisc.portlet.hrs.web.listoflinks.Link;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import org.jasig.springframework.security.portlet.authentication.PrimaryAttributeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/**
 * Exposes HRS roles as JSON suitable for rendering in a list-of-links widget
 * or for switching on the presence of a particular role.
 */
@Controller
@RequestMapping("VIEW")
public class RolesController
  extends HrsControllerBase {

  private HrsRolesDao rolesDao;

  public HrsRolesDao getRolesDao() {
    return rolesDao;
  }

  @Autowired
  public void setRolesDao(HrsRolesDao rolesDao) {
    this.rolesDao = rolesDao;
  }

  /**
   * Model is "content" --> "links" --> Link[],
   * suitable for (in JSON representation) use as the remote source for dynamic list-of-links
   * widget in uPortal-app-framework.
   * @param modelMap
   * @return String representing view
   * @throws IOException
   */
  @ResourceMapping("rolesAsListOfLinks")
  public String rolesAsListOfLinksResource(ModelMap modelMap) throws IOException {
    final String emplId = PrimaryAttributeUtils.getPrimaryId();
    final Set<String> roles = this.rolesDao.getHrsRoles(emplId);

    final List<Link> linkList = rolesAsLinks(roles);

    Map<String, Object[]> content = new HashMap<String, Object[]>();
    content.put("links", linkList.toArray());

    modelMap.put("content", content);

    return "contentAttrJsonView";
  }

  /**
   * Model is "report" --> [ {"rolename", "true"}, {"anotherRoleName", "false"}, ... ]
   * Suitable for predicating a uPortal Application Framework message on the employee having a role.
   * @param modelMap
   * @return
   * @throws IOException
   */
  @ResourceMapping("rolesAsReport")
  public String rolesAsReport(ModelMap modelMap) throws IOException {

    final String emplId = PrimaryAttributeUtils.getPrimaryId();

    final Map<String, Set<String>> generalRoleMapping = this.rolesDao.getHrsRoleMappings();

    Set<String> rolesThatPeopleMightHave = new HashSet<String>();

    // collect all the mapped portlet roles
    Collection<Set<String>> mappingValues = generalRoleMapping.values();
    for (Set<String> portletRoles : mappingValues) {
      rolesThatPeopleMightHave.addAll(portletRoles);
    }

    final Set<String> rolesHeldByEmployee = this.rolesDao.getHrsRoles(emplId);

    List<Map<String, String>> report = new ArrayList<Map<String, String>>();

    for (String portletRole : rolesThatPeopleMightHave) {
      Map<String, String> rolesMap = new HashMap<String, String>();
      rolesMap.put(portletRole,
          Boolean.valueOf(rolesHeldByEmployee.contains(portletRole)).toString());
      report.add(rolesMap);
    }

    modelMap.addAttribute("report", report);

    return "jsonView";
  }

  /**
   * Model is "content" --> "hasRole" --> boolean .
   * The boolean is true if the user has the requested role, false otherwise.
   * @param modelMap
   * @param role portlet role about which to ask whether the user has it
   * @return String representing view
   * @throws IOException
   */
  @ResourceMapping("hasRole")
  public String roleAsBoolean(ModelMap modelMap, @RequestParam String role) throws IOException {
    final String emplId = PrimaryAttributeUtils.getPrimaryId();
    final Set<String> roles = this.rolesDao.getHrsRoles(emplId);

    Map<String, Boolean> content = new HashMap<String, Boolean>();
    content.put("hasRole", roles.contains(role));

    modelMap.put("content", content);

    return "contentAttrJsonView";
  }

  @RequestMapping
  public String viewRoles( ModelMap modelMap, PortletRequest request){

    final String emplId = PrimaryAttributeUtils.getPrimaryId();
    final Set<String> roles = this.rolesDao.getHrsRoles(emplId);

    final List<Link> linkList = rolesAsLinks(roles);

    modelMap.put("links", linkList);
    return "listOfLinks";
  }

  protected List<Link> rolesAsLinks(Set<String> roles) {
    final List<Link> linkList = new ArrayList<Link>();

    for (final String role : roles) {
      final Link link = new Link();
      link.setTitle(role);
      link.setHref("https://wiki.doit.wisc.edu/confluence/display/MUM/HRS+Roles+and+MyUW");
      link.setTarget("_blank");
      link.setIcon("fa-user-circle");
      linkList.add(link);
    }
    return linkList;
  }
}
