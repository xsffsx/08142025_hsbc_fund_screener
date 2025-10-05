/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.datasource.constant.Constant;

import lombok.Setter;

public class JdbcDatasourceFactory {

	private static final Logger logger = LoggerFactory.getLogger(JdbcDatasourceFactory.class);

	@Setter
	private String datasourceName;

	@Setter
	private String jndiName;

	@Setter
	private String defaultDatasourceName;

	@Setter
	private String defaultExCode;

	@Setter
	private Map<String, Map<String, String>> multipleSources;

	private final Map<String, DataSource> datasources = new HashMap<>();

	@PostConstruct
	public void init() throws Exception {
		if (multipleSources != null && multipleSources.size() > 0) {
			for (Map.Entry<String, Map<String,String>> entry : multipleSources.entrySet()) {
				DriverManagerDataSource dataSource = new DriverManagerDataSource();
				dataSource.setDriverClassName(entry.getValue().get(Constant.DS_PARAM_DRIVER_CLASS_NAME));
				dataSource.setUrl(entry.getValue().get(Constant.DS_PARAM_URL));
				dataSource.setUsername(entry.getValue().get(Constant.DS_PARAM_USERNAME));
				dataSource.setPassword(entry.getValue().get(Constant.DS_PARAM_PASSWORD));
				this.datasources.put(entry.getKey(),dataSource);
			}
		} else {
			JdbcDatasourceFactory.logger
					.error("Please check your configuration, \"datasource.multipleSources\"  should not be empty");
			throw new Exception(this.defaultExCode);
		}
	}

	public Map<Object, Object> getTargetDataSources() {
		final Map<Object, Object> targetDataSources = new HashMap<>();
		for (final Entry<String, DataSource> dataSource : this.datasources.entrySet()) {
			targetDataSources.put(dataSource.getKey(), dataSource.getValue());
		}
		return targetDataSources;
	}

	public DataSource getDefaultDataSource() throws Exception {
		if (!StringUtils.isEmpty(this.defaultDatasourceName)) {
			if (this.datasources.containsKey(this.defaultDatasourceName)) {
				return this.datasources.get(this.defaultDatasourceName);
			} else {
				JdbcDatasourceFactory.logger.error(
						"Please check your configuration, \"datasource.default-name\" should be one of \"datasource.name\"");
				throw new Exception(this.defaultExCode);
			}
		} else {
			JdbcDatasourceFactory.logger
					.error("Please check your configuration, \"datasource.default-name\" should not be empty");
			throw new Exception(this.defaultExCode);
		}
	}

}
