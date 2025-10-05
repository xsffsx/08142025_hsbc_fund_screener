package com.dummy.wpb.wpc.utils.service;

import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;

public class SequenceService {
    protected MongoCollection<Document> colSequence;
    private static FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);

    public SequenceService(MongoDatabase mongodb) {
        this.colSequence = mongodb.getCollection(CollectionName.sequence);
    }

    public void initSequenceId(String sequenceName, long maxProdId) {
        Document doc = new Document();
        doc.put(Field._id, sequenceName);
        doc.put(Field.max, maxProdId);
        colSequence.replaceOne(Filters.eq(Field._id, sequenceName), doc, new ReplaceOptions().upsert(true));
    }

    public Long nextId(String idSequenceName) {
        Document doc = colSequence.findOneAndUpdate(Filters.eq(Field._id, idSequenceName), Updates.inc(Field.max, 1L), options);
        if(null == doc) {   // if not found, make one
            doc = new Document();
            doc.put(Field._id, idSequenceName);
            doc.put(Field.max, 1L);
            colSequence.insertOne(doc);
        }
        return doc.getLong(Field.max);
    }
}
