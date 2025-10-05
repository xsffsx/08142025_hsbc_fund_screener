package com.dummy.wpb.wpc.utils.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class InterceptorConfigTests {

    @InjectMocks
    private InterceptorConfig interceptorConfig;
    @Mock
    private InterceptorRegistry registry;
    @Mock
    private InterceptorRegistration interceptorRegistration;

    @Test
    public void testAddInterceptors_givenInterceptorRegistry_doesNotThrow() {
        try {
            Mockito.when(registry.addInterceptor(any())).thenReturn(interceptorRegistration);
            Mockito.when(interceptorRegistration.addPathPatterns(anyString())).thenReturn(interceptorRegistration);
            interceptorConfig.addInterceptors(registry);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
