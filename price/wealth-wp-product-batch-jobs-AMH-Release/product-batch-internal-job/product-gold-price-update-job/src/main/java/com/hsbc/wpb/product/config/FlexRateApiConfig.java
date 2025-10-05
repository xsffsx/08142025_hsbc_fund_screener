package com.dummy.wpb.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configure FlexRate API info
 */
@Component
@ConfigurationProperties(prefix = "flexrate")
@Data
public class FlexRateApiConfig {

    private String url;

    private String account;

    private String authorization;
}
