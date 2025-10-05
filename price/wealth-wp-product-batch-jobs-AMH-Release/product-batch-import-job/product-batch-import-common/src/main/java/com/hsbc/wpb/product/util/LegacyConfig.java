package com.dummy.wpb.product.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.dummy.wpb.product.model.GraphQLRequest;
import com.dummy.wpb.product.model.graphql.LegacyConfigInfo;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
public class LegacyConfig {

    private final GraphQLService graphQLService;

    private static final String LEGACY_CONFIG = "legacy-config";

    private static final Properties legacyConfigpro = new Properties();

    public LegacyConfig(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostConstruct
    public void init() {
        try {
            GraphQLRequest graphQLRequest = new GraphQLRequest();
            graphQLRequest.setQuery(CommonUtils.readResource("/gql/configuration-query.gql"));
            Map<String, String> filter = Collections.singletonMap("name", LEGACY_CONFIG);
            graphQLRequest.setVariables(Collections.singletonMap("filter", filter));
            graphQLRequest.setDataPath("data.configurationByFilter");
            List<LegacyConfigInfo> configList = graphQLService.executeRequest(graphQLRequest, new TypeReference<List<LegacyConfigInfo>>() {
            });
            configList.forEach(item -> legacyConfigpro.putAll(item.getProperties()));
        } catch (Exception e) {
            log.error("Failed to init legacyConfig", e);
        }
    }

    /**
     * Get config value with key, entity information will be from RequestContext
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        return legacyConfigpro.getProperty(key);
    }

    /**
     * Get value from a specific key, and separate values by | or ,
     *
     * @param key
     * @return
     */
    public static List<String> getList(String key) {
        String value = get(key);
        return Arrays.asList(value.split("[|,]"));
    }

    public static int getInt(String key) {
        String value = get(key);
        return Integer.parseInt(value);
    }
}
