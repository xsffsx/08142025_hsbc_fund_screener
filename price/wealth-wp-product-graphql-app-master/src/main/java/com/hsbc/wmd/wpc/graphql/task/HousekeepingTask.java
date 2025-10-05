package com.dummy.wmd.wpc.graphql.task;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.service.HousekeepingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HousekeepingTask implements Runnable {
    @Autowired
    private HousekeepingService housekeepingService;

    @Value("#{${product.document-revision.keep-days:7}}")
    private int documentRevisionKeepDays;
    @Value("#{${product.request-log.keep-days:7}}")
    private int requestLogKeepDays;

    @Value("#{${product.msg-process-log.keep-days:7}}")
    private int msgProcessLogKeepDays;

    @Value("#{${product.batch-job-log.keep-days:30}}")
    private int batchJobLogKeepDays;

    @Override
    public void run() {
        log.info("document_revision housekeeping, deleting records out of {} days ...", documentRevisionKeepDays);
        long deletedCount = housekeepingService.cleanByBatch(CollectionName.document_revision.toString(), Field.recCreatDtTm, documentRevisionKeepDays);
        log.info("document_revision housekeeping, {} records out of {} days has been deleted", deletedCount, documentRevisionKeepDays);

        log.info("request_log housekeeping, deleting records out of {} days ...", requestLogKeepDays);
        deletedCount = housekeepingService.cleanByBatch(CollectionName.request_log.toString(), Field.requestTime, requestLogKeepDays);
        log.info("request_log housekeeping, {} records out of {} days has been deleted", deletedCount, requestLogKeepDays);

        log.info("msg_proc_log housekeeping, deleting records out of {} days ...", msgProcessLogKeepDays);
        long deletedMsgProcCount = housekeepingService.cleanByBatch(CollectionName.msg_proc_log.toString(), Field.recCreatDtTm, msgProcessLogKeepDays);
        log.info("msg_proc_log housekeeping, {} records out of {} days has been deleted", deletedMsgProcCount, msgProcessLogKeepDays);

        log.info("batchJobLog housekeeping, deleting records out of {} days ...", batchJobLogKeepDays);
        long pgDeleteCount = housekeepingService.cleanBatchJobLog(batchJobLogKeepDays);
        log.info("batchJobLog housekeeping, {} records out of {} days has been deleted", pgDeleteCount, batchJobLogKeepDays);
    }
}
