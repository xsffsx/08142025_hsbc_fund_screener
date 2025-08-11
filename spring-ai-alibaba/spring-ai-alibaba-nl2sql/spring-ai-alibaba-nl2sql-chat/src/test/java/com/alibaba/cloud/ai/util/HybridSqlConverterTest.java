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
package com.alibaba.cloud.ai.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 混合SQL转换器测试类
 */
class HybridSqlConverterTest {

	private static final Logger logger = LoggerFactory.getLogger(HybridSqlConverterTest.class);

	private HybridSqlConverter converter;

	@BeforeEach
	void setUp() {
		converter = new HybridSqlConverter();
	}

	@Test
	@DisplayName("测试简单LIMIT转换（使用Druid）")
	void testSimpleLimitConversion() {
		String mysqlSql = "SELECT * FROM funds ORDER BY return_rate DESC LIMIT 5";
		String oracleSql = converter.convertSql(mysqlSql, "oracle");

		logger.info("简单LIMIT转换: {} -> {}", mysqlSql, oracleSql);

		// 验证不包含LIMIT
		assertFalse(oracleSql.toUpperCase().contains("LIMIT"), "转换后的SQL不应包含LIMIT");

		// 验证包含Oracle分页语法
		assertTrue(oracleSql.toUpperCase().contains("FETCH FIRST") || oracleSql.toUpperCase().contains("ROWNUM"),
				"转换后的SQL应包含Oracle分页语法");
	}

	@Test
	@DisplayName("测试复杂SQL转换（使用Calcite）")
	void testComplexSqlConversion() {
		String complexSql = "WITH ranked_funds AS (SELECT *, ROW_NUMBER() OVER (ORDER BY return_rate DESC) as rn FROM funds) SELECT * FROM ranked_funds LIMIT 10";
		String oracleSql = converter.convertSql(complexSql, "oracle");

		logger.info("复杂SQL转换: {} -> {}", complexSql, oracleSql);

		// 验证不包含LIMIT
		assertFalse(oracleSql.toUpperCase().contains("LIMIT"), "转换后的SQL不应包含LIMIT");

		// 验证包含WITH子句（复杂SQL结构保持）
		assertTrue(oracleSql.toUpperCase().contains("WITH"), "转换后的SQL应保持WITH子句");
	}

	@Test
	@DisplayName("测试反引号转换")
	void testBacktickConversion() {
		String mysqlSql = "SELECT `fund_name`, `return_rate` FROM `funds` ORDER BY `return_rate` DESC LIMIT 3";
		String oracleSql = converter.convertSql(mysqlSql, "oracle");

		logger.info("反引号转换: {} -> {}", mysqlSql, oracleSql);

		// 验证不包含反引号
		assertFalse(oracleSql.contains("`"), "转换后的SQL不应包含反引号");

		// 验证包含双引号
		assertTrue(oracleSql.contains("\""), "转换后的SQL应包含双引号");
	}

	@Test
	@DisplayName("测试单引号别名转换")
	void testSingleQuoteAliasConversion() {
		String mysqlSql = "SELECT fund_name AS '基金名称', return_rate AS '收益率' FROM funds LIMIT 5";
		String oracleSql = converter.convertSql(mysqlSql, "oracle");

		logger.info("单引号别名转换: {} -> {}", mysqlSql, oracleSql);

		// 验证不包含单引号别名
		assertFalse(oracleSql.contains("AS '"), "转换后的SQL不应包含单引号别名");

		// 验证包含双引号别名
		assertTrue(oracleSql.contains("AS \""), "转换后的SQL应包含双引号别名");
	}

	@Test
	@DisplayName("测试非Oracle数据库直接返回")
	void testNonOracleDatabase() {
		String mysqlSql = "SELECT * FROM funds ORDER BY return_rate DESC LIMIT 5";

		// 测试MySQL
		String mysqlResult = converter.convertSql(mysqlSql, "mysql");
		assertEquals(mysqlSql, mysqlResult, "MySQL数据库应直接返回原SQL");

		// 测试PostgreSQL
		String pgResult = converter.convertSql(mysqlSql, "postgresql");
		assertEquals(mysqlSql, pgResult, "PostgreSQL数据库应直接返回原SQL");
	}

	@Test
	@DisplayName("测试空SQL处理")
	void testEmptySql() {
		assertNull(converter.convertSql(null, "oracle"), "null SQL应返回null");
		assertEquals("", converter.convertSql("", "oracle"), "空字符串SQL应返回空字符串");
		assertEquals("   ", converter.convertSql("   ", "oracle"), "空白SQL应返回原值");
	}

	@Test
	@DisplayName("测试MySQL语法检测")
	void testMySqlSyntaxDetection() {
		assertTrue(converter.containsMySqlSyntax("SELECT * FROM t LIMIT 5"), "应检测到LIMIT语法");
		assertTrue(converter.containsMySqlSyntax("SELECT `col` FROM t"), "应检测到反引号语法");
		assertTrue(converter.containsMySqlSyntax("SELECT col AS '别名' FROM t"), "应检测到单引号别名");

		assertFalse(converter.containsMySqlSyntax("SELECT * FROM t ORDER BY col"), "标准SQL不应被检测为MySQL语法");
		assertFalse(converter.containsMySqlSyntax("SELECT col AS \"别名\" FROM t"), "Oracle语法不应被检测为MySQL语法");
	}

	@Test
	@DisplayName("测试智能路由逻辑")
	void testIntelligentRouting() {
		// 简单SQL应使用Druid（快速）
		String simpleSql = "SELECT * FROM funds LIMIT 5";
		String result1 = converter.convertSql(simpleSql, "oracle");
		assertNotNull(result1, "简单SQL转换应成功");

		// 复杂SQL应使用Calcite（标准化）
		String complexSql = "WITH cte AS (SELECT * FROM funds) SELECT * FROM cte UNION SELECT * FROM funds LIMIT 10";
		String result2 = converter.convertSql(complexSql, "oracle");
		assertNotNull(result2, "复杂SQL转换应成功");

		// 长SQL应使用Calcite（确保准确性）
		String longSql = "SELECT f.fund_id, f.fund_name, f.return_rate, f.risk_level, f.manager_name, f.inception_date, f.net_asset_value, f.expense_ratio, f.minimum_investment, f.category FROM funds f WHERE f.return_rate > 0.05 AND f.risk_level IN ('LOW', 'MEDIUM') AND f.inception_date >= '2020-01-01' ORDER BY f.return_rate DESC, f.net_asset_value DESC LIMIT 20";
		String result3 = converter.convertSql(longSql, "oracle");
		assertNotNull(result3, "长SQL转换应成功");
	}

	@Test
	@DisplayName("测试转换器统计信息")
	void testConverterStats() {
		String stats = converter.getConverterStats();
		assertNotNull(stats, "统计信息不应为null");
		assertTrue(stats.contains("Druid"), "统计信息应包含Druid");
		assertTrue(stats.contains("Calcite"), "统计信息应包含Calcite");

		logger.info("转换器统计信息: {}", stats);
	}

	@Test
	@DisplayName("测试错误处理和降级")
	void testErrorHandlingAndFallback() {
		// 测试包含语法错误的SQL
		String invalidSql = "SELECT * FROM funds WHERE ORDER BY return_rate DESC LIMIT 5"; // 语法错误
		String result = converter.convertSql(invalidSql, "oracle");

		// 应该返回原SQL或进行基本清理，不应抛出异常
		assertNotNull(result, "错误SQL处理后不应返回null");
		logger.info("错误SQL处理结果: {} -> {}", invalidSql, result);
	}

	@Test
	@DisplayName("性能对比测试")
	void testPerformanceComparison() {
		String[] testSqls = { "SELECT * FROM funds ORDER BY return_rate DESC LIMIT 5",
				"SELECT fund_name AS '基金名称', return_rate AS '收益率' FROM funds WHERE return_rate > 0.05 LIMIT 10",
				"SELECT `fund_id`, `fund_name` FROM `funds` WHERE `category` = 'EQUITY' ORDER BY `return_rate` DESC LIMIT 20",
				"WITH top_funds AS (SELECT * FROM funds WHERE return_rate > 0.1) SELECT * FROM top_funds ORDER BY net_asset_value DESC LIMIT 15" };

		logger.info("开始性能对比测试...");

		for (String sql : testSqls) {
			long startTime = System.nanoTime();
			String result = converter.convertSql(sql, "oracle");
			long endTime = System.nanoTime();

			long durationMs = (endTime - startTime) / 1_000_000;

			logger.info("SQL转换耗时: {}ms, 原SQL长度: {}, 结果长度: {}", durationMs, sql.length(), result.length());

			// 验证转换成功
			assertNotNull(result, "转换结果不应为null");
			assertFalse(result.toUpperCase().contains("LIMIT"), "转换后不应包含LIMIT");

			// 性能要求：单次转换应在100ms内完成
			assertTrue(durationMs < 100, "SQL转换耗时应小于100ms，实际耗时: " + durationMs + "ms");
		}
	}

	@Test
	@DisplayName("批量转换测试")
	void testBatchConversion() {
		String baseSql = "SELECT * FROM funds WHERE category = 'EQUITY' ORDER BY return_rate DESC LIMIT ";
		int batchSize = 50;

		logger.info("开始批量转换测试，批次大小: {}", batchSize);

		long totalStartTime = System.nanoTime();

		for (int i = 1; i <= batchSize; i++) {
			String sql = baseSql + i;
			String result = converter.convertSql(sql, "oracle");

			assertNotNull(result, "批量转换结果不应为null");
			assertFalse(result.toUpperCase().contains("LIMIT"), "批量转换后不应包含LIMIT");
		}

		long totalEndTime = System.nanoTime();
		long totalDurationMs = (totalEndTime - totalStartTime) / 1_000_000;
		double avgDurationMs = (double) totalDurationMs / batchSize;

		logger.info("批量转换完成，总耗时: {}ms, 平均耗时: {:.2f}ms/次", totalDurationMs, avgDurationMs);

		// 性能要求：平均转换时间应小于50ms
		assertTrue(avgDurationMs < 50, "平均转换耗时应小于50ms，实际平均耗时: " + avgDurationMs + "ms");
	}

}
