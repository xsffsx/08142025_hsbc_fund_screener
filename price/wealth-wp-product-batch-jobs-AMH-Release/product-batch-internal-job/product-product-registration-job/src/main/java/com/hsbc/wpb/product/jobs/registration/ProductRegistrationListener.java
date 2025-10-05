package com.dummy.wpb.product.jobs.registration;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.model.SystemParameter;
import com.dummy.wpb.product.service.SystemParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class ProductRegistrationListener implements JobExecutionListener {

    private final SystemParameterService systemParameterService;

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("#{jobParameters['isDaltaSync']}")
    private String isDaltaSync;

    @Value("${start-time-delay-minutes}")
    private int startTimeDelayMinutes;

    private String registerStartTime;

    public ProductRegistrationListener(SystemParameterService systemParameterService) {
        this.systemParameterService = systemParameterService;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        //For AMH, to avoid the situation where the product is updated while the job is running, we need to subtract the job running time for better fault tolerance
        //For SGH, to avoid the situation where there is a delay in synchronizing data from WPC to Smart, we need to subtract the data delay time for better fault tolerance
        registerStartTime = ZonedDateTime.now().minusMinutes(startTimeDelayMinutes).format(DateTimeFormatter.ISO_INSTANT);

        if (Boolean.parseBoolean(isDaltaSync)) {
            ExecutionContext jobContext = jobExecution.getExecutionContext();
            SystemParameter systemParameter = systemParameterService.getSystemParameter(ctryRecCde, grpMembrRecCde, BatchConstants.Q_CODE_LAST_REGISTER_DT_TM);
            String latestRegisterDtTm = Objects.isNull(systemParameter) ? createSystemParameterAndReturnCurrentTimestamp() : systemParameter.getParmValueText();
            jobContext.put("latestRegisterDtTm", latestRegisterDtTm);
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (ExitStatus.COMPLETED.equals(jobExecution.getExitStatus())) {
            SystemParameter systemParameter = systemParameterService.getSystemParameter(ctryRecCde, grpMembrRecCde, BatchConstants.Q_CODE_LAST_REGISTER_DT_TM);
            systemParameter.setParmValueText(registerStartTime);
            systemParameterService.updateSystemParameter(systemParameter);
        }
    }

    private String createSystemParameterAndReturnCurrentTimestamp() {
        String currentTimestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
        systemParameterService.createSystemParameter(ctryRecCde, grpMembrRecCde, BatchConstants.Q_CODE_LAST_REGISTER_DT_TM, currentTimestamp);
        return currentTimestamp;
    }
}
