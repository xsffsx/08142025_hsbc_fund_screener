package com.dummy.wmd.wpc.graphql;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class GraphQLSchemaGeneratorTest {
    @Mock
    MongoCollection<Document> collection;
    @Mock
    private MongoDatabase mockMongodb;

    private GraphQLSchemaGenerator graphQLSchemaGeneratorUnderTest;

    @Before
    public void setUp() throws Exception {
        Document doc = CommonUtils.readResourceAsDocument("/files/metadata.json");
        List<Document> metadata = doc.getList("metadata",Document.class).subList(0,3);
        Document document = new Document("attrName","test");
        document.put("jsonPath","revision.test");
        document.put("parent","revision");
        document.put("graphQLType","ProductInput");
        metadata.add(document);
        Document document2 = new Document("attrName","test2");
        document.put("jsonPath","parent.test2");
        document.put("parent","parent");
        document.put("graphQLType","String");
        metadata.add(document2);
        Mockito.when(mockMongodb.getCollection(CollectionName.metadata.toString())).thenReturn(collection);
        FindIterable findIterable = Mockito.mock(FindIterable.class);
        Mockito.when(collection.find()).thenReturn(findIterable);
        Mockito.when(findIterable.projection(any(Bson.class))).thenReturn(findIterable);
        Mockito.when(findIterable.into(new ArrayList<>())).thenReturn(metadata);
        graphQLSchemaGeneratorUnderTest = new GraphQLSchemaGenerator(mockMongodb);
    }

    @Test
    public void testGenerateProductTypeSchema_givenNull_returnsString() {
        String schema =graphQLSchemaGeneratorUnderTest.generateProductTypeSchema();
        assertNotNull(schema);
    }

    @Test
    public void testGenerateProductInputSchema_givenNull_returnsString() {
        String schema = graphQLSchemaGeneratorUnderTest.generateProductInputSchema();
        assertNotNull(schema);
    }
    @Test
    public void testUnwrappedType_givenString_returnsString() throws Exception {
        Method method = graphQLSchemaGeneratorUnderTest.getClass().getDeclaredMethod("unwrappedType", String.class);
        method.setAccessible(true);
        String collectionName = (String) method.invoke(graphQLSchemaGeneratorUnderTest, "[ROOT]");
        assertNotNull(collectionName);
    }
}
