package com.dummy.wpb.product.writer.support;

import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemStreamWriter;

public interface StepAwareItemStreamWriter<T> extends ItemStreamWriter<T>, StepExecutionListener {
}
