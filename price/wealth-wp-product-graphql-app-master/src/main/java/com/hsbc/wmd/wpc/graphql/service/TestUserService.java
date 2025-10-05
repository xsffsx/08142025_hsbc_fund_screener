package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("java:S1168")
@Service
@Slf4j
public class TestUserService {
    protected MongoCollection<Document> collection;

    public TestUserService(MongoDatabase mongodb) {
        this.collection = mongodb.getCollection(CollectionName.test_user.toString());
    }

    public List<String> getRoles(String staffId) {
        Document doc = collection.find(eq(Field.staffId, staffId)).first();
        if(null != doc) {
            return doc.getList("roles", String.class);
        }
        return null;
    }
}
