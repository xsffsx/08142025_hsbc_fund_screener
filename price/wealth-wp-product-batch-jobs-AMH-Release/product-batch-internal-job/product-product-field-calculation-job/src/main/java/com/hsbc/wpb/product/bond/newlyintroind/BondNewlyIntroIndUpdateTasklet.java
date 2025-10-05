package com.dummy.wpb.product.bond.newlyintroind;

import com.dummy.wpb.product.bond.BondJobService;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

public class BondNewlyIntroIndUpdateTasklet implements Tasklet {

    @Value("${batch.bond.newly-intro.max-num}")
    int newlyIntroMaxNum;
    @Value("${batch.bond.newly-intro.launchDate.count}")
    int newlyIntroLaunchDateCount;
    @Value("${batch.bond.newly-intro.launchDate.type}")
    int newlyIntroLaunchDateType;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    BondJobService jobService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        StepExecution stepExecution = contribution.getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();

        Criteria filter = new Criteria()
                .and(Field.ctryRecCde).is(jobParameters.getString(Field.ctryRecCde))
                .and(Field.grpMembrRecCde).is(jobParameters.getString(Field.grpMembrRecCde))
                .and(Field.prodTypeCde).is(BOND_CD)
                .and(Field.prodLnchDt).gte(jobService.calculateDate(newlyIntroLaunchDateType,newlyIntroLaunchDateCount))
                .and(Field.prodStatCde).in(ACTIVE, SUSPENDED)
                .and(Field.dmyProdSubtpRecInd).nin(null, INDICATOR_YES)
                .and(Field.prodSubtpCde).ne(BOND_CD);
        Query query = Query.query(filter)
                .limit(newlyIntroMaxNum)
                .with(Sort.by(DESC, Field.prodLnchDt));
        query.fields().include(Field._id);

        List<Document> productList = mongoTemplate.find(query, Document.class, CollectionName.product.name());

        Update update = new Update()
                .set(Field.introProdCurrPrdInd, INDICATOR_YES)
                .set(Field.recUpdtDtTm, LocalDateTime.now())
                .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());
        List<Object> prodIdList = productList.stream().map(product -> product.get(Field._id)).collect(Collectors.toList());

        Criteria updateFilter = new Criteria()
                .and(Field.prodId).in(prodIdList)
                .and(Field.introProdCurrPrdInd).ne(INDICATOR_YES);

        UpdateResult updateResult = mongoTemplate.updateMulti(new Query(updateFilter),
                update, CollectionName.product.name());
        stepExecution.setReadCount((int) updateResult.getMatchedCount());
        stepExecution.setWriteCount((int) updateResult.getModifiedCount());

        return RepeatStatus.FINISHED;
    }
}
