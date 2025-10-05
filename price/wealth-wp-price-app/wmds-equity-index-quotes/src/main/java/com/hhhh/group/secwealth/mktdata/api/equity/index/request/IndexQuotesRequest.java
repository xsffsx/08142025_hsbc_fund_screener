/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.index.request;

import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexQuotesRequest extends EquityCommonRequest{

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.symbol.message}")
    @Valid
    private List<String> symbol;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.market.message}")
    @RegEx(regexp = "{validator.regex.quotesRequest.market}", message = "{validator.regex.quotesRequest.market.message}")
    private String market;
}
