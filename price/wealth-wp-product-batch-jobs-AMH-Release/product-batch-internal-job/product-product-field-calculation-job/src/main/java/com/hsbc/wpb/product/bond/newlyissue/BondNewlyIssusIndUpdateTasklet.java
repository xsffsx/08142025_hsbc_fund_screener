package com.dummy.wpb.product.bond.newlyissue;

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

public class BondNewlyIssusIndUpdateTasklet implements Tasklet {

    @Value("${batch.bond.newly-prod.max-num}")
    int bondNewlyProdMaxNum;
    @Value("${batch.bond.newly-prod.issueDate.count}")
    int newlyProdIssueDateCount;
    @Value("${batch.bond.newly-prod.issueDate.type}")
    int newlyProdIssueDateType;

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
                .and("debtInstm.prodIssDt").gte(jobService.calculateDate(newlyProdIssueDateType, newlyProdIssueDateCount))
                .and(Field.prodStatCde).in(ACTIVE, SUSPENDED)
                .and(Field.dmyProdSubtpRecInd).nin(null, INDICATOR_YES);
        Query query = Query.query(filter)
                .limit(bondNewlyProdMaxNum)
                .with(Sort.by(Sort.Order.desc("debtInstm.prodIssDt"), Sort.Order.asc(Field._id)));
        query.fields().include(Field._id);

        List<Document> productList = mongoTemplate.find(query, Document.class, CollectionName.product.name());

        Update update = new Update()
                .set("debtInstm.newlyIssBondInd", INDICATOR_YES)
                .set(Field.recUpdtDtTm, LocalDateTime.now())
                .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());
        List<Object> prodIdList = productList.stream().map(product -> product.get(Field._id)).collect(Collectors.toList());

        Criteria updateFilter = new Criteria()
                .and(Field.prodId).in(prodIdList)
                .and("debtInstm.newlyIssBondInd").ne(INDICATOR_YES);

        UpdateResult updateResult = mongoTemplate.updateMulti(new Query(updateFilter),
                update, CollectionName.product.name());
        stepExecution.setReadCount((int) updateResult.getMatchedCount());
        stepExecution.setWriteCount((int) updateResult.getModifiedCount());

        return RepeatStatus.FINISHED;
    }
}
