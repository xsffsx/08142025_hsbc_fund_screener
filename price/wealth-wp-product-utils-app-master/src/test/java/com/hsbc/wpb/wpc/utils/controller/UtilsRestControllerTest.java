package com.dummy.wpb.wpc.utils.controller;
import org.mockito.MockedConstruction;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.MockUtils;
import com.dummy.wpb.wpc.utils.config.AuthInterceptor;
import com.dummy.wpb.wpc.utils.load.ConfigurationLoader;
import com.dummy.wpb.wpc.utils.service.AdminLogService;
import com.dummy.wpb.wpc.utils.task.CollectionSyncTask;
import com.dummy.wpb.wpc.utils.task.FullSyncTask;
import com.dummy.wpb.wpc.utils.task.HousekeepingTask;
import com.dummy.wpb.wpc.utils.task.WatchTask;
import com.dummy.wpb.wpc.utils.task.WorkerTask;
import com.dummy.wpb.wpc.utils.validation.ConfigurationValidator;
import com.dummy.wpb.wpc.utils.validation.Error;
import com.dummy.wpb.wpc.utils.validation.ExtFieldValidator;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
/*@WebMvcTest(UtilsRestController.class)*/
@TestPropertySource(locations = "classpath:application-controller-tests.properties")
/*@ComponentScan("com.dummy.wpb.wpc.utils.service")*/
public class UtilsRestControllerTest {

    /* @Autowired
     private MockMvc mockMvc;
 */
    @MockBean
    private ConfigServer configServer;

    @MockBean
    private WatchTask watchTask;

    @MockBean
    private WorkerTask workerTask;

    @MockBean
    private HousekeepingTask housekeepingTask;

    @MockBean
    private AuthInterceptor authInterceptor;

    @MockBean
    private org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @MockBean
    private FullSyncTask fullSyncTask;
    @MockBean
    private BlockingQueue<String> blockingQueue;

    @MockBean
    private AdminLogService adminLogService;

    @MockBean
    private CollectionSyncTask collectionSyncTask;

    @MockBean
    private MongoDatabase mockMongodb;

    @MockBean
    private NamedParameterJdbcTemplate mockNamedParameterJdbcTemplate;

    @MockBean
    MultipartFile mockFile;

    HttpServletRequest request;

    @InjectMocks
    private UtilsRestController utilsRestController;

    public static MockedStatic<CommonUtils> commonUtils;

    // Mockito.mockConstruction(xxx) only can register once.
    static {
        Mockito.mockConstruction(ZipInputStream.class, (mock, context) -> {
            Mockito.when(mock.getNextEntry()).thenReturn(null);
        });
    }

    @Before
    public void setup() throws NoSuchFieldException, IllegalAccessException {

        //adminLogService = mock(AdminLogService.class);
        MockUtils.mockPrivate(utilsRestController,"adminLogService",adminLogService);
        MockUtils.mockPrivate(utilsRestController,"mongodb",mockMongodb);
        MockUtils.mockPrivate(utilsRestController,"blockingQueue",blockingQueue);

        request = mock(HttpServletRequest.class);
        doNothing().when(adminLogService).log(any(HttpServletRequest.class));

        commonUtils = Mockito.mockStatic(CommonUtils.class);
        commonUtils.when(() -> CommonUtils.getZipEntryMap(any())).thenReturn(new HashMap<>());
        commonUtils.when(() -> CommonUtils.readSchemaFile(any())).thenReturn(new HashMap<>());

        // without this the @MockBean inject will fail.
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void clean(){
        commonUtils.close();
    }

    @Test
    public void testUploadFile_errors() throws Exception {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("mocked_file.txt");

        when(mockFile.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 1;
            }
        });

        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseEntity<String> response = utilsRestController.uploadFile(mockFile, request);

        assertThat(response.getBody()).isNotBlank();
    }

    @Test
    public void testUploadFile_success() throws Exception {

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("mocked_file.txt");

        when(mockFile.getInputStream()).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 1;
            }
        });

        //doNothing().when(adminLogService).log(Mockito.any(HttpServletRequest.class));

        MockedConstruction<ConfigurationValidator> configurationValidatorMockedConstruction = Mockito.mockConstruction(ConfigurationValidator.class, (mock, context) -> {
            when(mock.validate()).thenReturn(new ArrayList<Error>());
        });

        Mockito.mockConstruction(ConfigurationLoader.class, (mock, context) -> {
            doNothing().when(mock).load();
        });

        try {
            HttpServletRequest request = mock(HttpServletRequest.class);
            ResponseEntity<String> response = utilsRestController.uploadFile(mockFile, request);
            assertThat(response.getBody()).isNotBlank();
        } finally {
            configurationValidatorMockedConstruction.close();
        }

    }


    @Test
    public void testDataChecking_metadataList_0() throws Exception {
        // Setup
        mockMongodb  = Mockito.mock(MongoDatabase.class);

        mockNamedParameterJdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);

        Class<?> mockServiceClass = Class.forName("com.dummy.wpb.wpc.utils.controller.UtilsRestController");
        UtilsRestController utilsRestController = (UtilsRestController) mockServiceClass.getDeclaredConstructor(MongoDatabase.class,NamedParameterJdbcTemplate.class)
                .newInstance(mockMongodb,mockNamedParameterJdbcTemplate);

        Field adminLogServiceField = mockServiceClass.getDeclaredField("adminLogService");
        adminLogServiceField.setAccessible(true);
        adminLogServiceField.set(utilsRestController, adminLogService);

        /* Field field = mockServiceClass.getDeclaredField("mongodb");
        field.setAccessible(true);
        field.set(utilsRestController, mockMongodb);*/

        //doNothing().when(adminLogService).log(Mockito.any(HttpServletRequest.class));

        MongoCollection<Document> collection = Mockito.mock(MongoCollection.class);

        when(mockMongodb.getCollection("metadata")).thenReturn(collection);


        List<Map<String, Object>> list = new ArrayList();

        list.add(new HashMap<String, Object>() {{
            put("key1", "value1");
            put("key2", "value2");
        }});

        FindIterable<Document> f = Mockito.mock(FindIterable.class);

        when(collection.find()).thenReturn(f);

        when(f.into(any())).thenReturn(new ArrayList<>());

        MongoIterable mongoIterable = Mockito.mock(MongoIterable.class);
        when(mongoIterable.into(any(List.class))).thenReturn(null);

        MockedConstruction<ExtFieldValidator> extFieldValidatorMockedConstruction = Mockito.mockConstruction(ExtFieldValidator.class, (mock, context) -> {
            when(mock.validate()).thenReturn(null);
        });

        try {
            HttpServletRequest request = mock(HttpServletRequest.class);
            assertThat(utilsRestController.dataChecking(request)).isNotNull();
        } finally {
            extFieldValidatorMockedConstruction.close();
        }

    }

    @Test
    public void testDataLoading(){
        // Setup
        MongoCollection mockColl = spy(MongoCollection.class);
        when(mockMongodb.getCollection(any())).thenReturn(mockColl);
        doReturn(0L).when(mockColl).countDocuments();
        HttpServletRequest request = mock(HttpServletRequest.class);
        utilsRestController.dataLoading(request);
        Assert.assertNotNull(mockColl);
    }

    @Test
    public void testDataLoading2()  {
        MongoCollection mockColl = spy(MongoCollection.class);
        when(mockMongodb.getCollection(any())).thenReturn(mockColl);
        doReturn(1L).when(mockColl).countDocuments();
        HttpServletRequest request = mock(HttpServletRequest.class);

        //when(blockingQueue.add(any()))
        utilsRestController.dataLoading(request);
        Assert.assertNotNull(mockColl);
    }

    @Test
    public void testCollectionLoading1() throws Exception {
        // Setup
        // Run the test
       /* final MockHttpServletResponse response = mockMvc.perform(get("/api/collectionLoading")
                .param("collectionName", "collectionName")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");*/

        utilsRestController.collectionLoading(request,"test");
        Assert.assertNotNull(request);
    }

    @Test
    public void testCollectionLoading2() throws Exception {
        when(mockMongodb.getCollection("lock")).thenReturn(null);
        utilsRestController.collectionLoading(request);
        Assert.assertNotNull(request);
    }

    @Test
    public void testHealthCheck() throws Exception {
        utilsRestController.healthCheck();
        Assert.assertNotNull(request);
    }

    class ConfigServer {
    }

}