/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.component;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDatasource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDatasourceHolder.getDatasource();
    }

}
