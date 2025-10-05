/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "cache.distribute")
@Getter
@Setter
public class CacheDistributeProperties {
    @NotEmpty
    private String awsUri;

    private String[] patterns = {"/wealth/api/v1/market-data/**"};

    private ExecutorProperties executor = new ExecutorProperties();

    private ConnectionProperties connection = new ConnectionProperties();

}
