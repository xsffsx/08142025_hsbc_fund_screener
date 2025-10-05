package com.dummy.wpb.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.product.service.OperationService;
import com.dummy.wpb.product.utils.CommonUtils;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultOperationServiceTest {


    ObjectMapper objectMapper = new ObjectMapper();

    OperationService operationService = new DefaultOperationService();

    @Test
    public void testCalcOperations() throws Exception {
        Document originalProduct = objectMapper.readValue(CommonUtils.readResource("/test/ELI-SESIN24A0972-before.json"), Document.class);
        Document updatedProduct = objectMapper.readValue(CommonUtils.readResource("/test/ELI-SESIN24A0972-after.json"), Document.class);
        String operationJson = objectMapper.writeValueAsString(operationService.calcOperations(originalProduct, updatedProduct));
        JSONAssert.assertEquals(operationJson, CommonUtils.readResource("/test/operation-SESIN24A0972-expect.json"), false);
    }

}