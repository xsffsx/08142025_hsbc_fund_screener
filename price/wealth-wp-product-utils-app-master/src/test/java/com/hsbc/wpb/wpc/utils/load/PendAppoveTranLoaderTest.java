package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PendAppoveTranLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    protected MongoCollection mockCollection;

    private PendAppoveTranLoader pendAppoveTranLoader;

    private MockedStatic<DbUtils> dbUtils;
    private MockedStatic<Base64> base64;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.pend_appove_tran)).thenReturn(mockCollection);
        pendAppoveTranLoader = new PendAppoveTranLoader(mockNamedParameterJdbcTemplate, mockMongodb);
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
    public void testLoad_givenNull_returnsNull() {
        when(mockCollection.countDocuments()).thenReturn(1L);
        String sql = String.format("select CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM, count(*) as parts from PEND_APROVE_TRAN group by CTRY_REC_CDE, GRP_MEMBR_REC_CDE, REC_PEND_APROVE_NUM");
        Map<String, Object> map = new HashMap<>();
        map.put("key", "value");
        when(mockNamedParameterJdbcTemplate.queryForList(sql, Collections.emptyMap())).thenReturn(Arrays.asList(map));
        try{
            pendAppoveTranLoader.load();
        }catch (Exception e){
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
        Method loadRecord = pendAppoveTranLoader.getClass().getDeclaredMethod("loadRecord", Map.class);
        loadRecord.setAccessible(true);
        Object object = loadRecord.invoke(pendAppoveTranLoader, map);
        Assert.assertNull(object);
        base64.close();
    }

}
