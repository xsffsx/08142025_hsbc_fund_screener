package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Projections.include;

@Service
public class MetadataService {
    private MongoCollection<Document> collMetadata;

    public MetadataService(MongoDatabase mongodb) {
        collMetadata = mongodb.getCollection(CollectionName.metadata.toString());
    }

    public List<Document> getMetadataList(Bson filter) {
        return collMetadata.find(filter).into(new ArrayList<>());
    }

    /**
     * Get jsonPath to business name mapping
     *
     * @return
     */
    public Map<String, String> getBusinessNameMapping() {
        List<Document> list = collMetadata.find().projection(include(Field.jsonPath, Field.businessName)).into(new ArrayList<>());
        Map<String, String> mapping = new LinkedHashMap<>();
        list.forEach(doc -> mapping.put(doc.getString(Field.jsonPath), doc.getString(Field.businessName)));
        return mapping;
    }
}
