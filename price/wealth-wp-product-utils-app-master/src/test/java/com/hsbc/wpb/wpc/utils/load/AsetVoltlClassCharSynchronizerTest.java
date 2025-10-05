package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Sequence;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AsetVoltlClassCharSynchronizerTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection<Document> colAsetVoltlClass;
    @Mock
    private SequenceService sequenceService;
    @Mock
    private FindIterable findIterable;

    private AsetVoltlClassCharSynchronizer asetVoltlClassCharSynchronizer;

    @Before
    public void setUp() throws JsonProcessingException {
        when(mockMongodb.getCollection(CollectionName.aset_voltl_class_char)).thenReturn(colAsetVoltlClass);
        asetVoltlClassCharSynchronizer = new AsetVoltlClassCharSynchronizer(mockNamedParameterJdbcTemplate, mockMongodb);
        ReflectionTestUtils.setField(asetVoltlClassCharSynchronizer, "sequenceService", sequenceService);
    }

    @Test
    public void testSync_givenNullMockDocument_returnsNull() throws SQLException {
        // Setup
        Set<Map<String, Object>> keySet = new HashSet<>();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("ctryRecCde")).thenReturn("HK");
        when(resultSet.getString("grpMembrRecCde")).thenReturn("HASE");
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(RowCallbackHandler.class));
        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put("ctryRecCde", "HK");
                    put("grpMembrRecCde", "HASE");
                }}
        );
        when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(findIterable);
        Document document = new Document(Field._id, 1L);
        document.put(Field.revision, 1L);
        when(findIterable.first()).thenReturn(document);
        // Run the test
        try {
            asetVoltlClassCharSynchronizer.sync(keySet);
        } catch (Exception e) {
            Assert.fail();
        } finally {
            dbUtils.close();
        }
    }

    @Test
    public void testDelete_givenNullMockNull_returnsNull() throws SQLException {
        // Setup
        Set<String> keySet = new HashSet<>();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("ctryRecCde")).thenReturn("HK");
        when(resultSet.getString("grpMembrRecCde")).thenReturn("HASE");
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(RowCallbackHandler.class));
        MockedStatic<DbUtils> dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(resultSet)).thenReturn(
                new HashMap<String, String>() {{
                    put("ctryRecCde", "HK");
                    put("grpMembrRecCde", "HASE");
                }}
        );
        when(colAsetVoltlClass.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.first()).thenReturn(null);
        when(sequenceService.nextId(Sequence.aset_voltl_class_char_id)).thenReturn(1L);
        // Run the test
        try {
            asetVoltlClassCharSynchronizer.delete(keySet);
        } catch (Exception e) {
            Assert.fail();
        } finally {
            dbUtils.close();
        }

    }


}