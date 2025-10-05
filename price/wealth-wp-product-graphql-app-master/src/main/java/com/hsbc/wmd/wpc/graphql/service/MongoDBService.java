package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
@SuppressWarnings({"java:S1118", "java:S2209", "java:S3010"})
@Component
public class MongoDBService {
    private static MongoDatabase mongoDatabase;

    public MongoDBService(MongoDatabase mongoDatabase){
        this.mongoDatabase = mongoDatabase;
    }

    public static List<Map<String, Object>> queryForMapList(CollectionName collName, Bson filter){
        MongoCollection<Document> collection = mongoDatabase.getCollection(collName.toString());
        return collection.find(filter).into(new LinkedList<>());
    }

    public static long countDocuments(CollectionName collName, Bson filter){
        MongoCollection<Document> collection = mongoDatabase.getCollection(collName.toString());
        return collection.countDocuments(filter);
    }
}
