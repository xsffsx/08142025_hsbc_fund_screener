package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.model.TableInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties
@Component
public class ExportFieldConfig {

    private static Map<String, TableInfo> exportTableMap = new HashMap<>();

    public static TableInfo getTableInfo(String tableName) {
        return exportTableMap.get(tableName);
    }

    public void setExportTable(List<TableInfo> exportTable) {
        exportTable.forEach(table -> exportTableMap.put(table.getTableName(), table));
    }
}
