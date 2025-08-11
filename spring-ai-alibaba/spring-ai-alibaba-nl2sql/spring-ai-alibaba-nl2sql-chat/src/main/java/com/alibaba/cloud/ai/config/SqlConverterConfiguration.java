/*
 * Copyright 2024-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.ai.config;

import com.alibaba.cloud.ai.util.HybridSqlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SQL转换器配置类
 */
@Configuration
public class SqlConverterConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(SqlConverterConfiguration.class);

	@Bean
	@ConditionalOnMissingBean(HybridSqlConverter.class)
	public HybridSqlConverter hybridSqlConverter() {
		logger.info("创建混合SQL转换器: Druid(快速兜底) + Calcite(标准化渲染)");
		return new HybridSqlConverter();
	}

}
