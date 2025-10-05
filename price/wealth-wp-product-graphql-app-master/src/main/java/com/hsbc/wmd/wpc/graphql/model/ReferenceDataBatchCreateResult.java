package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
public class ReferenceDataBatchCreateResult {
    private List<Map<String, Object>> createdReferenceData = Collections.emptyList();
    private List<ReferDataValidationResult> invalidReferenceData = Collections.emptyList();
}
