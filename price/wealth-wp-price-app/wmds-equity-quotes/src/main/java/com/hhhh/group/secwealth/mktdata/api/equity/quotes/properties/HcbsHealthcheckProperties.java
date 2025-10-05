package com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "healthcheck")
@Getter
@Setter
public class HcbsHealthcheckProperties {

    private String service;

    private List<String> item;

    private List<String> filter;

    private CommonRequestHeader header;
    
    private String midfsProdNum;

}
