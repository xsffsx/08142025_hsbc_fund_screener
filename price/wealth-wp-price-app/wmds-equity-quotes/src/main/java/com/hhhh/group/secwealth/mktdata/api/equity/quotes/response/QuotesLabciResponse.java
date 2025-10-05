/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesLabciResponse {

    private List<? extends QuotesLabciQuote> priceQuotes;
    
    private BigDecimal remainingQuota;

    private BigDecimal totalQuota;

    private List<Message> messages;
}
