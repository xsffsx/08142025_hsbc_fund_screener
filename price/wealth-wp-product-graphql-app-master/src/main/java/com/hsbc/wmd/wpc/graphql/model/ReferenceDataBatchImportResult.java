package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class ReferenceDataBatchImportResult {
    private List<Document> importedReferenceData;
    private List<ReferDataValidationResult> invalidReferenceData;
}
