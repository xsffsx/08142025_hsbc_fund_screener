package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StaffLicenceCheckSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> collection;
    @Mock
    private FindIterable<Document> findIterable;
    @Mock
    private DeleteResult result;

    private StaffLicenceCheckSynchronizer staffLicenceCheckSynchronizer;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.staff_license_check)).thenReturn(collection);
        when(collection.find(any(Bson.class))).thenReturn(findIterable);
        Document document = new Document(Field._id, 1L);
        when(findIterable.first()).thenReturn(document);
        staffLicenceCheckSynchronizer = new StaffLicenceCheckSynchronizer("table",
                mockNamedParameterJdbcTemplate, mockMongodb
        );
    }

    @Test
    public void testGetMasterRecord_givenKeys_returnsMap() {
        Set<Map<String, Object>> keySet = new HashSet<>();
        keySet.add(Collections.emptyMap());
        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
                    RowCallbackHandler callbackHandler = invocation.getArgument(2);
                    callbackHandler.processRow(resultSet);
                    return null;
                })
                .when(mockNamedParameterJdbcTemplate)
                .query(Mockito.anyString(), any(MapSqlParameterSource.class), any(RowCallbackHandler.class));
        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put(Field.msgRecName, "msgRecName");
                }}
        );
        // Run the test
        try {
            staffLicenceCheckSynchronizer.sync(keySet);
        } catch (Exception e) {
            Assert.fail();
        } finally {
            dbUtils.close();
        }
    }

    @Test
    public void testDelete_givenRowIdSet_returnsNull() {
        when(collection.deleteMany(any(Bson.class))).thenReturn(result);
        Set<String> rowidSet = new HashSet<>();
        rowidSet.add("value");
        try {
            staffLicenceCheckSynchronizer.delete(rowidSet);
        } catch (Exception e) {
            Assert.fail();
        }
    }
}