package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DataLoader;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MetadataLoader implements DataLoader {
    private static final  String TARGET_COLLECTION_NAME = CollectionName.metadata;
    protected MongoCollection<Document> collection;
    private List<Map<String, Object>> metadataList;

    public MetadataLoader(MongoDatabase mongodb, List<Map<String, Object>> metadataList){
        this.collection = mongodb.getCollection(TARGET_COLLECTION_NAME);
        this.metadataList = metadataList;
    }

    @Override
    public void load() {
        if(collection.countDocuments() > 0){
            collection.drop();
            log.warn("Target collection [{}] is not empty, dropped", TARGET_COLLECTION_NAME);
        }

        log.info("Loading Metadata ...");
        long start = System.currentTimeMillis();

        List<Document> docs = metadataList.stream().map(Document::new).collect(Collectors.toList());
        collection.insertMany(docs);

        long cost = (System.currentTimeMillis() - start) / 1000;
        log.info("{} seconds consumed", cost);
    }

}
