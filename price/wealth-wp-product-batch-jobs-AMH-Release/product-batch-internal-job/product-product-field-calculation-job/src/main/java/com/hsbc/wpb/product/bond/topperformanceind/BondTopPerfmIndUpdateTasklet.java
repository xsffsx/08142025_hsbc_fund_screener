package com.dummy.wpb.product.bond.topperformanceind;

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

public class BondTopPerfmIndUpdateTasklet implements Tasklet {

    @Value("${batch.bond.top-performance.max-num}")
    int topPerformanceMaxNum;

    @Value("${batch.bond.top-performance.closingDate.type}")
    int closingDateType;

    @Value("${batch.bond.top-performance.closingDate.count}")
    int closingDateCount;

    @Value("${batch.bond.top-performance.endDate.type}")
    int endDateType;

    @Value("${batch.bond.top-performance.endDate.count}")
    int endDateCount;

    private static final String PRODUCT = CollectionName.product.name();
    private static final String HISTORY_PRCEFFDT = "history.prcEffDt";


    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    BondJobService jobService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        StepExecution stepExecution = contribution.getStepExecution();
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String ctryRecCde = jobParameters.getString(Field.ctryRecCde);
        String grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);

        //query PRODSUBTP reference data
        List<String> prodSubtpCdeList = jobService.getBondProdSubtpCdes(ctryRecCde, grpMembrRecCde);

        LocalDate closingDate = jobService.calculateDate(closingDateType, closingDateCount);
        LocalDate endDate = jobService.calculateDate(endDateType, endDateCount);

        // update prodTopPerfmRankNum according PRODSUBTP one by one
        int readCount = 0;
        int writeCount = 0;
        for (String prodSubtpCde : prodSubtpCdeList) {
            Criteria productCriteria = new Criteria()
                    .and(Field.ctryRecCde).is(ctryRecCde)
                    .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                    .and(Field.prodTypeCde).is(BOND_CD)
                    .and(Field.prodSubtpCde).is(prodSubtpCde)
                    .and(Field.prodStatCde).in(ACTIVE, SUSPENDED)
                    .and(Field.allowBuyProdInd).is(INDICATOR_YES)
                    .and(Field.dmyProdSubtpRecInd).nin(null, INDICATOR_YES);

            // first query prodId and maxEffDt
            Aggregation productAggregation = Aggregation.newAggregation(
                    Aggregation.match(productCriteria),
                    Aggregation.project(Field._id),
                    Aggregation.lookup(CollectionName.prod_prc_hist.name(), Field._id, Field.prodId, "history"),
                    Aggregation.unwind("history"),
                    Aggregation.match(new Criteria()
                            .and("history.pdcyPrcCde").is("D")
                            .and("history.prodOffrPrcAmt").nin(null, 0)
                            .andOperator(
                                    Criteria.where(HISTORY_PRCEFFDT).lte(closingDate),
                                    Criteria.where(HISTORY_PRCEFFDT).gte(endDate))
                    ),
                    Aggregation.group(Field._id).max(HISTORY_PRCEFFDT).as("maxEffDt")
            ).withOptions(newAggregationOptions().allowDiskUse(true).build());
            List<Document> maxEffDtList = mongoTemplate.aggregate(productAggregation, PRODUCT, Document.class).getMappedResults();
            if (maxEffDtList.isEmpty()) {
                continue;
            }

            // sort diffPrice according to prodId and maxEffDt
            Criteria[] historyCriterias = maxEffDtList.stream().map(item ->
                    new Criteria()
                            .and(Field.prodId).is(item.get(Field._id))
                            .and(Field.prcEffDt).is(item.get("maxEffDt"))
            ).toArray(Criteria[]::new);
            ArithmeticOperators.Divide diffPrice = ArithmeticOperators.Divide
                    .valueOf(
                            ArithmeticOperators.Subtract.valueOf("product.prodOffrPrcAmt").subtract("prodOffrPrcAmt"))
                    .divideBy("prodOffrPrcAmt");
            Aggregation historyAggregation = Aggregation.newAggregation(
                    Aggregation.match(new Criteria().orOperator(historyCriterias)),
                    Aggregation.project(Field.prodId, Field.prcEffDt, Field.prodOffrPrcAmt),
                    Aggregation.lookup(PRODUCT, Field.prodId, Field._id, PRODUCT),
                    Aggregation.unwind(PRODUCT),
                    Aggregation.project(Field.prodId, Field.prcEffDt).and(diffPrice).as("diffPrice"),
                    Aggregation.sort(DESC, "diffPrice"),
                    Aggregation.limit(topPerformanceMaxNum)
            ).withOptions(newAggregationOptions().allowDiskUse(true).build());
            // update the prodTopPerfmRankNum of the first max num products
            List<Document> maxDiffPriceList = mongoTemplate.aggregate(historyAggregation, CollectionName.prod_prc_hist.name(), Document.class).getMappedResults();
            Criteria updateFilter;
            for (long seq = 1; seq <= maxDiffPriceList.size(); seq++) {
                Object productId = maxDiffPriceList.get((int) (seq - 1)).get(Field.prodId);
                Update update = new Update()
                        .set(Field.prodTopPerfmRankNum, seq)
                        .set(Field.recUpdtDtTm, LocalDateTime.now())
                        .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());
                updateFilter = new Criteria()
                        .and(Field._id).is(productId)
                        .and(Field.prodTopPerfmRankNum).ne(seq);

                UpdateResult updateResult = mongoTemplate.updateMulti(new BasicQuery(new Query(updateFilter).getQueryObject()),
                        update, CollectionName.product.name());
                readCount += updateResult.getMatchedCount();
                writeCount += updateResult.getModifiedCount();
            }
        }

        stepExecution.setReadCount(readCount);
        stepExecution.setWriteCount(writeCount);
        return RepeatStatus.FINISHED;
    }

}
