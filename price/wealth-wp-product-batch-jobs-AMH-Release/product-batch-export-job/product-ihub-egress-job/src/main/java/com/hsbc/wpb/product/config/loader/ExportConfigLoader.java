package com.dummy.wpb.product.config.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.utils.YamlUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ExportConfigLoader {

    private ExportConfigLoader() {
    }


    private static final Map<String, List<String>> PRODUCT_TBALE_FIELDS_MAP;

    private static final Map<String, List<String>> SIGNLE_TBALE_FIELDS_MAP;

    static {
        Map<String, Map<String, List<String>>> tableFieldsMap = YamlUtils.readResource("/export-config.yaml", new TypeReference<Map<String, Map<String, List<String>>>>() {
        });

        PRODUCT_TBALE_FIELDS_MAP = tableFieldsMap.get("productTables");
        SIGNLE_TBALE_FIELDS_MAP = tableFieldsMap.get("singleTables");
    }


    public static Collection<String> getProductTables() {
        return PRODUCT_TBALE_FIELDS_MAP.keySet();
    }

    public static List<String> getProductTableFields(String tableName) {
        return PRODUCT_TBALE_FIELDS_MAP.get(tableName);
    }

    public static List<String> getSingleTableFields(String tableName) {
        return SIGNLE_TBALE_FIELDS_MAP.get(tableName);
    }

}
