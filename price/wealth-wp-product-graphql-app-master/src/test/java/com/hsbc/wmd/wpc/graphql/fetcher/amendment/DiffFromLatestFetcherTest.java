package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.DiffType;
import com.dummy.wmd.wpc.graphql.service.AmendmentService;
import com.dummy.wmd.wpc.graphql.utils.DocumentDiff;
import graphql.Assert;
import graphql.schema.DataFetchingEnvironment;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;

@RunWith(MockitoJUnitRunner.class)
public class DiffFromLatestFetcherTest {

    @InjectMocks
    private DiffFromLatestFetcher diffFromLatestFetcher;
    @Mock
    private AmendmentService service;
    @Mock
    private DataFetchingEnvironment environment;

    @Before
    public void setUp() {
        diffFromLatestFetcher = new DiffFromLatestFetcher(service);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnsList() {
        Document document = new Document();
        document.put(Field.docBase, new Document());
        document.put(Field.docType, "product");
        document.put(Field.docId, "docId");
        Mockito.when(environment.getSource()).thenReturn(document);
        Mockito.when(service.getLatestDocument(any(DocType.class), any(Object.class))).thenReturn(document);
        MockedStatic<DocumentDiff> diffMockedStatic = Mockito.mockStatic(DocumentDiff.class);
        diffMockedStatic.when(() -> DocumentDiff.diff(anyMap(), anyMap())).thenReturn(new ArrayList<>());
        List<DiffType> list = diffFromLatestFetcher.get(environment);
        Assert.assertNotNull(list);
        diffMockedStatic.close();
    }
}
