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

import com.alibaba.cloud.ai.connector.AbstractDBConnectionPool;
import com.alibaba.cloud.ai.enums.DatabaseDialectEnum;
import com.alibaba.cloud.ai.enums.ErrorCodeEnum;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.stereotype.Service;

import static com.alibaba.cloud.ai.enums.ErrorCodeEnum.*;

/**
 * Oracle数据库连接池实现
 *
 * 支持Oracle Database 11g及以上版本，包括： - Oracle Database 19c - Oracle Database 21c - Oracle
 * Database 23ai Free
 *
 * @author nl2sql-team
 * @since 2025-08-10
 */
@Service("oracleJdbcConnectionPool")
public class OracleJdbcConnectionPool extends AbstractDBConnectionPool {

	private final static String DRIVER = "oracle.jdbc.OracleDriver";

	@Override
	public DatabaseDialectEnum getDialect() {
		return DatabaseDialectEnum.ORACLE;
	}

	@Override
	public String getDriver() {
		return DRIVER;
	}

	@Override
	public ErrorCodeEnum errorMapping(String sqlState) {
		ErrorCodeEnum ret = ErrorCodeEnum.fromCode(sqlState);
		if (ret != null) {
			return ret;
		}

		switch (sqlState) {
			// Oracle连接失败相关错误码
			case "08S01":
			case "08003":
			case "08006":
				return DATASOURCE_CONNECTION_FAILURE_08S01;
			case "08001":
				return DATASOURCE_CONNECTION_FAILURE_08001;

			// Oracle认证失败错误码
			case "28000":
			case "01017":
				return PASSWORD_ERROR_28000;

			// Oracle数据库/表不存在错误码
			case "42000":
			case "00942":
				return DATABASE_NOT_EXIST_42000;
			case "72000": // ORA-00942: table or view does not exist
				return DATABASE_NOT_EXIST_42000;

			// Oracle特有错误码映射
			case "72001": // ORA-00904: invalid identifier
				return DATABASE_NOT_EXIST_42000;
			case "72002": // ORA-00955: name is already used by an existing object
				return DATABASE_NOT_EXIST_42000;

			default:
				return OTHERS;
		}
	}

	@Override
	public javax.sql.DataSource createdDataSource(String url, String username, String password) {
		HikariConfig cfg = new HikariConfig();
		cfg.setDriverClassName(DRIVER);
		cfg.setJdbcUrl(url);
		cfg.setUsername(username);
		cfg.setPassword(password);
		cfg.setConnectionTestQuery("SELECT 1 FROM DUAL");
		cfg.setMinimumIdle(1);
		cfg.setMaximumPoolSize(5);
		cfg.setConnectionTimeout(10000);
		cfg.setValidationTimeout(5000);
		return new HikariDataSource(cfg);
	}

}
