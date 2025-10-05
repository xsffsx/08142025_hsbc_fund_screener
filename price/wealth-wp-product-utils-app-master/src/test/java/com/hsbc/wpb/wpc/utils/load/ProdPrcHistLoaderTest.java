package com.dummy.wpb.wpc.utils.load;

import com.dummy.wpb.wpc.utils.DbUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.constant.Field;
import com.dummy.wpb.wpc.utils.constant.Table;
import com.dummy.wpb.wpc.utils.service.LockService;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.sql.ResultSet;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProdPrcHistLoaderTest {

    @Mock
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;
    @Mock
    private MongoDatabase mockMongodb;
    @Mock
    private ThreadPoolTaskExecutor mockThreadPoolTaskExecutor;
    @Mock
    private LockService mockLockService;
    @Mock
    protected MongoCollection mockCollection;
    private ProdPrcHistLoader prodPrcHistLoader;
    private ProdPrcHistLoader prodPrcHistLoader2;

    @Before
    public void setUp() throws Exception {
        when(mockMongodb.getCollection(CollectionName.prod_prc_hist)).thenReturn(mockCollection);
        prodPrcHistLoader = spy(new ProdPrcHistLoader(mockNamedParameterJdbcTemplate, Table.PROD_PRC_HIST, mockMongodb, CollectionName.prod_prc_hist, mockThreadPoolTaskExecutor, mockLockService));
        prodPrcHistLoader2 = spy(new ProdPrcHistLoader(mockNamedParameterJdbcTemplate, Table.PROD_PRC_HIST, mockMongodb, CollectionName.prod_prc_hist, mockThreadPoolTaskExecutor, mockLockService, false));

    }
    @Test
    public void testGetTableName_givenNull_returnsString(){
        assertNotNull(prodPrcHistLoader.getTableName());
    }

    @Test
    public void testLoad_givenNull_returnsNull() {
        when(mockCollection.countDocuments()).thenReturn(1L);

        doAnswer(invocation -> {
            ((Runnable) invocation.getArguments()[0]).run();
            return null;
        }).when(mockThreadPoolTaskExecutor).execute(any(Runnable.class));

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

        when(mockNamedParameterJdbcTemplate.queryForObject(anyString(), anyMap(), Mockito.<Class> any())).thenReturn(1);
        try{
            prodPrcHistLoader.load();
            prodPrcHistLoader2.load();
        }catch (Exception e){
            Assert.fail();
        }finally {
            dbUtils.close();
        }


    }

}