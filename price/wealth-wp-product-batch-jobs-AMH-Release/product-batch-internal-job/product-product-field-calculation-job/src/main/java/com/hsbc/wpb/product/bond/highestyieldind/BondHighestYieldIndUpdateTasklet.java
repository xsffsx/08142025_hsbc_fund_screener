package com.dummy.wpb.product.bond.highestyieldind;

import com.dummy.wpb.product.bond.BondJobService;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.List;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
@Slf4j
public class BondHighestYieldIndUpdateTasklet implements Tasklet {
    @Value("${batch.bond.highest-yield.max-num}")
    int highestYieldMaxNum;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    BondJobService jobService;
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = contribution.getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();

        String ctryRecCde = jobParameters.getString(Field.ctryRecCde);
        String grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);

        //query PRODSUBTP reference data
        List<String> prodSubtpCdeList = jobService.getBondProdSubtpCdes(ctryRecCde, grpMembrRecCde);

        // update prodTopYieldRankNum according PRODSUBTP one by one
        int readCount = 0;
        int writeCount = 0;
        for (String prodSubtpCde : prodSubtpCdeList) {
            Criteria productCriteria = new Criteria()
                    .and(Field.ctryRecCde).is(ctryRecCde)
                    .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                    .and(Field.prodTypeCde).is(BOND_CD)
                    .and(Field.prodSubtpCde).is(prodSubtpCde)
                    .and("debtInstm.yieldOfferText").ne(null)
                    .and(Field.prodStatCde).in(ACTIVE, SUSPENDED)
                    .and(Field.allowBuyProdInd).is(INDICATOR_YES)
                    .and(Field.dmyProdSubtpRecInd).nin(null, INDICATOR_YES);
            Query productQuery = Query.query(productCriteria)
                    .limit(highestYieldMaxNum)
                    .with(Sort.by(DESC, "debtInstm.yieldOfferText"));
            productQuery.fields().include(Field._id);
            List<Document> prodIdList = mongoTemplate.find(productQuery, Document.class, CollectionName.product.name());
            Criteria updateFilter;
            for (long seq = 1; seq <= prodIdList.size(); seq++) {
                Object productId = prodIdList.get((int) (seq - 1)).get(Field._id);
                Update update = new Update()
                        .set(Field.prodTopYieldRankNum, seq)
                        .set(Field.recUpdtDtTm, LocalDateTime.now())
                        .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());
                updateFilter = new Criteria()
                        .and(Field._id).is(productId)
                        .and(Field.prodTopYieldRankNum).ne(seq);

                UpdateResult updateResult = mongoTemplate.updateMulti(new BasicQuery(new Query(updateFilter).getQueryObject()),
                        update, CollectionName.product.name());
                readCount += updateResult.getMatchedCount();
                writeCount += updateResult.getModifiedCount();
            }
        }

        log.info("the mongodb update readCount: {}， readCount: {}",readCount,writeCount);
        stepExecution.setReadCount(readCount);
        stepExecution.setWriteCount(writeCount);
        return RepeatStatus.FINISHED;
    }
}
