package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.StaffLicenseCheckBatchImportResult;
import com.dummy.wmd.wpc.graphql.model.StaffLicenseCheckValidationResult;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StafLicCheckService {

    @Autowired
    private StaffLicenseCheckChangeService staffLicenseCheckChangeService;

    public StaffLicenseCheckBatchImportResult saveStaffLicenseCheck(List<Map<String, Object>> stafLicCheckList) {
        Document doc = new Document();
        StaffLicenseCheckBatchImportResult result = new StaffLicenseCheckBatchImportResult();
        List<Document> createdStaffLicenseCheck = new ArrayList<>();
        List<Document> updatedStaffLicenseCheckList = new ArrayList<>();
        List<StaffLicenseCheckValidationResult> invalidStaffLicenseCheckList = new ArrayList<>();
        List<Error> errorList;
        for (Map<String, Object> stafLicCheckMap : stafLicCheckList) {
            Document staffLicenseCheck = new Document(stafLicCheckMap);
            doc.put(Field.doc, staffLicenseCheck);
            doc.put(Field.ctryRecCde, staffLicenseCheck.getString(Field.ctryRecCde));
            doc.put(Field.grpMembrRecCde, staffLicenseCheck.getString(Field.grpMembrRecCde));
            if (Objects.isNull(staffLicenseCheck.get(Field._id))) {
                doc.put(Field.actionCde, ActionCde.add.name());
                errorList = staffLicenseCheckChangeService.validate(doc);
                if (errorList.isEmpty()) {
                    staffLicenseCheckChangeService.add(staffLicenseCheck);
                    createdStaffLicenseCheck.add(staffLicenseCheck);
                }else {
                    invalidStaffLicenseCheckList.add(new StaffLicenseCheckValidationResult(staffLicenseCheck, errorList));
                }
            }else {
                doc.put(Field.actionCde, ActionCde.update.name());
                errorList = staffLicenseCheckChangeService.validate(doc);
                if (errorList.isEmpty()) {
                    staffLicenseCheckChangeService.update(staffLicenseCheck);
                    updatedStaffLicenseCheckList.add(staffLicenseCheck);
                }else {
                    invalidStaffLicenseCheckList.add(new StaffLicenseCheckValidationResult(staffLicenseCheck, errorList));
                }
            }
        }
        result.setCreatedStaffLicenseCheck(createdStaffLicenseCheck);
        result.setUpdatedStaffLicenseCheck(updatedStaffLicenseCheckList);
        result.setInvalidStaffLicenseCheck(invalidStaffLicenseCheckList);
        return result;
    }

}
