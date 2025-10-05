package com.dummy.wpb.product.writer.support;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.lang.Nullable;

import java.util.List;

public class CompositeStepAwareItemStreamWriter<T> extends CompositeItemWriter<T> implements StepAwareItemStreamWriter<T> {

    private final List<ItemWriter<? super T>> delegates;

    public CompositeStepAwareItemStreamWriter(List<ItemWriter<? super T>> delegates) {
        this.delegates = delegates;
        this.setDelegates(delegates);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        for (ItemWriter<? super T> delegate : delegates) {
            if (delegate instanceof StepExecutionListener) {
                ((StepExecutionListener) delegate).beforeStep(stepExecution);
            }
        }
    }

    @Override
    @Nullable
    public ExitStatus afterStep(StepExecution stepExecution) {
        for (ItemWriter<? super T> delegate : delegates) {
            if (delegate instanceof StepExecutionListener) {
                ExitStatus close = ((StepExecutionListener) delegate).afterStep(stepExecution);
                stepExecution.setExitStatus(stepExecution.getExitStatus().and(close));
            }
        }
        return stepExecution.getExitStatus();
    }
}
