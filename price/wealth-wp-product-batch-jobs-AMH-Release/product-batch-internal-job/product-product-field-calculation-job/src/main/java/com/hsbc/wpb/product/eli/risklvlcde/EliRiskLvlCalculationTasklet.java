package com.dummy.wpb.product.eli.risklvlcde;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

import static com.dummy.wpb.product.constant.BatchConstants.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregationOptions;

@Slf4j
public class EliRiskLvlCalculationTasklet implements Tasklet {

    private final MongoTemplate mongoTemplate;

    private static final Long YEAR = 12L;
    private final BigDecimal cptlProtcPctAll = new BigDecimal(100);
    private final BigDecimal cptlProtcPctPart = new BigDecimal(95);

    private static final String RISKLVLCDE_1 = "1";
    private static final String RISKLVLCDE_2 = "2";
    private static final String RISKLVLCDE_3 = "3";
    private static final String RISKLVLCDE_4 = "4";
    private static final String RISKLVLCDE_5 = "5";

    ExpressionParser spelParser = new SpelExpressionParser();

    private final Map<String, String> conditionMap;

    public EliRiskLvlCalculationTasklet(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        Map<String, String> mutableMap = new LinkedHashMap<>();
        mutableMap.put("cptlProtcPct >= #cptlProtcPctAll and tenor > #year", RISKLVLCDE_2);
        mutableMap.put("cptlProtcPct >= #cptlProtcPctAll and tenor <= #year", RISKLVLCDE_1);
        mutableMap.put("cptlProtcPct >= #cptlProtcPctPart and cptlProtcPct < #cptlProtcPctAll and tenor > #year", RISKLVLCDE_3);
        mutableMap.put("cptlProtcPct >= #cptlProtcPctPart and cptlProtcPct < #cptlProtcPctAll and tenor <= #year", RISKLVLCDE_2);
        mutableMap.put("undlQtyInd == 'SINGLE' and ((undlRiskLvlCde != null and undlRiskLvlCde <= '2') or (undlRiskLvlCde == '3' and tenor <= #year))", RISKLVLCDE_3);
        mutableMap.put("undlQtyInd=='SINGLE' and undlRiskLvlCde == '3' and tenor > #year", RISKLVLCDE_4);
        mutableMap.put("undlQtyInd=='BASKET' and undlRiskLvlCde == '3' and tenor <= #year", RISKLVLCDE_4);
        conditionMap = Collections.unmodifiableMap(mutableMap);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        StepExecution stepExecution = contribution.getStepExecution();

        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String ctryRecCde = jobParameters.getString(Field.ctryRecCde);
        String grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);

        if (StringUtils.isAnyBlank(ctryRecCde, grpMembrRecCde)) {
            log.error("Please check the incoming parameters: ctryRecCde, grpMembrRecCde.");
            return RepeatStatus.FINISHED;
        }

        UpdateResult updateResult = updateRiskLvl(ctryRecCde, grpMembrRecCde, stepExecution);
        stepExecution.setReadCount((int) updateResult.getMatchedCount());
        stepExecution.setWriteCount((int) updateResult.getModifiedCount());
        return RepeatStatus.FINISHED;
    }

    public UpdateResult updateRiskLvl(String ctryRecCde, String grpMembrRecCde, StepExecution stepExecution) {
        String cptlProtcPctPath = Field.eqtyLinkInvst + "." + Field.cptlProtcPct;
        String undlQtyIndPath = Field.eqtyLinkInvst + "." + Field.undlQtyInd;

        String undlRiskLvlCdeName = "undlRiskLvlCde";
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(new Criteria()
                        .and(Field.ctryRecCde).is(ctryRecCde)
                        .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                        .and(Field.prodStatCde).in(ACTIVE, CLOSED_FROM_SUBSCRIPTION)
                        .and(Field.prodTypeCde).is(EQUITY_LINKED_INVESTMENT)
                        .and(Field.prodMturDt).gt(new Date())
                        .and("eqtyLinkInvst.lnchProdInd").is(INDICATOR_YES)
                        .and("eqtyLinkInvst.rtrvProdExtnlInd").is(INDICATOR_NO)
                        .and(undlQtyIndPath).ne(null)
                        .and("eqtyLinkInvst.undlStock").ne(null)),
                Aggregation.unwind("eqtyLinkInvst.undlStock"),
                Aggregation.project(Fields.from(
                        Fields.field(Field.prodId),
                        Fields.field(Field.riskLvlCde),
                        Fields.field(Field.prdProdNum),
                        Fields.field(Field.undlQtyInd, undlQtyIndPath),
                        Fields.field(Field.cptlProtcPct, cptlProtcPctPath),
                        Fields.field("undlProdId", "eqtyLinkInvst.undlStock.prodIdUndlInstm")
                )),
                Aggregation.lookup(CollectionName.product.name(), "undlProdId", Field.prodId, "undlProd"),
                Aggregation.unwind("undlProd"),
                Aggregation.group(
                        Field.prodId,
                        Field.riskLvlCde,
                        Field.prdProdNum,
                        Field.cptlProtcPct,
                        Field.undlQtyInd
                ).max("undlProd.eliRiskLvlCde").as(undlRiskLvlCdeName),
                Aggregation.project(Fields.from(
                        Fields.field(Field.prodId, "_id.prodId"),
                        Fields.field(Field.riskLvlCde, "_id.riskLvlCde"),
                        Fields.field(Field.cptlProtcPct, "_id.cptlProtcPct"),
                        Fields.field("tenor", "_id.prdProdNum"),
                        Fields.field(Field.undlQtyInd, "_id.undlQtyInd"),
                        Fields.field(undlRiskLvlCdeName, undlRiskLvlCdeName)
                ))
        ).withOptions(newAggregationOptions().allowDiskUse(true).build());
        AggregationResults<EqtyLinkInvstRiskLevel> aggregate = mongoTemplate.aggregate(aggregation, CollectionName.product.name(), EqtyLinkInvstRiskLevel.class);
        List<EqtyLinkInvstRiskLevel> mappedResults = aggregate.getMappedResults();
        List<EqtyLinkInvstRiskLevel> riskLevelList = new ArrayList<>(mappedResults);

        StandardEvaluationContext spelContext = new StandardEvaluationContext();
        spelContext.setVariable("year", YEAR);
        spelContext.setVariable("cptlProtcPctAll", cptlProtcPctAll);
        spelContext.setVariable("cptlProtcPctPart", cptlProtcPctPart);

        long writeCount = 0;
        Criteria updateFilter ;
        for (EqtyLinkInvstRiskLevel riskLevel : riskLevelList) {
            String newRiskLvlCde = calcRiskLvlCde(riskLevel, spelContext);
            if (!StringUtils.equals(newRiskLvlCde, riskLevel.getRiskLvlCde())) {
                Update update = new Update()
                        .set(Field.riskLvlCde, newRiskLvlCde)
                        .set(Field.recUpdtDtTm, LocalDateTime.now())
                        .set(Field.lastUpdatedBy, stepExecution.getJobExecution().getJobInstance().getJobName());
                updateFilter = Criteria.where(Field._id).is(riskLevel.getProdId())
                        .and(Field.riskLvlCde).ne(newRiskLvlCde);

                UpdateResult updateResult = mongoTemplate.updateFirst(Query.query(updateFilter), update, CollectionName.product.name());
                writeCount += updateResult.getModifiedCount();
            }
        }
        return UpdateResult.acknowledged(riskLevelList.size(), writeCount, null);
    }

    private String calcRiskLvlCde(EqtyLinkInvstRiskLevel riskLevel, StandardEvaluationContext spelContext) {
        String undlQtyInd = riskLevel.getUndlQtyInd();
        Long tenor = riskLevel.getTenor();

        if (null == tenor || StringUtils.isBlank(undlQtyInd)) {
            return RISKLVLCDE_5;
        }

        spelContext.setRootObject(riskLevel);
        Predicate<String> evaluate = expression -> Boolean.TRUE.equals(spelParser.parseExpression(expression).getValue(spelContext, Boolean.class));

        for (Map.Entry<String, String> entry : conditionMap.entrySet()) {
            if (evaluate.test(entry.getKey())) {
                return entry.getValue();
            }
        }

        return RISKLVLCDE_5;
    }
}
