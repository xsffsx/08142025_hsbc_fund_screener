/*
 */
package com.hhhh.group.secwealth.mktdata.starter.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "core")
@Getter
@Setter
public class CoreProperties {

    private String defaultInvalidResponseExCode = "InvalidResponse";

}
