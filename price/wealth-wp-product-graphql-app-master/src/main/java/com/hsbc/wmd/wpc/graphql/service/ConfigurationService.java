package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Service
public class ConfigurationService {

    private static final String ID_TABLE_COLUMN_MAPPING = "ALL/table-columns-mapping";
    private MongoCollection<Document> colConfiguration;

    public ConfigurationService(MongoDatabase mongodb) {
        colConfiguration = mongodb.getCollection(CollectionName.configuration.toString());
    }

    @Cacheable("productCache")
    public List<Document> getConfigurationsByFilter(Bson filter){
        return colConfiguration.find(filter).into(new ArrayList<>());
    }

    @Cacheable("productCache")
    public Map<String, Object> getLegacyConfigByFilter() {
        Document legacyConfig = colConfiguration.find(eq(Field._id, "ALL/legacy-config")).first();
        return legacyConfig
                .getList("config", Document.class, new ArrayList<>())
                .stream()
                .collect(Collectors.toMap(
                        config -> config.getString("key"),
                        config -> config.get("value")));
    }

    /**
     * search table column mappings from MongoDB
     * @param field graphql updated field name
     * @return
     */
    @Cacheable("productCache")
    public List<String> getInterestedGroups(String field) {
        List<String> interestedGroups = new ArrayList<>();

        Bson filter = eq(Field._id, ID_TABLE_COLUMN_MAPPING);
        List<Document> results = this.getConfigurationsByFilter(filter);

        results.forEach(result -> {
            List<Document> configs = result.getList(Field.config, Document.class);
            configs.forEach(config -> {
                if ("Y".equals(config.getString(Field.productLevelSupport))) {
                    config.getList(Field.columns, Document.class).forEach(column -> {
                        if (field.equals(column.getString(Field.jsonPath))) {
                            interestedGroups.addAll(column.getList(Field.interestedGroups, String.class));
                        }
                    });
                }
            });
        });

        return interestedGroups;
    }

    /**
     * search table-columns-mapping from MongoDB
     * @return key - field name, value - interested groups
     */
    public Map<String, List<String>> getInterestedGroupFullMap() {
        Map<String, List<String>> map = new HashMap<>();
        Bson filter = eq(Field._id, ID_TABLE_COLUMN_MAPPING);
        List<Document> results = this.getConfigurationsByFilter(filter);
        results.forEach(result -> {
            List<Document> configs = result.getList(Field.config, Document.class);
            configs.forEach(config -> {
                if ("Y".equals(config.getString(Field.productLevelSupport))) {
                    List<String> groupsOfTable = new ArrayList<>();
                    config.getList(Field.columns, Document.class).forEach(column -> {
                        List<String> groupsOfColumn = column.getList(Field.interestedGroups, String.class);
                        groupsOfTable.addAll(groupsOfColumn);
                        // Map column interested groups on column level
                        map.put(column.getString(Field.jsonPath), groupsOfColumn);
                    });
                    // Map all column interested groups on table level
                    map.put(config.getString(Field.jsonPath), groupsOfTable.stream().distinct().collect(Collectors.toList()));
                }
            });
        });

        return map;
    }
}
