package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.FileIngressRecord;
import com.dummy.wmd.wpc.graphql.model.FileIngressStatus;
import com.dummy.wmd.wpc.graphql.model.PageResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Component
@SuppressWarnings("java:S107")
public class DataProcessingService {
    private final MongoTemplate mongoTemplate;
    private final MongoCollection<Document> collFileUpload;

    private static final String CRT_DT_TM = "crtDtTm";

    public DataProcessingService(MongoDatabase mongoDatabase, MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.collFileUpload = mongoDatabase.getCollection(CollectionName.file_upload.toString());
    }

    public PageResult<FileIngressStatus> queryFileIngressStatus(Long id, OffsetDateTime fromTime, OffsetDateTime toTime, String filename, String md5, List<String> statusIn,
                                                                Map<String, Object> sortMap, Integer skip, Integer limit) {
        // query master table
        Criteria criteria = new Criteria();
        if (null != id) {
            criteria.and("id").is(id);
        }

        if (null != fromTime && null != toTime) {
            criteria.andOperator(Criteria.where(CRT_DT_TM).gte(fromTime), Criteria.where(CRT_DT_TM).lte(toTime));
        } else if (null != fromTime) {
            criteria.and(CRT_DT_TM).gte(fromTime);
        } else if (null != toTime) {
            criteria.and(CRT_DT_TM).lte(toTime);
        }

        if (StringUtils.hasText(filename)) {
            criteria.and("fileName").regex(filename, "i");
        }
        if (StringUtils.hasText(md5)) {
            criteria.and("md5").is(md5);
        }
        if (!CollectionUtils.isEmpty(statusIn)) {
            criteria.and("stat").in(statusIn);
        }

        Query q = new Query(criteria);

        if (!CollectionUtils.isEmpty(sortMap)) {
            sortMap.forEach((k, v) -> q.with(Sort.by(Integer.parseInt(v.toString()) > 0 ? Sort.Direction.ASC : Sort.Direction.DESC, k)));
        }

        // query for total rows
        Long total = mongoTemplate.count(q, FileIngressStatus.class);
        PageResult<FileIngressStatus> pageResult = new PageResult<>();
        pageResult.setTotal(total);

        // query for the page data
        q.skip(skip).limit(limit);
        List<FileIngressStatus> list = mongoTemplate.find(q, FileIngressStatus.class);

        // for each ingress file, update extra fields
        list.forEach(this::updateFileIngressStatus);

        pageResult.setList(list);
        pageResult.setSkip(skip);
        pageResult.setLimit(limit);
        return pageResult;
    }

    /**
     * Update the fields left, like pendingCount, skippedCount, errorCount
     *
     * @param item
     */
    private void updateFileIngressStatus(FileIngressStatus item) {
        // check weather the file is uploaded through WPS excel form upload by match md5
        Bson filter = eq(Field.fileMd5, item.getMd5()); // may need to consider the time factor as well
        long count = collFileUpload.countDocuments(filter);
        item.setIsExcelUploadForm(count > 0);
    }

    public PageResult<FileIngressRecord> queryFileIngressRecord(Long fisid, List<String> statusIn, Map<String, Object> sortMap, Integer skip, Integer limit, Set<String> selectFields) {
        // raw_data and pay_load field are too big to retrieve, avoid to retrieve them for better performance
        Criteria criteria = where("fisid").is(fisid);
        if (!CollectionUtils.isEmpty(statusIn)) {
            criteria.and("stat").in(statusIn);
        }

        Query q = new Query(criteria);
        long total = mongoTemplate.count(q, FileIngressRecord.class);

        if (!CollectionUtils.isEmpty(sortMap)) {
            sortMap.forEach((key, direObj) -> {
                Sort.Direction direction = "1".equals(String.valueOf(direObj)) ? Sort.Direction.ASC : Sort.Direction.DESC;
                q.with(Sort.by(direction, key));
            });
        }

        if (!containsField(selectFields, "rawData")) {
            q.fields().exclude("rawData");
        }
        if (!containsField(selectFields, "payload")) {
            q.fields().exclude("payload");
        }

        q.skip(skip).limit(limit);

        List<FileIngressRecord> rowList = mongoTemplate.find(q, FileIngressRecord.class);

        // update record number by calculation, may not reliable, but better then no
        long recordNumBase = getRecordNumBase(fisid) - 1;
        rowList.forEach(rec -> rec.setRecordNum(rec.getId() - recordNumBase));

        PageResult<FileIngressRecord> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setSkip(skip);
        pageResult.setLimit(limit);
        pageResult.setList(rowList);
        return pageResult;
    }

    /**
     * Get the min sequence id return as base
     *
     * @param fisid
     * @return
     */
    private long getRecordNumBase(Long fisid) {
        Query q = query(where("fisid").is(fisid));
        q.with(Sort.by(Sort.Direction.ASC, "id"));
        q.fields().include("id");
        q.limit(1);
        List<FileIngressRecord> list = mongoTemplate.find(q, FileIngressRecord.class);

        return list.isEmpty() ? 1 : list.get(0).getId();
    }

    /**
     * Check weather a filed is in the selectFields, since the path may vary, just check suffix
     *
     * @param selectFields
     * @param field
     * @return
     */
    private boolean containsField(Set<String> selectFields, String field) {
        return selectFields.stream().anyMatch(f -> f.endsWith(field));
    }
}
