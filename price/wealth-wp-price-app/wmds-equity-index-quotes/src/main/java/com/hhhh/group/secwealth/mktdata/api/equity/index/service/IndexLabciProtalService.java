package com.hhhh.group.secwealth.mktdata.api.equity.index.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.index.request.IndexQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Messages;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.labci.Envelop_US;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.labci.IndicesList_US;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.IndexQuotesResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.Indice;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour.RetrieveTradeDateInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour.RetrieveTradeHourInfoResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.index.response.rbpTradingHour.TradeDateInfo;
import com.hhhh.group.secwealth.mktdata.api.equity.index.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResult;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResultStateEnum;
import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.utils.CacheDistributeHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;

@Service("labciProtalIndicesService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class IndexLabciProtalService
        extends AbstractBaseService<IndexQuotesRequest, IndexQuotesResponse, CommonRequestHeader> {

    private static final Logger logger = LoggerFactory.getLogger(IndexLabciProtalService.class);


    public static final long LARGE_QUOTE_COUNT = 99999999;

    public static final String ACCES_TYPE_REAL_TIME = "INDEX";

    public static final String ACCES_TYPE_DELAY = "EDIDX";

    public static final String ACCES_CMND_CDE = "ACCES_CMND_CDE";

    public static final String CUSTOMER_ID = "CUSTOMER_ID";

    public static final String USER_TYPE_CDE_CUST = "CUST";

    public static final String USER_TYPE_CDE_STFF = "STFF";

    private static final String TRADE_DATE = "tradeDay";

    public static final String ACCES_CMND_CDE_INDEX_DETAIL = "QUOTE_DETAIL";

    public static final String ACCES_CMND_CDE_INDEX_LIST = "QUOTE_LIST";

    public static final Map<String, String> LABCI_LOCALE_MAPPING = new HashMap<String, String>() {
        {
            put("en", "en");
            put("en_US", "en");
            put("zh_HK", "hk");
            put("zh", "cn");
            put("zh_CN", "cn");
        }
    };

    //    private static final String APP_CODE = "PIB";
    private static final String APP_CODE = "MDS_HASE";
    private static final String E_ID = "eid";
    private static final String OBO = "N";
    private static final String DURATION = "15";
    private static final String CHANNEL_ID = "OHI";
    private static final String NUMBER_OF_MARKET = "1";
    private static final String MARKET = "US";
    private static final String MARKET_FLAG = "Y";

    public static final String SPOT = ".";
    public static final String NORMAL_RESPONSE_CODE = "000";

    public static final String[] INDEX_QUOTE_US_SYMBOL_LIST = {".DJI", ".IXIC", ".GSPC", ".OEX", ".FTSE", ".GDAXI", ".FCHI", ".HSI", ".N225"};


    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    @Getter
    @Setter
    private LabciProtalProperties labciProtalProperties;

    @Autowired
    private LabciProtalTokenService labciProtalTokenService;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private IndexQuotesLogUSService indexQuotesLogUSService;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * convertRequest(java.lang.Object, java.lang.Object)
     */
    @Override
    protected Object convertRequest(IndexQuotesRequest request, CommonRequestHeader header) throws Exception {

        if ("OHB".equals(header.getChannelId())||"CMB".equals(header.getAppCode())){
            HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String staffID = httpRequest.getHeader(Constant.REQUEST_HEADER_KEY_USER_ID);
            ArgsHolder.putArgs(IndexLabciProtalService.CUSTOMER_ID, staffID);
        }else {
            /**
             * Before get result from the Cache Distribute, you can do some other business
             * logic
             */
            CacheDistributeResult result = CacheDistributeHolder.getCacheDistribute();
            CacheDistributeResultStateEnum resultState = result.getResultState();
            if (resultState == CacheDistributeResultStateEnum.OK) {
                CacheDistributeResponse response = result.getResponse();
                String value = response.getValue();
                // Get the value you are interested in
                ObjectMapper mapper = new ObjectMapper();
                try {
                    JsonNode node = mapper.readTree(value);
                    String customerId = node.get("eID").asText();
                    if (StringUtil.isInValid(customerId)) {
                        IndexLabciProtalService.logger.error("No eID found from rbp cache");
                        throw new VendorException(ExCodeConstant.EX_CODE_CACHE_NO_EID_FOUND);
                    }
                    ArgsHolder.putArgs(IndexLabciProtalService.CUSTOMER_ID, customerId);
                } catch (Exception e) {
                    IndexLabciProtalService.logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
            } else {
                if (resultState == CacheDistributeResultStateEnum.INVALID_PARAMETERS) {
                    IndexLabciProtalService.logger.error("Cache Distribute Bad Request");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_BAD_REQUEST);
                }
                if (resultState == CacheDistributeResultStateEnum.UNCACHED_RECORD) {
                    IndexLabciProtalService.logger.error("Cache Distribute don't contains the key you sent");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_UNCACHED_RECORD);
                }
                if (resultState == CacheDistributeResultStateEnum.INVALID_HTTP_STATUS) {
                    IndexLabciProtalService.logger.error("Get response from the Cache Distribute encounter error");
                    throw new VendorException(ExCodeConstant.EX_CODE_CACHE_INVALID_HTTP_STATUS);
                }
            }
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_QUOTES_REQUEST, request);
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        StringBuffer symbolList = new StringBuffer();
        List<String> symbols = request.getSymbol();
        if (symbols.size() <= 1) {
            ArgsHolder.putArgs(IndexLabciProtalService.ACCES_CMND_CDE, IndexLabciProtalService.ACCES_CMND_CDE_INDEX_DETAIL);
        } else {
            ArgsHolder.putArgs(IndexLabciProtalService.ACCES_CMND_CDE, IndexLabciProtalService.ACCES_CMND_CDE_INDEX_LIST);
        }

        for (String symbol : symbols) {
            if (!Arrays.asList(IndexLabciProtalService.INDEX_QUOTE_US_SYMBOL_LIST).contains(symbol)) {
                IndexLabciProtalService.logger.error(symbol + " index is NOT supported.");
                throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
            }
            symbolList.append("<indexsymbol>")
                    .append(symbol)
                    .append("</indexsymbol>");
        }
        StringBuffer requestParam = new StringBuffer();
        requestParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        requestParam.append("<envelop><header>")
                .append("<msgid>indices</msgid>")
                .append("<marketid>").append("US").append("</marketid>")
                .append("</header><body>")
                .append("<indiceslist>").append(symbolList.toString()).append("</indiceslist>")
                .append("<locale>").append(LABCI_LOCALE_MAPPING.get(header.getLocale())).append("</locale>")
                .append("</body></envelop>");

        String token = generateToken(site);

        List<NameValuePair> finalParams = new ArrayList<>();
        finalParams.add(new BasicNameValuePair("message", requestParam.toString()));
        finalParams.add(new BasicNameValuePair("token", token));
        return finalParams;
    }

    public String generateToken(String site) {
        LabciToken labciToken = new LabciToken();
        labciToken.setAppCode(APP_CODE);
        labciToken.setCustomerId(E_ID);
        labciToken.setObo(OBO);
        labciToken.setChannelId(CHANNEL_ID);
        labciToken.setDuration(DURATION);
        labciToken.setNumberOfMarket(NUMBER_OF_MARKET);
        labciToken.setMarketId(MARKET);
        labciToken.setMarketFlag(MARKET_FLAG);

        String timeStamp = DateUtil.afterMinutesOfCurrent(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss, TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID), 15);
        labciToken.setTimeStamp(timeStamp);

        return labciProtalTokenService.encryptLabciToken(site, labciToken);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * execute(java.lang.Object)
     */
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        List<NameValuePair> params = (List<NameValuePair>) serviceRequest;
        String xmlResp;
        try {
            xmlResp = this.httpClientHelper.doPost(labciProtalProperties.getProxyName(), labciProtalProperties.getQueryUrl(), params, null);
        } catch (Exception e) {
            logger.error("Access LabCI encounter error", e);
            throw new VendorException(ExCodeConstant.EX_CODE_ACCESS_LABCI_ERROR, e);
        }
        return xmlResp;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * validateServiceResponse(java.lang.Object)
     */
    @Override
    protected Object validateServiceResponse(Object obj) throws Exception {
        String responseStr = (String) obj;

        return responseStr;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService#
     * convertResponse(java.lang.Object)
     */
    @Override
    protected IndexQuotesResponse convertResponse(final Object validServiceResponse) throws Exception {
        IndexQuotesResponse response = new IndexQuotesResponse();
        List<Indice> indices = new ArrayList<>();

        InputStream is = new ByteArrayInputStream(((String) validServiceResponse).getBytes());
        Envelop_US envelop = (Envelop_US) unmarShallerClass(Envelop_US.class).unmarshal(new InputStreamReader(is, "UTF-8"));
        if (NORMAL_RESPONSE_CODE.equals(envelop.getHeader().getResponsecode())) {
            for (IndicesList_US indices_us : envelop.getBody().getIndiceslist()) {
                Indice indice = new Indice();
                indice.setSymbol(indices_us.getIndexsymbol());
                indice.setName(indices_us.getIndexname());
                indice.setLastPrice(BigDecimalUtil.fromStringAndCheckNull(indices_us.getLast()));
                indice.setChangeAmount(BigDecimalUtil.fromStringAndCheckNull(indices_us.getChange()));
                if (StringUtil.isValid(indices_us.getChangepct()) && indices_us.getChangepct().contains("%")) {
                    indice.setChangePercent(BigDecimalUtil.fromStringAndCheckNull(indices_us.getChangepct().replace("%", "")));
                }
                indice.setOpenPrice(BigDecimalUtil.fromStringAndCheckNull(indices_us.getOpen()));
                indice.setPreviousClosePrice(BigDecimalUtil.fromStringAndCheckNull(indices_us.getPreviousclose()));
                indice.setDayRangeHigh(BigDecimalUtil.fromStringAndCheckNull(indices_us.getDayhigh()));
                indice.setDayRangeLow(BigDecimalUtil.fromStringAndCheckNull(indices_us.getDaylow()));
                indice.setYearHighPrice(BigDecimalUtil.fromStringAndCheckNull(indices_us.getW52high()));
                indice.setYearLowPrice(BigDecimalUtil.fromStringAndCheckNull(indices_us.getW52low()));

                TimeZone timeZone = null;
                if (null != indices_us.getTimezone()){
                    indice.setAsOfDate(indices_us.getLastupdateddate());
                    indice.setAsOfTime(indices_us.getLastupdatedtime());
                    if (StringUtil.isValid(indices_us.getLastupdateddate()) && StringUtil.isValid(indices_us.getLastupdatedtime())) {
                        timeZone = TimeZone.getTimeZone(indices_us.getTimezone().replaceAll(" ", ""));
                        String timeStr = indices_us.getLastupdateddate() + " " + indices_us.getLastupdatedtime();
                        String timeresult = DateUtil.convertToISO8601Format(timeStr , DateUtil.DATE_HOUR_PATTERN, timeZone, null);
                        indice.setAsOfDateTime(timeresult);
                    }
                }

                indices.add(indice);
            }
        } else {
            throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
        }

        Map<String, List<Indice>> labciQuoteWithExchge = new HashMap<String, List<Indice>>();
        if (indices.size() > 0) {
            for (Indice indice : indices) {
                if (indice != null && StringUtils.isNotBlank(indice.getSymbol())) {
                    String symbol = indice.getSymbol();
                    String mappedExchangeName = "";
                    if (".DJI".equals(symbol)) {
                        mappedExchangeName = "INDU";
                    } else if (".IXIC".equals(symbol)) {
                        mappedExchangeName = "COMP";
                    } else if (".GSPC".equals(symbol)) {
                        mappedExchangeName = "SPX";
                    }
                    if (labciQuoteWithExchge.get(mappedExchangeName) != null) {
                        labciQuoteWithExchge.get(mappedExchangeName).add(indice);
                    } else {
                        List<Indice> indiceList = new ArrayList<>();
                        indiceList.add(indice);
                        labciQuoteWithExchge.put(mappedExchangeName, indiceList);
                    }
                }
            }
        }
        //update index quote counter & access log
        if (!labciQuoteWithExchge.isEmpty()) {
            this.indexQuotesLogUSService.updateQuoteMeterAndLog(labciQuoteWithExchge);
        }
        response.setIndices(indices);
        response.setMessages(getMessages());
        return response;
    }

    public Unmarshaller unmarShallerClass(final Class Class) {
        Unmarshaller unmarshaller = null;
        try {
            JAXBContext jsxbContext = JAXBContext.newInstance(Class);
            unmarshaller = jsxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("Unmarshaller init fail.");
        }
        return unmarshaller;
    }

    @SuppressWarnings("unchecked")
    private List<Messages> getMessages() {
        if (ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES) == null) {
            return null;
        } else {
            return (List<Messages>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_QUOTES_MESSAGES);
        }
    }


    public RetrieveTradeHourInfoResponse checkTradingHour(final CommonRequestHeader header) throws Exception {
        String rbpTradingDate = "";
        Map<String, String> saml = new HashMap<>();
        if (StringUtil.isValid(header.getSaml3String())) {
            saml.put(Constant.REQUEST_HEADER_KEY_SAML3, header.getSaml3String());
        } else if(StringUtil.isValid(header.getJwtString())){
            saml.put(Constant.REQUEST_HEADER_KEY_JWT, header.getJwtString());
        } else {
            saml.put(Constant.REQUEST_HEADER_KEY_SAML, header.getSamlString());
        }
        String params = new StringBuilder().append(this.labciProtalProperties.getTradingParamPrefix())
                .append(URLEncoder.encode(this.labciProtalProperties.getTradingParam(), "UTF-8")).toString();
        String url = this.labciProtalProperties.getTradingHourUrl().replace(IndexLabciProtalService.TRADE_DATE,
                TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE)));
        try {
            rbpTradingDate = this.httpClientHelper.doGet(url, params, saml);
        } catch (Exception e) {
            this.logger.error("access rbp TradingHour api failed, url is " + url);
            throw new VendorException(ExCodeConstant.EX_CODE_RBP_TRADE_HOUR_ERROR, e);
        }

        return JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeHourInfoResponse>() {
        }.getType());
    }

    public boolean checkTradingDate(final CommonRequestHeader header) throws Exception {
        String rbpTradingDate = "";
        Map<String, String> saml = new HashMap<>();
        String current =
                TradingHourUtil.current(TradingHourUtil.DATE_DAY_PATTERN, TimeZone.getTimeZone(TradingHourUtil.US_TIMEZONE));

        String params = new StringBuilder().append(this.labciProtalProperties.getTradingParamPrefix())
                .append(URLEncoder.encode(this.labciProtalProperties.getTradingParam(), "UTF-8")).toString();
        if (StringUtil.isValid(header.getSaml3String())) {
            saml.put(Constant.REQUEST_HEADER_KEY_SAML3, header.getSaml3String());
        } else if(StringUtil.isValid(header.getJwtString())){
            saml.put(Constant.REQUEST_HEADER_KEY_JWT, header.getJwtString());
        } else {
            saml.put(Constant.REQUEST_HEADER_KEY_SAML, header.getSamlString());
        }
        String url = this.labciProtalProperties.getTradingDayUrl().replace(IndexLabciProtalService.TRADE_DATE, current);
        try {
            rbpTradingDate = this.httpClientHelper.doGet(url, params, saml);
        } catch (Exception e) {
            IndexLabciProtalService.logger.error("access rbp TradingDate api failed, url is " + url);
            throw new VendorException(ExCodeConstant.EX_CODE_RBP_TRADE_HOUR_ERROR, e);
        }

        RetrieveTradeDateInfoResponse response =
                JsonUtil.fromJson(rbpTradingDate, new TypeToken<RetrieveTradeDateInfoResponse>() {
                }.getType());

        boolean isContainToday = false;
        if (response != null) {
            for (TradeDateInfo tradeDateInfo : response.getTradeDateList()) {
                // only compare day , ignore HH:mm:ss
                // TimeZone.getTimeZone("GMT+8")
                if (tradeDateInfo.getOrderTradeDate().compareTo(current) == 0) {
                    isContainToday = true;
                    break;
                }
            }
        }
        return isContainToday;
    }
}
