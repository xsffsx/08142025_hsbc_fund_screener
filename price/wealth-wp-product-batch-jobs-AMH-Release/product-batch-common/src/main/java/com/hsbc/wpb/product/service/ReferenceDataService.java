package com.dummy.wpb.product.service;


import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.model.graphql.ReferenceDataBatchCreateResult;

import java.util.List;
import java.util.Map;

public interface ReferenceDataService {
    List<ReferenceData> referenceDataByFilter(Map<String, Object> filterMap);

    ReferenceDataBatchCreateResult createRefData(List<ReferenceData> referenceDataList);
}
