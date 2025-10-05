package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.DocumentUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.springframework.stereotype.Service;

@Service
public class SequenceService {
    protected MongoCollection<Document> colSequence;
    private static FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);

    public SequenceService(MongoDatabase mongodb) {
        this.colSequence = mongodb.getCollection(CollectionName.sequence.toString());
    }


    public Long nextId(String idSequenceName) {
        Document doc = colSequence.findOneAndUpdate(Filters.eq(Field._id, idSequenceName), Updates.inc(Field.max, 1L), options);
        if(null == doc) {   // if not found, make one
            doc = new Document();
            doc.put(Field._id, idSequenceName);
            doc.put(Field.max, 1L);
            colSequence.insertOne(doc);
        }
        return DocumentUtils.getLong(doc, Field.max);
    }
}
