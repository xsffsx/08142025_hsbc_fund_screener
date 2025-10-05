package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import java.math.BigDecimal;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class CurrentIPOResult {

	private String symbol;

	private BigDecimal boardLot;

	private String nature;

	private String ipoSponsor;

	private String asOfDateTime;
}
