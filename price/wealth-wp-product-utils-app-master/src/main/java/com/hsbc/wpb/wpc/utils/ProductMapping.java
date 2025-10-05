package com.dummy.wpb.wpc.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.model.ProductTableInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductMapping {

    static List<ProductTableInfo> list;
    static {
        InputStream inputStream = ProductMapping.class.getClassLoader().getResourceAsStream("product-tables.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            list = mapper.readValue(inputStream, new TypeReference<List<ProductTableInfo>>(){});
            list.forEach(t -> {
                List<String> fields = t.getKeyFields();
                if(!CollectionUtils.isEmpty(fields)) {
                    // turn keyFields into camel case
                    t.setKeyFields(fields.stream().map(CodeUtils::toCamelCase).collect(Collectors.toList()));
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("error loading product-tables.yml", e);
        }
    }

    public static List<ProductTableInfo> getTableInfoList() {
        return list;
    }

    public static ProductTableInfo getTableInfo(String tableName) {
        return list.stream().filter(item -> item.getTable().equals(tableName)).findFirst()
                .orElseThrow((Supplier<RuntimeException>) () -> new IllegalArgumentException("Not support table: " + tableName));
    }

    public static boolean isProdUnrelatedTable(String tableName){
        ProductTableInfo tableInfo = list.stream().filter(item -> item.getTable().equals(tableName)).findFirst().orElse(null);
        return tableInfo ==null;
    }
    public static List<ProductTableInfo> getTableInfoListByImportMethod(String importMethod){
        List<ProductTableInfo> tableInfos = list.stream().filter(item -> item.getImportMethod().equals(importMethod)&&!item.getTable().equals("PROD_PERFM"))
        .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(tableInfos)) return Collections.emptyList();
        return tableInfos;
    }
    public static Boolean getImportMethod(String tableName,String importMethod){
        ProductTableInfo tableInfo = list.stream().filter(item -> item.getTable().equals(tableName)).findFirst().orElse(null);
        if(tableInfo == null) return false;
        return tableInfo.getImportMethod().equals(importMethod);
    }
    public static String getToAttributeCamelCase(String tableName){
        ProductTableInfo tableInfo = list.stream().filter(item -> item.getTable().equals(tableName)).findFirst().orElse(null);
        if(null != tableInfo){
            return CodeUtils.toCamelCase(tableInfo.getToAttribute());
        }else{
            return null;
        }
    }
    public static String getProductIdFieldCamelCase(String table){
        ProductTableInfo tableInfo = list.stream().filter(item -> item.getTable().equals(table)).findFirst().orElse(null);
        if(null != tableInfo){
            return CodeUtils.toCamelCase(tableInfo.getProdIdField());
        }
        throw new IllegalArgumentException("Can't find ProductIdField for table " + table);
    }


    public static String getProductIdField(String table){
        return getTableInfo(table).getProdIdField();
    }

    public static List<String> getTableKeyFields(String table){
        return getTableInfo(table).getKeyFields();
    }

    /**
     * Retrieve product table name list
     *
     * @return
     */
    public static Object getTableList() {
        return list.stream().map(ProductTableInfo::getTable).collect(Collectors.toList());
    }
}
