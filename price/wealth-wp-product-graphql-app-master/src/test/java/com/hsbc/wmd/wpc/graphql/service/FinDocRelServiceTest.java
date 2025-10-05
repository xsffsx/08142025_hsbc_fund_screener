package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.ApprovalAction;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.client.result.UpdateResult;
import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FinDocRelServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoCollection<Document> finDocUploadCollection;

    @Mock
    private MongoCollection<Document> productCollection;

    @Mock
    private MongoCollection<Document> prodTypeFinDocCollection;

    @Mock
    private FindIterable<Document> findIterable;

    @Mock
    private UpdateResult updateResult;

    @Mock
    private ProductService productService;

    @InjectMocks
    private FinDocRelService finDocRelService;

    private Document doc;

    @Before
    public void setUp() {
        doc = CommonUtils.readResourceAsDocument("/files/fin_doc_upld-doc.json");

        finDocRelService = new FinDocRelService();
        ReflectionTestUtils.setField(finDocRelService, "mongoTemplate", mongoTemplate);
        ReflectionTestUtils.setField(finDocRelService, "productService", productService);

        Mockito.when(mongoTemplate.getCollection(CollectionName.fin_doc_upld.name())).thenReturn(finDocUploadCollection);
        Mockito.when(mongoTemplate.getCollection(CollectionName.product.name())).thenReturn(productCollection);
        Mockito.when(mongoTemplate.getCollection(CollectionName.prod_type_fin_doc.name())).thenReturn(prodTypeFinDocCollection);

        Mockito.when(finDocUploadCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.sort(any())).thenReturn(findIterable);
        Mockito.when(findIterable.first()).thenReturn(doc);

        Mockito.when(finDocUploadCollection.replaceOne(any(Bson.class), any())).thenReturn(updateResult);
        Mockito.when(productCollection.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(prodTypeFinDocCollection.find(any(Bson.class))).thenReturn(findIterable);
    }

    @Test
    public void testUpdateFinDocUpload_givenFinDocUpldDocAndApproval_doesNotThrow() {
        try {
            finDocRelService.updateFinDocUpload(doc, ApprovalAction.approved, null);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testUpdateFinDocUpload_givenFinDocUpldDocAndRejection_doesNotThrow() {
        try {
            finDocRelService.updateFinDocUpload(
                    doc,
                    ApprovalAction.rejected,
                    "This financial document upload is rejected."
            );
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testUpdateOrInsertPDRL_givenFinDocUpldDoc_doesNotThrow() {
        ArrayList<Document> documents = new ArrayList<>();
        documents.add(doc);
        Mockito.when(findIterable.into(new ArrayList<>())).thenReturn(documents);
        Mockito.when(prodTypeFinDocCollection.updateOne(any(Bson.class), any(Document.class))).thenReturn(updateResult);
        try {
            finDocRelService.updateOrInsertPDRL(doc);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testUpdateOrInsertPDRL_givenNullProdCde_doesNotThrow() {
        doc.put(Field.ctryRecCde, "EE");
        Mockito.when(findIterable.into(new ArrayList<>())).thenReturn(new ArrayList<>());
        try {
            finDocRelService.updateOrInsertPDRL(doc);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }

    @Test
    public void testUpdateOrInsertPDRL_givenNonNullProdCde_doesNotThrow() {
        doc.put(Field.prodCde, "U5000");
        doc.put(Field.ctryRecCde, "ID");
        Mockito.when(productService.updateProduct(any(Document.class), any())).thenReturn(doc);
        try {
            finDocRelService.updateOrInsertPDRL(doc);
        } catch (Exception e) {
            Assert.fail("An unexpected exception occurred.");
        }
    }
}
