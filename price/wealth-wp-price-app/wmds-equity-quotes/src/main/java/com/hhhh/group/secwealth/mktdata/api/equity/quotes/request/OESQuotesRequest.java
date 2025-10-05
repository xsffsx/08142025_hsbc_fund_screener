/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConditionalOnProperty(value = "service.quotes.OES.injectEnabled")
public class OESQuotesRequest extends QuotesRequest {

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.prodAltNums.message}")
    @Valid
    private String prodAltNums[];

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.productType.message}")
    private String productType;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.market.message}")
    private String market;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.prodCdeAltClassCde.message}")
    @RegEx(regexp = "{validator.regex.quotesRequest.prodCdeAltClassCde}", message = "{validator.regex.quotesRequest.prodCdeAltClassCde.message}")
    private String prodCdeAltClassCde;

}
