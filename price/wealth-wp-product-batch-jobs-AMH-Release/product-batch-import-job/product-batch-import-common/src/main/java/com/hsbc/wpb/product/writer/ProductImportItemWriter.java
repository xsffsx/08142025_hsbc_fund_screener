package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.model.graphql.*;
import com.dummy.wpb.product.service.OperationService;
import com.dummy.wpb.product.service.ProductService;
import com.dummy.wpb.product.service.impl.DefaultOperationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.Document;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchImportAction.ADD;
import static com.dummy.wpb.product.constant.BatchImportAction.UPDATE;
import static com.dummy.wpb.product.util.ProductKeyUtils.buildProdKeyInfo;

@Slf4j
public class ProductImportItemWriter extends StepExecutionListenerSupport implements ItemWriter<ProductStreamItem>, ItemWriteListener<ProductStreamItem> {

    protected final ProductService productService;

    protected final OperationService operationService = new DefaultOperationService();

    public ProductImportItemWriter(ProductService productService) {
        this.productService = productService;
    }

    private final List<InvalidProduct> updatedFailedProducts = Collections.synchronizedList(new LinkedList<>());
    private final List<InvalidProduct> createdFailedProducts = Collections.synchronizedList(new LinkedList<>());

    private JobExecution jobExecution;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        Stream.concat(updatedFailedProducts.stream(), createdFailedProducts.stream()).forEach(invalidProduct -> {
            // only keep product key info
            Document productKey = new Document();
            Document product = invalidProduct.getProduct();
            productKey.put(Field.ctryRecCde, product.getString(Field.ctryRecCde));
            productKey.put(Field.grpMembrRecCde, product.getString(Field.grpMembrRecCde));
            productKey.put(Field.prodTypeCde, product.getString(Field.prodTypeCde));
            productKey.put(Field.prodAltPrimNum, product.getString(Field.prodAltPrimNum));
            invalidProduct.setProduct(productKey);
        });

        ExecutionContext executionContext = stepExecution.getExecutionContext();
        if (!updatedFailedProducts.isEmpty()) {
            executionContext.put("updatedFailedProducts", updatedFailedProducts);
        }
        if (!createdFailedProducts.isEmpty()) {
            executionContext.put("createdFailedProducts", createdFailedProducts);
        }

        int failedCount = updatedFailedProducts.size() + createdFailedProducts.size();
        stepExecution.setWriteSkipCount(stepExecution.getWriteSkipCount() + failedCount);
        stepExecution.setWriteCount(stepExecution.getWriteCount() - failedCount);
        return super.afterStep(stepExecution);
    }

    @Override
    public void write(List<? extends ProductStreamItem> streamItemList){
        List<ProductBatchUpdateByIdInput> updateParams = new ArrayList<>();
        List<Map<String, ?>> createdProducts = new ArrayList<>();
        String signature = jobExecution.getJobInstance().getJobName() + " -- " + jobExecution.getJobParameters().getString("systemCde");

        for (ProductStreamItem streamItem : streamItemList) {
            BatchImportAction actionCode = streamItem.getActionCode();

            Document updateProduct = streamItem.getUpdatedProduct();

            if (UPDATE == actionCode) {
                List<Operation> operations = operationService.calcOperations(streamItem.getOriginalProduct(), updateProduct);

                if (CollectionUtils.isEmpty(operations)) {
                    log.info("The product {} has not changed.", buildProdKeyInfo(updateProduct));
                    continue;
                }

                operations.add(new Operation("put", "lastUpdatedBy", signature));

                ProductBatchUpdateByIdInput updateParam = new ProductBatchUpdateByIdInput();
                updateParam.setProdId(updateProduct.get(Field.prodId, Number.class).longValue());
                updateParam.setOperations(operations);
                updateParams.add(updateParam);

            } else if (ADD == actionCode) {
                updateProduct.putIfAbsent("createdBy", signature);
                updateProduct.put("lastUpdatedBy", signature);
                updateProduct.put("recUpdtDtTm", OffsetDateTime.now().toString());
                createdProducts.add(operationService.decorate(updateProduct));
            } else {
                log.error("Product {} not support import action: {}", buildProdKeyInfo(updateProduct), actionCode);
            }
        }

        if (!updateParams.isEmpty()) {
            ProductBatchUpdateResult updateResult = productService.batchUpdateProductById(updateParams);
            updateResult.logUpdateResult(log);
            updatedFailedProducts.addAll(updateResult.getInvalidProducts());
        }

        if (!createdProducts.isEmpty()) {
            ProductBatchCreateResult createResult = productService.batchCreateProduct(createdProducts);
            createResult.logCreateResult(log);
            createdFailedProducts.addAll(createResult.getInvalidProducts());
        }
    }

    @Override
    public void beforeWrite(List<? extends ProductStreamItem> items) {
        // do nothing by default
    }

    @Override
    public void afterWrite(List<? extends ProductStreamItem> items) {
        // do nothing by default
    }

    @Override
    public void onWriteError(Exception exception, List<? extends ProductStreamItem> items) {
        // do nothing by default
    }
}