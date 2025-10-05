package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.utils.GraphQLSchemaUtils;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FieldSelectionQueryFetcher implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment environment) {
        String queryName = environment.getArgument("queryName");
        List<String> excludes = environment.getArgument("excludes");
        return new GraphQLSchemaUtils(environment.getGraphQLSchema(), excludes).getFieldSelectionOfQuery(queryName);
    }
}