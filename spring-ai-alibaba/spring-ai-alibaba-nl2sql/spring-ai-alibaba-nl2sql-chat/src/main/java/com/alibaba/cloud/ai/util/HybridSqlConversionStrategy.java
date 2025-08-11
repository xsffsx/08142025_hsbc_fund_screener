package com.alibaba.cloud.ai.util;

import com.alibaba.cloud.ai.connector.config.DbConfig;
import com.alibaba.cloud.ai.connector.sql.SqlConversionStrategy;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 适配 HybridSqlConverter 的通用SQL转换策略实现。
 */
@Component
public class HybridSqlConversionStrategy implements SqlConversionStrategy {

	private static final Logger logger = LoggerFactory.getLogger(HybridSqlConversionStrategy.class);

	private final HybridSqlConverter hybrid;

	public HybridSqlConversionStrategy(HybridSqlConverter hybrid) {
		this.hybrid = hybrid;
	}

	@Override
	public String maybeConvert(DbConfig dbConfig, String originalSql) {
		if (dbConfig == null) {
			return originalSql;
		}
		String dialect = dbConfig.getDialectType();
		logger.info("[SQL-Conv/Strategy] dialect='{}' originalSql='{}'", dialect, originalSql);

		// 如果目标是oracle，先尝试ORACLE→ORACLE规范化，避免双重转换
		if ("oracle".equalsIgnoreCase(dialect)) {
			try {
				SQLStatement oracleStmt = SQLUtils.parseSingleStatement(originalSql, JdbcConstants.ORACLE);
				String normalized = SQLUtils.toSQLString(oracleStmt, JdbcConstants.ORACLE);
				if (!normalized.equals(originalSql)) {
					logger.info("[SQL-Conv/Normalize] oracle round-trip before='{}' after='{}'", originalSql,
							normalized);
				}
				return normalized;
			}
			catch (Exception ignore) {
				// 非ORACLE语法，继续走混合转换
			}
		}

		String converted = hybrid.convertSql(originalSql, dialect);
		if (!originalSql.equals(converted)) {
			logger.info("[SQL-Conv/Hybrid] dialect={} before='{}' after='{}'", dialect, originalSql, converted);
		}

		if ("oracle".equalsIgnoreCase(dialect)) {
			logger.info("[SQL-Conv/Strategy] Oracle dialect detected, applying normalization");
			try {
				SQLStatement oracleStmt = SQLUtils.parseSingleStatement(converted, JdbcConstants.ORACLE);
				String normalized = SQLUtils.toSQLString(oracleStmt, JdbcConstants.ORACLE);
				if (!normalized.equals(converted)) {
					logger.info("[SQL-Conv/Normalize] oracle round-trip after hybrid, before='{}' after='{}'",
							converted, normalized);
				}
				return normalized;
			}
			catch (Exception first) {
				logger.warn("[SQL-Conv/Normalize] Oracle parsing failed: {}, trying MySQL->Oracle fallback",
						first.getMessage());
				try {
					SQLStatement mysqlStmt = SQLUtils.parseSingleStatement(converted, JdbcConstants.MYSQL);
					String oracleStr = SQLUtils.toSQLString(mysqlStmt, JdbcConstants.ORACLE);
					SQLStatement oracleStmt2 = SQLUtils.parseSingleStatement(oracleStr, JdbcConstants.ORACLE);
					String normalized = SQLUtils.toSQLString(oracleStmt2, JdbcConstants.ORACLE);
					logger.info("[SQL-Conv/Normalize] mysql->oracle->oracle fallback applied, result='{}'", normalized);
					return normalized;
				}
				catch (Exception second) {
					logger.warn("[SQL-Conv/Normalize] All normalization attempts failed: {}", second.getMessage());
				}
			}
		}
		else {
			logger.info("[SQL-Conv/Strategy] Non-Oracle dialect '{}', skipping normalization", dialect);
		}

		return converted;
	}

}
