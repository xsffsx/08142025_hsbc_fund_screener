package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("java:S5777")
public class AmendmentCreateFetcherTest {

    @InjectMocks
    private AmendmentCreateFetcher amendmentCreateFetcher;

    private AmendmentService amendmentService;
    @Mock
    private DataFetchingEnvironment environment;

    @Mock
    private GraphQLSchema graphQLSchema;

    MockedStatic<AmendmentUtils> amendmentUtilsMockedStatic;

    @Before
    public void setUp() {
        amendmentService = Mockito.mock(AmendmentService.class);
        amendmentCreateFetcher = new AmendmentCreateFetcher(amendmentService);
        amendmentUtilsMockedStatic = Mockito.mockStatic(AmendmentUtils.class);
    }

    @After
    public void tearDown() {
        amendmentUtilsMockedStatic.close();
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsDocument() {
        List<Object[]> list = new ArrayList<>();
        list.add(new Object[]{"add", true});
        list.add(new Object[]{"update", false});
        list.add(new Object[]{"delete", false});
        for (Object[] arr : list) {
            prepareMockData((String) arr[0], (Boolean) arr[1]);
            Document document = amendmentCreateFetcher.get(environment);
            Assert.assertNotNull(document);
        }
    }

    @Test(expected = InvocationTargetException.class)
    public void testValidateForUpdate_givenDocumentAndDocumentAndDocType_throwException1() throws Exception {
        invokeMethod(false, new Document());
    }

    @Test(expected = InvocationTargetException.class)
    public void testValidateForUpdate_givenDocumentAndDocumentAndDocType_throwException2() throws Exception {
        invokeMethod(true, null);
    }

    @Test(expected = InvocationTargetException.class)
    public void testValidateForUpdate_givenDocumentAndDocumentAndDocType_throwException3() throws Exception {
        invokeMethod(false, null);
    }

    @Test(expected = InvocationTargetException.class)
    public void testValidateForUpdate_givenDocumentAndDocumentAndDocType_throwException4() throws Exception {
        Document doc = new Document();
        doc.put(Field._id, 1L);
        invokeMethod(false, doc);
    }

    @Test
    public void testValidateForUpdate_givenTwoConflictRevisionWhenCreateThenResolve() throws Exception {
        Document amendment = new Document();
        amendment.put(Field._id, 1L);
        Document docBase = new Document(Field.revision, 1L).append(Field._id, 1L);
        Document doc = new Document(Field.revision, 2L).append(Field._id, 1L);
        doc.put(Field.prodAltPrimNum, "abc");
        amendment.put(Field.docBase, docBase);
        amendment.put(Field.doc, docBase);
        amendment.put(Field.actionCde, "update");
        invokeMethod(false, amendment);

        Assert.assertTrue(amendment.get(Field.doc, Document.class).containsKey(Field.prodName));
    }

    @Test(expected = InvocationTargetException.class)
    public void testValidateForUpdate_givenTwoConflictRevisionWhenCreateThenThrowException() throws Exception {
        Document amendment = new Document();
        amendment.put(Field._id, 1L);
        Document doc = new Document(Field.revision, 2L).append(Field._id, 1L);
        doc.put(Field.prodAltPrimNum, "abc");
        amendment.put(Field.actionCde, "update");
        amendment.put(Field.doc, doc);
        invokeMethod(false, amendment);
        Assert.assertTrue(amendment.get(Field.doc, Document.class).containsKey(Field.prodName));
    }

    private void invokeMethod(Boolean isTrue, Document document) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method validateForUpdate = amendmentCreateFetcher.getClass().getDeclaredMethod("validateForUpdate", Document.class, DocType.class, GraphQLSchema.class);
        validateForUpdate.setAccessible(true);
        Mockito.lenient().when(amendmentService.hasOngoingAmendment(any(Document.class), any(DocType.class), anyLong())).thenReturn(isTrue);
        Document doc = new Document();
        doc.put(Field.prodName, "prodName");
        doc.put(Field.revision, 3L);
        Mockito.lenient().when(amendmentService.getLatestDocument(any(DocType.class), any(), any(Document.class))).thenReturn(doc);
        Document doc2 = new Document();
        doc2.put(Field._id, 1L);
        doc2.put(Field.revision, 2L);
        document = document == null ? doc2 : document;
        validateForUpdate.invoke(amendmentCreateFetcher, document, DocType.product, graphQLSchema);
    }

    private void prepareMockData(String ActionCde, Boolean isTrue) {
        Document doc = new Document();
        doc.put(Field._id, 1L);
        doc.put(Field.revision, 2L);
        Mockito.when(environment.getArgument(anyString())).thenReturn(ActionCde).
                thenReturn("product_prod_relation").thenReturn(doc).thenReturn(isTrue).thenReturn(doc);
        amendmentUtilsMockedStatic.clearInvocations();
        Mockito.doNothing().when(amendmentService).validateCreateDocument(any(DocType.class), any(Document.class));
        Mockito.doNothing().when(amendmentService).validateDocument(any(Document.class));
        Mockito.when(amendmentService.hasOngoingAmendment(any(Document.class), any(DocType.class), anyLong())).thenReturn(false);
        Mockito.when(amendmentService.createAmendment(any(Document.class))).thenReturn(new Document());
        Document docBase = new Document();
        docBase.put(Field.revision, 2L);
        Mockito.when(amendmentService.getLatestDocument(any(DocType.class), any(), any(Document.class))).thenReturn(docBase);
    }
}
