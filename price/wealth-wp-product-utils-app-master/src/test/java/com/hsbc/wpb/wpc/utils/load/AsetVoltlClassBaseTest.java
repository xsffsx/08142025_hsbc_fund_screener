package com.dummy.wpb.wpc.utils.load;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.DbUtils;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AsetVoltlClassBaseTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;

    private AsetVoltlClassBase asetVoltlClassBase;

    @Before
    public void setUp() {
        asetVoltlClassBase = new AsetVoltlClassBase(mockNamedParameterJdbcTemplate);
    }

    @Test
    public void testLoadAndGroupDocuments_givenTableName_returnsMap() throws SQLException {
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
        Map<Map<String, Object>, List<Document>> result = asetVoltlClassBase.loadAndGroupDocuments("table");
        dbUtils.close();
        assertNotNull(result);
    }

    @Test
    public void testToDocument_givenList_returnsDocument() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // Setup
        List<Document> list = Arrays.asList(new Document("key", "value"));
        // Run the test
        Document result = asetVoltlClassBase.toDocument(list);
        Map<String, Object> m = mapper.readValue(result.toJson(), Map.class);
        // Verify the results
        assertThat(((Map) ((List) m.get("list")).get(0)).get("key")).toString();
    }
}
