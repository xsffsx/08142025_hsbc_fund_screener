/**
 * @Title TopMoverLabciService.java
 * @description TODO
 * @author OJim
 * @time Jun 27, 2019 7:45:05 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.LabciProtalTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProtalProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.entity.LabciBody;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.entity.LabciEnvelop;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.entity.LabciStockList;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.TopMoverRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciProduct;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.topmover.response.TopMoverLabciTable;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ParameterException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.MapUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("labciProtalTopmoverService")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
public class TopMoverLabciProtalService extends AbstractBaseService<TopMoverRequest, TopMoverLabciResponse, CommonRequestHeader> {

    //todo
    public static final String EX_CODE_LABCI_PORTAL_INVALID_RESPONSE = "LabciPortalInvalidResponse";

    private static final Logger logger = LoggerFactory.getLogger(TopMoverLabciProtalService.class);

    private static final String HK_MARKET_QUOTE_STATUS = "HK_MARKET_STATUS";

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    @Getter
    @Setter
    private LabciProtalProperties labciProtalProperties;

    @Autowired
    private LabciProtalTokenService labciProtalTokenService;

    private DocumentBuilderFactory documentBuilderFactory = null;
    private DocumentBuilder documentBuilder = null;


    private static final int MINUTES = 15;
    private static final String OBO = "N";
    private static final String APP_CODE = "PIB";
    private static final String DURATION = "15";
    private static final String MARKET_FLAG = "Y";
    private static final String NUMBER_OF_MARKET = "1";

    public LabciToken getTokenModel(String channelId, String market) {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(channelId);
        labciToken.setCustomerId(null);
        labciToken.setMarketId(market);
        labciToken.setAppCode("MDS_HASE");
        labciToken.setNumberOfMarket(NUMBER_OF_MARKET);
        labciToken.setDuration(DURATION);
        labciToken.setMarketFlag(MARKET_FLAG);
        labciToken.setObo(OBO);
        String timeStamp = DateUtil.afterMinutesOfCurrent(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss, TimeZone.getTimeZone(DateUtil.DEFAULT_TIMEZONE_ID),
                MINUTES);
        labciToken.setTimeStamp(timeStamp);
        return labciToken;
    }

    @Override
    protected Object convertRequest(final TopMoverRequest request, final CommonRequestHeader header) throws Exception {
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        if (StringUtil.isInValid(request.getProductType()) || StringUtil.isInValid(request.getExchangeCode())
                || StringUtil.isInValid(request.getMoverType()) || (request.getTopNum() != null && request.getTopNum() <= 0)) {
            TopMoverLabciProtalService.logger.error(" ProductType or Exchange or MoverType or BoardType or TopNum is empty...... ");
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }

//        String tokenPattern = this.labciProtalProperties.getTokenPattern().get(request.getMarket())
//                .replace("eID", "eID").replace("channelID", header.getChannelId())
//                .replace("timeStamp", DateUtil.current(DateUtil.DATE_DAY_PATTERN_yyyyMMddHHmmss,
//                        TimeZone.getTimeZone(this.labciProtalProperties.getVendorTimezone(request.getMarket()))));
        LabciToken tokenModel = getTokenModel(header.getChannelId(), request.getMarket());
        String encryptedToken = labciProtalTokenService.encryptLabciToken(site, tokenModel);
//        String encryptedToken = this.labciTokenService.encryptLabciToken(TopMoverCommonService.SERVICE_API, request.getMarket(), tokenPattern);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String requestMessage = buildMessageRequest2LabciProtal(request, header);
        params.add(new BasicNameValuePair("token", encryptedToken));
        params.add(new BasicNameValuePair("message", requestMessage));

        Map<String, String> serviceRequestMapper = new LinkedHashMap<String, String>();
        String reqParams = URLEncodedUtils.format(params, "UTF-8");
        serviceRequestMapper.put(request.getMoverType() + SymbolConstant.SYMBOL_VERTICAL_LINE + request.getBoardType(), reqParams);
        if (MapUtils.isEmpty(serviceRequestMapper)) {
            TopMoverLabciProtalService.logger.error(" MoverType error ......");
            throw new ParameterException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
        }
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST, request);
        ArgsHolder.putArgs(TopMoverLabciProtalService.HK_MARKET_QUOTE_STATUS, request.getMarket());
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_DELAY, isDelay(request));
        return serviceRequestMapper;
    }

    private LabciEnvelop processDocToObject(final String xmlResp) {
        // TODO: exception handling
        try {
            if (this.documentBuilderFactory == null) {
                this.documentBuilderFactory = DocumentBuilderFactory.newInstance();
                //DTD disabled, securing it against XXE attack
                this.documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            }
            if (this.documentBuilder == null) {
                this.documentBuilder = this.documentBuilderFactory.newDocumentBuilder();
            }

            Document document = this.documentBuilder.parse(new ByteArrayInputStream(xmlResp.getBytes()));
            Unmarshaller unmarshaller;
            JAXBContext jc = JAXBContext.newInstance(LabciEnvelop.class);
            unmarshaller = jc.createUnmarshaller();
            JAXBElement<LabciEnvelop> je = unmarshaller.unmarshal(document.getFirstChild(), LabciEnvelop.class);
            return je.getValue();
        } catch (Exception e) {
            // TODO: handle exception
            throw new CommonException(EX_CODE_LABCI_PORTAL_INVALID_RESPONSE);
        }
    }

    public String buildMessageRequest2LabciProtal(final TopMoverRequest serviceRequest, final CommonRequestHeader header) {
        StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<envelop><header>").append("<msgid>topmovers</msgid>").append("<marketid>").append(serviceRequest.getMarket())
                .append("</marketid>").append("</header><body>").append("<boardtype>MAIN</boardtype>").append("<movertype>")
                .append(serviceRequest.getMoverType()).append("</movertype>").append("<locale>").append(getLocaleByHeader(header))
                .append("</locale>").append("<returnnumber>10</returnnumber>").append("<useragenttype></useragenttype>")
                .append("<exchange>").append(serviceRequest.getExchangeCode()).append("</exchange>").append("</body></envelop>");
        return sb.toString();
    }

    private String getLocaleByHeader(final CommonRequestHeader header) {
        String locale = header.getLocale();
        switch (locale) {
            case "zh_HK":
                return "tc";
            case "zh":
            case "zh_CN":
                return "sc";
            case "en":
            case "en_US":
                return "en";
            default:
                return "en";
        }
    }

    private boolean isDelay(final TopMoverRequest request) {
        if (request.getDelay() != null) {
            return request.getDelay();
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        Map<String, String> serviceRequestMapper = (Map<String, String>) serviceRequest;
        Map<String, Object> serviceResponseMapper = new LinkedHashMap<>();
        // Map<String, String> serviceRequestMapper = (Map<String, String>)
        // serviceRequest;
        for (Map.Entry<String, String> requestMapper : serviceRequestMapper.entrySet()) {
            String key = requestMapper.getKey();
            String request = requestMapper.getValue();
            // TODO
            String xmlResp = this.httpClientHelper.doGet(this.labciProtalProperties.getProxyName(),
                    this.labciProtalProperties.getQueryUrl(), request, null);
            //System.out.println(xmlResp);

            LabciEnvelop labciEnvelop = processDocToObject(xmlResp);
            serviceResponseMapper.put(key, labciEnvelop);
        }
        return serviceResponseMapper;
    }

    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, LabciEnvelop> serviceResponseMapper = (Map<String, LabciEnvelop>) serviceResponse;
        for (Map.Entry<String, LabciEnvelop> responseMapper : serviceResponseMapper.entrySet()) {
            if (responseMapper.getValue() != null) {
                String responsecode = responseMapper.getValue().getHeader().getResponsecode();
                if (!"000".equals(responsecode)) {

                    // TODO: exception todo
                    throw new VendorException(EX_CODE_LABCI_PORTAL_INVALID_RESPONSE);
                }
            }
        }
        return serviceResponse;
    }

    @Override
    protected TopMoverLabciResponse convertResponse(final Object validServiceResponse) throws Exception {
        TopMoverLabciResponse response = new TopMoverLabciResponse();
        response.setTopMovers(convertQuotesList(validServiceResponse));
        return response;
    }

    @SuppressWarnings("unchecked")
    private List<TopMoverLabciTable> convertQuotesList(final Object validServiceResponse) throws ParseException {
        TopMoverRequest request = (TopMoverRequest) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_TOP_TEN_MOVERS_REQUEST);
        Map<String, LabciEnvelop> resultJsonArrayMapper = (Map<String, LabciEnvelop>) validServiceResponse;
        List<TopMoverLabciTable> topMoverLabciTables = new ArrayList<>();

        List<TopMoverLabciProduct> topMoverLabciProducts = new ArrayList<>();
        for (Map.Entry<String, LabciEnvelop> labciEnvelop : resultJsonArrayMapper.entrySet()) {
            TopMoverLabciTable topMoverLabciTable = new TopMoverLabciTable();
            topMoverLabciTable.setTableKey(request.getMoverType());
            if (labciEnvelop.getValue().getBody().getStocklist() != null) {
                for (LabciStockList stock : labciEnvelop.getValue().getBody().getStocklist()) {
                    TopMoverLabciProduct topMoverLabciProduct = new TopMoverLabciProduct();
                    topMoverLabciProduct.setDelay(true);//
                    topMoverLabciProduct.setSymbol(stock.getStocksymbol());
                    topMoverLabciProduct.setRic(stock.getRiccode());
                    topMoverLabciProduct.setName(stock.getStockname());
                    topMoverLabciProduct.setPreviousClosePrice(convert2Decimal(stock.getPreviousclose()));
                    topMoverLabciProduct.setPrice(convert2Decimal(stock.getLast()));
                    topMoverLabciProduct.setChangeAmount(convertChangeNumToDecimal(stock.getChange(), request.getMoverType()));
                    topMoverLabciProduct.setChangePercent(convertChangeNumToDecimal(stock.getChangepct().replace("%", ""), request.getMoverType()));
                    topMoverLabciProduct.setOpenPrice(convert2Decimal(stock.getOpen()));
                    topMoverLabciProduct.setTurnover(convert2Decimal(stock.getTurnover()));
                    topMoverLabciProduct.setVolume(convert2Decimal(stock.getVolume()));
                    topMoverLabciProduct.setScore(convert2Decimal(stock.getScore()));
                    LabciBody body = labciEnvelop.getValue().getBody();
                    if (null != body.getTimezone()) {
                        TimeZone timeZone = TimeZone.getTimeZone(body.getTimezone());
                        topMoverLabciProduct.setAsOfDate(body.getLastupdateddate());
                        topMoverLabciProduct.setAsOfTime(body.getLastupdatedtime());

                        String timeStr = body.getLastupdateddate() + " " + body.getLastupdatedtime();
                        if(!" null".equals(timeStr)){
                            if (StringUtil.isValid(body.getLastupdateddate())
                                    && StringUtil.isValid(body.getLastupdatedtime())) {
                                topMoverLabciProduct.setAsOfDateTime(DateUtil.convertToISO8601Format(timeStr,
                                        DateUtil.DATE_HOUR_PATTERN, timeZone, null));
                            }
                        }
                    }
                    topMoverLabciProducts.add(topMoverLabciProduct);
                }
            }
            topMoverLabciTable.setProducts(topMoverLabciProducts);
            topMoverLabciTables.add(topMoverLabciTable);
        }
        return topMoverLabciTables;
    }

    public BigDecimal convert2Decimal(final String str) {
        if (!(str == null || str.trim().length() == 0 || "null".equals(str))) {
            Pattern pattern = Pattern.compile("[0-9]\\d*\\.?\\d*");
            Matcher isNum = pattern.matcher(str);
            if (isNum.matches()) {
                return new BigDecimal(str);
            }
        }
        return null;
    }

    public BigDecimal convertChangeNumToDecimal(final String str, String moverType) {
        if (!(str == null || str.trim().length() == 0 || "null".equals(str))) {
            //VENDOR
            Pattern vendor = Pattern.compile("(-)?[0-9]\\d*\\.?\\d*");
            //LOSEPCT
            Pattern losepct = Pattern.compile("-[0-9]\\d*\\.?\\d*");
            //GAINPCT
            Pattern gainpct = Pattern.compile("[0-9]\\d*\\.?\\d*");

            Matcher isNum;

            if (moverType.equals("LOSEPCT")){
                isNum = losepct.matcher(str);
                return (isNum.matches() == true) ? new BigDecimal(str) : null;
            } else if (moverType.equals("GAINPCT")){
                isNum = gainpct.matcher(str);
                return (isNum.matches() == true) ? new BigDecimal(str) : null;
            } else {
                isNum = vendor.matcher(str);
                return (isNum.matches() == true) ? new BigDecimal(str) : null;
            }

        }
        return null;
    }
}
