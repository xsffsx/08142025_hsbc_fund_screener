package com.dummy.wmd.wpc.graphql.service;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.sun.org.apache.xpath.internal.res.XPATHErrorResources_sv;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.lt;

@Slf4j
@Component
public class HousekeepingService {
    @Autowired
    private MongoDatabase mongodb;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Value("#{${product.housekeeping.deleteSize:10000}}")
    private int batchSize;

    public long cleanByBatch(String collectionName, String sortField, int keepingDays) {
        Date keepingDate = new DateTime().minusDays(keepingDays).toDate();
        MongoCollection<Document> coll = mongodb.getCollection(collectionName);
        boolean hasNext;
        long deletedCount = 0;
        do {
            Date cutByDate = keepingDate;
            Document doc = coll.find(lt(sortField, keepingDate)).sort(Sorts.ascending(sortField)).skip(batchSize).limit(1).first();
            hasNext = null != doc;  // if a record found in between per the batch size, will have another batch
            if (hasNext) {
                cutByDate = doc.getDate(sortField);
            }

            DeleteResult result = coll.deleteMany(lt(sortField, cutByDate));
            log.info("{} delete result: {}", collectionName, result);
            deletedCount += result.getDeletedCount();
        } while (hasNext);
        return deletedCount;
    }

    /**
     * delete below postgreSql tables by start_time, delete the data older than 30 days by default
     * postgreSql only support delete one table at the same time
     *
     * batch_job_execution   			primary key:job_execution_id ,foreign key:job_instance_id. MAIN TABLE
     * batch_job_instance				primary key:job_instance_id, association: batch_job_execution.job_instance_id
     * batch_step_execution_context		primary key and foreign key:step_execution_id，refer batch_step_execution.step_execution_id
     * batch_step_execution				primary key:step_execution_id association:job_execution_id
     * batch_job_execution_params		primary key:job_execution_id
     * batch_job_execution_context		primary key:job_execution_id
     *
     * so the deletion order is as below:
     * 1st.     batch_step_execution_context         contain foreign key: batch_step_execution.step_execution_id
     * sed.     batch_step_execution
     * third.   batch_job_execution_params
     * 4th.     batch_job_execution_context
     * 5th.     batch_job_execution                  contain foreign key: batch_job_instance.job_instance_id
     * 6th.     batch_job_instance
     *
     * @param keepingDays
     * @return  return the deletion count of the main table
     */
    public long cleanBatchJobLog(int keepingDays) {

        //prepare parameters
        LocalDateTime deleteTime = LocalDateTime.now().minusDays(keepingDays);
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("startTime",deleteTime);

        //1.delete batch_step_execution_context table
        String sql1 = "delete FROM batch_step_execution_context WHERE step_execution_id IN(SELECT step_execution_id FROM batch_step_execution WHERE job_execution_id IN (SELECT job_execution_id FROM batch_job_execution WHERE start_time < :startTime));";
        namedParameterJdbcTemplate.update(sql1, parameters);

        //2.delete batch_step_execution table
        String sql2 = "delete FROM batch_step_execution WHERE job_execution_id IN(SELECT job_execution_id FROM batch_job_execution WHERE start_time < :startTime);";
        namedParameterJdbcTemplate.update(sql2, parameters);

        //3.delete batch_job_execution_params table
        String sql3 = "delete FROM batch_job_execution_params WHERE job_execution_id IN(SELECT job_execution_id FROM batch_job_execution WHERE start_time < :startTime);";
        namedParameterJdbcTemplate.update(sql3, parameters);


        //4.delete batch_job_execution_context table
        String sql4 = "delete FROM batch_job_execution_context WHERE job_execution_id IN(SELECT job_execution_id FROM batch_job_execution WHERE start_time < :startTime);";
        namedParameterJdbcTemplate.update(sql4, parameters);

        //5. batch_job_execution tables ,Main table, return the deletion count
        String sql5 = "delete FROM batch_job_execution WHERE start_time < :startTime;";
        int deletedCount = namedParameterJdbcTemplate.update(sql5, parameters);

        //6.delete batch_job_instance table
        String sql6 = "delete FROM batch_job_instance WHERE job_instance_id NOT IN(SELECT job_instance_id FROM batch_job_execution);";
        namedParameterJdbcTemplate.update(sql6, parameters);
        return deletedCount;
    }
}


