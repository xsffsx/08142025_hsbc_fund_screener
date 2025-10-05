package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.CollectionName;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
@Component
public class CoverageFileListener implements JobExecutionListener {

    private final Date currDate;

    private final MongoCollection<Document> equityRecomColl;

    private final Long equityRecomCollNum;

    public CoverageFileListener(MongoTemplate mongoTemplate) {
        equityRecomColl = mongoTemplate.getCollection(String.valueOf(CollectionName.equity_recommendations));
        this.equityRecomCollNum = equityRecomColl.countDocuments();

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
        this.currDate = calendar.getTime();
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("================================");
        log.info("Equity Recommendation coverage excel file job begin");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (equityRecomCollNum < equityRecomColl.countDocuments()) {
            equityRecomColl.deleteMany(Filters.lt("recCreatDtTm", currDate));
            equityRecomColl.createIndex(Indexes.ascending("reuters"), new IndexOptions().name("idx_reuters"));
        }
        Optional<StepExecution> stepExecutionOpt = jobExecution.getStepExecutions().stream().findFirst();
        if (stepExecutionOpt.isPresent()) {
            StepExecution stepExecution = stepExecutionOpt.get();
            log.info("Equity Recommendation coverage excel file job ended");
            log.info("Total number of records:" + (stepExecution.getWriteCount() + stepExecution.getFilterCount()));
            log.info("              processed:" + stepExecution.getWriteCount());
            log.info("         Illegal record:" + stepExecution.getFilterCount());
        }
    }
}
