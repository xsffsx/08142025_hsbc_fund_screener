package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.conversions.Bson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProdTypeFinDocSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection collection;
    @Mock
    private DeleteResult result;

    private ProdTypeFinDocSynchronizer prodTypeFinDocSynchronizer;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.prod_type_fin_doc)).thenReturn(collection);
        prodTypeFinDocSynchronizer = new ProdTypeFinDocSynchronizer(Table.PROD_TYPE_FIN_DOC, mockNamedParameterJdbcTemplate,
                mockMongodb);
    }

    @Test
    public void testSync_givenKeySet_returnsResultMap() {

        Map<String, Object> key = new HashMap<>();
        key.put("key","value");
        Set<Map<String, Object>> keySet = new HashSet<>();
        keySet.add(key);
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(2);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(MapSqlParameterSource.class), any(RowCallbackHandler.class));
        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put(Field.msgRecName, "msgRecName");
                }}
        );
        try {
            prodTypeFinDocSynchronizer.sync(keySet);
        }catch (Exception e){
            Assert.fail();
        }finally {
            dbUtils.close();
        }

    }

    @Test
    public void testDelete_givenRowIdSet_returnsNull() {
        when(collection.deleteMany(any(Bson.class))).thenReturn(result);
        try {
            prodTypeFinDocSynchronizer.delete(new HashSet<>(Arrays.asList("value")));
        }catch (Exception e){
            Assert.fail();
        }
    }
}