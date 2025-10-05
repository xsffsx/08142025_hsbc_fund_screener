/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.index.response;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Indice {

	@NotEmpty(message = "{validator.notEmpty.quotesResponse.quotesPriceQuote.symbol.message}")
	private String symbol;

	private String name;

	private BigDecimal lastPrice;
	
	private BigDecimal changeAmount;
	
	private BigDecimal changePercent;
	
	private BigDecimal openPrice;
	
	private BigDecimal previousClosePrice;
	
	private BigDecimal dayRangeHigh;
	
	private BigDecimal dayRangeLow;
	
	private BigDecimal changePercent1M;
	
	private BigDecimal changePercent2M;
	
	private BigDecimal changePercent3M;
	
	private BigDecimal changePercent1Y;
	
	private BigDecimal oneMonthLowPrice;
	
	private BigDecimal twoMonthLowPrice;
	
	private BigDecimal threeMonthLowPrice;
	
	private BigDecimal oneMonthHighPrice;
	
	private BigDecimal twoMonthHighPrice;
	
	private BigDecimal threeMonthHighPrice;
	
	private BigDecimal yearHighPrice;
	
	private BigDecimal yearLowPrice;

	private String asOfDateTime;

	private String asOfDate;

	private String asOfTime;

}
