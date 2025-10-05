package com.hhhh.group.secwealth.mktdata.configuration;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import oracle.jdbc.pool.OracleDataSource;

//@Configuration
@ConfigurationProperties(prefix="self.datasource")
public class ApplicationSelfDatasourceConfiguration {

	private String databaseName;

	private String username;

	private String password;

	private String url;
	
	@Bean("dataSource")
    public DataSource oracleDataSource() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setDatabaseName(this.databaseName);
        dataSource.setUser(this.username);
        dataSource.setPassword(this.password);
        dataSource.setURL(this.url);
        return dataSource;
    }

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
}

