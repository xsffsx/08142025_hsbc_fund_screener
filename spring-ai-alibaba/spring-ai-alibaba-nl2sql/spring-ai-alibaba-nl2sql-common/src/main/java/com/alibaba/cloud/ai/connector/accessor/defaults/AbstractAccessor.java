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

package com.alibaba.cloud.ai.connector.accessor.defaults;

import com.alibaba.cloud.ai.connector.AbstractJdbcDdl;
import com.alibaba.cloud.ai.connector.DBConnectionPool;
import com.alibaba.cloud.ai.connector.accessor.Accessor;
import com.alibaba.cloud.ai.connector.support.DdlFactory;
import com.alibaba.cloud.ai.connector.SqlExecutor;
import com.alibaba.cloud.ai.connector.sql.SqlConversionStrategy;
import com.alibaba.cloud.ai.connector.bo.ColumnInfoBO;
import com.alibaba.cloud.ai.connector.bo.DatabaseInfoBO;
import com.alibaba.cloud.ai.connector.bo.DbQueryParameter;
import com.alibaba.cloud.ai.connector.bo.ForeignKeyInfoBO;
import com.alibaba.cloud.ai.connector.bo.ResultSetBO;
import com.alibaba.cloud.ai.connector.bo.SchemaInfoBO;
import com.alibaba.cloud.ai.connector.bo.TableInfoBO;
import com.alibaba.cloud.ai.connector.config.DbConfig;
import com.alibaba.cloud.ai.connector.DBConnectionPoolContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.util.List;

/**
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 */

public abstract class AbstractAccessor implements Accessor {

	private static final Logger logger = LoggerFactory.getLogger(AbstractAccessor.class);

	private final DdlFactory ddlFactory;

	private final DBConnectionPool dbConnectionPool;

	@Autowired
	private DBConnectionPoolContext dbConnectionPoolContext;

	// 可选注入：SQL转换策略，由上层模块提供实现
	@Autowired(required = false)
	private SqlConversionStrategy sqlConversionStrategy;

	protected AbstractAccessor(DdlFactory ddlFactory, DBConnectionPool dbConnectionPool) {
		this.dbConnectionPool = dbConnectionPool;
		this.ddlFactory = ddlFactory;
	}

	protected abstract String getDbAccessorType();

	public <T> T accessDb(DbConfig dbConfig, String method, DbQueryParameter param) throws Exception {

		try (Connection connection = getConnection(dbConfig)) {

			AbstractJdbcDdl ddlExecutor = (AbstractJdbcDdl) ddlFactory.getDdlExecutor(dbConfig);

			switch (method) {
				case "showDatabases":
					return (T) ddlExecutor.showDatabases(connection);
				case "showSchemas":
					return (T) ddlExecutor.showSchemas(connection);
				case "showTables":
					return (T) ddlExecutor.showTables(connection, param.getSchema(), param.getTablePattern());
				case "fetchTables":
					return (T) ddlExecutor.fetchTables(connection, param.getSchema(), param.getTables());
				case "showColumns":
					return (T) ddlExecutor.showColumns(connection, param.getSchema(), param.getTable());
				case "showForeignKeys":
					return (T) ddlExecutor.showForeignKeys(connection, param.getSchema(), param.getTables());
				case "sampleColumn":
					return (T) ddlExecutor.sampleColumn(connection, param.getSchema(), param.getTable(),
							param.getColumn());
				case "scanTable":
					return (T) ddlExecutor.scanTable(connection, param.getSchema(), param.getTable());
				case "executeSqlAndReturnObject":
					String sqlToRun = param != null ? param.getSql() : null;
					logger.info("[SQL-Conv] dialect='{}', strategyPresent={}, method='{}'",
							dbConfig != null ? dbConfig.getDialectType() : "null", sqlConversionStrategy != null,
							method);
					if (sqlConversionStrategy != null && sqlToRun != null) {
						try {
							String converted = sqlConversionStrategy.maybeConvert(dbConfig, sqlToRun);
							logger.info("[SQL-Conv] before='{}' after='{}'", sqlToRun, converted);
							sqlToRun = converted;
						}
						catch (Exception ex) {
							logger.warn("[SQL-Conv] conversion failed: {}", ex.getMessage());
						}
					}

					// 执行前兜底修正：Oracle JDBC 不需要尾部分号，去除之（方案B）
					if (sqlToRun != null) {
						String trimmed = sqlToRun.trim();
						if (trimmed.endsWith(";") && dbConfig != null
								&& "oracle".equalsIgnoreCase(dbConfig.getDialectType())) {
							logger.info(
									"[SQL-Conv/Fix] Oracle dialect detected, stripping trailing semicolon before execution");
							sqlToRun = trimmed.substring(0, trimmed.length() - 1);
						}
					}
					logger.info("[SQL-Conv] executing final SQL='{}'", sqlToRun);
					return (T) SqlExecutor.executeSqlAndReturnObject(connection, param.getSchema(), sqlToRun);
				default:
					throw new UnsupportedOperationException("Unknown method: " + method);
			}
		}
		catch (Exception e) {

			logger.error("Error accessing database with method: {}", method, e);
			throw e;
		}
	}

	public List<DatabaseInfoBO> showDatabases(DbConfig dbConfig) throws Exception {
		return accessDb(dbConfig, "showDatabases", null);
	}

	public List<SchemaInfoBO> showSchemas(DbConfig dbConfig) throws Exception {
		return accessDb(dbConfig, "showSchemas", null);
	}

	public List<TableInfoBO> showTables(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "showTables", param);
	}

	public List<TableInfoBO> fetchTables(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "fetchTables", param);
	}

	public List<ColumnInfoBO> showColumns(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "showColumns", param);
	}

	public List<ForeignKeyInfoBO> showForeignKeys(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "showForeignKeys", param);
	}

	public List<String> sampleColumn(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "sampleColumn", param);
	}

	public ResultSetBO scanTable(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "scanTable", param);
	}

	public ResultSetBO executeSqlAndReturnObject(DbConfig dbConfig, DbQueryParameter param) throws Exception {
		return accessDb(dbConfig, "executeSqlAndReturnObject", param);
	}

	public Connection getConnection(DbConfig config) {
		// 根据DbConfig动态选择连接池
		DBConnectionPool selectedPool = selectConnectionPool(config);
		return selectedPool.getConnection(config);
	}

	/**
	 * 根据数据库配置动态选择正确的连接池
	 * @param config 数据库配置
	 * @return 对应的连接池
	 */
	private DBConnectionPool selectConnectionPool(DbConfig config) {
		String dialectType = config.getDialectType();
		if (dialectType == null || dialectType.trim().isEmpty()) {
			logger.warn("DbConfig dialectType is null or empty, using default connection pool");
			return this.dbConnectionPool;
		}

		// 使用DBConnectionPoolContext动态选择连接池
		DBConnectionPool selectedPool = dbConnectionPoolContext.getPoolByType(dialectType);
		if (selectedPool != null) {
			logger.debug("Selected connection pool: {} for dialectType: {}", selectedPool.getClass().getSimpleName(),
					dialectType);
			return selectedPool;
		}
		else {
			logger.warn("No connection pool found for dialectType: {}, using default connection pool", dialectType);
			return this.dbConnectionPool;
		}
	}

}
