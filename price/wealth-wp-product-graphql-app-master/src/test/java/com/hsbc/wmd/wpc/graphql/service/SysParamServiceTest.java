package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.SysParamBatchUpdateResult;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class SysParamServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoCollection<Document> colSysParam;

    @InjectMocks
    private SysParamService sysParamService;

    @Before
    public void setUp() {
        sysParamService = new SysParamService(mockMongoDatabase);
        ReflectionTestUtils.setField(sysParamService, "colSysParam", colSysParam);
    }

    @Test
    public void testbatchUpdate_giveSysParams_returnsSysParamBatchUpdateResult() {
        // Setup
        Document doc = CommonUtils.readResourceAsDocument("/files/sys_params.json");
        List<Map<String, Object>> sysParams = (List<Map<String, Object>>) doc.get("sysParams");
        // use the prepared data, size is 2, the first one is for modification, the second one is for validation
        assertTrue(sysParams.size() >= 2);
        Map<String, Object> first = sysParams.get(0);
        Mockito.when(colSysParam.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
                .thenReturn(new Document(first))
                .thenReturn(null);
        // Run the test
        SysParamBatchUpdateResult result = sysParamService.batchUpdate(sysParams);
        // Verify the results
        assertNotNull(result);
        List<Document> updatedSysParams = result.getUpdatedSysParams();
        assertTrue(updatedSysParams != null && updatedSysParams.size() == 1);
        Document updatedDoc = updatedSysParams.get(0);
        assertEquals(updatedDoc.get(Field.ctryRecCde), first.get(Field.ctryRecCde));
        assertEquals(updatedDoc.get(Field.grpMembrRecCde), first.get(Field.grpMembrRecCde));
        assertEquals(updatedDoc.get(Field.parmCde), first.get(Field.parmCde));
        List<Document> notExistingSysParams = result.getNotExistingSysParams();
        assertTrue(notExistingSysParams != null && notExistingSysParams.size() == sysParams.size() - 1);
    }

    @Test
    public void testbatchUpdate_giveEmptySysParams_returnsSysParamBatchUpdateResult() {
        // Setup
        List<Map<String, Object>> sysParams = new ArrayList<>();
        SysParamBatchUpdateResult result = sysParamService.batchUpdate(sysParams);
        // Verify the results
        assertNotNull(result);
        List<Document> updatedSysParams = result.getUpdatedSysParams();
        assertTrue(updatedSysParams != null && updatedSysParams.isEmpty());
        List<Document> notExistingSysParams = result.getNotExistingSysParams();
        assertTrue(notExistingSysParams != null && notExistingSysParams.isEmpty());
    }

}
