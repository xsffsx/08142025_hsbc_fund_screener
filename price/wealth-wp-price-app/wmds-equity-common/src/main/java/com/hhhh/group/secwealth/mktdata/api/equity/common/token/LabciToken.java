package com.hhhh.group.secwealth.mktdata.api.equity.common.token;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabciToken {

	private final String EMPTY_VALUE = "";
	private final String SEPARATOR_VALUE = "~~";

	private String appCode;
	private String customerId;
	private String obo;
	private String timeStamp;
	private String duration;
	private String channelId;
	private String numberOfMarket;
	private String marketId;
	private String marketFlag;

	public LabciToken() {
		initDefaultValue();
	}

	private void initDefaultValue() {
		this.appCode = EMPTY_VALUE;
		this.customerId = EMPTY_VALUE;
		this.obo = EMPTY_VALUE;
		this.timeStamp = EMPTY_VALUE;
		this.duration = EMPTY_VALUE;
		this.channelId = EMPTY_VALUE;
		this.numberOfMarket = EMPTY_VALUE;
		this.marketId = EMPTY_VALUE;
		this.marketFlag = EMPTY_VALUE;
	}

	public String generateTokenStr() {
		return appCode + SEPARATOR_VALUE + customerId + SEPARATOR_VALUE + obo + SEPARATOR_VALUE + timeStamp + SEPARATOR_VALUE
				+ duration + SEPARATOR_VALUE + channelId + SEPARATOR_VALUE + numberOfMarket + SEPARATOR_VALUE + marketId
				+ SEPARATOR_VALUE + marketFlag;
	}

}
