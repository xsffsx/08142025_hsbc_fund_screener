package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.service.SequenceService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import static org.apache.http.client.methods.RequestBuilder.put;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AsetVoltlClassCorlLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private MongoCollection colAsetVoltlClass;
    @Mock
    private SequenceService sequenceService;

    private AsetVoltlClassCorlLoader asetVoltlClassCorlLoader;


    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.aset_voltl_class_corl)).thenReturn(colAsetVoltlClass);
        asetVoltlClassCorlLoader = new AsetVoltlClassCorlLoader(mockNamedParameterJdbcTemplate,mockMongodb);
        ReflectionTestUtils.setField(asetVoltlClassCorlLoader, "sequenceService", sequenceService);

    }

    @Test
    public void testLoad_givenNull_returnsNull() {

        ResultSet resultSet = mock(ResultSet.class);
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
        when(colAsetVoltlClass.countDocuments()).thenReturn(1L);
        try{
            asetVoltlClassCorlLoader.load();
        }catch (Exception e){
            Assert.fail();
        }finally {
            dbUtils.close();
        }
    }

}