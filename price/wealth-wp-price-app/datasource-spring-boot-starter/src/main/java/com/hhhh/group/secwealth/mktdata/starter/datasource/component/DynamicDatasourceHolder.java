/*
 */
package com.hhhh.group.secwealth.mktdata.starter.datasource.component;

public final class DynamicDatasourceHolder {

    private static final ThreadLocal<String> datasourceHolder = new ThreadLocal<>();

    private DynamicDatasourceHolder() {}

    public static void putDatasource(final String datasource) {
        DynamicDatasourceHolder.datasourceHolder.set(datasource);
    }

    public static String getDatasource() {
        return DynamicDatasourceHolder.datasourceHolder.get();
    }

    public static void removeDatasource() {
        DynamicDatasourceHolder.datasourceHolder.remove();
    }

}
