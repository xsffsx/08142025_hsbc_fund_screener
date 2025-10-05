/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request;
import javax.validation.constraints.NotNull;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesRequest extends EquityCommonRequest {

    @NotNull(message = "{validator.notNull.quotesRequest.delay.message}")
    private Boolean delay;
}
