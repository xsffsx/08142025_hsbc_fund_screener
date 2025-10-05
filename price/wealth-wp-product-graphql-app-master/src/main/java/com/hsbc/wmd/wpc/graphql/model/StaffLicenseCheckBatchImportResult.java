package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class StaffLicenseCheckBatchImportResult {
    private List<Document> createdStaffLicenseCheck;
    private List<Document> updatedStaffLicenseCheck;
    private List<StaffLicenseCheckValidationResult> invalidStaffLicenseCheck;
}
