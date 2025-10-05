/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class QuotesLabciResponseByRequestFields extends QuotesLabciResponse{

    private List<QuotesLabciQuoteByRequestFields> priceQuotesByRequestFields;

}
