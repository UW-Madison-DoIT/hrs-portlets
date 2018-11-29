package edu.wisc.portlet.hrs.web.urls;

import edu.wisc.portlet.hrs.web.HrsControllerBase;
import edu.wisc.portlet.hrs.web.listoflinks.Link;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.portlet.PortletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class UrlsController
  extends HrsControllerBase {

  @ResourceMapping("hrsUrlsListOfLinks")
  public String hrsUrlsAsListOfLinksJson(final ModelMap modelMap) {

    final List<Link> linkList = hrsUrlsAsLinks();

    final Map<String, Object[]> content = new HashMap<String, Object[]>();
    content.put("links", linkList.toArray());

    modelMap.put("content", content);

    return "contentAttrJsonView";
  }

  @RequestMapping
  public String viewHrsUrls(final ModelMap modelMap) {

    final List<Link> linkList = hrsUrlsAsLinks();

    modelMap.put("links", linkList);

    return "listOfLinks";
  }

  protected List<Link> hrsUrlsAsLinks() {
    Map<String, String> hrsUrls = this.getHrsUrls();

    List<String> urlKeys = new ArrayList(hrsUrls.keySet());

    Collections.sort(urlKeys);

    List<Link> linkList = new ArrayList<Link>();

    for (String urlName : urlKeys) {
      final Link link = new Link();
      link.setTitle(urlName);
      link.setHref(hrsUrls.get(urlName));
      link.setIcon("link");
      linkList.add(link);
    }
    return linkList;
  }

}
