package com.hhhh.group.secwealth.mktdata.test.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Quotes {

    private List<ProdAltNumSeg> prodAltNumSegs;

    private List<BidAskQueue> bidAskQueues;

    @JsonAlias("Symbol")
    private String symbol;

    private String market;

    @JsonAlias("ExchangeCode")
    private String exchangeCode;

    private String productType;

    @JsonAlias("ProductSubType")
    private String productSubType;

    private String companyName;

    private Boolean riskAlert;

    private String riskLvlCde;

    private BigDecimal nominalPrice;

    private BigDecimal settlementPrice;

    private String currency;

    private BigDecimal changeAmount;

    private BigDecimal changePercent;

    private Boolean delay;

    private String asOfDateTime;

    private String asOfDate;

    private String asOfTime;

    private BigDecimal bidPrice;

    private BigDecimal bidSize;

    private BigDecimal askPrice;

    private BigDecimal askSize;

    private BigDecimal bidSpread;

    private BigDecimal askSpread;

    private String spreadCode;

    private BigDecimal openPrice;

    private BigDecimal previousClosePrice;

    private BigDecimal prevTradePrice;

    private BigDecimal dayLowPrice;

    private BigDecimal dayHighPrice;

    private BigDecimal yearLowPrice;

    private BigDecimal yearHighPrice;

    private BigDecimal volume;

    private BigDecimal boardLotSize;

    private BigDecimal marketCap;

    private BigDecimal sharesOutstanding;

    private BigDecimal peRatio;

    private BigDecimal eps;

    private BigDecimal iep;

    private BigDecimal iev;

    private String auctionIndicator;

    private String brokerBidQueue;

    private String brokerAskQueue;

    private BigDecimal turnover;

    private BigDecimal dividend;

    private BigDecimal dividendYield;

    private String vcmStatus;

    private String vcmEligible;

    private String vcmStartTime;

    private String vcmEndTime;

    private BigDecimal vcmLowerLimitPrice;

    private BigDecimal vcmUpperLimitPrice;

    private String casEligible;

    private BigDecimal casLowerLimitPrice;

    private BigDecimal casUpperLimitPrice;

    private BigDecimal casReferencePrice;

    private BigDecimal lowerLimitPrice;

    private BigDecimal upperLimitPrice;

    private BigDecimal primaryLastActivity;

    private String accumulatedVolume;

    private BigDecimal unscaledTurnover;

    private BigDecimal totalSharesOutstanding;

    private BigDecimal totalIssuedShares;

    private BigDecimal strikePrice;

    private BigDecimal callPrice;

    private String warrantType;

    private String premium;

    private String maturityDate;

    private String callPutIndicator;

    private BigDecimal conversionRatio;

    private BigDecimal expectedPERatio;

    private BigDecimal predictDividendYield;

    private BigDecimal gearingRatio;

    private BigDecimal impliedVolatility;

    private Boolean isSuspended;

    private String nominalPriceType;

    private BigDecimal vcmReferencePrice;

    private String derivativeFlag;

    private String orderImbalanceDirection;

    private String orderImbalanceQuantity;

    private BigDecimal strikeUpper;

    private BigDecimal strikeLower;

    private String marketStatus;

    private String posEligible;

    private String auctionType;

    private BigDecimal posReferencePrice;

    private BigDecimal posBuyUpperPrice;

    private BigDecimal posBuyLowerPrice;

    private BigDecimal posSellUpperPrice;

    private BigDecimal posSellLowerPrice;

    private String underlyingProduct;

    private String lastTradeDate;

    private String cbbcType;

    private String industry;
    private String exDivDate;
    @JsonAlias("IsADR")
    private Boolean isADR;
    private BigDecimal adrRatio;
    private BigDecimal adrPrice;
    private String adrCcy;
    private String hkStockCode;
    private String hkStockName;
    @JsonAlias("IsETF")
    private Boolean isETF;
    private String region;
    private String fundType;
    private String investmentSector;
    private BigDecimal beta6m;
    private BigDecimal beta1y;
    private BigDecimal beta3y;
    private BigDecimal beta5y;
    private BigDecimal beta10y;
    private BigDecimal aum;
    private String aumCcy;
    private String aumDate;
    private BigDecimal expenseRatio;

    private PastPerformance pastPerformance;

    private String exDividendDate;
    private String financialStatus;
    private String tradingStatus;

    private List<LastTradeRecord> lastTradeRecords;

    private String lastExecutionTime;
}
