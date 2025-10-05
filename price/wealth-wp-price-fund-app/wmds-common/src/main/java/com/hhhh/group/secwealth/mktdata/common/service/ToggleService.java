package com.hhhh.group.secwealth.mktdata.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.config.FeatureTogglesConfig;

@Service
public class ToggleService {

	@Autowired
	protected FeatureTogglesConfig featureTogglesConfig;

    public boolean isMarketdataAdaptorEnable() {
    	return featureTogglesConfig.marketdataAdaptorEnable.getBoolean();
    }

    public boolean isMarketdataAdaptorFailRetryEnable() {
    	return featureTogglesConfig.marketdataAdaptorFailRetryEnable.getBoolean();
    }
}