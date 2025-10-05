package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.jpo.LogEqtyLinkInvstPo;
import com.dummy.wpb.product.service.BulkInsertService;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@StepScope
@Component
public class LogEqtyLinkInvstWriter implements ItemWriter<LogEqtyLinkInvstPo> {

    @Autowired
    private BulkInsertService bulkInsertService;

    @Override
    public void write(List<? extends LogEqtyLinkInvstPo> items) throws Exception {
        bulkInsertService.batchInsert(items);
    }
}
