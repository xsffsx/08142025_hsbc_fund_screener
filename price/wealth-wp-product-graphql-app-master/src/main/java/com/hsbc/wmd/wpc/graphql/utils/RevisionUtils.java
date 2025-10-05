package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.constant.Field;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.Document;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RevisionUtils {

    /**
     * Set the revision number field of the new document, base on the revision number on the old document, which should be increased by 1,
     * we do this in synchronized mode, to keep the number in a sequence with no gap
     * @param oldDoc
     * @param newDoc
     */
    public static synchronized void setRevisionNumber(Document oldDoc, Document newDoc){
        Long revision = DocumentUtils.getLong(oldDoc, Field.revision);
        if(null == revision) {
            revision = 0L;
        }
        newDoc.put(Field.revision, revision + 1);   // revision++
    }

    /**
     * Increase the revision number in the document.
     * we do this in synchronized mode, to keep the number in a sequence with no gap
     * @param doc
     */
    public static synchronized void increaseRevisionNumber(Map<String, Object> doc) {
        Long revision = DocumentUtils.getLong(doc, Field.revision);
        if(null == revision) {
            revision = 0L;
        }
        doc.put(Field.revision, revision + 1);   // revision++
    }
}
