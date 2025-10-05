package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class ReferenceDataBatchUpdateResult {
    private long matchCount;
    private List<Document> matchReferenceData;
    private List<Document> updatedReferenceData;
    private List<ReferDataValidationResult> invalidReferenceData;
}
