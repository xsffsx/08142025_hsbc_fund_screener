package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.slf4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
public class ReferenceDataBatchCreateResult {
    private List<ReferenceData> createdReferenceData;
    private List<InvalidReferData> invalidReferenceData;

    public void logCreateResult(Logger log) {
        if (!CollectionUtils.isEmpty(createdReferenceData)) {
            for (ReferenceData createdData : createdReferenceData) {
                log.info(String.format("\nReference Data has been created %s", buildReferDataKey(createdData)));
            }
        }

        if (!CollectionUtils.isEmpty(invalidReferenceData)) {
            for (InvalidReferData invalidData : invalidReferenceData) {
                log.error("\nReference Data created failed. {}", buildReferDataKey(invalidData.getReferData()));
                StringBuilder stringBuilder = new StringBuilder();
                invalidData.getErrors().forEach(err -> stringBuilder.append(String.format("\n[%s] %s", err.getJsonPath(), err.getMessage())));
                log.info(stringBuilder.toString());
            }
        }

    }

    private String buildReferDataKey(ReferenceData refData) {
        return String.format("(ctryRecCde: %s, grpMembrRecCde: %s, cdvTypeCde: %s, cdvCde: %s)",
                refData.getCtryRecCde(), refData.getGrpMembrRecCde(), refData.getCdvTypeCde(), refData.getCdvCde());
    }
}
