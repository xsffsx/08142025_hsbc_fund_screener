package com.dummy.wpb.wpc.utils.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CreateIndexesSyncTaskTest {

    private CreateIndexesSyncTask createIndexesSyncTask;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    MongoTemplate mongoTemplate;

    MongoCollection<Document> configColl = Mockito.mock(MongoCollection.class);

    protected static final String CONFIG_COLLECTION_NAME = CollectionName.configuration;


    @Mock
    private FindIterable findIterable;


    public List<Document> configIndexList;

    public List mongoIndexList;

    MongoCollection<Document> listIndexColl = Mockito.mock(MongoCollection.class);

    MongoCollection<Document> mockMongoColl = Mockito.mock(MongoCollection.class);

    @Before
    public void setUp() throws Exception {
        createIndexesSyncTask = new CreateIndexesSyncTask();
        ReflectionTestUtils.setField(createIndexesSyncTask, "mongoDatabase", mongoDatabase);
        ReflectionTestUtils.setField(createIndexesSyncTask, "mongoTemplate", mongoTemplate);

        List<Document> configYamlList = readResource("/config_index.yml", new TypeReference<List<Document>>() {
        });

        List<Map<String, Object>> readValue = readValue(configYamlList.get(0), "indexes");

        assert readValue != null;
        configIndexList = new ArrayList<>();
        Document docProduct = new Document();
        List<Document> docItems = readValue
                .stream()
                .map(item -> {
                    Document docItem = new Document();
                    docItem.putAll(item);
                    try {
                        if(item.containsKey("partialFilterExpression")){
                            ObjectMapper mapper = new ObjectMapper();
                            Document partial = Document.parse(mapper.writeValueAsString(item.get("partialFilterExpression")));
                            docItem.put("partialFilterExpression", partial);
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return docItem;
                })
                .collect(Collectors.toList());
        docProduct.put("indexes", docItems);
        docProduct.put("collection", "product");
        configIndexList.add(docProduct);

        mongoIndexList = BsonArray.parse(CommonUtils.readResource("/mongodb_index.json"))
                .getValues()
                .stream()
                .map(item -> Document.parse(item.asDocument().toJson()))
                .collect(Collectors.toList());
    }

    @Test
    public void testGetTaskName_noArgs_returnsString() {
        String taskName = createIndexesSyncTask.getTaskName();
        Assert.assertEquals("CreateIndexesSyncTask", taskName);
    }

    @Test
    public void testSetLockToken_givenString_doesNotThrow() {
        createIndexesSyncTask.setLockToken("lockToken");
        Assert.assertEquals("lockToken", createIndexesSyncTask.getLockToken());
    }

    @Test
    @SuppressWarnings(value = "unchecked")
    public void testRun_noArgs_doesNotThrow1() {
        Mockito.when(mongoDatabase.getCollection(CONFIG_COLLECTION_NAME)).thenReturn(configColl);
        Mockito.when(configColl.find(any(Bson.class))).thenReturn(findIterable);

        Document configDoc = new Document();
        configDoc.put("_id", "ALL/mongodb-collection-config");
        configDoc.put("forEntity", "ALL");

        Mockito.when(findIterable.first()).thenReturn(configDoc);
        configDoc.put("config", configIndexList);

        ListIndexesIterable<Document> listIterableMocked = (ListIndexesIterable<Document>) Mockito.mock(ListIndexesIterable.class);
        Mockito.when(mongoTemplate.getCollection(Mockito.anyString())).thenReturn(listIndexColl);
        Mockito.when(listIndexColl.listIndexes()).thenReturn(listIterableMocked);

        Iterator<Document> mongoIterator = mongoIndexList.iterator();
        MongoCursor<Document> mongoCursor = new MongoCursor<Document>() {
            @Override
            public void close() {

            }

            @Override
            public boolean hasNext() {
                return mongoIterator.hasNext();
            }

            @Override
            public Document next() {
                return mongoIterator.next();
            }

            @Override
            public int available() {
                return 0;
            }

            @Override
            public Document tryNext() {
                return null;
            }

            @Override
            public ServerCursor getServerCursor() {
                return null;
            }

            @Override
            public ServerAddress getServerAddress() {
                return null;
            }
        };
        doReturn(mongoCursor).when(listIterableMocked).iterator();

        createIndexesSyncTask.run();

        Mockito.verify(mongoTemplate, times(26))
                .getCollection(anyString());

        Mockito.verify(listIndexColl, times(13))
                .createIndex((Bson) any(), (IndexOptions) any());

        Mockito.verify(listIndexColl, times(12))
                .dropIndex(anyString());
    }

    private static <T> T readResource(String classpath, TypeReference<T> typeReference) {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        URL yamlUrl = CreateIndexesSyncTaskTest.class.getResource(classpath);
        if (null == yamlUrl) {
            throw new IllegalArgumentException("Resource not found: " + classpath);
        }
        try {
            return yamlMapper.readValue(yamlUrl, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Error reading yaml resource: " + classpath, e);
        }
    }

    private static <T> T readValue(Map<?, ?> map, String jsonPath) {
        try {
            return JsonPath.read(map, jsonPath);
        } catch (JsonPathException e) {
            return null;
        }
    }
}
