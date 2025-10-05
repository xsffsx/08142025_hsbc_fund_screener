package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Service
public class DocumentRevisionService {
    private MongoCollection<Document> colDocRevision;
    private MongoDatabase mongodb;

    public DocumentRevisionService(MongoDatabase mongodb){
        this.mongodb = mongodb;
        this.colDocRevision = mongodb.getCollection(CollectionName.document_revision.toString());
    }

    public void saveDocumentRevision(DocType docType, Map<String, Object> document) {
        Document revDoc = new Document();
        Object ctryRecCde = document.get(Field.ctryRecCde);
        Object grpMembrRecCde = document.get(Field.grpMembrRecCde);
        Object docId = document.get(Field._id);
        Object docRevision = document.get(Field.revision);
        revDoc.put(Field.ctryRecCde, ctryRecCde);
        revDoc.put(Field.grpMembrRecCde, grpMembrRecCde);
        revDoc.put(Field.docType, docType.name());
        revDoc.put(Field.docId, docId);
        revDoc.put(Field.docRevision, docRevision);
        revDoc.put(Field.doc, document);
        revDoc.put(Field.recCreatDtTm, new Date());
        colDocRevision.insertOne(revDoc);
    }

    public void saveDocumentRevision(DocType docType, List<Map<String, Object>> documentList) {
        List<Document> revDocList = new ArrayList<>();
        for (Map<String, Object> document : documentList) {
            Document revDoc = new Document();
            Object ctryRecCde = document.get(Field.ctryRecCde);
            Object grpMembrRecCde = document.get(Field.grpMembrRecCde);
            Object docId = document.get(Field._id);
            Object docRevision = document.get(Field.revision);
            revDoc.put(Field.ctryRecCde, ctryRecCde);
            revDoc.put(Field.grpMembrRecCde, grpMembrRecCde);
            revDoc.put(Field.docType, docType.name());
            revDoc.put(Field.docId, docId);
            revDoc.put(Field.docRevision, docRevision);
            revDoc.put(Field.doc, document);
            revDoc.put(Field.recCreatDtTm, new Date());
            revDocList.add(revDoc);
        }

        colDocRevision.insertMany(revDocList);
    }


    public Document getDocument(String docType, Long docId, Long revision) {
        Bson filter = and(
                eq(Field.docId, docId),
                eq(Field.docType, docType),
                eq(Field.docRevision, revision)
        );
        Document docRev = colDocRevision.find(filter).first();
        if(null != docRev) {
            return (Document)docRev.get(Field.doc);
        }

        // otherwise, try to get the latest doc
        MongoCollection<Document> coll = mongodb.getCollection(docType);
        filter = and(eq(Field._id, docId), eq(Field.revision, revision));
        return coll.find(filter).first();
    }
}
