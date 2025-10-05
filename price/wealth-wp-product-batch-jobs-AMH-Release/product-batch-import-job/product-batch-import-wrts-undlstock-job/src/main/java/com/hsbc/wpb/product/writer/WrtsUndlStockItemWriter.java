package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.error.ErrorCode;
import com.dummy.wpb.product.error.ErrorLogger;
import com.dummy.wpb.product.model.Product;
import com.dummy.wpb.product.model.UndlStock;
import com.dummy.wpb.product.model.WrtsUndlStockRecord;
import com.mongodb.bulk.BulkWriteResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.ImportWrtsUndlStockConfiguration.JOB_NAME;
import static com.dummy.wpb.product.constant.BatchConstants.*;
import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ000;
import static com.dummy.wpb.product.error.ErrorCode.OTPSERR_EBJ103;
import static com.dummy.wpb.product.utils.ImportWrtsUndlStockUtils.*;

@Component
@StepScope
@Slf4j
public class WrtsUndlStockItemWriter implements ItemWriter<WrtsUndlStockRecord> {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    private final MongoTemplate mongoTemplate;

    private static final String STOCK_INSTM_UNDL_STOCK = "stockInstm.undlStock";

    private static final String PROD_ID_UNDL_INSTM = "prodIdUndlInstm";

    private static final String INSTM_UNDL_CDE = "instmUndlCde";


    public WrtsUndlStockItemWriter(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void write(List<? extends WrtsUndlStockRecord> items) {
        log.debug("Number of items to write in this chunk: {}", items.size());
        if (items.isEmpty()) {
            return;
        }
        Map<String, Product> existingWrtsProductMap = new HashMap<>();
        Map<String, Product> existingUndlProductMap = new HashMap<>();

        fillExistingWrtsAndUndlProductMaps(items, existingWrtsProductMap, existingUndlProductMap);

        List<WrtsUndlStockRecord> recordsToUpdate = new ArrayList<>(); // For logging purpose

        Map<String, WrtsUndlStockRecord> securityCodeToRecordMap = items.stream()
                                                                        .collect(Collectors.toMap(
                                                                                WrtsUndlStockRecord::getSecurityCode,
                                                                                rec -> rec,
                                                                                // In case the file has records with duplicate security codes
                                                                                (rec1, rec2) -> {
                                                                                    String message = String.format(
                                                                                            "%s: Duplicate security code",
                                                                                            getRecordInfoString(rec1)
                                                                                    );
                                                                                    log.error(message);
                                                                                    logErrorToCloud(
                                                                                            OTPSERR_EBJ000,
                                                                                            message
                                                                                    );
                                                                                    return rec2;
                                                                                }
                                                                        ));

        List<Pair<Query, Update>> updates = prepareUpdates(
                securityCodeToRecordMap,
                existingWrtsProductMap,
                existingUndlProductMap,
                recordsToUpdate
        );

        if (updates.isEmpty()) {
            log.info("No products to update in this chunk.");
        } else {
            BulkWriteResult result = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Product.class)
                                                  .updateOne(updates)
                                                  .execute();
            logUpdateResult(result, recordsToUpdate);
        }
    }

    private void fillExistingWrtsAndUndlProductMaps(
            List<? extends WrtsUndlStockRecord> items,
            Map<String, Product> existingWrtsProductMap,
            Map<String, Product> existingUndlProductMap) {
        // WRTS product M code list
        List<String> securityCodeList = items.stream()
                                             .map(WrtsUndlStockRecord::getSecurityCode)
                                             .collect(Collectors.toList());
        // Underlying SEC product M code list
        List<String> underlyingCodeList = items.stream()
                                               .map(WrtsUndlStockRecord::getUnderlyingCode)
                                               .collect(Collectors.toList());
        List<Product> combinedExistingProductList = findCombinedExistingProducts(securityCodeList, underlyingCodeList);
        combinedExistingProductList.forEach(product -> {
            if (WARRANT.equals(product.getProdTypeCde())) {
                existingWrtsProductMap.put(product.getMCode(), product);
            } else {
                existingUndlProductMap.put(product.getMCode(), product);
            }
        });
        logNonExistingRecords(items, existingWrtsProductMap);
    }

    private List<Product> findCombinedExistingProducts(
            List<String> securityCodeList,
            List<String> underlyingCodeList) {
        String altIdProdAltNum = String.format("%s.%s", Field.altId, Field.prodAltNum);
        Query query = new Query();
        query.addCriteria(Criteria.where(Field.ctryRecCde)
                                  .is(ctryRecCde)
                                  .and(Field.grpMembrRecCde)
                                  .is(grpMembrRecCde)
                                  .and(Field.prodStatCde)
                                  .nin(DELISTED, EXPIRED));
        query.addCriteria(new Criteria().orOperator(
                // WRTS products
                Criteria.where(Field.prodTypeCde)
                        .is(WARRANT)
                        .and(altIdProdAltNum)
                        .in(securityCodeList)
                        .and(Field.altId)
                        .elemMatch(Criteria.where(Field.prodCdeAltClassCde)
                                           .is(PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET)
                                           .and(Field.prodAltNum)
                                           .in(securityCodeList)),
                // Underlying SEC products
                Criteria.where(Field.prodTypeCde)
                        .is(SECURITY)
                        .and(altIdProdAltNum)
                        .in(underlyingCodeList)
                        .and(Field.altId)
                        .elemMatch(Criteria.where(Field.prodCdeAltClassCde)
                                           .is(PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET)
                                           .and(Field.prodAltNum)
                                           .in(underlyingCodeList))
        ));
        return mongoTemplate.find(query, Product.class);
    }

    private List<Pair<Query, Update>> prepareUpdates(
            Map<String, WrtsUndlStockRecord> securityCodeToRecordMap,
            Map<String, Product> existingWrtsProductMap,
            Map<String, Product> existingUndlProductMap,
            List<WrtsUndlStockRecord> recordsToUpdate) {
        List<Pair<Query, Update>> updates = new ArrayList<>();
        existingWrtsProductMap.forEach((wrtsMCode, wrtsProduct) -> {
            WrtsUndlStockRecord rec = securityCodeToRecordMap.get(wrtsMCode);
            Product newUndlProduct = existingUndlProductMap.get(rec.getUnderlyingCode());
            UndlStock oldUndlStock = wrtsProduct.getUndlStock();

            if (ObjectUtils.allNull(oldUndlStock, newUndlProduct)) {
                logSkippedRecord(
                        rec,
                        "This record doesn't have underlying product or the underlying product doesn't exist in SMART+. Skipping."
                );
                return;
            }

            if (isUndlProductAlreadyRegistered(oldUndlStock, newUndlProduct)) {
                logSkippedRecord(
                        rec,
                        "The underlying SEC product was already registered to this WRTS product in SMART+. Skipping."
                );
                return;
            }

            if (oldUndlStock != null) {
                String message = String.format(
                        "Existing underlying stock for WRTS product (ID: %d, M code: %s) is SEC product (ID: %d, P code: %s). Trying to update with a new underlying stock M code \"%s\".",
                        wrtsProduct.getProdId(),
                        wrtsMCode,
                        oldUndlStock.getProdIdUndlInstm(),
                        oldUndlStock.getInstmUndlCde(),
                        rec.getUnderlyingCode()
                );
                log.error(message);
                logErrorToCloud(OTPSERR_EBJ000, message);
            }

            updates.add(updateUndlStock(wrtsProduct.getProdId(), newUndlProduct));
            recordsToUpdate.add(rec);
        });
        return updates;
    }

    private boolean isUndlProductAlreadyRegistered(UndlStock oldUndlStock, Product newUndlProduct) {
        Long newUndlProductId = Optional.ofNullable(newUndlProduct).map(Product::getProdId).orElse(null);
        return (oldUndlStock != null) && Objects.equals(oldUndlStock.getProdIdUndlInstm(), newUndlProductId);
    }

    private Pair<Query, Update> updateUndlStock(Long prodId, Product newUndlProduct) {
        Query query = new Query().addCriteria(Criteria.where(Field.prodId).is(prodId));

        Update update = new Update();

        LocalDateTime now = LocalDateTime.now();

        if (newUndlProduct == null) {
            update.unset(STOCK_INSTM_UNDL_STOCK);
        } else {
            Document undlStock = new Document();
            undlStock.put(Field.rowid, UUID.randomUUID().toString());
            undlStock.put(PROD_ID_UNDL_INSTM, newUndlProduct.getProdId());
            undlStock.put(INSTM_UNDL_CDE, newUndlProduct.getProdAltPrimNum());
            undlStock.put(Field.recCreatDtTm, now);
            undlStock.put(Field.recUpdtDtTm, now);
            List<Document> undlStockList = Collections.singletonList(undlStock);
            update.set(STOCK_INSTM_UNDL_STOCK, undlStockList);
        }
        update.set(Field.recUpdtDtTm, now)
              .set(Field.lastUpdatedBy, JOB_NAME);
        return Pair.of(query, update);
    }

    private void logSkippedRecord(WrtsUndlStockRecord rec, String reason) {
        log.info("{}: {}", getRecordInfoString(rec), reason);
        incrementSkippedCount(stepExecution);
    }

    private void logNonExistingRecords(
            List<? extends WrtsUndlStockRecord> items,
            Map<String, Product> existingWrtsProductMap) {
        items.forEach(item -> {
            if (!existingWrtsProductMap.containsKey(item.getSecurityCode())) {
                logSkippedRecord(item, "This WRTS product doesn't exist in SMART+. Skipping.");
            }
        });
    }

    private void logUpdateResult(BulkWriteResult result, List<WrtsUndlStockRecord> recordsToUpdate) {
        int modifiedCount = result.getModifiedCount();
        int expectedUpdateCount = recordsToUpdate.size();
        if (modifiedCount == expectedUpdateCount) {
            recordsToUpdate.forEach(rec -> log.info(
                    "{}: This WRTS product has been successfully updated in SMART+",
                    getRecordInfoString(rec)
            ));
        } else {
            int failedCount = expectedUpdateCount - modifiedCount;
            String message = String.format(
                    "%d products were to be updated in SMART+ but only %d were updated. For some reason, %d products failed to get updated.",
                    expectedUpdateCount,
                    modifiedCount,
                    failedCount
            );
            log.error(message);
            logErrorToCloud(OTPSERR_EBJ103, message);
            incrementFailedCount(stepExecution, failedCount);
        }
        incrementUpdatedCount(stepExecution, modifiedCount);
    }

    private void logErrorToCloud(ErrorCode errorCode, String message) {
        ErrorLogger.logErrorMsg(errorCode, JOB_NAME, message);
    }
}
