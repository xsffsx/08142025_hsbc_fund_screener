package com.dummy.wpb.product.bond.newlyissue;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;

import static com.dummy.wpb.product.constant.BatchConstants.*;
public class BondNewlyIssueIndCleanOldSortTasklet implements Tasklet {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = contribution.getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();

        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(jobParameters.getString(Field.ctryRecCde))
                .and(Field.grpMembrRecCde).is(jobParameters.getString(Field.grpMembrRecCde))
                .and(Field.prodTypeCde).is(BOND_CD)
                .and("debtInstm.newlyIssBondInd").nin(null,INDICATOR_NO);
        Update update = new Update()
                .set("debtInstm.newlyIssBondInd", INDICATOR_NO)
                .set(Field.recUpdtDtTm, LocalDateTime.now())
                .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());

        UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(criteria), update, CollectionName.product.name());
        stepExecution.setReadCount((int) updateResult.getMatchedCount());
        stepExecution.setWriteCount((int) updateResult.getModifiedCount());

        return RepeatStatus.FINISHED;
    }
}
