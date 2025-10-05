package com.dummy.wmd.wpc.graphql.utils;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Component;

@SuppressWarnings({"java:S1118", "java:S3010"})
@Component
public class DBUtils {
    private static MongoDatabase mongoDatabase;

    public DBUtils(MongoDatabase mongodb){
        mongoDatabase = mongodb;
    }

    public static MongoDatabase getMongoDatabase(){
        return mongoDatabase;
    }

    public static MongoCollection<Document> getCollection(String collectionName){
        return mongoDatabase.getCollection(collectionName);
    }
}
