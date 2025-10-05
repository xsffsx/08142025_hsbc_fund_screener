package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.model.Schema;
import graphql.schema.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GraphQLTypeSchemaFetcher implements DataFetcher<List<Schema>> {

    @Override
    public List<Schema> get(DataFetchingEnvironment env) throws Exception {
        String graphQLType = env.getArgument("graphQLType");
        GraphQLSchema graphQLSchema = env.getGraphQLSchema();
        GraphQLType type = graphQLSchema.getType(graphQLType);
        return retrieveSchema(type, "");
    }

    private List<Schema> retrieveSchema(GraphQLType graphQLType, String parentAttr) {
        List<Schema> schemaList = new LinkedList<>();

        if (graphQLType instanceof GraphQLScalarType) {
            schemaList.add(new Schema(parentAttr, ((GraphQLScalarType) graphQLType).getName()));
        } else if (graphQLType instanceof GraphQLObjectType) {
            GraphQLObjectType objectType = (GraphQLObjectType) graphQLType;
            iteratorGraphqlFieldDefinitions(parentAttr, objectType, schemaList);
        } else if (graphQLType instanceof GraphQLInputObjectType) {
            GraphQLInputObjectType inputObjectType = (GraphQLInputObjectType) graphQLType;
            iteratorGrahqlInputObjectField(parentAttr, inputObjectType, schemaList);
        } else if (graphQLType instanceof GraphQLList) {
            GraphQLList graphQLList = (GraphQLList) graphQLType;
            GraphQLType originalType = graphQLList.getOriginalWrappedType();
            schemaList.addAll(retrieveSchema(originalType, parentAttr));
        }

        return schemaList;
    }

    private void iteratorGrahqlInputObjectField(String parentAttr, GraphQLInputObjectType inputObjectType, List<Schema> schemaList) {
        for (GraphQLInputObjectField field : inputObjectType.getFieldDefinitions()) {
            GraphQLInputType type = field.getType();
            String attr = StringUtils.isBlank(parentAttr) ? field.getName() : String.format("%s.%s", parentAttr, field.getName());
            if (type instanceof GraphQLList) {
                GraphQLList graphQLList = (GraphQLList) type;
                GraphQLType originalType = graphQLList.getOriginalWrappedType();
                schemaList.addAll(retrieveSchema(originalType, String.format("%s[*]", attr)));
            } else {
                schemaList.addAll(retrieveSchema(type, attr));
            }
        }
    }

    private void iteratorGraphqlFieldDefinitions(String parentAttr, GraphQLObjectType objectType, List<Schema> schemaList) {
        for (GraphQLFieldDefinition field : objectType.getFieldDefinitions()) {
            GraphQLType type = field.getType();
            String attr = StringUtils.isBlank(parentAttr) ? field.getName() : String.format("%s.%s", parentAttr, field.getName());

            if (type instanceof GraphQLObjectType && ((GraphQLObjectType) type).getName().equals("ProductType")) {
                schemaList.add(new Schema(attr, "ProductType"));
                continue;
            }

            schemaList.addAll(retrieveSchema(type, attr));
        }
    }
}
