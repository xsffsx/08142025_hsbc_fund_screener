package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class FinDocBatchCreateResult {

    private List<Document> createdFinDocs;

    private List<InvalidFinDoc> invalidFinDocs;
}
