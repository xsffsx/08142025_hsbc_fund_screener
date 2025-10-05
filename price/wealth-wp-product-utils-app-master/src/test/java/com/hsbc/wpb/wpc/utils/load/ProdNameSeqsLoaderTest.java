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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class ProdNameSeqsLoaderTest {
    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> mongoCollection;

    private ProdNameSeqsLoader loader;
    private MockedStatic<DbUtils> dbUtils;

    private List<Map<String, Object>> sequences = new ArrayList<>();

    @Before
    public void setUp() {
        dbUtils = Mockito.mockStatic(DbUtils.class);
        Mockito.when(mockMongodb.getCollection(CollectionName.sys_parm)).thenReturn(mongoCollection);
        loader = new ProdNameSeqsLoader(mockNamedParameterJdbcTemplate, mockMongodb);

        sequences.add(new ImmutableMap.Builder<String, Object>()
                .put("prodMatDt", LocalDateTime.parse("2024-06-21T00:00:00"))
                .put("seqNum", 198L)
                .put("verNum", 198L)
                .build());
        sequences.add(new ImmutableMap.Builder<String, Object>()
                .put("prodMatDt", LocalDateTime.parse("2024-08-20T00:00:00"))
                .put("seqNum", 268L)
                .put("verNum", 268L)
                .build());
    }

    @After
    public void tearDown() {
        dbUtils.close();
    }

    @Test
    public void testSync() {
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            for (Map<String, Object> sequence : sequences) {
                dbUtils.when(() -> DbUtils.getStringObjectMap(any())).thenReturn(sequence);
                callbackHandler.processRow(resultSet);
            }
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(anyString(), any(RowCallbackHandler.class));

        ArgumentCaptor<Bson> updateCaptor = ArgumentCaptor.forClass(Bson.class);
        Mockito.when(mongoCollection.updateOne(any(Bson.class), updateCaptor.capture(), any(UpdateOptions.class)))
                .thenReturn(UpdateResult.acknowledged(1, 1L, null));
        loader.load();
        Map<String, Object> updates = BsonUtils.toMap(updateCaptor.getValue());

        Map<String, Object> set = (Map<String, Object>) updates.get("$set");
        Map<String, Object> parmValueTexts = (Map<String, Object>) set.get("parmValueTexts");

        Assert.assertEquals(sequences.size(), parmValueTexts.size());
        Assert.assertEquals(198, parmValueTexts.get("2024-06-21"));
        Assert.assertEquals(268, parmValueTexts.get("2024-08-20"));
    }
}
