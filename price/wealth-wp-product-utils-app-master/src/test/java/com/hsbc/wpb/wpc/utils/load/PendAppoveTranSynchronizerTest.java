package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PendAppoveTranSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    protected MongoCollection mockCollection;
    @Mock
    private MongoCollection<Document> pendAppoveTran;
    @Mock
    private DeleteResult deleteResult;

    private PendAppoveTranSynchronizer pendAppoveTranSynchronizer;

    private MockedStatic<DbUtils> dbUtils;
    private MockedStatic<Base64> base64;

    @Before
    public void setUp() {
        pendAppoveTranSynchronizer = new PendAppoveTranSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb);
        ReflectionTestUtils.setField(pendAppoveTranSynchronizer, "pendAppoveTran", pendAppoveTran);
        Mockito.when(pendAppoveTran.deleteOne(any(Bson.class))).thenReturn(deleteResult);
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(2);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(anyString(), any(Map.class), any(RowCallbackHandler.class));
        dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put(Field.msgRecName, "msgRecName");
                }}
        );
    }

    @After
    public void tearDown() {
        dbUtils.close();
    }

    @Test
    public void sync() {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        map.put("CTRY_REC_CDE", "CTRY_REC_CDE");
        map.put("GRP_MEMBR_REC_CDE", "GRP_MEMBR_REC_CDE");
        map.put("REC_PEND_APROVE_NUM", "REC_PEND_APROVE_NUM");
        when(mockNamedParameterJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(Arrays.asList(map));
        Set<Map<String, Object>> keySet = new HashSet<>();
        keySet.add(map);
        try {
            pendAppoveTranSynchronizer.sync(keySet);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void delete() {
        Set<String> rowidSet = new HashSet<>();
        try {
            pendAppoveTranSynchronizer.delete(rowidSet);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testLoadRecord_givenNull_returnsNull() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        base64 = Mockito.mockStatic(Base64.class);
        Base64.Decoder mockDecoder = mock(Base64.Decoder.class);
        base64.when(() -> Base64.getDecoder()).thenReturn(mockDecoder);
        Mockito.when(mockDecoder.decode(anyString())).thenThrow(RuntimeException.class);
        Method loadRecord = pendAppoveTranSynchronizer.getClass().getDeclaredMethod("loadRecord", Map.class);
        loadRecord.setAccessible(true);
        Object object = loadRecord.invoke(pendAppoveTranSynchronizer, map);
        Assert.assertNull(object);
        base64.close();
    }
}
