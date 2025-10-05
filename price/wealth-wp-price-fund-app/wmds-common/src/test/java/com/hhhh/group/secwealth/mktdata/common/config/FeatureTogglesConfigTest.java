package com.hhhh.group.secwealth.mktdata.common.config;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.hhhh.wealth.feature.proxy.FeaValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FeatureTogglesConfigTest {

    @InjectMocks
    private FeatureTogglesConfig featureTogglesConfig;

    @Mock
    private FeaValue marketdataAdaptorEnable;

    @Mock
    private FeaValue marketdataAdaptorFailRetryEnable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMarketdataAdaptorEnable() {
        when(marketdataAdaptorEnable.getString()).thenReturn("true");
        assertEquals("true", featureTogglesConfig.marketdataAdaptorEnable.getString());
    }

    @Test
    void testMarketdataAdaptorFailRetryEnable() {
        when(marketdataAdaptorFailRetryEnable.getString()).thenReturn("false");
        assertEquals("false", featureTogglesConfig.marketdataAdaptorFailRetryEnable.getString());
    }
}