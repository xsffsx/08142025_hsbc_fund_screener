package com.dummy.wpb.product.config.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.ProductMetadata;
import com.dummy.wpb.product.model.UserDefinedFieldConfig;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DBConfigLoader implements InitializingBean {
    @Autowired
    private MongoTemplate mongoTemplate;
    private static final Map<String, List<ProductMetadata>> tableMetadataMap = new HashMap<>();
    private static final Map<String, List<UserDefinedFieldConfig>> userDefinedFieldConfigMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws IOException {
        // init product metadata
        Query query = new Query()
                .addCriteria(Criteria.where("table").ne(null))
                .addCriteria(Criteria.where("fieldName").ne(null));
        List<ProductMetadata> metadataList = mongoTemplate.find(query, ProductMetadata.class);
        tableMetadataMap.putAll(metadataList.stream().collect(Collectors.groupingBy(ProductMetadata::getTable)));
        // init user defined field mapping
        query = new Query()
                .addCriteria(Criteria.where("name").is("user-defined-field-mapping"));
        query.fields().include("config");
        Document configDoc = mongoTemplate.findOne(query, Document.class, CollectionName.configuration.name());
        if (null != configDoc) {
            userDefinedFieldConfigMap.putAll(JsonUtil.convertType(configDoc.get("config"), new TypeReference<Map<String, List<UserDefinedFieldConfig>>>() {
            }));
        }

    }

    public static List<ProductMetadata> getMetadata(String tableName) {
        return tableMetadataMap.get(tableName);
    }

    private static final Map<String, String> EXTEND_TABLE_TYPE_MAP = new HashMap<>();

    static {
        EXTEND_TABLE_TYPE_MAP.put("TB_PROD_USER_DEFIN_EXT_FIELD", "ext");
        EXTEND_TABLE_TYPE_MAP.put("TB_PROD_USER_DEFIN_OP_EXT_FIEL", "extOp");
        EXTEND_TABLE_TYPE_MAP.put("TB_PROD_USER_DEFIN_EG_EXT_FIEL", "extEg");
        EXTEND_TABLE_TYPE_MAP.put("TB_PROD_USER_DEFIN_FIELD", "udf");
    }

    public static List<UserDefinedFieldConfig> getUserDefinedFieldConfig(String extendTableName) {
        return userDefinedFieldConfigMap.get(EXTEND_TABLE_TYPE_MAP.get(extendTableName));
    }
}
