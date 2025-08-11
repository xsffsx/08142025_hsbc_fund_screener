package com.alibaba.cloud.ai.config;

import com.alibaba.cloud.ai.connector.accessor.Accessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Database Accessor Configuration
 *
 * This configuration class provides dynamic selection of database accessor based on the
 * spring.datasource.driver-class-name property.
 */
@Configuration
public class DatabaseAccessorConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(DatabaseAccessorConfiguration.class);

	@Bean("dbAccessor")
	public Accessor dbAccessor(Environment env, @Qualifier("postgreAccessor") Accessor postgreAccessor,
			@Qualifier("mysqlAccessor") Accessor mysqlAccessor, @Qualifier("oracleAccessor") Accessor oracleAccessor) {

		String driver = env.getProperty("spring.datasource.driver-class-name", "").trim();
		logger.info("Selecting Accessor by driver-class-name: '{}'", driver);

		switch (driver) {
			case "org.postgresql.Driver":
				logger.info("Use postgreAccessor");
				return postgreAccessor;
			case "com.mysql.cj.jdbc.Driver":
				logger.info("Use mysqlAccessor");
				return mysqlAccessor;
			case "oracle.jdbc.OracleDriver":
				logger.info("Use oracleAccessor");
				return oracleAccessor;
			default:
				String msg = String.format("Unsupported or empty spring.datasource.driver-class-name: '%s'. "
						+ "Supported: org.postgresql.Driver, com.mysql.cj.jdbc.Driver, oracle.jdbc.OracleDriver",
						driver);
				logger.error(msg);
				throw new IllegalStateException(msg);
		}
	}

}
