package com.dummy.wpc.datadaptor.util;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

public class DaoUtils {
    public static SimpleJdbcInsert getSimpleJdbcInsert(DataSource dataSource, String tableName) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(tableName);
        return simpleJdbcInsert;
    }
}
