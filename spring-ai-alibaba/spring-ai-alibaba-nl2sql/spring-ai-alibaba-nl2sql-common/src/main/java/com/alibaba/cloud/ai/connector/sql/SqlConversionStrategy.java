/*
 * Copyright 2024-2025 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.alibaba.cloud.ai.connector.sql;

import com.alibaba.cloud.ai.connector.config.DbConfig;

/**
 * 可插拔的SQL转换策略接口。 由上层模块（如 chat 模块）提供具体实现，以避免在common层硬编码任何方言逻辑。
 */
public interface SqlConversionStrategy {

	/**
	 * 如有需要，对即将执行的SQL进行转换；否则返回原SQL。
	 * @param dbConfig 当前数据库配置（用于方言判断等）
	 * @param originalSql 原始SQL
	 * @return 转换后的SQL（或原SQL）
	 */
	String maybeConvert(DbConfig dbConfig, String originalSql);

}
