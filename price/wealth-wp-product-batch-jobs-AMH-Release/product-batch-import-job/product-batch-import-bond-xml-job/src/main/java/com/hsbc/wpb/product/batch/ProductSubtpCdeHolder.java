package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.service.ReferenceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import java.util.HashMap;
import java.util.Map;

import static com.dummy.wpb.product.constant.BatchConstants.PROD_SUBTP_CDE_REF_TYPE_CDE;


@Slf4j
public class ProductSubtpCdeHolder extends JobExecutionListenerSupport {

    private final ReferenceDataService referenceDataService;

    private static final Map<String, String> prodSubtpCdeDescMap = new HashMap<>();

    public ProductSubtpCdeHolder(ReferenceDataService referenceDataService) {
        this.referenceDataService = referenceDataService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        try {
            Map<String, Object> filter = new HashMap<>();
            filter.put(Field.ctryRecCde, jobParameters.getString(Field.ctryRecCde));
            filter.put(Field.grpMembrRecCde, jobParameters.getString(Field.grpMembrRecCde));
            filter.put(Field.cdvTypeCde, PROD_SUBTP_CDE_REF_TYPE_CDE);
            referenceDataService.referenceDataByFilter(filter)
                    .forEach(referenceData -> prodSubtpCdeDescMap.put(referenceData.getCdvDesc(), referenceData.getCdvCde()));
        } catch (Exception e) {
            log.error("Failed to init ProductSubtpCdeHolder reference data", e);
        }
    }

    public static String getProdSubtpCde(String prodSubtpCde) {
        return prodSubtpCdeDescMap.getOrDefault(prodSubtpCde, prodSubtpCde);
    }
}
