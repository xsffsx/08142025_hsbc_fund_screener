/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.UnSignedExchange;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesResponse {

    private List<QuotesPriceQuote> priceQuotes;

    private List<UnSignedExchange> unSignedExchanges;

    private List<Message> messages;

}
