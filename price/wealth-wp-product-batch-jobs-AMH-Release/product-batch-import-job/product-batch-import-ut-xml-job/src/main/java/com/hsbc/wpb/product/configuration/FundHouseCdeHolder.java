package com.dummy.wpb.product.configuration;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.graphql.ReferenceData;
import com.dummy.wpb.product.service.GraphQLService;
import com.dummy.wpb.product.service.ReferenceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dummy.wpb.product.constant.BatchConstants.FUND_HSE_CDE_REF_TYPE_CDE;

@Slf4j
public class FundHouseCdeHolder extends JobExecutionListenerSupport {

    private final ReferenceDataService referenceDataService;

    private static final Map<String, String> fundHouseCdeMap = new HashMap<>();

    public FundHouseCdeHolder(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        try {
            Criteria criteria = new Criteria()
                    .and(Field.ctryRecCde).is(jobParameters.getString(Field.ctryRecCde))
                    .and(Field.grpMembrRecCde).is(jobParameters.getString(Field.grpMembrRecCde))
                    .and(Field.cdvTypeCde).is(FUND_HSE_CDE_REF_TYPE_CDE);
            List<ReferenceData> referenceDataList = referenceDataService.referenceDataByFilter(criteria.getCriteriaObject());
            referenceDataList.forEach(referenceData -> fundHouseCdeMap.put(referenceData.getCdvCde(), referenceData.getCdvDesc()));
        } catch (Exception e) {
            log.error("Failed to init fundHouseCde reference data", e);
        }
    }

    public static String getFundHouseCde(String fundHouseCde) {
        return fundHouseCdeMap.getOrDefault(fundHouseCde, fundHouseCde);
    }
}
