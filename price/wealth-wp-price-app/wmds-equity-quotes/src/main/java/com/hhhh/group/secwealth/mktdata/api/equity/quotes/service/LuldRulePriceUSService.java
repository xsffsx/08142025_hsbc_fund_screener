package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.LabciResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Ric;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.Watchlist;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.LabciServletBoConvertor;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesLabciQuoteByRequestFields;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LuldRulePriceUSService {

    private static final Logger logger = LoggerFactory.getLogger(LuldRulePriceUSService.class);

    @Value("${labci.luld-url}")
    private String cachingServerUrl;

    @Autowired
    private HttpClientHelper httpClientHelper;

    // fid 75 UPLIMIT
    private static final String FIELD_UPLIMIT = "UPLIMIT";

    // fid 1465 ADJUST_CLS
    private static final String FIELD_ADJUST_CLS = "ADJUST_CLS";

    private static final String TRADING_HALT_INDICATOR = "H";

    public void calculateLuldRulePrice(List<QuotesLabciQuoteByRequestFields> priceQuotes, String service, String tradingSession) {
        if (ListUtil.isInvalid(priceQuotes)) {
            return;
        }

        SECQuotesRequest secQuotesRequest = (SECQuotesRequest)ArgsHolder.getArgs(QuotesUSCachingServerService.THREAD_INVISIBLE_QUOTES_US_REQUEST);

        List<String> ricCodes = secQuotesRequest.getProductKeys().stream()
                .filter(prodAltNumSeg -> "R".equalsIgnoreCase(prodAltNumSeg.getProdCdeAltClassCde()))
                .map(ProductKey::getProdAltNum)
                .collect(Collectors.toList());

        Table<String, String, BigDecimal> limitUpPriceProductTab = this.getProductLimitUpOrAdjClosePrice(ricCodes, service);
        /** During continuous trading
         from 9:30 am to 4:00pm EST: Limit up price or request trading session is CTS**/
        Boolean ctsIndicator = this.checkIfDuringContinuousTradingSession(tradingSession);
        for (QuotesLabciQuoteByRequestFields priceQuote : priceQuotes) {
            if (TRADING_HALT_INDICATOR.equalsIgnoreCase(priceQuote.getTradeStatus())) {
                this.setupLuldPriceWhenTradingHalt(priceQuote, ricCodes);
            } else if (Boolean.TRUE.equals(ctsIndicator)) {
                this.setupLuldDuringCts(limitUpPriceProductTab, priceQuote, ricCodes);
            }
        }
    }

    private void setupLuldDuringCts(Table<String, String, BigDecimal> limitUpPriceProductTab, QuotesLabciQuoteByRequestFields priceQuote, List<String> ricCodes) {
        String ricCode = this.extractRicCode(ricCodes);
        if (null != ricCode) {
            BigDecimal limitUpPrice = limitUpPriceProductTab.get(ricCode, FIELD_UPLIMIT);
            /**
             * "if Ask Price (HK) or Limit Up Price (US) are not returned from the feed,
             * use the trading halt formula instead
             */
            if (null == limitUpPrice || (limitUpPrice.compareTo(BigDecimal.ZERO)==0)) {
                this.setupLuldPriceWhenTradingHalt(priceQuote, ricCodes);
                return;
            }
            priceQuote.setLimitUpLimitDownRulePrice(limitUpPrice);
            BigDecimal adjustedPrice = this.getAdjustedClosePrice(priceQuote, ricCodes);
            priceQuote.setAdjustedClosePrice(adjustedPrice);
        }
    }

    private void setupLuldPriceWhenTradingHalt(QuotesLabciQuoteByRequestFields priceQuote, List<String> ricCodes) {
        BigDecimal adjustedPrice = this.getAdjustedClosePrice(priceQuote, ricCodes);
        BigDecimal previousClosePrice = adjustedPrice != null ? adjustedPrice : priceQuote.getPreviousClosePrice();
        BigDecimal lastTradedPrice = priceQuote.getNominalPrice();
        priceQuote.setLimitUpLimitDownRulePrice(this.calLuldPriceWhenTradingHalt(previousClosePrice, lastTradedPrice));
        priceQuote.setAdjustedClosePrice(adjustedPrice);
    }

    private BigDecimal getAdjustedClosePrice(QuotesLabciQuoteByRequestFields priceQuote, List<String> ricCodes) {
        String ricCode = this.extractRicCode(ricCodes);
        BigDecimal adjustedPrice = null;
        if (null != ricCode) {
            //HASE biz requested to use NASDAQ basic feed
            adjustedPrice = priceQuote.getAdjustedClosePrice();
        }

        return adjustedPrice;
    }

    private String extractRicCode(List<String> ricCodes) {
        // HASE not support get LULD for multi stocks in one request
        return ricCodes.size()==1?ricCodes.get(0):null;
    }

    private Boolean checkIfDuringContinuousTradingSession(String tradingSession) {
        Boolean tradingHourIndicator = (Boolean) ArgsHolder.getArgs(QuotesUSCommonService.THREAD_INVISIBLE_QUOTES_TRADING_HOUR);
        return Boolean.TRUE.equals(tradingHourIndicator) || "CTS".equalsIgnoreCase(tradingSession);
    }

    // During trading halt: 115% x Maximum (Previous close(fid 1465 ADJUST_CLS) or Last trade price(fid 6 TRDPRC_1))
    private BigDecimal calLuldPriceWhenTradingHalt(BigDecimal previousClosePrice, BigDecimal lastTradedPrice) {
        if (null != previousClosePrice && null != lastTradedPrice) {
            BigDecimal max = previousClosePrice.compareTo(lastTradedPrice) > 0 ? previousClosePrice : lastTradedPrice;
            return new BigDecimal("1.15").multiply(max);
        }
        log.error("Invalid previous close price: {} or last trade price: {}", previousClosePrice, lastTradedPrice);
        return null;
    }

    private Table<String, String, BigDecimal> getProductLimitUpOrAdjClosePrice(List<String> items, String service) {
        if (ListUtil.isInvalid(items)) {
            return HashBasedTable.create();
        }
        List<NameValuePair> labciParams = new ArrayList<>();
        String symbolList = LabciServletBoConvertor.genSymbols(items, service);
        labciParams.add(new BasicNameValuePair("SymbolList", symbolList));
        StringBuilder fieldStr = new StringBuilder(FIELD_UPLIMIT);
        fieldStr.append(SymbolConstant.SYMBOL_SEMISOLON).append(FIELD_ADJUST_CLS);
        labciParams.add(new BasicNameValuePair("FieldList", fieldStr.toString()));
        labciParams.add(new BasicNameValuePair("DataRepresentation", QuotesUSCommonService.LABCI_DATA_REPRESENTATION_XML));
        Map<String, LabciResponse> limitUpPriceLabciResponseMap = new HashMap<>();
        try {
            String labCiResponse = this.httpClientHelper.doGet(this.cachingServerUrl, labciParams, new HashMap<>());
            limitUpPriceLabciResponseMap = this.parseLabicResp(labCiResponse);
        } catch (Exception e) {
            log.error("Failed to get data from: {}, Error msg: {}", this.cachingServerUrl, e.getMessage());
        }

        Table<String, String, BigDecimal> productLimitUpPriceTable = HashBasedTable.create();
        if (!limitUpPriceLabciResponseMap.isEmpty()) {
            for (Map.Entry<String, LabciResponse> labciResponseEntry : limitUpPriceLabciResponseMap.entrySet()) {
                Map<String, Object> fields = labciResponseEntry.getValue().getFields();
                BigDecimal adjustedClosePrice = BigDecimalUtil.fromString((String) fields.get(FIELD_ADJUST_CLS));
                if(adjustedClosePrice!=null) {
                    productLimitUpPriceTable.put(labciResponseEntry.getKey(), FIELD_ADJUST_CLS, adjustedClosePrice);
                }

                BigDecimal upLimitPrice = BigDecimalUtil.fromString((String) fields.get(FIELD_UPLIMIT));
                if(upLimitPrice!=null) {
                    productLimitUpPriceTable.put(labciResponseEntry.getKey(), FIELD_UPLIMIT, upLimitPrice);
                }

            }
        }
        return productLimitUpPriceTable;
    }

    private Map<String, LabciResponse> parseLabicResp(String labciResponses) throws JAXBException {
        if (StringUtil.isInValid(labciResponses)) {
            log.error("Labic invalid response: {}", labciResponses);
            throw new VendorException(ExCodeConstant.EX_CODE_LABCI_INVALID_RESPONSE);
        }
        return this.transferEachLabciQuoteResponse(labciResponses);
    }

    private Map<String, LabciResponse> transferEachLabciQuoteResponse(String labciResponses) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Watchlist.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Watchlist res = (Watchlist) unmarshaller.unmarshal(new StringReader(labciResponses));
        Map<String, LabciResponse> response = new LinkedHashMap<>();
        if (null != res) {
            List<Ric> ricList = res.getRic();
            if (!ListUtils.isEmpty(ricList)) {
                response = LabciServletBoConvertor.getResponseMap(ricList);
            }
        }
        return response;
    }
}
