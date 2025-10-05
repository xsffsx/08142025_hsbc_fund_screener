package com.hhhh.group.secwealth.mktdata.common.Service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.hhhh.group.secwealth.mktdata.common.service.ToggleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.hhhh.group.secwealth.mktdata.common.config.FeatureTogglesConfig;
import com.hhhh.wealth.feature.proxy.FeaValue;


@RunWith(MockitoJUnitRunner.Silent.class)
public class ToggleServiceTest {
    @Mock
    private FeatureTogglesConfig featureTogglesConfig;

    @Mock
    private FeaValue marketdataAdaptorEnable;

    @Mock
    private FeaValue marketdataAdaptorFailRetryEnable;

    @InjectMocks
    private ToggleService toggleService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(featureTogglesConfig, "marketdataAdaptorEnable", marketdataAdaptorEnable);
        ReflectionTestUtils.setField(featureTogglesConfig, "marketdataAdaptorFailRetryEnable", marketdataAdaptorFailRetryEnable);
    }

    @Test
    public void isMarketdataAdaptorEnable_whenFeatureToggleIsTrue() {
        when(marketdataAdaptorEnable.getBoolean()).thenReturn(true);
        assertTrue(toggleService.isMarketdataAdaptorEnable());
    }

    @Test
    public void isMarketdataAdaptorEnable_whenFeatureToggleIsFalse() {
        when(marketdataAdaptorEnable.getBoolean()).thenReturn(false);
        assertFalse(toggleService.isMarketdataAdaptorEnable());
    }

    @Test
    public void isMarketdataAdaptorFailRetryEnable_whenFeatureToggleIsTrue() {
        when(marketdataAdaptorFailRetryEnable.getBoolean()).thenReturn(true);
        assertTrue(toggleService.isMarketdataAdaptorFailRetryEnable());
    }

    @Test
    public void isMarketdataAdaptorFailRetryEnable_whenFeatureToggleIsFalse() {
        when(marketdataAdaptorFailRetryEnable.getBoolean()).thenReturn(false);
        assertFalse(toggleService.isMarketdataAdaptorFailRetryEnable());
    }
}