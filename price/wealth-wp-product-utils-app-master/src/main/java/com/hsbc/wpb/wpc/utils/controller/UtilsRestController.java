package com.dummy.wpb.wpc.utils.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.html.HtmlEscapers;
import com.dummy.wpb.wpc.utils.CommonUtils;
import com.dummy.wpb.wpc.utils.constant.CollectionName;
import com.dummy.wpb.wpc.utils.load.ConfigurationLoader;
import com.dummy.wpb.wpc.utils.service.AdminLogService;
import com.dummy.wpb.wpc.utils.task.CreateIndexesSyncTask;
import com.dummy.wpb.wpc.utils.task.FullSyncTask;
import com.dummy.wpb.wpc.utils.task.CollectionSyncTask;
import com.dummy.wpb.wpc.utils.validation.ConfigurationValidator;
import com.dummy.wpb.wpc.utils.validation.Error;
import com.dummy.wpb.wpc.utils.validation.ExtFieldValidator;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipInputStream;

import static com.mongodb.client.model.Filters.eq;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class UtilsRestController {
    @Autowired
    private FullSyncTask fullSyncTask;
    @Autowired
    private BlockingQueue<String> blockingQueue;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private CollectionSyncTask collectionSyncTask;

    @Value("#{${product.load.disable}}")
    private boolean disableDataLoad;

    private final MongoDatabase mongodb;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

    @Autowired
    private CreateIndexesSyncTask createIndexesSyncTask;

    public UtilsRestController(MongoDatabase mongodb, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.mongodb = mongodb;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        try (InputStream inputStream = file.getInputStream()) {
            adminLogService.log(request);
            log.info("upload config package: {}", file.getOriginalFilename());
            ZipInputStream zis = new ZipInputStream(inputStream);
            Map<String, String> zipEntryMap = CommonUtils.getZipEntryMap(zis);
            CommonUtils.readSchemaFile(zipEntryMap);

            // validate input files
            ConfigurationValidator validator = new ConfigurationValidator(zipEntryMap);
            List<Error> errors = validator.validate();

            // load
            if (!errors.isEmpty()) {
                log.info("upload config package validation errors: {}", errors);
                return new ResponseEntity<>(yamlMapper.writeValueAsString(errors), HttpStatus.PARTIAL_CONTENT);
            } else {
                log.info("upload config package loading ...");
                ConfigurationLoader loader = new ConfigurationLoader(this.mongodb, zipEntryMap);
                loader.load();
                String message = HtmlEscapers.htmlEscaper().escape("Configuration uploaded: " + file.getOriginalFilename()
                        + "\n\n" + zipEntryMap.get("config-package-info.yaml"));
                log.info("{}", message);
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping(value = "/dataChecking")
    public ResponseEntity<String> dataChecking(HttpServletRequest request) {
        try {
            adminLogService.log(request);
            log.info("product data checking against metadata ...");
            MongoCollection<Document> collection = mongodb.getCollection(CollectionName.metadata);
            List<Map<String, Object>> metadataList = new ArrayList<>();
            collection.find().into(metadataList);
            if (metadataList.isEmpty()) {
                log.info("product metadata not found");
                return new ResponseEntity<>("Please perform STEP 1 to upload configuration package first", HttpStatus.PARTIAL_CONTENT);
            } else {
                ExtFieldValidator validator = new ExtFieldValidator(metadataList, namedParameterJdbcTemplate);
                List<Error> errors;
                errors = validator.validate();
                if (!errors.isEmpty()) {
                    log.info("product metadata errors: {}", errors);
                    return new ResponseEntity<>(yamlMapper.writeValueAsString(errors), HttpStatus.PARTIAL_CONTENT);
                } else {
                    log.info("product metadata checking ok");
                    return new ResponseEntity<>("Data checking OK", HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping(value = "/dataLoading")
    public ResponseEntity<String> dataLoading(HttpServletRequest request) {
        try {
            adminLogService.log(request);
            log.info("data loading ...");
            MongoCollection<Document> collection = mongodb.getCollection(CollectionName.metadata);
            if (collection.countDocuments() == 0) {
                log.info("data loading failed for product metadata not found");
                return new ResponseEntity<>("Please perform STEP 1 and STEP 2 first", HttpStatus.PARTIAL_CONTENT);
            } else {
                blockingQueue.add(fullSyncTask.getTaskName());
                if (disableDataLoad) {
                    return new ResponseEntity<>("Data loading is disabled, check log for details", HttpStatus.PARTIAL_CONTENT);
                }

                log.info("data loading is queued");

                return new ResponseEntity<>("Data loading is queued, check log for details", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping(path="/fetchDataIndexes")
    public ResponseEntity<String> fetchDataIndexes(HttpServletRequest request) {
        try {
            adminLogService.log(request);
            blockingQueue.add(createIndexesSyncTask.getTaskName());
            log.info("Mongodb DB index loading is queued");
            return new ResponseEntity<>("MongoDB Data Indexes is queued, check log for details", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }


    @GetMapping(value = "/collectionLoading")
    public ResponseEntity<String> collectionLoading(HttpServletRequest request, @RequestParam("collectionName") String collectionName) {
        try {
            adminLogService.log(request);
            collectionSyncTask.setCollectionName(collectionName);
            blockingQueue.add(collectionSyncTask.getTaskName());
            log.info("table loading is queued");
            return new ResponseEntity<>("Data loading is queued, check log for details", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping(value = "/releaseLock")
    public ResponseEntity<String> collectionLoading(HttpServletRequest request) {
        try {
            adminLogService.log(request);
            MongoCollection<Document> collection = mongodb.getCollection(CollectionName.lock);
            DeleteResult result = collection.deleteOne(eq("_id", "DATA_UTILS_TASK_LOCK"));
            return new ResponseEntity<>("Release Lock Result: " + result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(CommonUtils.exceptionInfo(e), HttpStatus.PARTIAL_CONTENT);
        }
    }

    @GetMapping(value = "/health")
    public ResponseEntity<String> healthCheck() {
        log.debug("Health check passed");
        return new ResponseEntity<>("Health Check Result: " + HttpStatus.OK.toString(), HttpStatus.OK);
    }
}
