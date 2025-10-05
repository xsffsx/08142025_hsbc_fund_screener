package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import org.bson.Document;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RevisionUtilsTest {
    @Test
    public void testNullRevision(){
        Document oldDoc = new Document();
        Document newDoc = new Document();
        RevisionUtils.setRevisionNumber(oldDoc, newDoc);
        assertEquals(1L, newDoc.get(Field.revision));
    }

    @Test
    public void testNormalRevision(){
        Document oldDoc = new Document();
        oldDoc.put(Field.revision, 7L);
        Document newDoc = new Document();
        RevisionUtils.setRevisionNumber(oldDoc, newDoc);
        assertEquals(DocumentUtils.getLong(oldDoc, Field.revision) + 1, DocumentUtils.getLong(newDoc, Field.revision).longValue());
    }

    @Test
    public void testIncreaseNullRevision(){
        Document doc = new Document();
        RevisionUtils.increaseRevisionNumber(doc);
        assertEquals(1L, doc.get(Field.revision));
    }

    @Test
    public void testIncreaseNormalRevision(){
        Document doc = new Document();
        doc.put(Field.revision, 7L);
        RevisionUtils.increaseRevisionNumber(doc);
        assertEquals(8L, doc.get(Field.revision));
    }
}
