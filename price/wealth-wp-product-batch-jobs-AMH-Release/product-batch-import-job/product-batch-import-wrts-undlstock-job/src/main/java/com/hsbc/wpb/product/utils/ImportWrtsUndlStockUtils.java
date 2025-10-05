package com.dummy.wpb.product.utils;

import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;

import static com.dummy.wpb.product.constant.JobExecutionContextKey.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImportWrtsUndlStockUtils {

    public static String getRecordInfoString(WrtsUndlStockRecord rec) {
        return String.format(
                "(Security Code=%s, Security Type=%s, Underlying Code=%s)",
                rec.getSecurityCode(),
                rec.getSecurityType(),
                rec.getUnderlyingCode()
        );
    }

    public static void incrementReadCount(StepExecution stepExecution) {
        incrementCount(stepExecution, READ_COUNT);
    }

    public static void incrementFailedCount(StepExecution stepExecution, int amount) {
        incrementCount(stepExecution, FAILED_COUNT, amount);
    }

    public static void incrementSkippedCount(StepExecution stepExecution) {
        incrementCount(stepExecution, SKIPPED_COUNT);
    }

    public static void incrementUpdatedCount(StepExecution stepExecution, int amount) {
        incrementCount(stepExecution, UPDATED_COUNT, amount);
    }

    private static void incrementCount(StepExecution stepExecution, String stepExecutionContextKey) {
        incrementCount(stepExecution, stepExecutionContextKey, 1);
    }

    private static void incrementCount(StepExecution stepExecution, String stepExecutionContextKey, int amount) {
        ExecutionContext stepContext = stepExecution.getExecutionContext();
        stepContext.put(stepExecutionContextKey, stepContext.getInt(stepExecutionContextKey) + amount);
    }
}
