package com.dummy.wpb.product.batch;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.*;

import static com.dummy.wpb.product.constant.BatchConstants.DELISTED;

@Slf4j
public class CleanTopSellRankListener extends JobExecutionListenerSupport {

    @Autowired
    private ProductService productService;

    @SneakyThrows
    @Override
    public void beforeJob(JobExecution jobExecution) {
        String ctryRecCde = jobExecution.getJobParameters().getString("ctryRecCde");
        String grpMembrRecCde = jobExecution.getJobParameters().getString("grpMembrRecCde");

        ProductBatchUpdateInput productBatchUpdateInput = new ProductBatchUpdateInput();
        Criteria criteria = new Criteria()
                .and(Field.ctryRecCde).is(ctryRecCde)
                .and(Field.grpMembrRecCde).is(grpMembrRecCde)
                .and(Field.prodTypeCde).is("BOND")
                .and(Field.prodTopSellRankNum).ne(null)
                .and(Field.prodStatCde).ne(DELISTED);

        List<Operation> opList = Collections.singletonList(new Operation("put", Field.prodTopSellRankNum, null));
        productBatchUpdateInput.setFilter(criteria.getCriteriaObject());
        productBatchUpdateInput.setOperations(opList);
        ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(productBatchUpdateInput);
        log.info("Cleaned up the prodTopSellRankNum of {} products", updateResult.getUpdatedProducts().size());
    }
}
