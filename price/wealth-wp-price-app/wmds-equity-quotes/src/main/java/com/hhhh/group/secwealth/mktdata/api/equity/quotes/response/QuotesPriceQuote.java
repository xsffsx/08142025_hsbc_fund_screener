/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.entity.BidAskQueue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuotesPriceQuote {

    @Valid
    @NotNull(message = "{validator.notNull.quotesResponse.quotesPriceQuote.prodAltNumSegs.message}")
    private List<ProdAltNumSeg> prodAltNumSegs;

    private List<BidAskQueue> bidAskQueues;

    @NotEmpty(message = "{validator.notEmpty.quotesResponse.quotesPriceQuote.symbol.message}")
    private String symbol;

    @NotEmpty(message = "{validator.notEmpty.quotesResponse.quotesPriceQuote.currency.message}")
    private String currency;

    private String market;

    private String exchangeCode;

    private String productType;

    private String companyName;

    private BigDecimal nominalPrice;
    
    private BigDecimal remainingQuota;

    private BigDecimal dayLowPrice;

    private BigDecimal dayHighPrice;

    private BigDecimal peRatio;

    private BigDecimal previousClosePrice;

    private BigDecimal yearLowPrice;

    private BigDecimal bidPrice;

    private BigDecimal askPrice;

    private String asOfDateTime;

    private BigDecimal turnover;

    private BigDecimal sharesOutstanding;

    private BigDecimal dividendYield;

    private BigDecimal volume;

    private BigDecimal changeAmount;

    private BigDecimal changePercent;

    private Boolean delay;

    private BigDecimal bidSize;

    private BigDecimal openPrice;

    private BigDecimal askSize;

    private BigDecimal bidSpread;

    private BigDecimal askSpread;

    private BigDecimal yearHighPrice;

    private BigDecimal boardLotSize;

    private BigDecimal marketCap;

    private String brokerAskQueue;

    private String brokerBidQueue;

    private Boolean vcmEligible;

    private Boolean casEligible;

    private BigDecimal vcmStatus;

    private BigDecimal casLowerPrice;

    private BigDecimal casUpperPrice;

    private String vcmStartTime;

    private String vcmEndTime;

    private BigDecimal vcmLowerLimitPrice;

    private BigDecimal vcmUpperLimitPrice;

    private BigDecimal upperLimitPrice;

    private BigDecimal lowerLimitPrice;

    private BigDecimal casLowerLimitPrice;

    private BigDecimal casUpperLimitPrice;

    private BigDecimal totalQuota;

    private Boolean auctionIndicator;

    private BigDecimal eps;

    private BigDecimal iep;

    private BigDecimal iev;

    private BigDecimal dividend;

    private BigDecimal prevTradePrice;

    private String riskLvlCde;

    private BigDecimal settlementPrice;

    private BigDecimal primaryLastActivity;

    private String accumulatedVolume;

    private BigDecimal unscaledTurnover;

    private BigDecimal totalSharesOutstanding;

    private BigDecimal totalIssuedShares;

    private BigDecimal daySecLowLimPrice;

    private BigDecimal daySecUpperLimPrice;

    private BigDecimal dayLowLimPrice;

    private BigDecimal dayUpperLimPrice;

    private BigDecimal limitReferencePrice;

    private String priceCode;

}
