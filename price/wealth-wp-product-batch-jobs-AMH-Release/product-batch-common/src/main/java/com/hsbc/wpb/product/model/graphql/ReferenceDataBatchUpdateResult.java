package com.dummy.wpb.product.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class ReferenceDataBatchUpdateResult {
    private long matchCount;
    private List<ReferenceData> matchReferenceData;
    private List<ReferenceData> updatedReferenceData;
    private List<InvalidReferData> invalidReferenceData;
}
