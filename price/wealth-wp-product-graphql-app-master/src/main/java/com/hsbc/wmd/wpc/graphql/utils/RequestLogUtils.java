package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bson.Document;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestLogUtils {
    public static void removeData(Document doc) {
        Document executionResult = (Document)doc.get("executionResult");
        if(null != executionResult) {
            executionResult.remove("data");
        }
    }
}
