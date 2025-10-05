package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.Operation;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateInput;
import com.dummy.wpb.product.model.graphql.ProductBatchUpdateResult;
import com.dummy.wpb.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.dummy.wpb.product.ImportBondWisdomJobApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.BatchConstants.*;

@Slf4j
public class WisdomBondItemWriter implements ItemWriter<ProductStreamItem> {

    @Autowired
    private ProductService productService;

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    @Value("${batch.top-seller-num}")
    private int topSellerNum;

    private final AtomicInteger updateCount = new AtomicInteger(0);

    @Override
    public void write(List<? extends ProductStreamItem> streamItems) throws Exception {
        for (ProductStreamItem streamItem : streamItems) {

            if (updateCount.get() >= topSellerNum) {
                break;
            }

            Document importProduct = streamItem.getImportProduct();
            Document origProduct = streamItem.getOriginalProduct();

            ProductBatchUpdateInput updateParam = new ProductBatchUpdateInput();
            List<Operation> operations = Arrays.asList(new Operation("put", Field.prodTopSellRankNum, importProduct.get(Field.prodTopSellRankNum)),
                    new Operation("put", Field.lastUpdatedBy, JOB_NAME));
            updateParam.setOperations(operations);
            updateParam.setFilter(Collections.singletonMap(Field.prodId, origProduct.get(Field.prodId)));

            ProductBatchUpdateResult updateResult = productService.batchUpdateProduct(updateParam);
            updateResult.logUpdateResult(log);

            if (ctryRecCde.equals("HK") && grpMembrRecCde.equals(HBAP)) {
                ProductBatchUpdateInput updateParamCmb = new ProductBatchUpdateInput();
                // for cmb product
                Criteria criteria = new Criteria()
                        .and(Field.ctryRecCde).is(ctryRecCde)
                        .and(Field.grpMembrRecCde).is(dummy)
                        .and(Field.prodTypeCde).is(BOND_CD)
                        .and(Field.prodAltPrimNum).is(origProduct.get(Field.prodAltPrimNum))
                        .and(Field.allowBuyProdInd).is(INDICATOR_YES)
                        .and(Field.prodStatCde).nin(EXPIRED, DELISTED);
                updateParamCmb.setFilter(criteria.getCriteriaObject());
                updateParamCmb.setOperations(operations);

                ProductBatchUpdateResult updateResultCmb = productService.batchUpdateProduct(updateParamCmb);
                updateResultCmb.logUpdateResult(log);
            }

            updateCount.incrementAndGet();
        }
    }
}
