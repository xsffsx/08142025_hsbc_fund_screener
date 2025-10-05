package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.model.StaffLicenseCheckBatchImportResult;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class StafLicCheckServiceTest {

    @Mock
    private StaffLicenseCheckChangeService staffLicenseCheckChangeService;

    @InjectMocks
    private StafLicCheckService stafLicCheckService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(stafLicCheckService, "staffLicenseCheckChangeService", staffLicenseCheckChangeService);
        Mockito.doNothing().when(staffLicenseCheckChangeService).update(any());
    }

    @Test
    public void testCreateStaffLicenseCheck() {
        Document doc = CommonUtils.readResourceAsDocument("/files/prod-type-staff-license-check-create.json");
        List<Map<String, Object>> stafLicCheckList = (List<Map<String, Object>>) doc.get("stafLicCheck");

        Mockito.when(staffLicenseCheckChangeService.validate(any())).thenReturn(Collections.emptyList());

        StaffLicenseCheckBatchImportResult result = stafLicCheckService.saveStaffLicenseCheck(stafLicCheckList);
        assertNotNull(result.getCreatedStaffLicenseCheck());
    }

    @Test
    public void testCreateStaffLicenseCheckError() {
        Document doc = CommonUtils.readResourceAsDocument("/files/prod-type-staff-license-check-create.json");
        List<Map<String, Object>> stafLicCheckList = (List<Map<String, Object>>) doc.get("stafLicCheck");

        Mockito.when(staffLicenseCheckChangeService.validate(any())).thenReturn(Collections.singletonList(new Error("$", "@null", "please input prodTypeCde/prodSubtpCde")));

        StaffLicenseCheckBatchImportResult result = stafLicCheckService.saveStaffLicenseCheck(stafLicCheckList);
        assertNotNull(result.getInvalidStaffLicenseCheck());
    }

    @Test
    public void testUpdateStaffLicenseCheck() {
        Document doc = CommonUtils.readResourceAsDocument("/files/prod-type-staff-license-check.json");
        List<Map<String, Object>> stafLicCheckList = (List<Map<String, Object>>) doc.get("stafLicCheck");

        Mockito.when(staffLicenseCheckChangeService.validate(any())).thenReturn(Collections.emptyList());
        StaffLicenseCheckBatchImportResult result = stafLicCheckService.saveStaffLicenseCheck(stafLicCheckList);
        assertNotNull(result.getUpdatedStaffLicenseCheck());
    }

}
