package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.utils.CommonUtils;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class BatchJobExecutionStepTest {

    @Test
    public void testSetContext() {
        String context = CommonUtils.readResource("/files/batch-step-execution-context.json");
        BatchJobExecutionStep batchJobExecutionStep = new BatchJobExecutionStep();
        batchJobExecutionStep.setShortContext(context);
        JSONAssert.assertEquals(CommonUtils.readResource("/files/batch-step-execution-context-expected.json"), batchJobExecutionStep.getShortContext(), JSONCompareMode.STRICT);
    }
}
