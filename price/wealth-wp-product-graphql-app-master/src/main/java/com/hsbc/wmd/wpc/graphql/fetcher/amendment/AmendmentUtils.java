package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import graphql.execution.ValuesResolver;
import graphql.language.NonNullType;
import graphql.language.Type;
import graphql.language.TypeName;
import graphql.language.VariableDefinition;
import graphql.schema.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.*;

@SuppressWarnings("java:S3740")
@Slf4j
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class AmendmentUtils {
    /**
     * Coerce the doc in the amendment document to align with GraphQLSchema
     *
     * @param amdm
     * @param graphQLSchema
     * @return
     */
    public static void coerceChangeDocument(Document amdm, GraphQLSchema graphQLSchema) {
        String typeName = DocType.valueOf(amdm.getString(Field.docType)).getInputTypeName();
        Type type = NonNullType.newNonNullType().type(new TypeName(typeName)).build();

        boolean hasDocBase = Objects.nonNull(amdm.get(Field.docBase));
        List<VariableDefinition> variableDefinitions = new ArrayList<>();
        variableDefinitions.add(new VariableDefinition(Field.doc, type));
        if(hasDocBase) {
            variableDefinitions.add(new VariableDefinition(Field.docBase, type));
        }

        removeExtraFields(amdm, graphQLSchema, typeName);

        ValuesResolver valuesResolver = new ValuesResolver();
        Map<String, Object> coercedVariables = valuesResolver.coerceVariableValues(graphQLSchema, variableDefinitions, amdm);
        amdm.put(Field.doc, new Document((Map<String, Object>)coercedVariables.get(Field.doc)));
        if(hasDocBase) {
            amdm.put(Field.docBase, new Document((Map<String, Object>) coercedVariables.get(Field.docBase)));
        }
    }

    /**
     * Remove the fields that is not in the input type, to avoid exception like below:
     *  The variables input contains a field name 'asetClassCde' that is not defined for input object type 'ProductInput'
     * @param amdm
     * @param graphQLSchema
     * @param typeName
     */
    private static void removeExtraFields(Document amdm, GraphQLSchema graphQLSchema, String typeName) {
        GraphQLInputObjectType inputType = (GraphQLInputObjectType)graphQLSchema.getType(typeName);
        Map<String, Object> doc = (Map<String, Object>) amdm.get(Field.doc);
        removeExtraFieldsFromDocument(doc, inputType);

        Map<String, Object> docBase = (Map<String, Object>) amdm.get(Field.docBase);
        if(null != docBase) {
            removeExtraFieldsFromDocument(docBase, inputType);
        }
    }

    /**
     *
     * @param map
     * @param objectType
     */
    private static void removeExtraFieldsFromDocument(Map<String, Object> map, GraphQLInputObjectType objectType) {
        Set<String> legalKeys = new LinkedHashSet<>();
        objectType.getFields().forEach(field -> legalKeys.add(field.getName()));
        Set<String> keys = new LinkedHashSet<>(map.keySet());
        keys.removeAll(legalKeys);
        for(String key: keys) {
            map.remove(key);
            log.debug("remove key: {}", key);
        }
        map.forEach((k, v) -> {
            if(v instanceof List) {
                GraphQLInputType type = objectType.getField(k).getType();
                GraphQLType graphQLType = ((GraphQLList) type).getOriginalWrappedType();
                if(graphQLType instanceof GraphQLInputObjectType) {
                    removeExtraFieldsFromList((List) v, (GraphQLInputObjectType)graphQLType);
                }
            }
        });
    }

    private static void removeExtraFieldsFromList(List list, GraphQLInputObjectType objectType) {
        list.forEach(item -> {
            if(item instanceof Document) {
                removeExtraFieldsFromDocument((Document) item, objectType);
            }
        });
    }
}
