package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class FinDocBatchCreateResult {

    private List<Document> createdFinDocs;

    private List<FinDocValidationResult> invalidFinDocs;
}
