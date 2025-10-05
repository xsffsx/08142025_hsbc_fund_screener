/*
 */
package com.hhhh.group.secwealth.mktdata.common.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.controller.RestfulController;
import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.dao.BaseDao;
import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.service.HealthcheckService;
import com.hhhh.group.secwealth.mktdata.common.svc.httpclient.HttpConnectionManager;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.DateConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.MstarConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.RegionServiceAdapter;
import com.hhhh.htsa.mipc.micipher.MICipher;

import net.sf.json.JSONObject;

@Service("healthcheckService")
public class HealthcheckServiceImpl implements HealthcheckService {

    @Value("#{systemConfig['dashboardkey.keystoreFile']}")
    private String keystoreFile;
    @Value("#{systemConfig['dashboardkey.passwordKeystoreFile']}")
    private String passwordKeystoreFile;
    @Value("#{systemConfig['dashboardKey.ivParam']}")
    private String ivParam;
    @Value("#{systemConfig['dashboardkey.userid']}")
    private String userid;
    @Value("#{systemConfig['dashboardkey.password']}")
    private String password;

    @Autowired
    @Qualifier("regionServiceAdapter")
    private RegionServiceAdapter regionServiceAdapter;

    // Health check Message
    private static final String FILE_PATH = "system/common/dashboard/dashboard.properties";
    private static final Properties healthCheckProperties = new Properties();

    private final static String TR_START = "<tr>";
    private final static String TR_END = "</tr>";
    private final static String SITE_TD_START = "<td colspan=\"2\" align=\"center\">";
    private final static String TD_START = "<td>";
    private final static String TD_END = "</td>";
    private final static String HTML_FONT_START = "<FONT COLOR=\"#008000\">OK, response time is ";
    private final static String HTML_FONT_END = "ms</FONT>";
    private final static String HTML_FONT_ERROR_START = "<FONT COLOR=\"#FF0000\">Failed with error:";
    private final static String HTML_FONT_ERROR_END = "</FONT>";
    private final static String INVALID_RESPONSE = "NoRecordFound";

    // Old Mstr Api
    @Value("#{systemConfig['mstar.conn.url.quotesummary']}")
    private String mstrGlobaUrl;

    // New mstr Api
    @Value("#{systemConfig['mstar.conn.url.mstarnewapi']}")
    private String mstrCnUrl;

    @Value("#{systemConfig['mstar.conn.url.advanceChartGrowth']}")
    private String mstrAdvanceChartGrowtUrl;

    @Value("#{systemConfig['mstar.conn.url.advanceChartToken']}")
    private String mstrAdvanceChartToken;

    @Resource(name = "mstarConnManager")
    protected HttpConnectionManager connManager;

    @Autowired
    @Qualifier("baseDao")
    private BaseDao baseDao;

    @Autowired
    @Qualifier("requestConverter")
    private RequestConverter requestConverter;

    private String decryptedUserid = "";

    private String decryptedPassword = "";

    @PostConstruct
    public void init() {
     //   URL keystoreFileUrl = this.getClass().getClassLoader().getResource(this.keystoreFile);
      //  URL passwordKeystoreFileUrl = this.getClass().getClassLoader().getResource(this.passwordKeystoreFile);
        try {
            //MICipher cipher = new MICipher(keystoreFileUrl.getPath(), passwordKeystoreFileUrl.getPath(), this.ivParam);
            MICipher cipher = new MICipher(this.keystoreFile, this.passwordKeystoreFile, this.ivParam);
            this.decryptedUserid = cipher.decrypt(this.userid);
            this.decryptedPassword = cipher.decrypt(this.password);
            HealthcheckServiceImpl.healthCheckProperties.load(this.getClass().getClassLoader()
                .getResourceAsStream(HealthcheckServiceImpl.FILE_PATH));
        } catch (Exception e) {
            LogUtil.error(HealthcheckServiceImpl.class, "Can't init HealthcheckServiceImpl|exception=" + e.getMessage()
                + "|keystoreFile=" + this.keystoreFile + "|passwordKeystoreFile=" + this.passwordKeystoreFile + "|ivParam="
                + this.ivParam + "|userid=" + this.userid + "|password=" + this.password, e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }

    @Override
    public String getSystemTime() throws Exception {
        try {
            Calendar todayDate = Calendar.getInstance(TimeZone.getTimeZone(DateConstants.TIMEZONE_HKT));
            DateFormat df = new SimpleDateFormat(DateConstants.DateFormat_MMddyyyyHHmmss_withSymbol);
            return df.format(todayDate.getTime());
        } catch (Exception e) {
            LogUtil.error(HealthcheckServiceImpl.class, "getSystemTime error: ", e);
            throw new SystemException(e);
        }
    }

    @Override
    public boolean authenticate(final String userid, final String password) {
        boolean isSuccess = false;
        if (this.decryptedUserid.equals(userid) && this.decryptedPassword.equals(password)) {
            isSuccess = true;
        }
        return isSuccess;
    }

    public Map<String, String> getHeaderMap(final String site) {
        String dashboardHeaderStr = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + "header");
        String[] dashboardHeaders = dashboardHeaderStr.split(",");
        Map<String, String> headerMap = new HashMap<String, String>();
        if (null != dashboardHeaders && dashboardHeaders.length > 0) {
            headerMap.put(CommonConstants.REQUEST_HEADER_LOCALE, dashboardHeaders[0]);
            headerMap.put(CommonConstants.REQUEST_HEADER_COUNTRYCODE, dashboardHeaders[1]);
            headerMap.put(CommonConstants.REQUEST_HEADER_GROUPMEMBER, dashboardHeaders[2]);
            headerMap.put(CommonConstants.REQUEST_HEADER_CHANNELID, dashboardHeaders[3]);
            headerMap.put(CommonConstants.REQUEST_HEADER_APPCODE, dashboardHeaders[4]);
        }
        return headerMap;
    }

    private Object doServiceForHealthCheck(final String body, final String serviceName, final String requestClassName,
        final String site, final String samlString) throws Exception {
        try {
            Object obj = null;
            Map<String, String> headerMap = StringUtil.isValid(site) ? getHeaderMap(site) : new HashMap<String, String>();
            if (StringUtil.isValid(samlString) && "wpcWebServiceDashboard".equalsIgnoreCase(serviceName)) {
                headerMap.put(CommonConstants.SALM_TOKEN, samlString);
            }
            if (StringUtil.isValid(requestClassName)) {
                obj = this.requestConverter.converterToRequest(headerMap, body, requestClassName);
            } else {
                obj = StringUtil.isValid(body) ? JSONObject.fromObject(body) : new JSONObject();
                ((JSONObject) obj).putAll(headerMap);
            }
            LogUtil.infoBeanToJson(RestfulController.class, "=========>: " + CommonConstants.INBOUND_MSG_PREFIX, obj);
            com.hhhh.group.secwealth.mktdata.common.service.Service service =
                this.regionServiceAdapter.getServices().get(serviceName);
            return service.doService(obj);
        } catch (Exception e) {
            LogUtil.error(HealthcheckServiceImpl.class, "doServiceForHealthCheck error: ", e);
            throw new SystemException(e);
        }
    }

    public String healthDashboardSite(final String samlString) {
        StringBuilder sb = new StringBuilder();
        String dashBoardSiteStr = HealthcheckServiceImpl.healthCheckProperties.getProperty("dashboard.sites");
        String[] dashBoardSites = dashBoardSiteStr.split(",");
        for (String site : dashBoardSites) {
            sb.append(HealthcheckServiceImpl.TR_START).append(HealthcheckServiceImpl.SITE_TD_START).append(site)
                .append(HealthcheckServiceImpl.TD_END).append(HealthcheckServiceImpl.TR_END);
            healthDashboardApi(sb, site + ".", samlString);
        }
        return sb.toString();
    }

    public void healthDashboardApi(final StringBuilder sb, final String site, final String samlString) {
        // StringBuilder sb = new StringBuilder();
        String venderStr = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + "dashboard.venders");
        if (StringUtil.isValid(venderStr)) {
            String[] venders = venderStr.split(",");
            if (venders.length > 0) {
                for (String vender : venders) {
                    sb.append(HealthcheckServiceImpl.TR_START);
                    sb.append(HealthcheckServiceImpl.TD_START + vender + " status " + HealthcheckServiceImpl.TD_END);
                    if (vender.contains("wpc_data")) {
                        wpcDataHealthCheckMsg(sb, vender, site);
                    } else if (vender.contains("mstr")) {
                        if (vender.contains("globalApi")) {
                            mstrHealthCheckMsg(sb, vender, site,
                                this.mstrGlobaUrl + CommonConstants.SYMBOL_SLASH + MstarConstants.MSTARID);
                        } else if (vender.contains("cnApi") && StringUtil.isValid(this.mstrCnUrl)) {
                            mstrHealthCheckMsg(sb, vender, site, this.mstrCnUrl);
                        } else if (vender.contains("advanceChart") && StringUtil.isValid(this.mstrAdvanceChartGrowtUrl)
                            && StringUtil.isValid(this.mstrAdvanceChartToken)) {
                            String advanceChartUrl =
                                this.mstrAdvanceChartGrowtUrl + CommonConstants.SYMBOL_SLASH + this.mstrAdvanceChartToken;
                            mstrHealthCheckMsg(sb, vender, site, advanceChartUrl);
                        }
                    } else if (vender.contains("db")) {
                        dbHealthCheckMsg(sb, vender, site);
                    } else if (vender.contains("wpc_webservice")) {
                        wpcWebServiceHealthCheckMsg(sb, vender, site, samlString);
                    } else if (vender.contains("volume_service")) {
                        volumeServiceHealthCheckMsg(sb, vender, site);
                    }
                    sb.append(HealthcheckServiceImpl.TR_END);
                }
            }
        }
    }

    // get wpc data status message
    private void wpcDataHealthCheckMsg(final StringBuilder sb, final String vender, final String site) {
        String body = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".body");
        String function = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".function");
        String requestClassName = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".requestClassName");
        long startTime = System.currentTimeMillis();
        try {
            Object obj = doServiceForHealthCheck(body, function, requestClassName, site, "");
            long endTime = System.currentTimeMillis();
            if (null != obj) {
                String objStr = obj.toString();
                if (null != obj && "[]" != objStr) {
                    String time = Long.toString(endTime - startTime);
                    sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                        + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
                } else {
                    sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START
                        + HealthcheckServiceImpl.INVALID_RESPONSE + HealthcheckServiceImpl.HTML_FONT_ERROR_END
                        + HealthcheckServiceImpl.TD_END);
                }
            }
        } catch (Exception e) {
            LogUtil.debug(HealthcheckServiceImpl.class, "health check fail: " + e.getMessage());
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START + e.getMessage()
                + HealthcheckServiceImpl.HTML_FONT_ERROR_END + HealthcheckServiceImpl.TD_END);
        }
    }

    // get MSTR health check message
    private void mstrHealthCheckMsg(final StringBuilder sb, final String vender, final String site, final String url) {
        String params = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".params");
        params = params == null ? "" : params;
        long startTime = System.currentTimeMillis();
        try {
            LogUtil.infoOutboundMsg(HealthcheckServiceImpl.class, VendorType.MSTAR, url, params);
            String respStr = this.connManager.sendRequest(url, params, params);
            LogUtil.infoInboundMsg(HealthcheckServiceImpl.class, VendorType.MSTAR, respStr);
            long endTime = System.currentTimeMillis();
            if (StringUtil.isValid(respStr)) {
                if (vender.contains("advanceChart")) {
                    if (StringUtil.isValid(respStr) && respStr.indexOf("TimeSeries") > -1) {
                        String time = Long.toString(endTime - startTime);
                        sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                            + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
                    } else {
                        sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START
                            + HealthcheckServiceImpl.INVALID_RESPONSE + HealthcheckServiceImpl.HTML_FONT_ERROR_END
                            + HealthcheckServiceImpl.TD_END);
                    }
                } else {
                    String time = Long.toString(endTime - startTime);
                    sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                        + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
                }
            } else {
                sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START
                    + HealthcheckServiceImpl.INVALID_RESPONSE + HealthcheckServiceImpl.HTML_FONT_ERROR_END
                    + HealthcheckServiceImpl.TD_END);
            }
        } catch (Exception e) {
            LogUtil.debug(HealthcheckServiceImpl.class, "health check fail: " + e.getMessage());
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START + e.getMessage()
                + HealthcheckServiceImpl.HTML_FONT_ERROR_END + HealthcheckServiceImpl.TD_END);
        }
    }

    // get db health check message
    private void dbHealthCheckMsg(final StringBuilder sb, final String vender, final String site) {
        StringBuilder hql = new StringBuilder();
        String sql = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".sql");
        hql.append(sql);
        long startTime = System.currentTimeMillis();
        try {
            EntityManager entityManager = this.baseDao.getEntityManager();
            Query query = entityManager.createNativeQuery(hql.toString());
            Object object = query.getSingleResult();
            LogUtil.warn(HealthcheckServiceImpl.class, "dbHealthCheckMsg: " + object);
            long endTime = System.currentTimeMillis();
            String time = Long.toString(endTime - startTime);
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
        } catch (Exception e) {
            LogUtil.debug(HealthcheckServiceImpl.class, "health check fail: " + e.getMessage());
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START + e.getMessage()
                + HealthcheckServiceImpl.HTML_FONT_ERROR_END + HealthcheckServiceImpl.TD_END);
        }
    }

    // get wpc webService health check message
    private void wpcWebServiceHealthCheckMsg(final StringBuilder sb, final String vender, final String site,
        final String samlString) {
        String function = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".function");
        long startTime = System.currentTimeMillis();
        try {
            Object obj = doServiceForHealthCheck(null, function, null, site, samlString);
            long endTime = System.currentTimeMillis();
            String time = Long.toString(endTime - startTime);
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
        } catch (Exception e) {
            LogUtil.debug(HealthcheckServiceImpl.class, "health check fail: " + e.getMessage());
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START + e.getMessage()
                + HealthcheckServiceImpl.HTML_FONT_ERROR_END + HealthcheckServiceImpl.TD_END);
        }
    }


    // get volumeService health check message
    private void volumeServiceHealthCheckMsg(final StringBuilder sb, final String vender, final String site) {
        String function = HealthcheckServiceImpl.healthCheckProperties.getProperty(site + vender + ".function");
        long startTime = System.currentTimeMillis();
        try {
            String body = "{\"site\":\"" + site.substring(0, site.length() - 1) + "\"}";
            Object obj = doServiceForHealthCheck(body, function, null, null, "");
            long endTime = System.currentTimeMillis();
            String time = Long.toString(endTime - startTime);
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_START + time
                + HealthcheckServiceImpl.HTML_FONT_END + HealthcheckServiceImpl.TD_END);
        } catch (Exception e) {
            LogUtil.debug(HealthcheckServiceImpl.class, "health check fail: " + e.getMessage());
            sb.append(HealthcheckServiceImpl.TD_START + HealthcheckServiceImpl.HTML_FONT_ERROR_START + e.getMessage()
                + HealthcheckServiceImpl.HTML_FONT_ERROR_END + HealthcheckServiceImpl.TD_END);
        }
    }
}
