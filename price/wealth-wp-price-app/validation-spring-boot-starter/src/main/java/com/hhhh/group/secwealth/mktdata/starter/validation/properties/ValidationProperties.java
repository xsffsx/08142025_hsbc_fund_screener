/*
 */
package com.hhhh.group.secwealth.mktdata.starter.validation.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "validation.group")
@Getter
@Setter
public class ValidationProperties {

    private boolean enabled = false;

    private String path = "classpath:validation/";

    private String mapping = "validation-mapping.xml";

    private String config = "validation-config.xml";

    private String scanning = "com.hhhh.group.secwealth.mktdata.starter.validation.group";

    private String entityKey = "SITE";

}
