package com.hhhh.group.secwealth.mktdata.api.equity.quotes.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs.RequestPattern;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.ApplicationProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.MIDFSProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BeanUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.QuoteHistoryRepository;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.QuoteHistory;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.OESQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service.QuoteListServiceHKRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.Message;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesPriceQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesResponse;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.core.service.AbstractBaseService;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.exception.ExTraceCodeGenerator;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.ExResponseComponent;
import com.hhhh.group.secwealth.mktdata.starter.global_exception_handler.response.entity.ExResponseEntity;
import com.hhhh.group.secwealth.mktdata.starter.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import com.google.gson.JsonObject;

@Service("midfs")
@ConditionalOnProperty(value = "service.quotes.OES.injectEnabled")
public class QuotesMidfsService extends AbstractBaseService<OESQuotesRequest, QuotesResponse, CommonRequestHeader> {


    private static final Logger logger = LoggerFactory.getLogger(QuotesMidfsService.class);

    private static final String SERVICE_ID = "QUOTE_LIST";

    private static final String VENDOR_API = "StockPortfolio";

    private static final String THREAD_INVISIBLE_STOCK = "STOCK";

    private static final String THREAD_INVISIBLE_LOCALE = "LOCALE";

    private static final String THREAD_INVISIBLE_CLASSCDE = "CLASSCDE";

    private static final String THREAD_INVISIBLE_TYPE = "type";

    private static final String BEAN_PRICE_QUOTE = "PRICE_QUOTE";

    private static final String REQUEST_STATUS = "staus";

    private static final String responseCode_14 = "14";

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private MIDFSProperties midfsProperties;

    @Autowired
    private HttpClientHelper httpClientHelper;

    @Autowired
    private QuoteHistoryRepository quoteHistoryDao;

    @Autowired
    private ExResponseComponent exRespComponent;

    @Autowired
    private QuoteHistoryService quoteHistoryService;

    @Override
    protected Object convertRequest(final OESQuotesRequest request, final CommonRequestHeader header) throws Exception {
        QuoteListServiceHKRequest serviceRequest = new QuoteListServiceHKRequest();
        serviceRequest.setDelay(request.getDelay());
        serviceRequest.setLocale(header.getLocale());

        List<String> prodAltNums = new ArrayList<>();
        for (String prodAltNum : request.getProdAltNums()) {
            prodAltNums.add(prodAltNum);
        }

        String stock = String.join(SymbolConstant.SYMBOL_SEMISOLON, prodAltNums);
        serviceRequest.setStock(stock);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER, header);
        ArgsHolder.putArgs(QuotesMidfsService.THREAD_INVISIBLE_CLASSCDE, request.getProdCdeAltClassCde());
        ArgsHolder.putArgs(QuotesMidfsService.THREAD_INVISIBLE_TYPE, request.getProductType());
        ArgsHolder.putArgs(QuotesMidfsService.THREAD_INVISIBLE_STOCK, stock);
        ArgsHolder.putArgs(QuotesMidfsService.THREAD_INVISIBLE_LOCALE, header.getLocale());
        ArgsHolder.putArgs(QuotesMidfsService.REQUEST_STATUS, request.getDelay());

        return serviceRequest;
    }

    @Override
    protected Object execute(final Object serviceRequest) throws Exception {
        QuoteListServiceHKRequest request = (QuoteListServiceHKRequest) serviceRequest;
        String commandId = this.midfsProperties.getCommandId(QuotesMidfsService.VENDOR_API, request.getLocale(), request.isDelay());
        RequestPattern requestPattern = this.midfsProperties.getRequestPattern(QuotesMidfsService.VENDOR_API);
        String dummyParameter = requestPattern.getDummyParameter();
        String requestParameter = dummyParameter.replace(Constant.REQUEST_PARAMETER_COMMAND_ID, commandId)
            .replace(Constant.REQUEST_PARAMETER_QUOTE_STOCK, request.getStock());
        return this.httpClientHelper.doGet(requestPattern.getUrl(), requestParameter, null);
    }

    @Override
    protected Object validateServiceResponse(final Object serviceResponse) throws Exception {
        JsonObject jsonNode = JsonUtil.getAsJsonObject(String.valueOf(serviceResponse));
        String status = jsonNode.get(this.midfsProperties.getResponseStatusKey()).getAsString();
        if (!this.midfsProperties.isCorrectResponseStatus(status)) {
            String errorMessage = this.midfsProperties.getResponseMessage(status);
            QuotesMidfsService.logger.error(errorMessage);
            if (status.equals(QuotesMidfsService.responseCode_14)) {
                throw new VendorException(ExCodeConstant.EX_CODE_REQUEST_NOTMATCH_ERROR);
            } else {
                throw new VendorException(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
            }
        }
        return jsonNode;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected QuotesResponse convertResponse(final Object validServiceResponse) throws Exception {
        CommonRequestHeader commonHeader = (CommonRequestHeader) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_COMMON_HEADER);
        QuotesResponse response = new QuotesResponse();
        List<QuotesPriceQuote> priceQuotes = new ArrayList<>();
        Boolean args = (Boolean) ArgsHolder.getArgs(QuotesMidfsService.REQUEST_STATUS);
        response.setPriceQuotes(priceQuotes);
        String subscriberId = (String) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_ENCRYPTED_CUSTOMER_ID);
        // String subscriberId = null != customerId ? customerId :
        // this.applicationProperties.getCustomerId();
        JsonObject responseNode = (JsonObject) validServiceResponse;
        String stocks = String.valueOf(ArgsHolder.getArgs(QuotesMidfsService.THREAD_INVISIBLE_STOCK));
        Long seqNum = this.quoteHistoryDao.querySeqNum();
        List<QuoteHistory> quoteHistoryList = new ArrayList<QuoteHistory>();
        for (String stock : stocks.split(SymbolConstant.SYMBOL_SEMISOLON)) {
            JsonObject stockNode = responseNode.get(Constant.MIDFS_RESPONSE_PREFIX + stock).getAsJsonObject();
            QuoteHistory quoteHistory = new QuoteHistory();
            if (stockNode != null && stockNode.size() > 0) {
                QuotesPriceQuote priceQuote = new QuotesPriceQuote();
                BeanUtil.jsonToBean(priceQuote, this.applicationProperties.getResponseFieldMapper(QuotesMidfsService.SERVICE_ID,
                    QuotesMidfsService.BEAN_PRICE_QUOTE), stockNode);
                if (!args) {
                    String priceCode = stockNode.get("mkt_stat").getAsString();
                    priceQuote.setPriceCode(this.midfsProperties.getFiledConvert(priceCode));
                }
                String date = stockNode.get("data_date").getAsString();
                String time = stockNode.get("time_modified").getAsString();
                if (!StringUtils.isEmpty(date) && !StringUtils.isEmpty(time)) {
                    priceQuote.setAsOfDateTime(DateUtil.parseDateByTimezone(
                        DateUtil.parseString(date + time, Constant.DATE_FORMAT_MIDFS,
                            TimeZone.getTimeZone(this.applicationProperties.getTimezone())),
                        Constant.TIMEZONE, Constant.DATE_FORMAT_TRIS_ISO8601));
                }
                priceQuote.setCompanyName(stockNode
                    .get(this.midfsProperties
                        .getCompanyField(String.valueOf(ArgsHolder.getArgs(QuotesMidfsService.THREAD_INVISIBLE_LOCALE))))
                    .getAsString());
                priceQuote.setSymbol(stock);

                String prodCdeAltClassCde = (String) ArgsHolder.getArgs(QuotesMidfsService.THREAD_INVISIBLE_CLASSCDE);
                ProdAltNumSeg seg = new ProdAltNumSeg();
                List<ProdAltNumSeg> segList = new ArrayList<ProdAltNumSeg>();
                seg.setProdAltNum(stock);
                seg.setProdCdeAltClassCde(prodCdeAltClassCde);
                segList.add(seg);
                priceQuote.setProdAltNumSegs(segList);
                priceQuotes.add(priceQuote);
                quoteHistory.setTradeDatetime(priceQuote.getAsOfDateTime());
                quoteHistory.setResponseText(stockNode.toString());
                quoteHistory.setRequestStatus("Y");
            } else {
                // TODO: currently hard code;not exist,request parameter is
                // wrong.
                List<Message> messages = new ArrayList<>();
                response.setMessages(messages);
                Message message = new Message();
                ExResponseEntity exResponse = this.exRespComponent.getExResponse(ExCodeConstant.EX_CODE_MIDFS_INVALID_RESPONSE);
                message.setReasonCode(exResponse.getReasonCode());
                message.setText(exResponse.getText());
                String traceCode = ExTraceCodeGenerator.generate();
                message.setTraceCode(traceCode);
                String type = (String) ArgsHolder.getArgs(QuotesMidfsService.THREAD_INVISIBLE_TYPE);
                message.setProductType(type);
                String prodCdeAltClassCde = (String) ArgsHolder.getArgs(QuotesMidfsService.THREAD_INVISIBLE_CLASSCDE);
                message.setProdCdeAltClassCde(prodCdeAltClassCde);
                message.setProdAltNum(stock);
                messages.add(message);
                quoteHistory.setResponseText("Request paramter Error");
                quoteHistory.setRequestStatus("N");
            }

            // TODO: not sure about the config
            quoteHistory.setSubscriberId(subscriberId);
            quoteHistory.setUpdatedOn(Calendar.getInstance().getTime());
            quoteHistory.setSymbol(stock);
            quoteHistory.setExchangeCode("HKG");
            quoteHistory.setMarketCode("HK");
            if (args) {
                quoteHistory.setRequestType(Constant.DELAYED);
            } else {
                quoteHistory.setRequestType(Constant.REAL_TIME);
            }
            quoteHistory.setQuotHistBatId(seqNum);
            quoteHistory.setRequestTime(Calendar.getInstance().getTime());
            quoteHistory.setCountryCode(commonHeader.getCountryCode());
            quoteHistory.setGroupMember(commonHeader.getGroupMember());
            quoteHistory.setRequestTimeZone(DateUtil.getTimeZoneName());
            quoteHistory.setChannelId(commonHeader.getChannelId());
            quoteHistory.setAppCode(commonHeader.getAppCode());
            quoteHistoryList.add(quoteHistory);
        }
        this.quoteHistoryService.updatequoteHistory(quoteHistoryList);
        return response;
    }

}
