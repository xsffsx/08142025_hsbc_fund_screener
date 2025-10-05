package com.dummy.wpb.product.listener;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ExportAck;
import com.dummy.wpb.product.model.ExportRequest;
import com.dummy.wpb.product.service.SystemParameterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ExportFileNameContextPopulator extends JobExecutionListenerSupport {

    @Autowired
    SystemParameterService sysParamService;

    @Value("#{exportRequest}")
    ExportRequest exportRequest;

    private static final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX, SystemPropertyUtils.PLACEHOLDER_SUFFIX);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        ExecutionContext context = jobExecution.getExecutionContext();
        Date startTime = new Date();
        context.put("startTime", startTime);

        JobParameters jobParameters = jobExecution.getJobParameters();
        String defaultSequenceKey = jobParameters.getString("systemCde") + "_SEQ";
        exportRequest.getTasks()
                .stream()
                .flatMap(t -> t.getFiles().stream())
                .forEach(file -> {
                    String sequenceKey = StringUtils.defaultString(file.getSequenceKey(), defaultSequenceKey);
                    file.setFileName(helper.replacePlaceholders(file.getFileName(), new ExportFileNamePlaceholderResolver(jobExecution, sequenceKey)));
                });

        ExportAck ack = exportRequest.getAck();
        if (ack.isGenerate()) {
            ack.setFileName(helper.replacePlaceholders(ack.getFileName(), new ExportFileNamePlaceholderResolver(jobExecution, defaultSequenceKey)));
        }
    }

    private class ExportFileNamePlaceholderResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

        final ExecutionContext context;
        final JobParameters jobParameters;
        final String sequenceKey;

        ExportFileNamePlaceholderResolver(JobExecution jobExecution, String sequenceKey) {
            this.context = jobExecution.getExecutionContext();
            this.jobParameters = jobExecution.getJobParameters();
            this.sequenceKey = sequenceKey;
        }


        @Override
        public String resolvePlaceholder(String placeholder) {
            if (placeholder.startsWith("date:")) {
                return new SimpleDateFormat(placeholder.substring(5)).format((Date) context.get("startTime"));
            }
            if (placeholder.equals("sequence")) {
                String sequence = context.getString(sequenceKey, "");
                if (StringUtils.isBlank(sequence)) {
                    String ctryRecCde = jobParameters.getString(Field.ctryRecCde);
                    String grpMembrRecCde = jobParameters.getString(Field.grpMembrRecCde);
                    // Generate sequence if not already present
                    sequence = sysParamService.getNextSequence(ctryRecCde, grpMembrRecCde, sequenceKey);
                    context.put(sequenceKey, sequence);
                }
                return sequence;
            }
            return String.valueOf(jobParameters.getParameters().get(placeholder));
        }
    }
}
