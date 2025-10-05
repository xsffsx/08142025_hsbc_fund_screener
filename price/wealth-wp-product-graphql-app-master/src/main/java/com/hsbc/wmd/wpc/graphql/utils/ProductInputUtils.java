package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.GraphQLProvider;
import com.dummy.wmd.wpc.graphql.constant.AltClassCode;
import com.dummy.wmd.wpc.graphql.constant.Field;
import graphql.schema.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@SuppressWarnings("java:S3740")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductInputUtils {
    private static Map<String, Object> supplementFieldsForMap(Map<String, Object> map, GraphQLInputObjectType objectType) {
        Map<String, Object> output = new LinkedHashMap<>();
        Date now = new Date();
        objectType.getFields().forEach(field -> {
            GraphQLInputType type = field.getType();
            String name = field.getName();
            Object value = map.get(name);

            if(type instanceof GraphQLScalarType) {
                value = getGraphqlScalarTypeValue(value, name, now);
            } else if(null != value) {
                if (type instanceof GraphQLList) {
                    GraphQLType graphQLType = ((GraphQLList) type).getOriginalWrappedType();
                    if(graphQLType instanceof GraphQLInputObjectType) {
                        value = supplementFieldsForList((List) value, (GraphQLInputObjectType)graphQLType);
                    }
                } else if (type instanceof GraphQLInputObjectType) {
                    value = supplementFieldsForMap((Map<String, Object>)value, (GraphQLInputObjectType)type);
                }
            }
            if(null != value) { // keep only non-null fields
                output.put(name, value);
            }
        });
        return output;
    }

    private static Object getGraphqlScalarTypeValue(Object value, String name, Date now) {
        if(null == value) {
            if (Field.recCreatDtTm.equals(name)) {
                value = now;
            } else if (Field.recUpdtDtTm.equals(name)) {
                value = now;
            } else if (Field.rowid.equals(name)) {
                value = UUID.randomUUID().toString();
            }
        }
        return value;
    }

    private static List supplementFieldsForList(List list, GraphQLInputObjectType objectType) {
        List output = new ArrayList();
        list.forEach(item -> {
            if(item instanceof Map) {
                output.add(supplementFieldsForMap((Map<String, Object>) item, objectType));
            } else {
                output.add(item);
            }
        });
        return output;
    }

    /**
     * Supplement missing rowid, recCreatDtTm and recUpdtDtTm according to the ProductInput schema
     *
     * @param doc
     * @return
     */
    public static Map<String, Object> supplementMissingFields(Map<String, Object> doc) {
        GraphQLInputObjectType inputType = (GraphQLInputObjectType)GraphQLProvider.getGraphQLSchema().getType("ProductInput");
        supplementAltIds(doc);
        return supplementFieldsForMap(doc, inputType);
    }

    /**
     * If the P code is missing in the alt id list, add a P code
     * If the T code is missing in the alt id list, add a T code
     * @param prod
     */
    private static void supplementAltIds(Map<String, Object> prod) {
        prod.computeIfAbsent(Field.altId, key -> new ArrayList<>() );
        List<Map<String, Object>> altIds = (List)prod.get(Field.altId);

        String prodTypeCde = (String)prod.get(Field.prodTypeCde);
        boolean hasPCode = altIds.stream().anyMatch(map -> AltClassCode.P.equals(map.get(Field.prodCdeAltClassCde)));
        if(!hasPCode) {
            Map<String, Object> pCode = new LinkedHashMap<>();
            pCode.put(Field.prodCdeAltClassCde, AltClassCode.P);
            pCode.put(Field.prodAltNum, prod.get(Field.prodAltPrimNum));
            pCode.put(Field.prodTypeCde, prodTypeCde);
            altIds.add(pCode);
        }

        boolean hasTCode = altIds.stream().anyMatch(map -> AltClassCode.T.equals(map.get(Field.prodCdeAltClassCde)));
        if("UT".equals(prodTypeCde) && !hasTCode) {
            String prodAltNum = org.apache.commons.lang.StringUtils.leftPad(prod.get(Field.prodId).toString(), 30, '0');
            Map<String, Object> tCode = new LinkedHashMap<>();
            tCode.put(Field.prodCdeAltClassCde, AltClassCode.T);
            tCode.put(Field.prodAltNum, prodAltNum);
            tCode.put(Field.prodTypeCde, prodTypeCde);
            altIds.add(tCode);
        }
    }
}
