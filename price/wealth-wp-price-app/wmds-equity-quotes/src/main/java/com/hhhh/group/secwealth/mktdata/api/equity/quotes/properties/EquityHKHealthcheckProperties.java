/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "equity-healthcheck")
@Getter
@Setter
public class EquityHKHealthcheckProperties {

    private String keyWord;

    private String productKeys;
    
    private String tCodeSymbol;

    private CommonRequestHeader header;
}
