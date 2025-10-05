/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "healthcheck")
@Getter
@Setter
public class HealthcheckProperties {

    private String username = "mkddashboard";

    private String password = "$2a$10$7ebHDkSGFrK6wJxRyxjNaupqo9wBAzJJ/xnPiEkNB3wLrzA1xL9mW";
    
    private String roles = "USER";

}
