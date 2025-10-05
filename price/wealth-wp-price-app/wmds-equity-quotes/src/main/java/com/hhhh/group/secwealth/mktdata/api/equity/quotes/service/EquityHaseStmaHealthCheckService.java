/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.EtnetProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.LabciPortalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties.EquityHealthcheckProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.TrisQuoteServiceRequest;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties.CacheDistributeProperties;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean.Status;
import com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.impl.ApplicationHealthcheckService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@ConditionalOnProperty(value = "service.hase.HealthCheck.injectEnabled")
public class EquityHaseStmaHealthCheckService extends ApplicationHealthcheckService {

    private static final Logger logger = LoggerFactory.getLogger(EquityHaseStmaHealthCheckService.class);

    private static final String HEALTHCHECK_ETNET = "ETNET";

    private static final String HEALTHCHECK_ETNET_NEWS = "ETNET_NEWS";

    private static final String HEALTHCHECK_ETNET_CHART = "ETNET_CHART";

    private static final String HEALTHCHECK_ETNET_QUOTE = "ETNET_QUOTE";

    private static final String HEALTHCHECK_ETNET_TOPMOVER = "ETNET_TOPMOVER";

    private static final String HEALTHCHECK_ETNET_LISTIPO = "ETNET_LISTIPO";

    private static final String HEALTHCHECK_ETNET_CURRENTIPO = "ETNET_CURRENTIPO";

    private static final String HEALTHCHECK_ETNET_INDICES = "ETNET_INDICES";

    private static final String HEALTHCHECK_MIDFS_BMP = "MIDFS_BMP";

    private static final String HEALTHCHECK_MIDFS_LIST = "MIDFS_LIST";

    private static final String HEALTHCHECK_MIDFS_DETAIL = "MIDFS_DETAIL";

    private static final String HEALTHCHECK_TRIS = "TRIS";
    
    private static final String HEALTHCHECK_RBP_CACHE = "RBP_CACHE";

    private static final String HEALTHCHECK_LABCI_PORTA = "LABCI-PORTAL";

    private static final String HEALTHCHECK_LABCI_CACHING = "LABCI-CACHING";

    private static final String HEALTHCHECK_PREDICTIVE_SEARCH = "PREDICTIVE-SEARCH";

    private static final String HEALTHCHECK_DB = "DB_CONNECTION";

    private static final String HEALTHCHECK_LABCI_NB = "LABCI-NB";

    private static final String HEALTHCHECK_LABCI_PORTAL_QUERY = "LABCI-PORTAL-QUERY";

    private static final String HEALTHCHECK_LABCI_PORTAL_CHART_DATA = "LABCI-PORTAL-CHART-DATA";

    private static final String HEALTHCHECK_LABCI_PORTAL_PAST_PERFORMANCE = "LABCI-PORTAL-PAST-PERFORMANCE";

    private static final String HEALTHCHECK_LABCI_PORTAL_STOCK_INFO = "LABCI-PORTAL-STOCK-INFO";

    private static final String DOWN_LINE = "_";

    @Autowired
    private TrisProperties trisProps;

    @Autowired
    private TrisTokenService trisTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private EtnetProperties etnetProperties;

    @Autowired
    private LabciProperties labciProperties;

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private CacheDistributeProperties cacheDistributeProperties;

    @Autowired
    private EquityHealthcheckProperties healthCheckProperties;

    @Autowired
    private LabciPortalProperties labciPortalProperties;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.healthcheck.service.Healthcheck
     * #getStatus()
     */
    @Override
    public List<Status> getStatus() {
        List<Status> statusList = new ArrayList<>();
        statusList.add(EtnetCheck());
        statusList.add(EtnetCheck_News());
        statusList.add(EtnetCheck_Chart());
        statusList.add(EtnetCheck_Quote());
        statusList.add(EtnetCheck_Indices());
        statusList.add(EtnetCheck_Topmover());
        statusList.add(EtnetCheck_ListIPO());
        statusList.add(EtnetCheck_CurrentIPO());
        statusList.add(MidfsCheck_BMP());
        statusList.add(MidfsCheck_List());
        statusList.add(MidfsCheck_DETAIL());
        statusList.add(DbCheck());
        statusList.add(RbpCheck());
//        statusList.add(TrisCheck());
        statusList.add(LabciPortalCheck());
        statusList.add(LabciCachingCheck());
        statusList.add(PredictiveSearchCheck());
        statusList.add(LabciNbCheck());
        statusList.add(LabciPortalQueryCheck());
        statusList.add(LabciPortalChartDataUrlCheck());
        statusList.add(LabciPortalPastPerformanceUrlCheck());
        statusList.add(LabciPortalStockInfoUrlCheck());
        return statusList;
    }

    private Status MidfsCheck_List() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("symbol", "00001"));
            httpClientHelper.doGet(labciProperties.getMidfsQuoteListUrl(), params, null);

            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.SUCCESS, HEALTHCHECK_MIDFS_LIST, (endTime - startTime));
            return status;
        } catch (Exception e) {
        	
            long endTime = System.currentTimeMillis();
            logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, HEALTHCHECK_MIDFS_LIST, (endTime - startTime));
            status.setException(e.getMessage());
            return new Status(Status.FAILURE, HEALTHCHECK_MIDFS_LIST, (endTime - startTime));
        }
    }

    private Status MidfsCheck_BMP() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("symbol", "00001"));
            httpClientHelper.doGet(labciProperties.getMidfsBmpQuoteUrl(), params, null);

            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.SUCCESS, HEALTHCHECK_MIDFS_BMP, (endTime - startTime));
            return status;
        } catch (Exception e) {

            long endTime = System.currentTimeMillis();
            logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, HEALTHCHECK_MIDFS_BMP, (endTime - startTime));
            status.setException(e.getMessage());
            return new Status(Status.FAILURE, HEALTHCHECK_MIDFS_BMP, (endTime - startTime));
        }
    }

    private Status MidfsCheck_DETAIL() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("symbol", "00001"));
            httpClientHelper.doGet(labciProperties.getMidfsQuoteListUrl(), params, null);

            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.SUCCESS, HEALTHCHECK_MIDFS_DETAIL, (endTime - startTime));
            return status;
        } catch (Exception e) {

            long endTime = System.currentTimeMillis();
            logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, HEALTHCHECK_MIDFS_DETAIL, (endTime - startTime));
            status.setException(e.getMessage());
            return new Status(Status.FAILURE, HEALTHCHECK_MIDFS_DETAIL, (endTime - startTime));
        }
    }

    private Status RbpCheck() {
        long startTime = System.currentTimeMillis();
        try {
            String url = cacheDistributeProperties.getAwsUri()+ "hc/";
            httpClientHelper.doGet(url, "", null);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_RBP_CACHE, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            return new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_RBP_CACHE, (endTime - startTime));
        }
    }

    private Status DbCheck() {
        long startTime = System.currentTimeMillis();
        try {
            Integer num = this.quoteHistoryDao.dbCheck();
            logger.warn("EquityHaseStmaHealthCheckService", "dbHealthCheckMsg: " + num);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, HEALTHCHECK_DB, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, HEALTHCHECK_DB, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status TrisCheck() {
        long startTime = System.currentTimeMillis();
        try {
            TrisQuoteServiceRequest req = new TrisQuoteServiceRequest();
            req.setService(this.healthCheckProperties.getService());
            CommonRequestHeader commonHeader = this.healthCheckProperties.getHeader();
            String site = commonHeader.getCountryCode() + DOWN_LINE + commonHeader.getGroupMember();
            String closure = this.trisProps.getTrisClosure(site);
            req.setClosure(closure);
            String appCode = this.trisProps.getTrisTokenAppCode(site);
            String timezone = this.trisProps.getTrisTokenTimezone(site);
            String token = this.trisTokenService.getEncryptedTrisToken(site, commonHeader, appCode, closure, timezone);
            req.setToken(token);
            req.addFilter(this.healthCheckProperties.getFilter());
            req.addItem(this.healthCheckProperties.getItem());
            String serviceResponse = this.httpClientHelper.doPost(this.trisProps.getTrisUrl(), JsonUtil.toJson(req), null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(serviceResponse);
            if (respJsonObj == null) {
                logger.error("Invalid response: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            String statusKey = this.trisProps.getResponseStatusCodeKey();
            String status = JsonUtil.getAsString(respJsonObj, statusKey);
            if (!this.trisProps.isCorrectResponseStatus(status)) {
                logger.error("Invalid response, status is incorrect: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            JsonArray resultJsonArray = JsonUtil.getAsJsonArray(respJsonObj, "result");
            if (resultJsonArray == null || resultJsonArray.size() <= 0) {
                logger.error("Invalid response, result array is empty: " + serviceResponse);
                throw new VendorException(ExCodeConstant.EX_CODE_TRIS_INVALID_RESPONSE);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, HEALTHCHECK_TRIS, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            logger.error("Error: Failed to tris health check.", e);
            Status status = new Status(Status.FAILURE, HEALTHCHECK_TRIS, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status LabciPortalCheck() {
        long startTime = System.currentTimeMillis();
        try {
            httpClientHelper.doGet(labciProperties.getProxyName(), labciProperties.getChartDataUrl(), "", null);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTA, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTA, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status LabciCachingCheck() {
        long startTime = System.currentTimeMillis();
        try {
            httpClientHelper.doPost(labciProperties.getLabciUrl(), "", null);
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_CACHING, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_CACHING, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck() {
        long startTime = System.currentTimeMillis();
        try {
            etnetProperties.getEtnetToken();
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_News() {
        long startTime = System.currentTimeMillis();
        try {
            String[] symbols = {"00005","00011","00008","00006","00007","00012","00013","00014","00015","00016","00017","00018","00019","00020","00021","00022","00023","00024","00025","00026","00027","00028","00029","00030","00031","00032","00033","00034","00035","00036","00037","00038","00039","00040","00041","00042","00043","00044","00045","00046","00047","00200","02282","01928","01128","00880","00168","01801","00960","00268"};
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("category", "relatednews"));
            params.add(new BasicNameValuePair("recordsPerPage", "100"));
            params.add(new BasicNameValuePair("pageId", "1"));
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            params.add(new BasicNameValuePair("symbol", converSymbol(symbols)));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getNewsHeadlinesUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getNewsHeadlinesUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_NEWS, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_NEWS, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_Chart() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("realTime", "Y"));
            params.add(new BasicNameValuePair("timePeriod", "1D"));
            params.add(new BasicNameValuePair("timeInterval", "5m"));
            params.add(new BasicNameValuePair("indicators", "SMA10, SMA20, SMA50"));
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            params.add(new BasicNameValuePair("symbol", "00001"));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getChartDataUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getChartDataUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_CHART, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_CHART, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_ListIPO() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("recordsPerPage", "10"));
            params.add(new BasicNameValuePair("pageId", "1"));
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getListedipoUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getListedipoUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_LISTIPO, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_LISTIPO, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_CurrentIPO() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getCurrentipoUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getCurrentipoUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_CURRENTIPO, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_CURRENTIPO, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_Quote() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("realTime", "Y"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            params.add(new BasicNameValuePair("symbol", "00001"));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getEtnetQuoteUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getEtnetQuoteUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_QUOTE, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_QUOTE, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_Indices() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            params.add(new BasicNameValuePair("symbol", "TEH"));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getEtnetIndicesUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getEtnetIndicesUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_INDICES, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_INDICES, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    private Status EtnetCheck_Topmover() {
        long startTime = System.currentTimeMillis();
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("moverType", "TURN"));
            params.add(new BasicNameValuePair("boardType", "MAIN"));
            params.add(new BasicNameValuePair("realTime", "Y"));
            params.add(new BasicNameValuePair("locale", "en"));
            params.add(new BasicNameValuePair("token", etnetProperties.getEtnetTokenWithoutVerify()));
            String response = httpClientHelper.doGet(etnetProperties.getProxyName(),
                    etnetProperties.getEtnetTopMoversUrl(), params, null);
            JsonObject respJsonObj = JsonUtil.getAsJsonObject(response);
            String errCode = JsonUtil.getAsString(respJsonObj, "err_code");
            if (StringUtil.isValid(errCode) && EtnetProperties.EX_CODE_ETNET_INVALID_TOKEN_CODE.equals(errCode)) {
                List<NameValuePair> params2 = params;
                int index = 0;
                for (int i = 0; i < params2.size(); i++) {
                    if ("token".equals(params2.get(i).getName())) {
                        index = i;
                    }
                }
                params2.remove(index);
                params2.add(new BasicNameValuePair("token", etnetProperties.getEtnetToken()));
                response = this.httpClientHelper.doGet(etnetProperties.getProxyName(),
                        etnetProperties.getEtnetTopMoversUrl(), params2, null);
            }
            long endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_TOPMOVER, (endTime - startTime));
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            EquityHaseStmaHealthCheckService.logger.error("Error: Failed to database health check.", e);
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_ETNET_TOPMOVER, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }
    }

    public String converSymbol(String[] symbols) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < symbols.length; i++) {
            sb.append(symbols[i]);
            if (i < symbols.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private Status PredictiveSearchCheck() {
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        try {
            httpClientHelper.doGet(healthCheckProperties.getPredictiveSearchHealthCheckUrl(), "", null);
            endTime = System.currentTimeMillis();
            return new Status(Status.SUCCESS, EquityHaseStmaHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH, (endTime - startTime));
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error("Predictive Search Health Check fail! ");
            Status status = new Status(Status.FAILURE, EquityHaseStmaHealthCheckService.HEALTHCHECK_PREDICTIVE_SEARCH, (endTime - startTime));
            status.setException(e.getMessage());
            return status;
        }

    }

    private Status LabciNbCheck() {
        return callHealthCheck(EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_NB, ()->{
            httpClientHelper.doGet(labciProperties.getLabciNbUrl(), "", null);
            return null;
        });
    }

    private Status LabciPortalQueryCheck() {
        return callHealthCheck(EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTAL_QUERY, ()->{
            httpClientHelper.doGet(labciPortalProperties.getProxyName(), labciPortalProperties.getQueryUrl(), "", null);
            return null;
        });
    }

    private Status LabciPortalChartDataUrlCheck() {
        return callHealthCheck(EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTAL_CHART_DATA, ()->{
            httpClientHelper.doGet(labciPortalProperties.getProxyName(), labciPortalProperties.getChartDataUrl(), "", null);
            return null;
        });
    }

    private Status LabciPortalPastPerformanceUrlCheck() {
        return callHealthCheck(EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTAL_PAST_PERFORMANCE, ()->{
            httpClientHelper.doGet(labciPortalProperties.getProxyName(), labciPortalProperties.getPastPerformanceUrl(), "", null);
            return null;
        });
    }

    private Status LabciPortalStockInfoUrlCheck() {
        return callHealthCheck(EquityHaseStmaHealthCheckService.HEALTHCHECK_LABCI_PORTAL_STOCK_INFO, ()->{
            httpClientHelper.doGet(labciPortalProperties.getProxyName(), labciPortalProperties.getStockInfoUrl(), "", null);
            return null;
        });
    }

    private Status callHealthCheck(String name, Callable<Void> task){
        final long CALL_TIMEOUT_SECONDS = 10L;
        final ExecutorService exec = Executors.newSingleThreadExecutor();
        long startTime = System.currentTimeMillis();
        try{
            return exec.submit(() -> {
                task.call();
                long endTime = System.currentTimeMillis();
                return new Status(Status.SUCCESS, name, (endTime - startTime));
            }).get(CALL_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e){
            long endTime = System.currentTimeMillis();
            Status status = new Status(Status.FAILURE, name, (endTime - startTime));
            if(e instanceof TimeoutException){
                status.setException("AccessTimeoutError");
            }else{
                status.setException(e.getMessage());
            }
            return status;
        } finally {
            exec.shutdown();
        }
    }

}
