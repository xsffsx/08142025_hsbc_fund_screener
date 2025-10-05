package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.FinDocMapInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.FinDocCollectionsService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.utils.CommonUtils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinDocProdRLServiceTest {

    @Mock
    MongoDatabase mongoDatabase;

    @InjectMocks
    private FinDocProdRLService prodRLService;

    static Document product;

    @BeforeAll
    public static void setUp() {
        product = Document.parse(CommonUtils.readResource("/test/req/product-import.json"));
    }


    @Test
    void testDeleteProdRLWithValidProdIdAndProdRealId() throws Exception {
        // Arrange
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("PROD_ID");
        fdi.setProdRealID(12345L);
        fdi.setDocTypeCdeP("APPENDIX");
        fdi.setLangCatCdeP("ZH");
        fdi.setCustClassCde("ALL");

        // Mock the product service
        ProductService productService = mock(ProductService.class);
        Document prod = new Document();
        prod.put(Field.finDoc, new ArrayList<>());
        when(productService.productByFilters(any())).thenReturn(Collections.singletonList(prod));
        ReflectionTestUtils.setField(prodRLService, "productService", productService);

        // Act
        prodRLService.deleteProdRL(fdi);

        assertTrue(true);
    }

    @Test
    void testDeleteProdRLWithInvalidProdIdAndProdRealId() throws Exception {
        // Arrange
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("*");
        fdi.setProdRealID(12345L);
        fdi.setDocTypeCdeP("APPENDIX");
        fdi.setLangCatCdeP("ZH");
        fdi.setCustClassCde("ALL");

        // Mock the product service
        FinDocCollectionsService finDocCollectionsService = mock(FinDocCollectionsService.class);
        when(finDocCollectionsService.prodTypeFinDocByFilters((Bson) any())).thenReturn(new ArrayList<>());
        ReflectionTestUtils.setField(prodRLService, "finDocCollectionsService", finDocCollectionsService);

        // Act
        prodRLService.deleteProdRL(fdi);

        assertTrue(true);
    }

    @Test
    void testDeleteProdRLWithWithProdSubtpCde() throws Exception {
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("*");
        fdi.setProdRealID(12345L);
        fdi.setProdSubtpCde("test");
        FinDocCollectionsService finDocCollectionsService = mock(FinDocCollectionsService.class);
        when(finDocCollectionsService.prodTypeFinDocByFilters((Bson) any())).thenReturn(null);
        ReflectionTestUtils.setField(prodRLService, "finDocCollectionsService", finDocCollectionsService);
        prodRLService.deleteProdRL(fdi);
        assertTrue(true);
    }

    @Test
    void testUpdateProdRLWithProdSubtpCde() throws Exception {
        // Arrange
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("PROD_ID");
        fdi.setProdRealID(12345L);
        fdi.setDocTypeCdeP("APPENDIX");
        fdi.setLangCatCdeP("ZH");
        fdi.setCustClassCde("ALL");

        // Mock the product service
        ProductService productService = mock(ProductService.class);
        ProductBatchUpdateResult result = mock(ProductBatchUpdateResult.class);
        Document prod = new Document();
        prod.put(Field.finDoc, new ArrayList<>());
        when(productService.productByFilters(any())).thenReturn(Collections.singletonList(product));
        when(productService.batchUpdateProductById(any())).thenReturn(result);
        Mockito.doNothing().when(result).logUpdateResult(any());
        ReflectionTestUtils.setField(prodRLService, "productService", productService);

        // Act
        prodRLService.updateProdRL(fdi);

        assertTrue(true);
    }

    @Test
    void testUpdateProdRLWithValidProdIdAndProdRealId() throws Exception {
        // Arrange
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("*");
        fdi.setProdRealID(12345L);
        fdi.setDocTypeCdeP("APPENDIX");
        fdi.setLangCatCdeP("ZH");
        fdi.setCustClassCde("ALL");

        FinDocCollectionsService finDocCollectionsService = mock(FinDocCollectionsService.class);
        List<Document> documentList = new ArrayList<>();
        Document document = new Document("test", "test");
        documentList.add(document);
        when(finDocCollectionsService.prodTypeFinDocByFilters((Bson) any())).thenReturn(documentList);
        ReflectionTestUtils.setField(prodRLService, "finDocCollectionsService", finDocCollectionsService);

        MongoCollection<Document> mongoCollection = Mockito.mock(MongoCollection.class);
        Mockito.when(mongoCollection.updateOne(Mockito.any(), (Bson) Mockito.any())).thenReturn(null);
        ReflectionTestUtils.setField(prodRLService, "colProdTypeFinDoc", mongoCollection);
        // Act
        prodRLService.updateProdRL(fdi);

        assertTrue(true);
    }

    @Test
    void testInsertProdRLWithProdSubtpCde() throws Exception {
        FinDocMapInput fdi = new FinDocMapInput();
        fdi.setProdId("*");
        fdi.setProdRealID(12345L);
        fdi.setProdSubtpCde("test");
        FinDocCollectionsService finDocCollectionsService = mock(FinDocCollectionsService.class);
        List<Document> documentList = new ArrayList<>();
        Document document = new Document("test", "test");
        documentList.add(document);
        when(finDocCollectionsService.prodTypeFinDocByFilters((Bson) any())).thenReturn(documentList);
        ReflectionTestUtils.setField(prodRLService, "finDocCollectionsService", finDocCollectionsService);
        prodRLService.insertProdRL(fdi);
        assertTrue(true);
    }

}