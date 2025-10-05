package com.dummy.wmd.wpc.graphql.utils;

import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@SuppressWarnings("java:S1149")
@Slf4j
public class GraphQLSchemaUtils {
    private GraphQLSchema schema;
    private Stack<String> typeNameStack = new Stack<>();
    private Stack<String> attrNameStack = new Stack<>();
    private List<String> excludes = new ArrayList<>();

    public GraphQLSchemaUtils(GraphQLSchema graphQLSchema) {
        this.schema = graphQLSchema;
    }

    public GraphQLSchemaUtils(GraphQLSchema graphQLSchema, List<String> excludes) {
        this.schema = graphQLSchema;
        if(null != excludes) {
            this.excludes = excludes;
        }
    }

    /**
     * Get all field selection of a query
     *
     * @param queryName
     * @return in case the query is not found, return null
     */
    public String getFieldSelectionOfQuery(String queryName) {
        GraphQLFieldDefinition fieldDefinition = schema.getQueryType().getFieldDefinition(queryName);
        if(null == fieldDefinition){
            log.warn("GraphQLFieldDefinition is not found for: {}", queryName);
            return null;
        }
        GraphQLOutputType outputType = fieldDefinition.getType();
        return getFieldSelection(outputType);
    }

    /**
     * Retrieve all field names from the outputType.
     *  return null to indicate the field is a loop reference, should be ignored
     *  return "" empty string to indicate no sub fields
     *
     * @param outputType
     * @return
     */
    private String getFieldSelection(GraphQLOutputType outputType) {
        StringBuilder sb = new StringBuilder();
        GraphQLFieldsContainer fieldsContainer =  null;
        if(outputType instanceof GraphQLFieldsContainer) {
            fieldsContainer = (GraphQLFieldsContainer)outputType;
        } else if(outputType instanceof GraphQLList) {
            GraphQLType wrappedType = ((GraphQLList)outputType).getWrappedType();
            if(wrappedType instanceof GraphQLFieldsContainer) {
                fieldsContainer = (GraphQLFieldsContainer)wrappedType;
            }
        }
        if(null != fieldsContainer) {
            // to avoid infinite loop type reference, eg a query with sub field is of ProductType
            if(typeNameStack.contains(fieldsContainer.getName())) {
                return null;
            }

            typeNameStack.push(fieldsContainer.getName());
            iteratorFiledDefinitions(fieldsContainer, sb);
            typeNameStack.pop();
        }
        return sb.toString().trim();
    }

    private void iteratorFiledDefinitions(GraphQLFieldsContainer fieldsContainer, StringBuilder sb) {
        fieldsContainer.getFieldDefinitions().forEach(item -> {
            attrNameStack.push(item.getName());
            String fullname = String.join(".", attrNameStack);
            boolean exclude = excludes.stream().anyMatch(fullname::matches);
            if(!exclude) {
                String subFields = getFieldSelection(item.getType());
                if (null != subFields) {
                    sb.append(item.getName()).append("\n");
                    if (subFields.length() > 0) {
                        sb.append("{\n").append(subFields).append("\n}\n");
                    }
                } else {
                    log.warn("Ignore loop reference: {} of {}", item.getName(), item.getType());
                }
            }
            attrNameStack.pop();
        });
    }

    /**
     * Simply retrieve field selections as a set of String
     *
     * @param environment
     * @return
     */
    public static Set<String> getSelectedFields(DataFetchingEnvironment environment){
        List<SelectedField> selectedFields = environment.getSelectionSet().getFields();
        Set<String> fieldNames = new HashSet<>();
        selectedFields.forEach(field -> fieldNames.add(field.getQualifiedName()));
        return fieldNames;
    }
}
