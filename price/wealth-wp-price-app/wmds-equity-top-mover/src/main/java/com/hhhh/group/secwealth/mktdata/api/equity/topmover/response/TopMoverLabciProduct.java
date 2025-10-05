/**
 * @Title TopMoverLabciProduct.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:24:31 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
/**
 * @Title TopMoverLabciProduct.java
 * @description TODO
 * @author OJim
 * @time Jun 28, 2019 5:24:31 PM
 */
@Setter
@Getter
public class TopMoverLabciProduct implements Serializable {

	private static final long serialVersionUID = 3116603200672306042L;

	private String ric;

	private String symbol;

	private String market;

	private String name;

	private BigDecimal price;

	private Boolean delay;

	private BigDecimal changeAmount;

	private BigDecimal changePercent;

	private BigDecimal volume;

	private BigDecimal openPrice;

	private BigDecimal previousClosePrice;

	private BigDecimal turnover;

	private BigDecimal score;

	private String productType;

	private String currency;

	private String asOfDateTime;

	private String asOfDate;

	private String asOfTime;
	
	private Boolean isQuotable;

}