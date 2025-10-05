package com.dummy.wpb.product;

import com.dummy.wpb.product.repository.LogEqtyLinkInvstRepository;
import com.dummy.wpb.product.repository.ReferenceDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteSimpleTableListener extends JobExecutionListenerSupport {

    @Autowired
    public ReferenceDataRepository repository;

    @Autowired
    private LogEqtyLinkInvstRepository logEqtyLinkInvstRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        repository.deleteAll();
        logEqtyLinkInvstRepository.deleteAll();
        log.info("Delete all data from table CDE_DESC_VALUE and LOG_EQTY_LINK_INVST");
    }

}
