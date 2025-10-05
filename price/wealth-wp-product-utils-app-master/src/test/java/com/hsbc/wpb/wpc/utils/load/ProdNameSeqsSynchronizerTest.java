package com.dummy.wpb.wpc.utils.load;

import com.google.common.collect.ImmutableMap;
import com.dummy.wpb.wpc.utils.BsonUtils;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProdNameSeqsSynchronizerTest {
    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> mongoCollection;

    private ProdNameSeqsSynchronizer synchronizer;
    private MockedStatic<DbUtils> dbUtils;

    @Before
    public void setUp() {
        dbUtils = Mockito.mockStatic(DbUtils.class);
        Mockito.when(mockMongodb.getCollection(CollectionName.sys_parm)).thenReturn(mongoCollection);
        synchronizer = new ProdNameSeqsSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb);
    }

    @After
    public void tearDown() {
        dbUtils.close();
    }

    Map<String, Object> sequence = new ImmutableMap.Builder<String, Object>()
            .put("prodMatDt", LocalDateTime.parse("2024-06-21T00:00:00"))
            .put("seqNum", 198L)
            .put("verNum", 198L)
            .build();

    @Test
    public void testSync() {
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(2);
            dbUtils.when(() -> DbUtils.getStringObjectMap(any())).thenReturn(sequence);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(anyString(), any(SqlParameterSource.class), any(RowCallbackHandler.class));

        ArgumentCaptor<Bson> updateCaptor = ArgumentCaptor.forClass(Bson.class);
        Mockito.when(mongoCollection.updateOne(any(Bson.class), updateCaptor.capture(), any(UpdateOptions.class)))
                .thenReturn(UpdateResult.acknowledged(1, 1L, null));
        synchronizer.sync(Collections.singleton(Collections.singletonMap("ROWID", "AABIimAAAAAEpVjAAj")));

        Map<String, Object> updates = BsonUtils.toMap(updateCaptor.getValue());

        Map<String, Object> set = (Map<String, Object>) updates.get("$set");
        Assert.assertEquals(198, set.get("parmValueTexts.2024-06-21"));
    }

    @Test
    public void testDelete() {
        synchronizer.delete(Collections.emptySet());
        Assert.assertNotNull(synchronizer);
    }
}
