package com.dummy.wmd.wpc.graphql.listener;

import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryBatchImportResult;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryValidationResult;
import com.dummy.wmd.wpc.graphql.service.PriceHistoryService;
import com.dummy.wmd.wpc.graphql.validator.Error;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;

@RunWith(MockitoJUnitRunner.class)
public class PriceHistoryListenerTest {

    private PriceHistoryListener priceHistoryListener;
    @Mock
    private PriceHistoryService priceHistoryService;

    @Before
    public void setUp() {
        priceHistoryListener = new PriceHistoryListener(priceHistoryService);
        ProductPriceHistoryBatchImportResult importResult = new ProductPriceHistoryBatchImportResult();
        List<ProductPriceHistoryValidationResult> invalidPriceHistories = new ArrayList<>();
        ProductPriceHistoryValidationResult priceHistoryValidationResult = new ProductPriceHistoryValidationResult();
        List<Error> errors = new ArrayList<>();
        Error error = new Error();
        error.setMessage("ERROR");
        errors.add(error);
        priceHistoryValidationResult.setErrors(errors);
        invalidPriceHistories.add(priceHistoryValidationResult);
        importResult.setInvalidPriceHistories(invalidPriceHistories);
        Mockito.when(priceHistoryService.batchImport(anyList(), anyBoolean())).thenReturn(importResult);
    }

    @Test
    public void testBeforeValidation_givenMapAndMap_DoesNotThrow() {
        try {
            priceHistoryListener.beforeValidation(new HashMap<>(), new HashMap<>());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testAfterInsert_givenDocument_DoesNotThrow() {
        try {
            priceHistoryListener.afterInsert(new Document());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testAfterUpdate_givenDocument_DoesNotThrow() {
        try {
            priceHistoryListener.afterAllUpdate(Collections.singletonList(new Document()));
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testInterestJsonPaths() {
        Assertions.assertNotNull(priceHistoryListener.interestJsonPaths());
    }
}
