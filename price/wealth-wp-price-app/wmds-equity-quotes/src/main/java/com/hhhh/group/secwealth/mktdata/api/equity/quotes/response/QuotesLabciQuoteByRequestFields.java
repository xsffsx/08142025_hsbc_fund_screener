/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuotesLabciQuoteByRequestFields extends QuotesLabciQuote{

    private List<ProdAltNumSeg> prodAltNumSegs;

    private String symbol;

    private String currency;

    private int recStatus;

    private BigDecimal tradePrice;

    private BigDecimal limitUpLimitDownRulePrice;

    private String tradeStatus;

    private String tradeDate;

    private String tradeTime;

    private BigDecimal adjustedClosePrice;

    private BigDecimal previousClosePrice;

}
