package com.dummy.wpb.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class InvestCharIndJobDecider implements JobExecutionDecider {

    private static final Map<String,String> BATCH_CODE_MAP = new HashMap<>();

    static {
        BATCH_CODE_MAP.put("I", "INIT");
        BATCH_CODE_MAP.put("M", "COMMON");
        BATCH_CODE_MAP.put("D", "DAILY_UPDATE");
        BATCH_CODE_MAP.put("R", "MONTHLY_RECONCILIATION");
        BATCH_CODE_MAP.put("E", "EX_RETRY");
    }

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String batchCode = jobExecution.getJobParameters().getString("batchCode");
        String batchName = BATCH_CODE_MAP.get(batchCode);

        if (StringUtils.isBlank(batchName)){
            log.error("Unknown batch code.");
            log.error(" -- I: init ic to wpc from GSOPS via interface at first time");
            log.error(" -- M: 20-minutes IC flag");
            log.error(" -- D: daily ic flag update");
            log.error(" -- R: monthly reconciliation");
            log.error(" -- E: exception items retry after CUTAS product update job");
            log.error("   Mandatory for all IC batch except batch code is M");
            return FlowExecutionStatus.UNKNOWN;
        }

        return new FlowExecutionStatus(batchName);
    }
}
