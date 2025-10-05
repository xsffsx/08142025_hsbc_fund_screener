package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.DbUtils;
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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProdTypeFinDocLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;

    private ProdTypeFinDocLoader prodTypeFinDocLoaderUnderTest;

    private MongoCollection collection;

    MockedStatic<DbUtils> dbUtils;

    @Before
    public void setUp() throws Exception {
        collection = mock(MongoCollection.class);
        when(mockMongodb.getCollection(any())).thenReturn(collection);
        prodTypeFinDocLoaderUnderTest = new ProdTypeFinDocLoader(mockNamedParameterJdbcTemplate, mockMongodb);

        dbUtils = Mockito.mockStatic(DbUtils.class);
        dbUtils.when(() -> DbUtils.getStringObjectMap(any(ResultSet.class))).thenReturn(new HashMap() {{
            put("a", "a");
        }});

        ResultSet resultSet = mock(ResultSet.class);
        Mockito.doAnswer(invocation -> {
            RowCallbackHandler callbackHandler = invocation.getArgument(1);
            callbackHandler.processRow(resultSet);
            return null;
        }).when(mockNamedParameterJdbcTemplate).query(Mockito.anyString(), any(RowCallbackHandler.class));
    }

    @After
    public void clean(){
        dbUtils.close();
    }

    @Test
    public void testLoad1() {
        prodTypeFinDocLoaderUnderTest.load();
        Assert.assertNotNull(collection);
    }

    @Test
    public void testLoad2() {
        when(collection.countDocuments()).thenReturn(0L);
        prodTypeFinDocLoaderUnderTest.load();
        Assert.assertNotNull(collection);
    }
}
