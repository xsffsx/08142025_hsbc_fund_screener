/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.PredSrchService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciPropsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuotesServiceRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.*;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.service.stockinfo.QuotesUSLabciPortalService;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.ConvertorsUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.TradingHourUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.hhhh.group.secwealth.mktdata.api.equity.quotes.util.FieldNameConstant.*;
import static com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant.PROD_CDE_ALT_CLASS_CODE_M;

@Service("quotesUSCachingServer4RbpService")
@ConditionalOnProperty(value = "service.quotes.usEquity.injectEnabled", havingValue = "true")
public class QuotesUSCachingServer4RbpService extends QuotesUSCachingServerService {

    private static final Logger logger = LoggerFactory.getLogger(QuotesUSCachingServer4RbpService.class);

    private static final String RBP_REQUEST_FIELDS = "RBP_REQUEST_FIELDS";

    @Autowired
    private QuotesUSLabciPortalService quotesUSLabciPortalService;

    @Autowired
    private LabciProperties labciProperties;

    @Autowired
    private PredSrchService predSrchService;

    @Autowired
    private LuldRulePriceUSService luldRulePriceUSService;

    @Autowired
    private QuotesUSCommonService quotesUSCommonService;

    @Override
    protected Object convertRequest(final SECQuotesRequest request, final CommonRequestHeader header) throws Exception {

        boolean isTradingHour = false;
        boolean isDelay = request.getDelay();

        if (!Arrays.asList(QuotesUSCommonService.EXCLUDE_APPCODE).contains(header.getAppCode())
                && this.quotesUSCommonService.checkTradingDate(header)) {
            if (TradingHourUtil.withinTradingHour(true, this.quotesUSCommonService.checkTradingHour(header))) {
                isTradingHour = true;
            }
        }

        if (Constant.CASE_20.equals(request.getRequestType()) || Constant.CASE_0.equals(request.getRequestType())) {
            ArgsHolder.putArgs(ACCES_CMND_CDE, ACCES_CMND_CDE_QUOTE_DETAIL);
        } else {
            ArgsHolder.putArgs(ACCES_CMND_CDE, ACCES_CMND_CDE_QUOTE_LIST);
        }

        ArgsHolder.putArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR, isTradingHour);
        ArgsHolder.putArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_DELAY, isDelay);

        QuotesServiceRequest quotesServiceRequest = buildServiceRequest(request, header);
        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));
        String service = this.labciProperties.getLabciService(site, this.isDelayedQuotes(isDelay, isTradingHour));
        Map<String, List<ServiceProductKey>> groupedProductKeysMapper = getGroupedProductKeysMapper(site, quotesServiceRequest);
        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_PRODUCT_KEY_MAPPER, groupedProductKeysMapper);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);

        ArgsHolder.putArgs(RBP_REQUEST_FIELDS, request.getRequestFields());
        ArgsHolder.putArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST, request);

        return extractServiceMapper(groupedProductKeysMapper, service);
    }

    @Override
    protected void setServiceProductKeys(QuotesServiceRequest quoteServiceRequest,
                                         SECQuotesRequest request,
                                         Map<String, List<String>> symbolsMap,
                                         List<PredSrchResponse> responses) {

        List<ProductKey> productKeys = request.getProductKeys().stream().filter(productKey -> productKey.getProdCdeAltClassCde()
                .equalsIgnoreCase(PROD_CDE_ALT_CLASS_CODE_M)).collect(Collectors.toList());

        quoteServiceRequest.setServiceProductKeys(ConvertorsUtil.constructServiceProductKeys(productKeys,
                symbolsMap, responses, Constant.PROD_CDE_ALT_CLASS_CODE_T));
    }

    @Override
    protected List<QuotesLabciQuoteByRequestFields> convertLabciResponse(Object validServiceResponse){
        List<String> requestFieldNames = (List<String>) ArgsHolder.getArgs(RBP_REQUEST_FIELDS);
        boolean isDelay = (boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_DELAY);
        boolean isTradingHour = (boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR);
        ArgsHolder.putArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR, true);
        SECQuotesRequest request = (SECQuotesRequest) ArgsHolder.getArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST);

        String site = String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_SITE));

        Map<String, Map<String, LabciResponse>> resultJsonArrayMapper = (Map<String, Map<String, LabciResponse>>) validServiceResponse;
        List<QuotesLabciQuoteByRequestFields> priceQuotes = new ArrayList<>();

        for (Map.Entry<String, Map<String, LabciResponse>> jsonArrayMapper : resultJsonArrayMapper.entrySet()) {
            String key = jsonArrayMapper.getKey();
            Map<String, LabciResponse> resultJsonArray = jsonArrayMapper.getValue();
            for (Map.Entry<String, LabciResponse> labciResponseMap : resultJsonArray.entrySet()) {
                if (labciResponseMap.getKey().contains(KEY_D)) {
                    continue;
                }

                LabciResponse labciResponse = labciResponseMap.getValue();
                String symbolWithNb = labciResponseMap.getKey();

                QuotesLabciQuoteByRequestFields priceQuote = new QuotesLabciQuoteByRequestFields();
                priceQuotes.add(priceQuote);
                priceQuote.setTradeStatus((String)labciResponse.getFields().get("TRD_STATUS"));
                priceQuote.setNominalPrice(BigDecimalUtil.fromString((String) labciResponse.getFields().get("TRDPRC_1")));
                priceQuote.setPreviousClosePrice(BigDecimalUtil.fromString((String) labciResponse.getFields().get("HST_CLOSE")));
                priceQuote.setAdjustedClosePrice(BigDecimalUtil.fromString((String) labciResponse.getFields().get("ADJUST_CLS")));
                requestFieldNames.forEach(fieldName -> {
                    setRequestField(priceQuote,
                            labciResponse,
                            key,
                            fieldName,
                            symbolWithNb);
                });
            }
        }

        // set up luldprice if enable luld rule indicator
        this.setupLuldPrice(isDelay, isTradingHour, site, request, priceQuotes);

        return priceQuotes;
    }

    protected void setRequestField(QuotesLabciQuoteByRequestFields quotesLabciQuoteByRequestFields,
                                   LabciResponse labciResponse,
                                   String key,
                                   String fieldName,
                                   String symbolWithNb) {

        switch (fieldName) {
            case SYMBOL:
                String symbol = this.getSymbol(symbolWithNb, key);
                quotesLabciQuoteByRequestFields.setSymbol(symbol);
                break;
            case TRADE_PRICE:
                //TRDPRC_1
                String[] labciFields = new String[]{"TRDPRC_1"};
                quotesLabciQuoteByRequestFields.setTradePrice(inOrderNumberProps4US(labciFields, labciResponse.getFields()));
                break;
            case CURRENCY:
                quotesLabciQuoteByRequestFields.setCurrency(LabciPropsUtil.inOrderStrProps(CURRENCY, key, labciResponse.getFields()));
                break;
            case BID_PRICE:
                quotesLabciQuoteByRequestFields.setBidPrice(LabciPropsUtil.inOrderNumberProps(BID_PRICE, key, labciResponse.getFields()));
                break;
            case ASK_PRICE:
                quotesLabciQuoteByRequestFields.setAskPrice(LabciPropsUtil.inOrderNumberProps(ASK_PRICE, key, labciResponse.getFields()));
                break;
            case TRADE_DATE:
                //TRADE_DATE
                quotesLabciQuoteByRequestFields.setTradeDate((String)labciResponse.getFields().get("TRADE_DATE"));
                break;
            case TRADE_TIME:
                //TRDTIM_1
                quotesLabciQuoteByRequestFields.setTradeTime((String)labciResponse.getFields().get("TRDTIM_1"));
                break;
            case REC_STATUS:
                //REC_STATUS
                quotesLabciQuoteByRequestFields.setRecStatus(labciResponse.getStatus());
                break;
            case LIMIT_UP_LIMIT_DOWN_RULE_PRICE:
                //quotesLabciQuoteByRequestFields.setLimitUpLimitDownRulePrice(null);
                break;
            default:
        }

    }

    private String getSymbol(String symbolWithNb, String key) {
        PredSrchResponse predSrchResp = this.predSrchService.localPredSrch(symbolWithNb,
                key.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE)[1],
                Constant.PROD_CDE_ALT_CLASS_CODE_T);

        return predSrchResp!=null?predSrchResp.getSymbol():null;
    }

    private void setupLuldPrice(boolean isDelay, boolean isTradingHour, String site, SECQuotesRequest request, List<QuotesLabciQuoteByRequestFields> priceQuotes) {
        if (Boolean.TRUE.equals(request.getLimitUpLimitDownRuleEnable())) {
            String service = this.labciProperties.getLabciService(site, this.isDelayedQuotes(isDelay, isTradingHour));
            this.luldRulePriceUSService.calculateLuldRulePrice(priceQuotes, service, request.getTradingSession());
        }
    }

    private boolean isDelayedQuotes(final boolean isDelay, final boolean isTradingHour) {
        return (isDelay || !isTradingHour);
    }

}
