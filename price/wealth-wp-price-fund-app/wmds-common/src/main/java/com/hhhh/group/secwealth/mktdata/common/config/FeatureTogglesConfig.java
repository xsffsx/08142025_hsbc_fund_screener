package com.hhhh.group.secwealth.mktdata.common.config;

import org.springframework.stereotype.Component;

import com.hhhh.wealth.feature.annotation.FeaProperty;
import com.hhhh.wealth.feature.proxy.FeaValue;

@SuppressWarnings({"java:S3749"}) //not an issue
@Component
public class FeatureTogglesConfig {

	@FeaProperty(key = "marketdata.adaptor.enable", defaultValue = "true")
	public FeaValue marketdataAdaptorEnable;

	@FeaProperty(key = "marketdata.adaptor.fail.retry.enable", defaultValue = "false")
	public FeaValue marketdataAdaptorFailRetryEnable;
}
