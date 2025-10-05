package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.dummy.wpb.product.utils.JsonPathUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.ExtendFieldDataType.DATE;
import static com.dummy.wpb.product.constant.ExtendFieldDataType.TIMESTAMP;

@Slf4j
public class DefaultProductFormatService implements ProductFormatService {
    private final GraphQLService graphQLService;

    private static final DateTimeFormatter defaultDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final List<Map<String, String>> userDefineFieldConfig = new ArrayList<>();

    private final Set<String> productInputFields = new HashSet<>();

    //Only update the record specified by key
    private static final Map<String, List<String>> LIST_FIELD_KEYS = new ImmutableMap.Builder<String, List<String>>()
            .put(Field.chanlAttr, Arrays.asList(Field.fieldCde, Field.chanlCde))
            .put("utTrstInstm.sw", Collections.singletonList("fundUnswCde"))
            .put(Field.altId, Collections.singletonList(Field.prodCdeAltClassCde))
            .put("debtInstm.credRtng", Collections.singletonList("creditRtngAgcyCde"))
            .put(Field.restrCustCtry, Arrays.asList(Field.ctryIsoCde, Field.restrCtryTypeCde))
            .build();

    // update all list
    private static final List<String> LIST_FIELDS = Arrays.asList(
            Field.altId,
            Field.undlAset,
            Field.tradeCcy,
            Field.asetVoltlClass,
            Field.formReqmt,
            Field.restrCustGroup,
            "debtInstm.credRtng",
            Field.restrCustCtry,
            "asetSicAlloc",
            "asetGeoLocAlloc",
            "eqtyLinkInvst.undlStock");

    public DefaultProductFormatService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostConstruct
    public void init() {
        initUserDefineFieldConfig();
        initProductInputFields();
    }

    protected void initUserDefineFieldConfig() {
        try {
            GraphQLRequest request = new GraphQLRequest();
            request.setQuery(CommonUtils.readResource("/gql/config-query.gql"));
            request.setDataPath("data.configurationByFilter[0].config");
            Map<String, String> filter = Collections.singletonMap("name", "user-defined-field-mapping");
            request.setVariables(Collections.singletonMap("filter", filter));

            Map<String, List<Map<String, String>>> udfConfigDoc = graphQLService.executeRequest(request, new TypeReference<Map<String, List<Map<String, String>>>>() {
            });
            if (MapUtils.isNotEmpty(udfConfigDoc)) {
                udfConfigDoc.forEach((type, configList) -> userDefineFieldConfig.addAll(configList));
            }
        } catch (Exception e) {
            log.error("Failed to init user defined field configuration", e);
        }
    }

    protected void initProductInputFields() {
        try {
            GraphQLRequest graphQLRequest = new GraphQLRequest();
            graphQLRequest.setQuery(CommonUtils.readResource("/gql/product-schema.gql"));
            graphQLRequest.setVariables(Collections.singletonMap("graphQLType", "ProductInput"));
            graphQLRequest.setDataPath("data.graphQLTypeSchema");
            List<Map<String, String>> types = graphQLService.executeRequest(graphQLRequest, new TypeReference<List<Map<String, String>>>() {
            });
            for (Map<String, String> type : types) {
                String path = type.get("name");
                if (path.contains("[*]")) {
                    productInputFields.add(path.substring(0, path.indexOf("[*]")));
                } else {
                    productInputFields.add(path);
                }
            }
        } catch (Exception e) {
            log.error("Failed to init product input schema", e);
        }
    }

    @Override
    public void formatByUpdateAttrs(Document importProduct, Document updatedProduct, Collection<String> updateAttrs) {
        for (String updateAttr : updateAttrs) {
            if (LIST_FIELD_KEYS.containsKey(updateAttr)) {
                formatListFields(importProduct, updatedProduct, updateAttr);
                continue;
            }

            if (StringUtils.equalsAny(updateAttr, EXTEND_FIELDS, EXTEND_EG_FIELDS, EXTEND_OP_FIELDS, USER_DEFINED_FIELDS)) {
                List<Document> importExtendFields = importProduct.getList(updateAttr, Document.class, Collections.emptyList());
                for (Document item : importExtendFields) {
                    formatExtendField(updatedProduct, item.getString(Field.fieldCde), item.get("fieldValue"));
                }
            } else {
                JsonPathUtils.copyValue(importProduct, updatedProduct, updateAttr, !LIST_FIELDS.contains(updateAttr));
            }
        }
    }

    @Override
    public void formatExtendField(Document product, String fieldCde, Object fieldValue) {
        for (Map<String, String> config : userDefineFieldConfig) {
            if (config.get(Field.fieldCde).equals(fieldCde)) {
                String jsonPath = config.get("jsonPath");
                String dataType = config.get("fieldDataTypeText");

                if (DATE.equals(dataType)) {
                    fieldValue = LocalDate.parse(fieldValue.toString(), defaultDateFormat);
                }

                if (TIMESTAMP.equals(dataType)) {
                    fieldValue = LocalDateTime.parse(fieldValue.toString(), defaultDateTimeFormat);
                }
                JsonPathUtils.setValue(product, jsonPath, fieldValue);
            }
        }
    }

    protected void formatListFields(Map<String, Object> importedProduct, Map<String, Object> updatedProduct, String updateAttr) {
        List<Map<String, Object>> importedValue = JsonPathUtils.readValue(importedProduct, updateAttr);
        if (null == importedValue) {
            return;
        }

        List<Map<String, Object>> updatedValue = JsonPathUtils.readValue(updatedProduct, updateAttr, new ArrayList<>());

        List<String> listFieldKey = LIST_FIELD_KEYS.get(updateAttr);
        Function<Map<String, Object>, String> keyMapper = item -> {
            StringBuilder key = new StringBuilder();
            listFieldKey.forEach(field -> key.append(item.getOrDefault(field, "")));
            return key.toString();
        };

        Map<String, Map<String, Object>> importedValueMap = importedValue.stream().collect(
                Collectors.toMap(keyMapper, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));
        Map<String, Map<String, Object>> updatedValueMap = updatedValue.stream().collect(
                Collectors.toMap(keyMapper, Function.identity(), (v1, v2) -> v1, LinkedHashMap::new));

        updatedValueMap.forEach((key, updatedElement) -> {
            Map<String, Object> importedElement = importedValueMap.get(key);

            if (null != importedElement) {
                Stream.of(Field.rowid, Field.recCreatDtTm, Field.recUpdtDtTm).forEach(persistentField ->
                        importedElement.compute(persistentField, (k, v) -> updatedElement.get(persistentField))
                );

                if (!updatedElement.equals(importedElement)) {
                    importedElement.put(Field.recUpdtDtTm, LocalDateTime.now());
                }
            } else {
                importedValue.add(updatedElement);
            }
        });

        JsonPathUtils.setValue(updatedProduct, updateAttr, importedValue);
    }

    @Override
    public Document initProduct(Document sourceProd) {
        Document importedProd = new Document();

        Stream.of(EXTEND_FIELDS, EXTEND_EG_FIELDS, EXTEND_OP_FIELDS, USER_DEFINED_FIELDS).forEach(type -> {
            List<Document> importExtendFields = sourceProd.getList(type, Document.class, Collections.emptyList());
            for (Document item : importExtendFields) {
                this.formatExtendField(importedProd, item.getString(Field.fieldCde), item.get("fieldValue"));
            }
        });

        for (String productInputField : productInputFields) {
            JsonPathUtils.copyValue(sourceProd, importedProd, productInputField, false);
        }

        return importedProd;
    }
}
