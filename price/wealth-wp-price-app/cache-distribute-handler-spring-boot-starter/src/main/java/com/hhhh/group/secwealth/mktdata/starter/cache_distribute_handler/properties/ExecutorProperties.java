/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutorProperties {

    private Integer corePoolSize = 50;

    private Integer maxPoolSize = 100;

    private Integer queueCapacity = 500;

}
