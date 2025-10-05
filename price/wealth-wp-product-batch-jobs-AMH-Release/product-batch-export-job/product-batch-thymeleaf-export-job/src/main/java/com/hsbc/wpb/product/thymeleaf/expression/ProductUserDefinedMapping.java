package com.dummy.wpb.product.thymeleaf.expression;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.UserDefinedFieldConfig;
import com.dummy.wpb.product.utils.JsonUtil;
import org.bson.Document;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.USER_DEFINED_FIELDS;

@Service
public class ProductUserDefinedMapping {

    private static final Map<UserDefinedFieldConfig, String[]> fieldPathArrMap = new ConcurrentHashMap<>();

    @Autowired
    MongoTemplate mongoTemplate;

    private static final Map<String, List<UserDefinedFieldConfig>> userDefinedFieldConfigMap = new HashMap<>();

    private static final Doubles DOUBLES = new Doubles();

    @PostConstruct
    public void init() throws IOException {
        Query query = new Query().addCriteria(Criteria.where("name").is("user-defined-field-mapping"));

        Document configDoc = mongoTemplate.findOne(query, Document.class, CollectionName.configuration.name());
        if (null != configDoc) {
            userDefinedFieldConfigMap.putAll(JsonUtil.convertType(configDoc.get("config"), new TypeReference<Map<String, List<UserDefinedFieldConfig>>>() {
            }));
        }
    }

    public List<Document> get(Document product, String... types) {
        List<Document> userDefinedFields = new ArrayList<>();
        for (String type : types) {
            userDefinedFieldConfigMap
                    .getOrDefault(type, Collections.emptyList())
                    .forEach(config -> {
                        Object value = readValueAndFormat(product, config);
                        if (null != value) {
                            Document udf = new Document()
                                    .append("fieldCde", config.getFieldCde())
                                    .append("value", value)
                                    .append("fieldTypeCde", config.getFieldTypeCde());
                            userDefinedFields.add(udf);
                        }
                    });
        }
        return userDefinedFields;
    }

    public Object udf(Document product, String fieldCde) {
        return userDefinedFieldConfigMap.get(USER_DEFINED_FIELDS)
                .stream()
                .filter(config -> config.getFieldCde().equals(fieldCde))
                .findFirst()
                .map(config -> readValueAndFormat(product, config))
                .orElse(null);
    }

    protected Object readValueAndFormat(Document product, UserDefinedFieldConfig config) {
        String[] paths = fieldPathArrMap.computeIfAbsent(config, cfg -> cfg.getJsonPath().split("\\."));
        Object value = product;
        for (String path : paths) {
            if (!(value instanceof Document)) {
                return value;
            }
            value = ((Document) value).get(path);
        }

        if (null == value || null == config.getFieldDataTypeText()) {
            return value;
        }

        return format(value, config.getFieldDataTypeText());
    }

    private Object format(Object value, UserDefinedFieldConfig.FieldDataType dataType) {
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(v -> format(v, dataType))
                    .collect(Collectors.toList());
        }

        switch (dataType) {
            case DECIMAL:
                return DOUBLES.stripTrailingZeros(value);
            case DATE:
                return new DateTime(value, DateTimeZone.UTC).toString("yyyy-MM-dd");
            case TIMESTAMP:
                return new DateTime(value, DateTimeZone.UTC).toString("HH:mm");
            default:
                return value;
        }
    }
}
