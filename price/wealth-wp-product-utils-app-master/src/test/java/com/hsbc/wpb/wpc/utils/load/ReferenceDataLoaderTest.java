package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.service.SequenceService;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceDataLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;

    private ReferenceDataLoader referenceDataLoaderUnderTest;

    private MockedStatic<DbUtils> dbUtils;

    @Before
    public void setUp() throws Exception {
        Class<?> mockServiceClass = Class.forName("com.dummy.wpb.wpc.utils.load.ReferenceDataLoader");
        referenceDataLoaderUnderTest = (ReferenceDataLoader) mockServiceClass.getDeclaredConstructor(NamedParameterJdbcTemplate.class,MongoDatabase.class)
                .newInstance(mockNamedParameterJdbcTemplate,mockMongodb);

        MongoCollection colReferenceData =  mock(MongoCollection.class);
        java.lang.reflect.Field colReferenceDataField = mockServiceClass.getDeclaredField("colReferenceData");
        colReferenceDataField.setAccessible(true);
        colReferenceDataField.set(referenceDataLoaderUnderTest, colReferenceData);

        SequenceService sequenceService = mock(SequenceService.class);
        java.lang.reflect.Field sequenceServiceField = mockServiceClass.getDeclaredField("sequenceService");
        sequenceServiceField.setAccessible(true);
        sequenceServiceField.set(referenceDataLoaderUnderTest, sequenceService);
        when(sequenceService.nextId(any())).thenReturn(1L);
        doReturn(null).when(colReferenceData).insertMany(any());

        dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(any(ResultSet.class))).thenReturn(new HashMap() {{
            put("a", "a");
        }});
    }

    @After
    public void clean () {
        dbUtils.close();
    }

    @Test
    public void testLoad() throws SQLException {
        when(referenceDataLoaderUnderTest.colReferenceData.countDocuments()).thenReturn(1L);
        doNothing().when(referenceDataLoaderUnderTest.colReferenceData).drop();

        ResultSet resultSet = mock(ResultSet.class);
        //Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Mockito.when(resultSet.getString(Field.CTRY_REC_CDE)).thenReturn("CN");
        Mockito.when(resultSet.getString(Field.GRP_MEMBR_REC_CDE)).thenReturn("dummy");
        Mockito.when(resultSet.getString(Field.CDV_TYPE_CDE)).thenReturn("FH");
        Mockito.when(resultSet.getString(Field.CDV_CDE)).thenReturn("UT");

        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(RowCallbackHandler.class));

        try{
            referenceDataLoaderUnderTest.load();
        }catch (Exception e){
            Assert.fail();
        }
    }
}