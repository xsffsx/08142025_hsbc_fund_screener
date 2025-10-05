package com.dummy.wmd.wpc.graphql.validator;

import com.dummy.wmd.wpc.graphql.service.ReferenceDataService;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

public class ValidatorManagerTest {


    MongoDatabase mongoDatabase = Mockito.mock(MongoDatabase.class);

    ReferenceDataService referenceDataService = Mockito.mock(ReferenceDataService.class);

    MongoCollection<Document> prodColl = Mockito.mock(MongoCollection.class);
    @Before
    public void before() {
        Document doc = CommonUtils.readResourceAsDocument("/files/metadata.json");
        List<Document> metadata = doc.getList("metadata", Document.class);
        Mockito.when(mongoDatabase.getCollection("metadata")).thenReturn(prodColl);
        FindIterable<Document> findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(prodColl.find()).thenReturn(findIterable);
        Mockito.when(findIterable.into(Mockito.any())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                List<Document> list = invocation.getArgument(0);
                list.addAll(metadata);
                return list;
            }
        });

    }

    @Test
    public void testNewInstance() {
        ValidatorManager validatorManager = new ValidatorManager(mongoDatabase, referenceDataService);
        Map<String, List<ConditionalRules>> path2RulesMap = (Map<String, List<ConditionalRules>>) ReflectionTestUtils.getField(validatorManager, "path2RulesMap");
        Assert.assertNotNull(path2RulesMap);
        Assert.assertFalse(path2RulesMap.isEmpty());
    }
}
