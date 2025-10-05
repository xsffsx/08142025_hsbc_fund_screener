package com.dummy.wmd.wpc.graphql.fetcher;

import com.dummy.wmd.wpc.graphql.productConfig;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Resolve user info from JWT token
 */
@Slf4j
@Component
public class SupportEntitiesFetcher implements DataFetcher<List<Map<String, String>>> {
    private productConfig productConfig;

    public SupportEntitiesFetcher(productConfig productConfig) {
        this.productConfig = productConfig;
    }

    @Override
    public List<Map<String, String>> get(DataFetchingEnvironment environment) {
        return productConfig.getSupportEntities();
    }
}