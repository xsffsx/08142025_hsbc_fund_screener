/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.controller;

import com.hhhh.group.secwealth.mktdata.api.equity.common.name.id.handler.NameIdHandler;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.IPORequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.OESQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.SECQuotesRequest;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.QuotesResponse;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.RequestHeader;
import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.parameter.JsonRequestParam;
import com.hhhh.group.secwealth.mktdata.starter.service_dispatcher.controller.DispatcherController;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;
import com.hhhh.wmd.itt.web.bind.annotation.SamlParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant.EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR;

@RestController("quotesController")
@RequestMapping(value = "/wealth/api/v1/market-data", method = RequestMethod.GET)
public class RESTfulController {

    @Autowired
    private DispatcherController dispatcherController;

    @Autowired
    private NameIdHandler nameIdHandler;

    private static final String QUOTES_SERVICE_API = "QUOTES";

    private static final String QUOTES_CMB_SERVICE_API = "QUOTES_CMB";

    private static final String QUOTES_RBP_SERVICE_API = "QUOTES_RBP";

    private static final String LISTED_IPO_SERVICE_API = "LISTED_IPO";

    private static final String CURRENT_IPO_SERVICE_API = "CURRENT_IPO";

    public static final String APP_CODE_CMB = "CMB";

    private static final String HEADER_CMB_FUNCTION_ID_CHECK = "|111|112|131|132|";


    @RequestMapping(value = "/equity/quotes", method = RequestMethod.GET)
    public Object equityQuotes(@JsonRequestParam("param") final SECQuotesRequest request,
                               @RequestHeader final CommonRequestHeader header,
                               @SamlParam(name = "nameId", required = false) final String customerId) throws Exception {

        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());
        String appCode = header.getAppCode();
        String channelId = header.getChannelId();
        String functionId = header.getFunctionId();
        String market = request.getMarket();

        if (appCode.equalsIgnoreCase(APP_CODE_CMB) && channelId.equalsIgnoreCase(Constant.CHANNEL_ID_OHB)) {
            if ((market.equals("HK") || market.equals("CN"))
                    && !HEADER_CMB_FUNCTION_ID_CHECK.contains(SymbolConstant.SYMBOL_VERTICAL_LINE + functionId + SymbolConstant.SYMBOL_VERTICAL_LINE)) {
                throw new Exception(EX_CODE_NO_AVAILABLE_SERVICE_MATCHED_ERROR);
            }
        }

        if(appCode!=null && appCode.equalsIgnoreCase(APP_CODE_CMB)) {
            return this.dispatcherController.execute(RESTfulController.QUOTES_CMB_SERVICE_API, header, request);
        } else if(appCode!=null && appCode.equalsIgnoreCase("RBP")){
            return this.dispatcherController.execute(RESTfulController.QUOTES_RBP_SERVICE_API, header, request);
        } else {
            return this.dispatcherController.execute(RESTfulController.QUOTES_SERVICE_API, header, request);
        }
    }

    /**
     * @param request
     * @param header
     * @param customerId required = false, filed nameId is option
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/quotes", method = RequestMethod.GET)
    public QuotesResponse oesEquityQuotes(@JsonRequestParam("param|body") final OESQuotesRequest request,
                                          @RequestHeader final CommonRequestHeader header,
                                          @SamlParam(name = "nameId", required = false) final String customerId) throws Exception {
        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());
        QuotesResponse response = this.dispatcherController.execute(RESTfulController.QUOTES_SERVICE_API, header, request);
        return response;
    }

    @RequestMapping(value = "/equity/listedIPO", method = RequestMethod.GET)
    public Object listedIPO(@JsonRequestParam("param") final IPORequest request,
                            @RequestHeader final CommonRequestHeader header,
                            @SamlParam(name = "nameId", required = false) final String customerId) throws Exception {
        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());
        Object response = this.dispatcherController.execute(RESTfulController.LISTED_IPO_SERVICE_API, header, request);
        return response;
    }

    @RequestMapping(value = "/equity/currentIPO", method = RequestMethod.GET)
    public Object currentIPO(@JsonRequestParam("param") final IPORequest request,
                             @RequestHeader final CommonRequestHeader header,
                             @SamlParam(name = "nameId", required = false) final String customerId) throws Exception {
        this.nameIdHandler.checkAndEncrypt(header, customerId, request.getProcessingFlag());
        Object response = this.dispatcherController.execute(RESTfulController.CURRENT_IPO_SERVICE_API, header, request);
        return response;
    }
}