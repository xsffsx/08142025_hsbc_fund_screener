package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.model.FileIngressRecord;
import com.dummy.wmd.wpc.graphql.model.FileIngressStatus;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RunWith(MockitoJUnitRunner.class)
public class DataProcessingServiceTest {

    @Mock
    private MongoDatabase mockMongoDatabase;
    @Mock
    private MongoTemplate mockMongoTemplate;
    @Mock
    private MongoCollection<Document> collFileUpload;
    @InjectMocks
    private DataProcessingService dataProcessingService;

    @Before
    public void setUp() {
        dataProcessingService = new DataProcessingService(mockMongoDatabase, mockMongoTemplate);
        ReflectionTestUtils.setField(dataProcessingService, "collFileUpload", collFileUpload);
    }

    @Test
    public void testQueryFileIngressStatus_givenArgs_returnPageResult() {
        // Setup
        Map<String, Object> sortMap = new HashMap<>();
        Long id = 1L;
        OffsetDateTime fromTime = OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), ZoneOffset.UTC);
        OffsetDateTime toTime = OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), ZoneOffset.UTC);
        String filename = "filename";
        String md5 = "md5";
        List<String> statusIn = Arrays.asList("value");
        Integer skip = 0;
        Integer limit = 0;

        Criteria criteria = new Criteria();
        criteria.and("id").is(id);
        criteria.andOperator(Criteria.where("crtDtTm").gte(fromTime), Criteria.where("crtDtTm").lte(toTime));
        criteria.and("fileName").regex(filename, "i");
        criteria.and("md5").is(md5);
        criteria.and("stat").in(statusIn);
        Query q = new Query(criteria);
        when(mockMongoTemplate.count(q, FileIngressStatus.class)).thenReturn(0L);
        // Configure MongoTemplate.find(...).
        FileIngressStatus fileIngressStatus = new FileIngressStatus();
        fileIngressStatus.setId(id);
        List<FileIngressStatus> fileIngressStatuses = Arrays.asList(fileIngressStatus);

        when(mockMongoTemplate.find(q, FileIngressStatus.class)).thenReturn(fileIngressStatuses);
        when(collFileUpload.countDocuments(any(Bson.class))).thenReturn(1L);
        // Run the test
        PageResult<FileIngressStatus> result = dataProcessingService.queryFileIngressStatus(id,
                fromTime, toTime, filename, md5, statusIn, sortMap, skip, limit);
        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testQueryFileIngressStatus_givenArgs_withoutToTime_returnPageResult() {
        // Setup
        Map<String, Object> sortMap = new HashMap<>();
        Long id = 1L;
        OffsetDateTime fromTime = OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), ZoneOffset.UTC);
        OffsetDateTime toTime = null;
        String filename = "filename";
        String md5 = "md5";
        List<String> statusIn = Arrays.asList("value");
        Integer skip = 0;
        Integer limit = 0;
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);
        criteria.and("crtDtTm").gte(fromTime);
        criteria.and("fileName").regex(filename, "i");
        criteria.and("md5").is(md5);
        criteria.and("stat").in(statusIn);
        when(mockMongoTemplate.count(new Query(criteria), FileIngressStatus.class)).thenReturn(0L);
        Query q = new Query(criteria);
        when(mockMongoTemplate.find(q, FileIngressStatus.class)).thenReturn(Collections.emptyList());

        // Run the test
        PageResult<FileIngressStatus> result = dataProcessingService.queryFileIngressStatus(id,
                fromTime, toTime, filename, md5, statusIn, sortMap, skip, limit);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testQueryFileIngressStatus_givenArgs_withoutFromTime_returnPageResult() {
        // Setup
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("key", 1);
        Long id = 1L;
        OffsetDateTime fromTime = null;
        OffsetDateTime toTime = OffsetDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0), ZoneOffset.UTC);

        String filename = "filename";
        String md5 = "md5";
        List<String> statusIn = Arrays.asList("value");
        Integer skip = 0;
        Integer limit = 0;
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);
        criteria.and("crtDtTm").lte(toTime);
        criteria.and("fileName").regex(filename, "i");
        criteria.and("md5").is(md5);
        criteria.and("stat").in(statusIn);
        Query q = new Query(criteria);
        sortMap.forEach((k, v) -> {
            q.with(Sort.by(Integer.parseInt(v.toString()) > 0 ? Sort.Direction.ASC : Sort.Direction.DESC, k));
        });

        when(mockMongoTemplate.find(q, FileIngressStatus.class)).thenReturn(Collections.emptyList());
        // Run the test
        PageResult<FileIngressStatus> result = dataProcessingService.queryFileIngressStatus(id,
                fromTime, toTime, filename, md5, statusIn, sortMap, skip, limit);

        // Verify the results
        assertNotNull(result);
    }

    @Test
    public void testQueryFileIngressRecord_givenArgs_returnsPageResult() {
        // Setup
        Map<String, Object> sortMap = new HashMap<>();
        sortMap.put("key", 1);
        Long id = 1L;
        List<String> statusIn = Arrays.asList("value");
        Integer skip = 0;
        Integer limit = 0;
        Set<String> selectFields = new HashSet<>(Arrays.asList("value", "value2"));
        Criteria criteria = where("fisid").is(id);
        criteria.and("stat").in(statusIn);
        Query q = new Query(criteria);
        q.with(Sort.by(Sort.Direction.ASC,"key"));
        q.fields().exclude("rawData");
        q.fields().exclude("payload");
        // Configure MongoTemplate.find(...).
        final FileIngressRecord fileIngressRecord = new FileIngressRecord();
        fileIngressRecord.setId(id);
        final List<FileIngressRecord> fileIngressRecords = Arrays.asList(fileIngressRecord);
        when(mockMongoTemplate.find(q, FileIngressRecord.class)).thenReturn(fileIngressRecords);
        // Run the test
        PageResult<FileIngressRecord> result = dataProcessingService.queryFileIngressRecord(id,
                statusIn, sortMap, skip, limit, selectFields);
        // Verify the results
        assertNotNull(result);
    }

}
