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

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.OracleSqlDialect;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.pretty.SqlPrettyWriter;
import org.apache.calcite.sql.validate.SqlConformanceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 混合SQL语法转换器 结合 Alibaba Druid 和 Apache Calcite 的优势： - 第一层（Druid）：快速兜底转换器，处理常见语法差异 -
 * 第二层（Calcite）：标准化渲染器，确保复杂SQL的语义正确性
 */
@Component
public class HybridSqlConverter {

	private static final Logger logger = LoggerFactory.getLogger(HybridSqlConverter.class);

	// SQL复杂度判断模式
	private static final Pattern COMPLEX_SQL_PATTERN = Pattern.compile(
			"(?i).*(WITH\\s+|UNION\\s+|INTERSECT\\s+|EXCEPT\\s+|WINDOW\\s+|OVER\\s*\\(|CASE\\s+WHEN|EXISTS\\s*\\(|IN\\s*\\(\\s*SELECT).*",
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	// MySQL特有语法检测
	private static final Pattern MYSQL_SYNTAX_PATTERN = Pattern
		.compile("(?i).*(LIMIT\\s+\\d+|`[^`]+`|\\sAS\\s+'[^']+'|;\\s*;).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

	/**
	 * 智能SQL语法转换
	 * @param sql 原始SQL
	 * @param dialectType 目标数据库类型
	 * @return 转换后的SQL
	 */
	public String convertSql(String sql, String dialectType) {
		if (sql == null || sql.trim().isEmpty()) {
			return sql;
		}

		if (!"oracle".equalsIgnoreCase(dialectType)) {
			// 非Oracle数据库，直接返回
			return sql;
		}

		try {
			// 智能路由：根据SQL复杂度选择转换策略
			if (shouldUseCalcite(sql)) {
				logger.info("[Hybrid] 使用Calcite进行标准化转换（复杂SQL）");
				return convertWithCalcite(sql, dialectType);
			}
			else {
				logger.info("[Hybrid] 使用Druid进行快速转换（简单SQL）");
				return convertWithDruid(sql, dialectType);
			}
		}
		catch (Exception e) {
			logger.warn("[Hybrid] SQL转换失败，返回原始SQL: {}", e.getMessage());
			return sql;
		}
	}

	/**
	 * 判断是否应该使用Calcite进行转换
	 * @param sql SQL语句
	 * @return true表示使用Calcite，false表示使用Druid
	 */
	private boolean shouldUseCalcite(String sql) {
		// 1. 检查是否包含复杂SQL结构
		if (COMPLEX_SQL_PATTERN.matcher(sql).matches()) {
			logger.debug("检测到复杂SQL结构，选择Calcite");
			return true;
		}

		// 2. 检查是否包含多个MySQL语法问题
		String upperSql = sql.toUpperCase();
		int mysqlSyntaxCount = 0;
		if (upperSql.contains("LIMIT"))
			mysqlSyntaxCount++;
		if (sql.contains("`"))
			mysqlSyntaxCount++;
		if (sql.contains("AS '"))
			mysqlSyntaxCount++;

		if (mysqlSyntaxCount >= 2) {
			logger.debug("检测到多个MySQL语法问题，选择Calcite进行标准化");
			return true;
		}

		// 3. SQL长度超过阈值，使用Calcite确保准确性
		if (sql.length() > 500) {
			logger.debug("SQL长度较长({}字符)，选择Calcite确保准确性", sql.length());
			return true;
		}

		// 默认使用Druid快速转换
		return false;
	}

	/**
	 * 使用Calcite进行标准化转换
	 * @param sql 原始SQL
	 * @param dialectType 目标数据库类型
	 * @return 转换后的SQL
	 */
	private String convertWithCalcite(String sql, String dialectType) {
		try {
			// 配置MySQL一致性解析器
			SqlParser.Config config = SqlParser.config()
				.withConformance(SqlConformanceEnum.MYSQL_5)
				.withCaseSensitive(false);

			// 解析SQL
			SqlParser parser = SqlParser.create(sql, config);
			SqlNode sqlNode = parser.parseStmt();

			// 使用Oracle方言渲染
			SqlPrettyWriter writer = new SqlPrettyWriter(OracleSqlDialect.DEFAULT);
			sqlNode.unparse(writer, 0, 0);
			String convertedSql = writer.toString();

			// 使用Druid(ORACLE)进行一次round-trip标准化，移除不兼容细节（如表别名AS）
			SQLStatement oracleStmt = SQLUtils.parseSingleStatement(convertedSql, JdbcConstants.ORACLE);
			String finalSql = SQLUtils.toSQLString(oracleStmt, JdbcConstants.ORACLE);

			logger.info("🎯 Calcite转换成功: {} -> {}", sql.trim(), finalSql.trim());
			return finalSql;

		}
		catch (Exception e) {
			logger.warn("Calcite转换失败，回退到Druid: {}", e.getMessage());
			return convertWithDruid(sql, dialectType);
		}
	}

	/**
	 * 使用Druid进行快速转换
	 * @param sql 原始SQL
	 * @param dialectType 目标数据库类型
	 * @return 转换后的SQL
	 */
	private String convertWithDruid(String sql, String dialectType) {
		try {
			// 使用Druid从MySQL解析并渲染为Oracle
			SQLStatement statement = SQLUtils.parseSingleStatement(sql, JdbcConstants.MYSQL);
			String convertedSql = SQLUtils.toSQLString(statement, JdbcConstants.ORACLE);

			// 再进行一次Druid(ORACLE) round-trip标准化，确保输出符合Oracle最佳实践
			SQLStatement oracleStmt = SQLUtils.parseSingleStatement(convertedSql, JdbcConstants.ORACLE);
			String finalSql = SQLUtils.toSQLString(oracleStmt, JdbcConstants.ORACLE);

			logger.info("⚡ Druid转换成功: {} -> {}", sql.trim(), finalSql.trim());
			return finalSql;

		}
		catch (Exception e) {
			logger.error("Druid转换失败: {}", e.getMessage());
			throw new RuntimeException("SQL转换失败", e);
		}
	}

	/**
	 * 检查SQL是否包含MySQL特有语法
	 * @param sql SQL语句
	 * @return true表示包含MySQL语法
	 */
	public boolean containsMySqlSyntax(String sql) {
		return sql != null && MYSQL_SYNTAX_PATTERN.matcher(sql).matches();
	}

	/**
	 * 获取转换器统计信息
	 * @return 统计信息
	 */
	public String getConverterStats() {
		return "HybridSqlConverter: Druid(快速) + Calcite(标准化)";
	}

}
