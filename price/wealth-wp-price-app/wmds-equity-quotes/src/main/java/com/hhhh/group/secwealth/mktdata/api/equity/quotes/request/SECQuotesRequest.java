/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalOnProperty(value = "service.quotes.SECTris.injectEnabled")
public class SECQuotesRequest extends QuotesRequest {

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.productKeys.message}")
    @Valid
    private List<ProductKey> productKeys;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.market.message}")
    @RegEx(regexp = "{validator.regex.quotesRequest.market}", message = "{validator.regex.quotesRequest.market.message}")
    private String market;
    
    /*
     * 22, 31, 33 - map with SRBPBE,SRBP,WD, for special agreement check
     */
    private String requestType;

    private String eid;

    private List<String> requestFields = new ArrayList<>();

    private Boolean limitUpLimitDownRuleEnable = false;

    private String tradingSession;
}
