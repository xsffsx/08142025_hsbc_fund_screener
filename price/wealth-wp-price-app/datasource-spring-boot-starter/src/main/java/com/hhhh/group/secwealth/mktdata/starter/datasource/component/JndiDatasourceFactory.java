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
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.datasource.constant.Constant;

import lombok.Setter;

public class JndiDatasourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(JndiDatasourceFactory.class);

    @Setter
    private String datasourceName;

    @Setter
    private String jndiName;

    @Setter
    private String defaultDatasourceName;

    @Setter
    private String defaultExCode;

    private final Map<String, DataSource> datasources = new HashMap<>();

    @PostConstruct
    public void init() throws Exception {
        if (!StringUtils.isEmpty(this.datasourceName) && !StringUtils.isEmpty(this.jndiName)) {
            final String[] datasourceNames = this.datasourceName.split(Constant.SYMBOL_COMMA);
            final String[] jndiNames = this.jndiName.split(Constant.SYMBOL_COMMA);
            if (datasourceNames.length == jndiNames.length) {
                for (int i = 0; i < datasourceNames.length; i++) {
                    this.datasources.put(datasourceNames[i], lookupJndiDataSource(jndiNames[i]));
                }
            } else {
                JndiDatasourceFactory.logger.error("\"datasource.name\" and \"datasource.jndi-name\" should be consistent");
                throw new Exception(this.defaultExCode);
            }
        } else {
            JndiDatasourceFactory.logger
                .error("Please check your configuration, \"datasource.name\" and \"datasource.jndi-name\" should not be empty");
            throw new Exception(this.defaultExCode);
        }
    }

    private DataSource lookupJndiDataSource(final String jndiName) {
        final JndiDataSourceLookup jndiDataSourceLookup = new JndiDataSourceLookup();
        jndiDataSourceLookup.setResourceRef(true);
        return jndiDataSourceLookup.getDataSource(jndiName);
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
                JndiDatasourceFactory.logger
                    .error("Please check your configuration, \"datasource.default-name\" should be one of \"datasource.name\"");
                throw new Exception(this.defaultExCode);
            }
        } else {
            JndiDatasourceFactory.logger.error("Please check your configuration, \"datasource.default-name\" should not be empty");
            throw new Exception(this.defaultExCode);
        }
    }

}
