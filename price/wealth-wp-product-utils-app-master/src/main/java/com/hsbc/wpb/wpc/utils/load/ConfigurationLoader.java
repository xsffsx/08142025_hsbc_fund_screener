package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.model.ConfigFile;
import com.dummy.wpb.wpc.utils.model.ConfigPackageInfo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ConfigurationLoader implements DataLoader {
    private static final String TARGET_COLLECTION_NAME = CollectionName.configuration;
    protected MongoDatabase mongodb;
    private MongoCollection<Document> collection;
    private Map<String, String> entryMap;
    private ConfigPackageInfo packageInfo;

    private ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    public ConfigurationLoader(MongoDatabase mongodb, Map<String, String> entryMap) throws IOException {
        this.mongodb = mongodb;
        this.entryMap = entryMap;
        String content = entryMap.get("config-package-info.yaml");
        if(null == content) {
            throw new IllegalArgumentException("config-package-info.yaml not found in the package root.");
        }
        packageInfo = yamlMapper.readValue(content, ConfigPackageInfo.class);
        collection = mongodb.getCollection(TARGET_COLLECTION_NAME);
    }

    public void load() {
        Map<String, ConfigFile> mapping = new LinkedHashMap<>();
        packageInfo.getConfigList().forEach(item -> {
            String id = String.format("%s/%s", item.getForEntity(), item.getName());
            item.setId(id);
            mapping.put(id, item);
        });

        // handle product metadata separately
        String id = "ALL/product-metadata";
        ConfigFile metadataConfig = mapping.get(id);
        mapping.remove(id);
        log.info("Loading configuration: {}", id);
        loadProductMetadata(metadataConfig);

        // handle other config
        if(collection.countDocuments() > 0){
            collection.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        for(ConfigFile config: mapping.values()) {
            log.info("Loading configuration: {}", config.getId());
            loadCommonConfig(config);
        }
    }


    private void loadProductMetadata(ConfigFile metadataConfig) {
        String metadataContent = entryMap.get(metadataConfig.getConfigFile());

        List<Map<String, Object>> metadataList = null;
        try {
            metadataList = yamlMapper.readValue(metadataContent, List.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading metadata content: " + e.getMessage(), e);
        }

        MetadataLoader metadataLoader = new MetadataLoader(mongodb, metadataList);
        metadataLoader.load();
    }

    private void loadCommonConfig(ConfigFile config) {
        String content = entryMap.get(config.getConfigFile());
        String schema = entryMap.get(config.getSchemaFile());
        Object configuration = null;
        try {
            configuration = yamlMapper.readValue(content, Object.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading config content: " + config.getConfigFile(), e);
        }
        Document doc = new Document();
        doc.put(Field._id, config.getId());
        doc.put(Field.forEntity, config.getForEntity());
        doc.put(Field.name, config.getName());
        doc.put(Field.description, config.getDescription());
        doc.put(Field.config, configuration);
        doc.put(Field.schema, schema);
        doc.put(Field.lastUpdateTime, new Date());
        collection.insertOne(doc);
    }
}
