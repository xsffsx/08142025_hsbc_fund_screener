package com.dummy.wpb.wpc.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Data
@Component
@ConfigurationProperties(prefix = "thread-pool-task-executor-configuration")
public class ThreadPoolTaskExecutorConfiguration {

	private Integer corePoolSize;

	private Integer maxPoolSize;

	private Integer keepAliveSeconds;
}
