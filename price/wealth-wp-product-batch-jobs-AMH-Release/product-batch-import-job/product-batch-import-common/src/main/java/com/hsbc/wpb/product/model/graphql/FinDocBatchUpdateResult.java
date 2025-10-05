package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class FinDocBatchUpdateResult {

    private long matchCount;

    private List<Document> matchFinDocs;

    private List<Document> updatedFinDocs;

    private List<InvalidFinDoc> invalidFinDocs;
}
