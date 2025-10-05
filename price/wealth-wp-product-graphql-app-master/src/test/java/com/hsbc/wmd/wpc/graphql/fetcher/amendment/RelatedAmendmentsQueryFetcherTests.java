package com.dummy.wmd.wpc.graphql.fetcher.amendment;

import com.dummy.wmd.wpc.graphql.constant.DocType;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import graphql.schema.DataFetchingEnvironment;
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

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@RunWith(MockitoJUnitRunner.class)
public class RelatedAmendmentsQueryFetcherTests {

    @InjectMocks
    private RelatedAmendmentsQueryFetcher relatedAmendmentsQueryFetcher;
    @Mock
    private MongoCollection<Document> collAmendment;
    @Mock
    private DocType docType;
    @Mock
    private MongoDatabase mongoDatabase;
    @Mock
    private DataFetchingEnvironment environment;
    @Mock
    private FindIterable findIterable;

    @Before
    public void setUp() {
        relatedAmendmentsQueryFetcher = new RelatedAmendmentsQueryFetcher(mongoDatabase, docType);
        ReflectionTestUtils.setField(relatedAmendmentsQueryFetcher, "collAmendment", collAmendment);
    }

    @Test
    public void testGet_givenDataFetchingEnvironment_returnList() throws Exception {
        Document document = new Document();
        document.put(Field._id, 1L);
        Mockito.when(environment.getSource()).thenReturn(document);
        Mockito.when(environment.getArgument("lastOnly")).thenReturn(true);
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        Mockito.when(environment.getArgument("sort")).thenReturn(map);
        Mockito.when(environment.getArgumentOrDefault("filter", Collections.EMPTY_MAP)).thenReturn(map);
        Mockito.when(collAmendment.find(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.sort(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.limit(anyInt())).thenReturn(findIterable);
        Mockito.when(findIterable.into(any(List.class))).thenReturn(new ArrayList());
        List<Document> list = relatedAmendmentsQueryFetcher.get(environment);
        Assert.assertNotNull(list);
    }
}
