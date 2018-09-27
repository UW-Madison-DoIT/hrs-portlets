package edu.wisc.portlet.hrs.servlet;

import edu.wisc.hr.dao.url.HrsUrlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UrlsRedirectServletController {

    private HrsUrlDao hrsUrlDao;

    @RequestMapping("/go")
    public View redirectTo(@RequestParam String urlKey) {
      String redirectTo = this.hrsUrlDao.getHrsUrls().get(urlKey);

      if (null == redirectTo) {
          return null;
      }

      return new RedirectView(redirectTo);
    }

    public HrsUrlDao getHrsUrlDao() {
        return hrsUrlDao;
    }

    @Autowired
    public void setHrsUrlDao(HrsUrlDao hrsUrlDao) {
        this.hrsUrlDao = hrsUrlDao;
    }

}
