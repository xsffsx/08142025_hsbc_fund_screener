package com.dummy.wmd.wpc.graphql.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import graphql.schema.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Map;

@Slf4j
public class FilterUtils {
    private final GraphQLObjectType rootType;
    private static Map<CollectionName, String> collectionNameToTypeMapping;
    private ObjectMapper mapper = new ObjectMapper();

    static {
        // collection name --> GraphQL Type mapping
        collectionNameToTypeMapping = new ImmutableMap.Builder<CollectionName, String>()
                .put(CollectionName.metadata, "ProductMetadata")
                .put(CollectionName.product, "ProductType")
                .put(CollectionName.pb_product, "PbProductType")
                .put(CollectionName.reference_data, "ReferenceData")
                .put(CollectionName.prod_prc_hist, "ProductPriceHistoryType")
                .put(CollectionName.prod_type_fin_doc, "ProdTypeFinDocType")
                .put(CollectionName.fin_doc, "FinDoc")
                .put(CollectionName.fin_doc_hist, "FinDoc")
                .put(CollectionName.fin_doc_upld, "FinDocUpld")
                .put(CollectionName.staff_license_check, "StaffLicenseCheck")
                .put(CollectionName.prod_atrib_map, "ProdAtribMap")
                .put(CollectionName.chanl_comn_cde, "ChanlComnCde")
                .put(CollectionName.configuration, "Configuration")
                .put(CollectionName.amendment, "AmendmentType")
                .put(CollectionName.aset_voltl_class_char, "AssetVolatilityClassChar")
                .put(CollectionName.aset_voltl_class_corl, "AssetVolatilityClassCorl")
                .put(CollectionName.file_upload,"UploadType")
                .put(CollectionName.request_log,"RequestLogType")
                .put(CollectionName.prod_type_chanl_attr,"ProdTypeChanlAttrType")
                .put(CollectionName.sys_parm,"SysParamType")
                .build();
    }

    public static String getTypeName(CollectionName collectionName) {
        String typeName = collectionNameToTypeMapping.get(collectionName);
        if(null == typeName) {
            String message = String.format("Collection not found in collection name --> GraphQL Type mapping, collection name: %s", collectionName);
            throw new productErrorException(productErrors.CollectionNotFound, message);
        }
        return typeName;
    }

    public FilterUtils(GraphQLObjectType graphQLType) {
        this.rootType = graphQLType;
    }

    public FilterUtils(CollectionName collectionName) {
        String typeName = getTypeName(collectionName);
        this.rootType = (GraphQLObjectType)GraphQLProvider.getGraphQLSchema().getType(typeName);
    }

    /**
     * Convert a filter map into a BsonDocument, will correct type in the go.
     * eg. "2021-01-02" in the filter map is a String, will be converted to Date type according to the GraphQL Schema of a given type document
     *
     * @param filterMap
     * @return
     */
    @SneakyThrows
    public BsonDocument toBsonDocument(Map<String, Object> filterMap) {
        String jsonFilter = mapper.writeValueAsString(filterMap);

        BsonDocument filterBson = BsonDocument.parse(jsonFilter);
        return reviseBsonDocument(filterBson, null);
    }

    /**
     * Revise the BsonDocument against GraphQL Schema Type, eg. change Date field into BsonDateTime instead of BsonString
     * Input:  {"prodLnchDt": "2010-08-06"}
     * Output: {"prodLnchDt": {"$date": 1281052800000}}
     *
     * @param docIn
     * @param parent
     * @return
     */
    private BsonDocument reviseBsonDocument(BsonDocument docIn, String parent) {
        BsonDocument out = new BsonDocument();
        docIn.forEach((k,v) -> {
            String fullName = parent;
            if(!k.startsWith("$")) {    // like $and, $or, but what about $elementMatch?
                fullName = null == parent ? k : parent + "." + k;
            }
            String type = getTypeBySchema(fullName, rootType);

            if(v instanceof BsonDocument){
                BsonDocument doc = reviseBsonDocument((BsonDocument)v, fullName);
                out.put(k, doc);
            } else if(v instanceof BsonArray){
                BsonArray list = new BsonArray();
                String finalFullName = fullName;
                ((BsonArray)v).forEach(item -> {
                    if(item instanceof Map) {
                        BsonDocument doc = reviseBsonDocument((BsonDocument) item, finalFullName);
                        list.add(doc);
                    } else {
                        list.add(reviseBsonValue(item, type));
                    }
                });
                out.put(k, list);
            } else {
                out.put(k, reviseBsonValue(v, type));
            }
        });
        return out;
    }

    /**
     * Get the type name of a field, support a.b.c format
     *
     * @param fullName
     * @param parentType
     * @return
     */
    private String getTypeBySchema(String fullName, GraphQLObjectType parentType) {
        if(null == fullName) return null;

        int idx = fullName.indexOf(".");
        String first = fullName;
        String second = null;
        if(idx > 0) {
            first = fullName.substring(0, idx);
            second = fullName.substring(idx + 1);
        }

        GraphQLFieldDefinition field = parentType.getFieldDefinition(first);
        if(null == field) { // in case the field doesn't exist
            return null;
        }
        GraphQLType type = field.getType();

        // unwrapped type
        if(type instanceof GraphQLNonNull) {
            type = ((GraphQLNonNull)type).getWrappedType();
        } else if(type instanceof GraphQLList) {
            type = ((GraphQLList)type).getWrappedType();
        }

        if(StringUtils.hasText(second)) {
            if(type instanceof GraphQLObjectType) {
                return getTypeBySchema(second, (GraphQLObjectType) type);
            } else {
                return null;
            }
        } else {
            if(type instanceof GraphQLScalarType) { // the leaf node expected to be a ScalarType
                return ((GraphQLScalarType) type).getName();
            }
        }

        return null;    // if finally can't get a type name
    }

    /**
     * If BsonValue type is not match with the schema type, create the BsonValue in correct type
     *
     * @param value a BsonValue
     * @param type GraphqlQL Schema Type
     * @return
     */
    private BsonValue reviseBsonValue(BsonValue value, String type) {
        // GraphQLSchema Type: String, DateTime, Date, Float, Long
        if(value.isString()) {
            String strValue = value.asString().getValue();
            if (("Date".equals(type))) {
                DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC().parseDateTime(strValue);
                return new BsonDateTime(dateTime.getMillis());
            } else if ("DateTime".equals(type)) {
                DateTime dateTime = new DateTime(strValue).withZone(DateTimeZone.UTC);
                return new BsonDateTime(dateTime.getMillis());
            } else if ("Long".equals(type)) {
                long val = Long.parseLong(strValue);
                return new BsonInt64(val);
            } else if (("Float".equals(type) || "Double".equals(type))) {
                double val = Double.parseDouble(strValue);
                return new BsonDouble(val);
            }
        }

        return value;
    }
}
