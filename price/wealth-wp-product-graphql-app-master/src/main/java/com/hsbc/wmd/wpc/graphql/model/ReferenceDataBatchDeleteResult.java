package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReferenceDataBatchDeleteResult {
    private long deletedCount;
    private List<Map<String, Object>> deletedReferenceData;
    private List<Map<String, Object>> invalidReferenceData;
}
