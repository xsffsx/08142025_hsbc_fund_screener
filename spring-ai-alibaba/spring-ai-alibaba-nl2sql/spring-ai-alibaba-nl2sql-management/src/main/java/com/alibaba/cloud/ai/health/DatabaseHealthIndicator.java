package com.alibaba.cloud.ai.health;

import com.alibaba.cloud.ai.connector.DBConnectionPool;
import com.alibaba.cloud.ai.connector.DBConnectionPoolContext;
import com.alibaba.cloud.ai.connector.accessor.Accessor;
import com.alibaba.cloud.ai.connector.bo.DbQueryParameter;
import com.alibaba.cloud.ai.connector.bo.TableInfoBO;
import com.alibaba.cloud.ai.connector.config.DbConfig;
import com.alibaba.cloud.ai.enums.ErrorCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * NL2SQL 数据库健康检查：系统DB(PostgreSQL) + 业务DB(通过dbAccessor)
 */
@Component("database")
public class DatabaseHealthIndicator implements HealthIndicator {

	private static final Logger log = LoggerFactory.getLogger(DatabaseHealthIndicator.class);

	private final Accessor dbAccessor; // 已选中的业务DB Accessor

	private final DbConfig dbConfig; // 业务DB配置

	private final DBConnectionPoolContext poolContext; // 各DB连接池

	// 系统数据库（Actuator自带的 DataSourceHealthIndicator 已覆盖，这里补充带细节的检测）
	@Value("${spring.datasource.url:}")
	private String systemDbUrl;

	@Value("${spring.datasource.username:}")
	private String systemDbUsername;

	@Value("${spring.datasource.password:}")
	private String systemDbPassword;

	public DatabaseHealthIndicator(@Qualifier("dbAccessor") Accessor dbAccessor, DbConfig dbConfig,
			DBConnectionPoolContext poolContext) {
		this.dbAccessor = dbAccessor;
		this.dbConfig = dbConfig;
		this.poolContext = poolContext;
	}

	@Override
	public Health health() {
		Map<String, Object> details = new HashMap<>();
		boolean ok = true;

		boolean sysOk = checkSystemDb(details);
		ok &= sysOk;

		boolean bizOk = checkBusinessDb(details);
		ok &= bizOk;

		return (ok ? Health.up() : Health.down()).withDetails(details).build();
	}

	private boolean checkSystemDb(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		try {
			if (systemDbUrl == null || systemDbUrl.isBlank()) {
				details.put("systemDatabase",
						Map.of("status", "SKIP", "reason", "spring.datasource.url not configured"));
				return true;
			}
			DbConfig cfg = new DbConfig();
			cfg.setUrl(systemDbUrl);
			cfg.setUsername(systemDbUsername);
			cfg.setPassword(systemDbPassword);
			cfg.setDialectType("postgresql");
			cfg.setConnectionType("postgresql");
			// 优先从URL解析 currentSchema，没有则回退为 public
			String schema = extractSchemaFromJdbcUrl(systemDbUrl);
			cfg.setSchema((schema != null && !schema.isBlank()) ? schema : "public");

			DBConnectionPool pool = poolContext.getPoolByType("postgresql");
			if (pool == null) {
				details.put("systemDatabase", Map.of("status", "DOWN", "error", "PostgreSQL pool not found"));
				return false;
			}
			ErrorCodeEnum r = pool.ping(cfg);
			long cost = System.currentTimeMillis() - start;
			boolean up = r == ErrorCodeEnum.SUCCESS;
			details.put("systemDatabase", Map.of("status", up ? "UP" : "DOWN", "type", "PostgreSQL", "responseTime",
					cost + "ms", "endpoint", maskUrl(cfg.getUrl()), "errorCode", r.name()));
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			details.put("systemDatabase",
					Map.of("status", "DOWN", "error", e.getMessage(), "responseTime", cost + "ms"));
			return false;
		}
	}

	private boolean checkBusinessDb(Map<String, Object> details) {
		long start = System.currentTimeMillis();
		try {
			if (dbConfig == null || dbConfig.getDialectType() == null) {
				details.put("businessDatabase",
						Map.of("status", "SKIP", "reason", "dbConfig or dialectType not configured"));
				return true;
			}
			DBConnectionPool pool = poolContext.getPoolByType(dbConfig.getDialectType());
			if (pool == null) {
				details.put("businessDatabase",
						Map.of("status", "DOWN", "error", "Pool not found for: " + dbConfig.getDialectType()));
				return false;
			}
			ErrorCodeEnum r = pool.ping(dbConfig);

			int tableCount = 0;
			String tableErr = null;
			try {
				DbQueryParameter p = DbQueryParameter.from(dbConfig).setSchema(dbConfig.getSchema());
				List<TableInfoBO> ts = dbAccessor.fetchTables(dbConfig, p);
				tableCount = (ts == null) ? 0 : ts.size();
			}
			catch (Exception ex) {
				tableErr = ex.getMessage();
			}

			long cost = System.currentTimeMillis() - start;
			boolean up = r == ErrorCodeEnum.SUCCESS;
			Map<String, Object> m = new HashMap<>();
			m.put("status", up ? "UP" : "DOWN");
			m.put("type", String.valueOf(dbConfig.getDialectType()));
			m.put("responseTime", cost + "ms");
			m.put("endpoint", maskUrl(dbConfig.getUrl()));
			m.put("schema", dbConfig.getSchema());
			m.put("accessorType", dbAccessor.getClass().getSimpleName());
			m.put("tablesAccessible", tableCount);
			m.put("errorCode", r.name());
			if (tableErr != null)
				m.put("tableCheckError", tableErr);
			details.put("businessDatabase", m);
			return up;
		}
		catch (Exception e) {
			long cost = System.currentTimeMillis() - start;
			details.put("businessDatabase",
					Map.of("status", "DOWN", "error", e.getMessage(), "responseTime", cost + "ms"));
			return false;
		}
	}

	private String maskUrl(String url) {
		if (url == null)
			return null;
		return url.replaceAll("(password|pwd)=([^;&]+)", "$1=****");
	}

	/**
	 * 从 JDBC URL 中解析 schema（PostgreSQL常见参数 currentSchema） 支持格式示例：
	 * jdbc:postgresql://host:port/db?currentSchema=myschema
	 */
	private String extractSchemaFromJdbcUrl(String url) {
		if (url == null || url.isBlank()) {
			return null;
		}
		int q = url.indexOf('?');
		if (q < 0 || q >= url.length() - 1) {
			return null;
		}
		String query = url.substring(q + 1);
		String[] parts = query.split("&");
		for (String p : parts) {
			int eq = p.indexOf('=');
			if (eq > 0) {
				String k = p.substring(0, eq);
				String v = p.substring(eq + 1);
				if ("currentSchema".equalsIgnoreCase(k)) {
					return v;
				}
			}
		}
		return null;
	}

}
