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

package com.alibaba.cloud.ai.connector.oracle;

import com.alibaba.cloud.ai.connector.AbstractJdbcDdl;
import com.alibaba.cloud.ai.connector.SqlExecutor;
import com.alibaba.cloud.ai.connector.bo.ColumnInfoBO;
import com.alibaba.cloud.ai.connector.bo.DatabaseInfoBO;
import com.alibaba.cloud.ai.connector.bo.ForeignKeyInfoBO;
import com.alibaba.cloud.ai.connector.bo.ResultSetBO;
import com.alibaba.cloud.ai.connector.bo.SchemaInfoBO;
import com.alibaba.cloud.ai.connector.bo.TableInfoBO;
import com.alibaba.cloud.ai.enums.BizDataSourceTypeEnum;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.cloud.ai.connector.ColumnTypeParser.wrapType;

/**
 * Oracle数据库DDL执行器实现
 *
 * @author nl2sql-team
 * @since 2025-08-10
 */
@Service
public class OracleJdbcDdl extends AbstractJdbcDdl {

	private static final Logger logger = LoggerFactory.getLogger(OracleJdbcDdl.class);

	@Override
	public List<DatabaseInfoBO> showDatabases(Connection connection) {
		// Oracle中数据库概念不同，返回空列表
		return Collections.emptyList();
	}

	@Override
	public List<SchemaInfoBO> showSchemas(Connection connection) {
		String sql = "SELECT USERNAME FROM ALL_USERS ORDER BY USERNAME";
		List<SchemaInfoBO> schemaInfoList = Lists.newArrayList();
		try {
			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, sql);
			if (resultArr.length <= 1) {
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0) {
					continue;
				}
				String schemaName = resultArr[i][0];
				schemaInfoList.add(SchemaInfoBO.builder().name(schemaName).build());
			}
		}
		catch (SQLException e) {
			logger.error("Failed to show schemas", e);
			throw new RuntimeException(e);
		}

		return schemaInfoList;
	}

	@Override
	public List<TableInfoBO> showTables(Connection connection, String schema, String tablePattern) {
		String sql = "SELECT TABLE_NAME, COMMENTS " + "FROM ALL_TAB_COMMENTS " + "WHERE OWNER = '%s' "
				+ "AND TABLE_TYPE = 'TABLE'";

		if (StringUtils.isNotBlank(tablePattern)) {
			sql += " AND TABLE_NAME LIKE UPPER('%%'||'%s'||'%%')";
		}
		sql += " ORDER BY TABLE_NAME";
		sql += " FETCH FIRST 2000 ROWS ONLY";

		List<TableInfoBO> tableInfoList = Lists.newArrayList();
		try {
			// 如果schema为空，使用当前用户
			String currentSchema = StringUtils.isNotBlank(schema) ? schema.toUpperCase() : getCurrentSchema(connection);
			String finalSql = StringUtils.isNotBlank(tablePattern) ? String.format(sql, currentSchema, tablePattern)
					: String.format(sql.replace(" AND TABLE_NAME LIKE UPPER('%%'||'%s'||'%%')", ""), currentSchema);

			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, currentSchema, finalSql);

			if (resultArr.length <= 1) {
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0) {
					continue;
				}
				String tableName = resultArr[i][0];
				String tableComment = resultArr[i].length > 1 ? resultArr[i][1] : "";
				tableInfoList.add(TableInfoBO.builder().name(tableName).description(tableComment).build());
			}
		}
		catch (SQLException e) {
			logger.error("Failed to show tables for schema: " + schema, e);
			throw new RuntimeException(e);
		}

		return tableInfoList;
	}

	@Override
	public List<TableInfoBO> fetchTables(Connection connection, String schema, List<String> tables) {
		logger.info("fetchTables called with schema: {}, tables: {}", schema, tables);
		if (tables == null || tables.isEmpty()) {
			logger.warn("fetchTables returning empty list because tables is null or empty");
			return Lists.newArrayList();
		}

		String tableListStr = String.join(", ",
				tables.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.toList()));

		String sql = "SELECT TABLE_NAME, COMMENTS " + "FROM ALL_TAB_COMMENTS " + "WHERE OWNER = '%s' "
				+ "AND TABLE_TYPE = 'TABLE' " + "AND TABLE_NAME IN (%s) " + "ORDER BY TABLE_NAME "
				+ "FETCH FIRST 200 ROWS ONLY";

		List<TableInfoBO> tableInfoList = Lists.newArrayList();
		try {
			String currentSchema = StringUtils.isNotBlank(schema) ? schema.toUpperCase() : getCurrentSchema(connection);
			String finalSql = String.format(sql, currentSchema, tableListStr);
			logger.info("fetchTables executing SQL: {}", finalSql);
			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, currentSchema, finalSql);

			if (resultArr.length <= 1) {
				logger.warn("fetchTables found no tables, resultArr.length: {}", resultArr.length);
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0) {
					continue;
				}
				String tableName = resultArr[i][0];
				String tableComment = resultArr[i].length > 1 ? resultArr[i][1] : "";
				tableInfoList.add(TableInfoBO.builder().name(tableName).description(tableComment).build());
			}
			logger.info("fetchTables found {} tables", tableInfoList.size());
		}
		catch (SQLException e) {
			logger.error("Failed to fetch tables for schema: " + schema, e);
			throw new RuntimeException(e);
		}

		return tableInfoList;
	}

	@Override
	public List<ColumnInfoBO> showColumns(Connection connection, String schema, String table) {
		String sql = "SELECT " + "c.COLUMN_NAME, " + "cc.COMMENTS, " + "c.DATA_TYPE, "
				+ "CASE WHEN pk.COLUMN_NAME IS NOT NULL THEN 'true' ELSE 'false' END AS IS_PRIMARY, "
				+ "CASE WHEN c.NULLABLE = 'N' THEN 'true' ELSE 'false' END AS NOT_NULL " + "FROM ALL_TAB_COLUMNS c "
				+ "LEFT JOIN ALL_COL_COMMENTS cc ON c.OWNER = cc.OWNER AND c.TABLE_NAME = cc.TABLE_NAME AND c.COLUMN_NAME = cc.COLUMN_NAME "
				+ "LEFT JOIN (SELECT OWNER, TABLE_NAME, COLUMN_NAME FROM ALL_CONS_COLUMNS WHERE CONSTRAINT_NAME IN "
				+ "(SELECT CONSTRAINT_NAME FROM ALL_CONSTRAINTS WHERE CONSTRAINT_TYPE = 'P')) pk "
				+ "ON c.OWNER = pk.OWNER AND c.TABLE_NAME = pk.TABLE_NAME AND c.COLUMN_NAME = pk.COLUMN_NAME "
				+ "WHERE c.OWNER = '%s' AND c.TABLE_NAME = '%s' " + "ORDER BY c.COLUMN_ID";

		List<ColumnInfoBO> columnInfoList = Lists.newArrayList();
		try {
			String currentSchema = StringUtils.isNotBlank(schema) ? schema.toUpperCase() : getCurrentSchema(connection);
			String finalSql = String.format(sql, currentSchema, table.toUpperCase());

			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, currentSchema, finalSql);

			if (resultArr.length <= 1) {
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0) {
					continue;
				}

				columnInfoList.add(ColumnInfoBO.builder()
					.name(resultArr[i][0])
					.description(resultArr[i][1])
					.type(wrapType(resultArr[i][2]))
					.primary(BooleanUtils.toBoolean(resultArr[i][3]))
					.notnull(BooleanUtils.toBoolean(resultArr[i][4]))
					.build());
			}
		}
		catch (SQLException e) {
			logger.error("Failed to show columns for table: " + table, e);
			throw new RuntimeException(e);
		}

		return columnInfoList;
	}

	@Override
	public List<ForeignKeyInfoBO> showForeignKeys(Connection connection, String schema, List<String> tables) {
		String sql = "SELECT " + "    a.TABLE_NAME AS TABLE_NAME, " + "    a.COLUMN_NAME AS COLUMN_NAME, "
				+ "    a.CONSTRAINT_NAME AS CONSTRAINT_NAME, " + "    c_pk.TABLE_NAME AS REFERENCED_TABLE_NAME, "
				+ "    acc_pk.COLUMN_NAME AS REFERENCED_COLUMN_NAME " + "FROM ALL_CONS_COLUMNS a "
				+ "JOIN ALL_CONSTRAINTS c ON a.OWNER = c.OWNER AND a.CONSTRAINT_NAME = c.CONSTRAINT_NAME "
				+ "JOIN ALL_CONSTRAINTS c_pk ON c.R_OWNER = c_pk.OWNER AND c.R_CONSTRAINT_NAME = c_pk.CONSTRAINT_NAME "
				+ "JOIN ALL_CONS_COLUMNS acc_pk ON acc_pk.OWNER = c_pk.OWNER AND acc_pk.CONSTRAINT_NAME = c_pk.CONSTRAINT_NAME "
				+ "WHERE c.CONSTRAINT_TYPE = 'R' " + "AND a.OWNER = '%s' " + "AND a.TABLE_NAME IN (%s) "
				+ "AND c_pk.TABLE_NAME IN (%s)";

		List<ForeignKeyInfoBO> foreignKeyInfoList = Lists.newArrayList();
		String tableListStr = String.join(", ",
				tables.stream().map(x -> "'" + x.toUpperCase() + "'").collect(Collectors.toList()));

		try {
			String currentSchema = StringUtils.isNotBlank(schema) ? schema.toUpperCase() : getCurrentSchema(connection);
			String finalSql = String.format(sql, currentSchema, tableListStr, tableListStr);
			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, finalSql);
			if (resultArr.length <= 1) {
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0) {
					continue;
				}
				foreignKeyInfoList.add(ForeignKeyInfoBO.builder()
					.table(resultArr[i][0])
					.column(resultArr[i][1])
					.referencedTable(resultArr[i][3])
					.referencedColumn(resultArr[i][4])
					.build());
			}
		}
		catch (SQLException e) {
			logger.error("Failed to show foreign keys for schema: " + schema, e);
			// 不抛出异常，返回空列表
		}

		return foreignKeyInfoList;
	}

	@Override
	public List<String> sampleColumn(Connection connection, String schema, String table, String column) {
		String sql = "SELECT \"%s\" FROM \"%s\" WHERE ROWNUM <= 20";
		List<String> sampleInfo = Lists.newArrayList();
		try {
			String finalSql = String.format(sql, column, table);
			String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, finalSql);
			if (resultArr.length <= 1) {
				return Lists.newArrayList();
			}

			for (int i = 1; i < resultArr.length; i++) {
				if (resultArr[i].length == 0 || column.equalsIgnoreCase(resultArr[i][0])) {
					continue;
				}
				sampleInfo.add(resultArr[i][0]);
			}
		}
		catch (SQLException e) {
			logger.error("Failed to sample column: " + column, e);
		}

		Set<String> siSet = sampleInfo.stream().collect(Collectors.toSet());
		sampleInfo = siSet.stream().collect(Collectors.toList());
		return sampleInfo;
	}

	@Override
	public ResultSetBO scanTable(Connection connection, String schema, String table) {
		String sql = "SELECT * FROM \"%s\" WHERE ROWNUM <= 20";
		ResultSetBO resultSet = ResultSetBO.builder().build();
		try {
			resultSet = SqlExecutor.executeSqlAndReturnObject(connection, schema, String.format(sql, table));
		}
		catch (SQLException e) {
			logger.error("Failed to scan table: " + table, e);
			throw new RuntimeException(e);
		}
		return resultSet;
	}

	@Override
	public BizDataSourceTypeEnum getType() {
		return BizDataSourceTypeEnum.ORACLE;
	}

	/**
	 * 获取当前连接的schema
	 */
	private String getCurrentSchema(Connection connection) throws SQLException {
		String sql = "SELECT USER FROM DUAL";
		String[][] resultArr = SqlExecutor.executeSqlAndReturnArr(connection, sql);
		if (resultArr.length > 1 && resultArr[1].length > 0) {
			return resultArr[1][0];
		}
		return "NL2SQL_USER";
	}

}
