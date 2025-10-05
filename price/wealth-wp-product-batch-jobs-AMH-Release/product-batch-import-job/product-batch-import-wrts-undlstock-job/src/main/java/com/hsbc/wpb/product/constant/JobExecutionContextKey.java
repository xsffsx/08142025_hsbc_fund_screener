package com.dummy.wpb.product.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JobExecutionContextKey {
    public static final String READ_COUNT = "readCount";
    public static final String FAILED_COUNT = "failedCount";
    public static final String SKIPPED_COUNT = "skippedCount";
    public static final String UPDATED_COUNT = "updatedCount";
}
