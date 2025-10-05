package com.dummy.wpb.product.processor;


import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DebtInstmFormatProcessorTest {

    @Test
    public void test_process() {
        DebtInstmFormatProcessor processor = new DebtInstmFormatProcessor();
        ReflectionTestUtils.setField(processor, "systemCde", "MDSBE");
        ReflectionTestUtils.setField(processor, "formatSystemCodes", Stream.of("RBT").collect(Collectors.toList()));

        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setActionCode(BatchImportAction.ADD);
        Document importedProduct = new Document();
        Document bondProduct = new Document();
        importedProduct.put(Field.debtInstm, bondProduct);
        productStreamItem.setImportProduct(importedProduct);
        productStreamItem.setOriginalProduct(importedProduct);
        ProductStreamItem proceedProd  = processor.process(productStreamItem);
        Assert.assertNull(proceedProd);
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        proceedProd  = processor.process(productStreamItem);
        Assert.assertNull(proceedProd.getImportProduct().get(Field.debtInstm, Document.class).get(Field.yieldHist));

        Document yieldHistory = new Document();
        yieldHistory.put(Field.yieldBidPct, 15.55);
        bondProduct.put(Field.yieldHist, Collections.singletonList(yieldHistory));
        proceedProd  = processor.process(productStreamItem);
        Assert.assertEquals(15.55, proceedProd.getImportProduct().get(Field.debtInstm, Document.class).getDouble(Field.yieldBidPct), 0);

        productStreamItem.setActionCode(BatchImportAction.VALIDATE_ADD);
        proceedProd  = processor.process(productStreamItem);
        Assert.assertNotNull(proceedProd);
    }

    @Test
    public void test_shouldSkip() {
        DebtInstmFormatProcessor processor = new DebtInstmFormatProcessor();
        ReflectionTestUtils.setField(processor, "systemCde", "MDSBE");

        ProductStreamItem streamItem = new ProductStreamItem();
        streamItem.setActionCode(BatchImportAction.VALIDATE_ADD);
        Document importedProduct = new Document();
        Document originalProduct = new Document();
        Document importedBond = new Document();
        Document originalBond = new Document();
        importedProduct.put(Field.debtInstm, importedBond);
        originalProduct.put(Field.debtInstm, originalBond);
        streamItem.setImportProduct(importedProduct);
        streamItem.setOriginalProduct(originalProduct);
        Assert.assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(processor, "shouldSkip", streamItem));

        streamItem.setActionCode(BatchImportAction.ADD);
        Assert.assertEquals(Boolean.TRUE, ReflectionTestUtils.invokeMethod(processor, "shouldSkip", streamItem));
        streamItem.setActionCode(BatchImportAction.UPDATE);
        Assert.assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(processor, "shouldSkip", streamItem));

        ReflectionTestUtils.setField(processor, "systemCde", "RBT");
        Assert.assertEquals(Boolean.FALSE, ReflectionTestUtils.invokeMethod(processor, "shouldSkip", streamItem));
        originalBond.put(Field.isAlgoInd, "N");
        Assert.assertEquals(Boolean.TRUE, ReflectionTestUtils.invokeMethod(processor, "shouldSkip", streamItem));
    }
}