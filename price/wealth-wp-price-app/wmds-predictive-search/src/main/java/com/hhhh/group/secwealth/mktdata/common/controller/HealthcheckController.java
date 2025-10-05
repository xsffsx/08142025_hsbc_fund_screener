/*
 */
package com.hhhh.group.secwealth.mktdata.common.controller;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hhhh.group.secwealth.mktdata.common.service.HealthcheckService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

@Controller
public class HealthcheckController extends AbstractController {

    private static final FastDateFormat df = FastDateFormat.getInstance(DateConstants.DateFormat_ddMMMyyyyHHmmssSS, Locale.ENGLISH);

    private static final String SESSION_STATUS = "session.status";
    private static final String USER_ID = "userid";
    private static final String USER_PASSWORD = "password";

    // dashboard board projectName
    @Value("#{systemConfig['env.project.name']}")
    private String projectName;

    // dashboard board version
    @Value("#{systemConfig['dashboardkey.version']}")
    private String version;

    // dashboard board refreshTime
    @Value("#{systemConfig['dashboard.refreshTime']}")
    private String refreshTime;

    @Autowired
    @Qualifier("healthcheckService")
    private HealthcheckService healthcheckService;

    @Value("#{systemConfig['dashboard.probingPage']}")
    private String probingPage;

    @Value("#{systemConfig['dashboard.dashboardPage']}")
    private String dashboardPage;

    @Value("#{systemConfig['dashboard.dashboardLoginPage']}")
    private String dashboardLoginPage;

    // @Resource(name = "securityFilterAutoConfiguration")
    // private SecurityFilterAutoConfiguration securityFilterAutoConfiguration;

    /*
     * @RequestMapping(value = "healthcheck/samlGenerate", method =
     * {RequestMethod.POST, RequestMethod.GET}) public ModelAndView
     * samlGenerate(final HttpServletRequest request, final HttpServletResponse
     * response) throws Exception { LogUtil.debug(HealthcheckController.class,
     * "saml Generate"); String samlString = (String)
     * request.getAttribute(DefaultHttpRequestAttributeNames.GENERATED_SAML);
     * request.setAttribute("result", "samlGenerate"); if
     * (StringUtil.isInvalid(samlString)) { samlString =
     * "E2eTrust at this Env should be closed, try to generate token in other testing Env !"
     * ; } request.setAttribute("samlString", samlString); return new
     * ModelAndView(this.probingPage); }
     */

    @RequestMapping(value = "healthcheck/probing", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView probing(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.debug(HealthcheckController.class, "request probing");
        String result = "success";
        String systemTime;
        try {
            systemTime = this.healthcheckService.getSystemTime();
        } catch (Exception e) {
            LogUtil.error(HealthcheckController.class, "request probing", e);
            result = "failure";
            systemTime = e.getClass().getName();
        }
        request.setAttribute("result", result);
        request.setAttribute("systemTime", systemTime);
        return new ModelAndView(this.probingPage);
    }

    @RequestMapping(value = "predHealthcheck/dashboard", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView dashboard(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // String samlString = (String)
        // request.getAttribute(DefaultHttpRequestAttributeNames.GENERATED_SAML);
        Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(DateConstants.TIMEZONE_HKT));
        request.setAttribute("requestTimestamp", HealthcheckController.df.format(todayDate.getTime()));
//        if (Boolean.TRUE.equals(request.getSession(true).getAttribute(HealthcheckController.SESSION_STATUS))) {
            return dashboardDetail(request, response/* , samlString */);
//        }
//        return dashboardAuthenticate(request, response/* , samlString */);
    }

    private ModelAndView dashboardLogin(final HttpServletRequest request, final HttpServletResponse response) {
        LogUtil.debug(HealthcheckController.class, "request dashboardLogin");
        request.setAttribute("version", this.version);
        request.setAttribute("projectName", this.projectName);
        return new ModelAndView(this.dashboardLoginPage);
    }

    private ModelAndView dashboardAuthenticate(final HttpServletRequest request,
        final HttpServletResponse response/*
                                           * , final String samlString
                                           */) {
        LogUtil.debug(HealthcheckController.class, "request dashboardAuthenticate");
        String userid = request.getParameter(HealthcheckController.USER_ID);
        String password = request.getParameter(HealthcheckController.USER_PASSWORD);
        if (StringUtil.isValid(userid) && StringUtil.isValid(password)) {
            boolean isSuccess = this.healthcheckService.authenticate(userid, password);
            if (isSuccess) {
                request.getSession(true).setAttribute(HealthcheckController.SESSION_STATUS, Boolean.TRUE);
                return dashboardDetail(request, response/* , samlString */);
            } else {
                return dashboardLogin(request, response);
            }
        } else {
            return dashboardLogin(request, response);
        }
    }

    private ModelAndView dashboardDetail(final HttpServletRequest request,
        final HttpServletResponse response/*
                                           * , final String samlString
                                           */) {
        LogUtil.debug(HealthcheckController.class, "request dashboardDetail");
        Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(DateConstants.TIMEZONE_HKT));
        request.setAttribute("resposneTimestamp", HealthcheckController.df.format(todayDate.getTime()));
        request.setAttribute("version", this.version);
        request.setAttribute("projectName", this.projectName);
        request.setAttribute("refreshTime", this.refreshTime);
        response.setHeader("Refresh", this.refreshTime);
        request.setAttribute("HEALTH_DASHBOARD", this.healthcheckService.healthDashboardSite(/* samlString */));
        request.setAttribute("INSTANCE_NAME", StringUtil.getServerName());

        String ServerPath = StringUtil.getServerPath();
        if (StringUtil.isValid(ServerPath)) {
            this.listAllFiles(new File(ServerPath));
        }
        return new ModelAndView(this.dashboardPage);
    }

    private void listAllFiles(final File file) {
        File[] fs = file.listFiles();
        if (fs != null && fs.length > 0) {
            for (File f : fs) {
                if (f.isDirectory()) {
                    listAllFiles(f);
                }
                if (f.isFile()) {
                    LogUtil.info(HealthcheckController.class, "List All Files: {}", f.getPath());
                }
            }
        }
    }
}

