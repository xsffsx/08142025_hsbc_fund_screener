package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.constant.RecStatCde;
import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.dummy.wmd.wpc.graphql.utils.RevisionUtils;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import graphql.schema.GraphQLSchema;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class AmendmentUpdateFetcherTest {

    @InjectMocks
    private AmendmentUpdateFetcher amendmentUpdateFetcher;
    @Mock
    private AmendmentService amendmentService;

    private DataFetchingEnvironment environment;
    @Mock
    private GraphQLSchema graphQLSchema;
    MockedStatic<AmendmentUtils> amendmentUtilsMockedStatic;

    Map<String, Object> arguments;

    Document product = Document.parse(CommonUtils.readResource("/files/ELI-50504681.json"));

    Document amendment = Document.parse(CommonUtils.readResource("/files/amendment-product-50504681.json"));

    @Before
    public void setUp() {
        amendmentUpdateFetcher = new AmendmentUpdateFetcher(amendmentService);
        amendmentUtilsMockedStatic = Mockito.mockStatic(AmendmentUtils.class);
    }

    @After
    public void tearDown() {
        amendmentUtilsMockedStatic.close();
    }

    @Test
    public void testUpdateAmendment_returnSuccessResult() {
        Document docChanged = new Document(product).append(Field.prodStatCde, "A");
        DataFetchingEnvironment environment = environment(docChanged);

        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        Mockito.when(amendmentService.getLatestDocument(any(), any(), any())).thenReturn(product);
        Mockito.when(amendmentService.updateAmendment(any())).thenReturn(new Document(amendment).append(Field.doc, docChanged));
        amendmentUpdateFetcher.get(environment);

        Mockito.verify(amendmentService, Mockito.times(1)).requestApproval(anyLong(), anyString());
    }

    @Test(expected = productErrorException.class)
    public void testUpdateAmendment_givenConflictProductThenThrowException() {
        Document docChanged = new Document(product).append(Field.prodStatCde, "A");

        DataFetchingEnvironment environment = environment(docChanged);
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        Document docLatest = new Document(product).append(Field.prodStatCde, "C");
        RevisionUtils.increaseRevisionNumber(docLatest);
        Mockito.when(amendmentService.getLatestDocument(any(), any(), any())).thenReturn(docLatest);

        amendmentUpdateFetcher.get(environment);
    }

    @Test(expected = productErrorException.class)
    public void testUpdateAmendment_givenDifferentRevisionThenThrowException() {
        Document amendment = Document.parse(CommonUtils.readResource("/files/amendment-fin_doc_hist.json"))
                .append(Field.recStatCde, RecStatCde.draft.name());
        Document docChanged = amendment.get(Field.doc, Document.class);
        amendment.put(Field.doc, docChanged);
        DataFetchingEnvironment environment = environment(docChanged);
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        Document docLatest = new Document(docChanged);
        RevisionUtils.increaseRevisionNumber(docLatest);
        Mockito.when(amendmentService.getLatestDocument(any(), any(), any())).thenReturn(docLatest);

        amendmentUpdateFetcher.get(environment);
    }

    @Test(expected = productErrorException.class)
    public void testUpdateAmendment_givenNotExistedRecordThenThrowException() {
        Document amendment = Document.parse(CommonUtils.readResource("/files/amendment-fin_doc_hist.json"));
        Document docChanged = amendment.get(Field.doc, Document.class);
        amendment.put(Field.doc, docChanged);
        DataFetchingEnvironment environment = environment(docChanged);
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(null);
        amendmentUpdateFetcher.get(environment);
    }

    @Test(expected = productErrorException.class)
    public void testUpdateAmendment_givenApprovedRecordThenThrowException() {
        Document amendment = Document.parse(CommonUtils.readResource("/files/amendment-fin_doc_hist.json"));
        Document docChanged = amendment.get(Field.doc, Document.class);
        DataFetchingEnvironment environment = environment(docChanged);
        Mockito.when(amendmentService.getAmendmentById(environment.getArgument("amendmentId"))).thenReturn(amendment);
        amendmentUpdateFetcher.get(environment);
    }

    private DataFetchingEnvironment environment(Document docChanged) {
        Document arguments = new Document()
                .append("amendmentId", 1L)
                .append("docChanged", docChanged)
                .append("submit", true);

        return new DataFetchingEnvironmentImpl.Builder()
                .arguments(arguments)
                .build();
    }
}
