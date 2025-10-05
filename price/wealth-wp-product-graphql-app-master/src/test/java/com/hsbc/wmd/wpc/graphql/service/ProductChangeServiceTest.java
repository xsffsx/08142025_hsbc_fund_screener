package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ActionCde;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.listener.NotificationManager;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.dummy.wmd.wpc.graphql.validator.ProductValidator;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProductChangeServiceTest {

    ProductChangeService productChangeService;

    ProductService productService = Mockito.mock(ProductService.class);
    ProductValidator productValidator = Mockito.mock(ProductValidator.class);
    NotificationManager notificationManager = Mockito.mock(NotificationManager.class);


    @Before
    public void setUp() throws Exception {
        productChangeService = new ProductChangeService(productService, productValidator, notificationManager);
        ReflectionTestUtils.setField(productChangeService, "priceCompareService", Mockito.mock(PriceCompareService.class));
    }

    @Test
    public void testValidate_givenDelete_doNotValidate() {
        Mockito.when(productValidator.validate(Mockito.any(Map.class))).thenReturn(Collections.singletonList(new Error("prodName", "prodName@Required", "Required")));
        Document amendment = new Document();
        amendment.put(Field.emplyNum, "test");
        amendment.put(Field.recStatCde, "pending");
        amendment.put(Field.docType, "product");
        amendment.put(Field.actionCde, ActionCde.delete.name());
        assertTrue(productChangeService.validate(amendment).isEmpty());
    }

    @Test
    public void testValidate_givenUpdate_returnException() {
        List<Error> errors = Collections.singletonList(new Error("prodName", "prodName@Required", "Required"));
        Mockito.when(productValidator.validate(Mockito.any(Map.class))).thenReturn(errors);
        Document amendment = new Document();
        amendment.put(Field.emplyNum, "test");
        amendment.put(Field.doc, new Document());
        amendment.put(Field.recStatCde, "pending");
        amendment.put(Field.docType, "product");
        amendment.put(Field.actionCde, ActionCde.update.name());
        assertEquals(errors, productChangeService.validate(amendment));
    }

    @Test
    public void testUpdate() {
        Document oldProd = Document.parse(CommonUtils.readResource("/files/BOND-286859053.json"));
        Document newProd = Document.parse(CommonUtils.readResource("/files/BOND-286859053.json"));

        DocumentContext context = JsonPath.parse(newProd);

        Date now = new Date();
        context.add("$.altId", new Document()
                .append(Field.rowid, UUID.randomUUID().toString())
                .append(Field.prodCdeAltClassCde, "G")
                .append(Field.prodTypeCde, "BOND")
                .append(Field.prodAltNum, "AB2635826251")
                .append(Field.recCreatDtTm, now)
                .append(Field.recUpdtDtTm, now));
        context.set("$.altId[?(@.prodCdeAltClassCde=='I')].prodAltNum", "CD639826251");
        context.set("$.altId[?(@.prodCdeAltClassCde=='I')].recUpdtDtTm", now);
        newProd.put(Field.recOnlnUpdtDtTm, now);

        Mockito.when(productService.findFirst(Mockito.any())).thenReturn(oldProd);

        productChangeService.update(newProd);
        ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
        Mockito.verify(productService, Mockito.times(1)).updateProduct(captor.capture(), Mockito.anyList());
        Document actualProduct = captor.getValue();

        Document actualAltIdI = ((List<Document>)JsonPath.read(actualProduct, "$.altId[?(@.prodCdeAltClassCde=='I')]")).get(0);
        Document actualAltIdG = ((List<Document>)JsonPath.read(actualProduct, "$.altId[?(@.prodCdeAltClassCde=='G')]")).get(0);

        assertTrue(now.before(actualAltIdI.getDate(Field.recUpdtDtTm)));
        assertTrue(now.before(actualAltIdG.getDate(Field.recCreatDtTm)));
        assertTrue(now.before(actualAltIdG.getDate(Field.recUpdtDtTm)));
        assertTrue(now.before(actualProduct.getDate(Field.recOnlnUpdtDtTm)));
    }
}
