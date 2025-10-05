package com.dummy.wmd.wpc.graphql.service;

import com.dummy.wmd.wpc.graphql.constant.CollectionName;
import com.dummy.wmd.wpc.graphql.constant.Field;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistory;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryBatchImportResult;
import com.dummy.wmd.wpc.graphql.model.ProductPriceHistoryValidationResult;
import com.dummy.wmd.wpc.graphql.validator.Error;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

@Slf4j
@Service
public class PriceHistoryService {
    protected MongoCollection<Document> collPrcHist;
    private static final UpdateOptions options = new UpdateOptions().upsert(true);

    public PriceHistoryService(MongoDatabase mongodb) {
        this.collPrcHist = mongodb.getCollection(CollectionName.prod_prc_hist.toString());
    }

    private void update(List<ProductPriceHistory> priceHistoryList) {
        if (CollectionUtils.isEmpty(priceHistoryList)) return ;

        // proceed update
        List<UpdateOneModel<Document>> modelList = new ArrayList<>();
        for (ProductPriceHistory priceHistory : priceHistoryList) {
            Bson filter = and(eq(Field.prodId, priceHistory.getProdId()), eq(Field.pdcyPrcCde, priceHistory.getPdcyPrcCde()), eq(Field.prcEffDt, priceHistory.getPrcEffDt()));
            Bson updates = Updates.combine(
                    Updates.setOnInsert(Field.prodId, priceHistory.getProdId()),
                    Updates.setOnInsert(Field.pdcyPrcCde, priceHistory.getPdcyPrcCde()),
                    Updates.setOnInsert(Field.prcEffDt, priceHistory.getPrcEffDt()),
                    Updates.set(Field.prcInpDt, priceHistory.getPrcInpDt()),
                    Updates.set(Field.ccyProdMktPrcCde, priceHistory.getCcyProdMktPrcCde()),
                    Updates.set(Field.prodBidPrcAmt, priceHistory.getProdBidPrcAmt()),
                    Updates.set(Field.prodOffrPrcAmt, priceHistory.getProdOffrPrcAmt()),
                    Updates.set(Field.prodMktPrcAmt, priceHistory.getProdMktPrcAmt()),
                    Updates.set(Field.prodNavPrcAmt, priceHistory.getProdNavPrcAmt()),
                    Updates.setOnInsert(Field.recCreatDtTm, ObjectUtils.defaultIfNull(priceHistory.getRecCreatDtTm(), LocalDateTime.now())),
                    Updates.set(Field.recUpdtDtTm, ObjectUtils.defaultIfNull(priceHistory.getRecUpdtDtTm(), LocalDateTime.now()))
            );
            modelList.add(new UpdateOneModel<>(filter, updates, options));
        }

        collPrcHist.bulkWrite(modelList);
    }

    public ProductPriceHistoryBatchImportResult batchImport(List<ProductPriceHistory> priceHistoryList, boolean allowPartial) {
        ProductPriceHistoryBatchImportResult importResult = new ProductPriceHistoryBatchImportResult();

        if (CollectionUtils.isEmpty(priceHistoryList)){
            return importResult;
        }

        List<ProductPriceHistoryValidationResult> invalidPriceHistories = new ArrayList<>();
        List<ProductPriceHistory> importedPriceHistories = new ArrayList<>();

        priceHistoryList.forEach(priceHistory -> {
            Long prodId = priceHistory.getProdId();
            LocalDate prcEffDt = priceHistory.getPrcEffDt();
            String ccyProdMktPrcCde = priceHistory.getCcyProdMktPrcCde();
            Double prodBidPrcAmt = priceHistory.getProdBidPrcAmt();
            Double prodOffrPrcAmt = priceHistory.getProdOffrPrcAmt();
            Double prodMktPrcAmt = priceHistory.getProdMktPrcAmt();
            Double prodNavPrcAmt = priceHistory.getProdNavPrcAmt();
            if (StringUtils.isBlank(priceHistory.getPdcyPrcCde())) {
                priceHistory.setPdcyPrcCde("D");
            }
            List<Error> errorList = this.validate(prodId, prcEffDt, ccyProdMktPrcCde, prodBidPrcAmt, prodOffrPrcAmt, prodMktPrcAmt, prodNavPrcAmt);
            if (!errorList.isEmpty()) {
                ProductPriceHistoryValidationResult validationResult = new ProductPriceHistoryValidationResult();
                validationResult.setPriceHistory(priceHistory);
                validationResult.setErrors(errorList);
                invalidPriceHistories.add(validationResult);
                return;
            }

            importedPriceHistories.add(priceHistory);
        });

        importResult.setInvalidPriceHistories(invalidPriceHistories);
        if (!invalidPriceHistories.isEmpty() && !allowPartial) {
            return importResult;
        }

        this.update(importedPriceHistories);

        importResult.setImportedPriceHistories(importedPriceHistories);
        return importResult;
    }

    /**
     * Validate input parameters
     *
     * @param prodId
     * @param prcEffDt
     * @param ccyProdMktPrcCde
     * @param prodBidPrcAmt
     * @param prodOffrPrcAmt
     * @param prodMktPrcAmt
     * @param prodNavPrcAmt
     * @return
     */
    public List<Error> validate(Long prodId, LocalDate prcEffDt, String ccyProdMktPrcCde, Double prodBidPrcAmt, Double prodOffrPrcAmt, Double prodMktPrcAmt, Double prodNavPrcAmt) {
        List<Error> errorList = new ArrayList<>();
        if (null == prodId) {
            errorList.add(new Error("prodId", "prodId@Required", "Invalid product price history data: prodId is null"));

        }
        if (null == prcEffDt) {
            errorList.add(new Error("prcEffDt", "prcEffDt@Required", "Invalid product price history data: prcEffDt is null"));
        } else if (prcEffDt.isAfter(LocalDate.now())) {
            String message = String.format("Invalid product price history data: prcEffDt should not be after current day: %s", prcEffDt);
            errorList.add(new Error("prcEffDt", "prcEffDt@Illegal", message));
        }
        if (null == ccyProdMktPrcCde) {
            errorList.add(new Error("ccyProdMktPrcCde", "ccyProdMktPrcCde@Required", "Invalid product price history data: ccyProdMktPrcCde is null"));
        }
        if (null == prodBidPrcAmt && null == prodOffrPrcAmt && null == prodMktPrcAmt && null == prodNavPrcAmt) {
            errorList.add(new Error("priceAmount", "priceAmount@Required",
                    "Invalid product price history data: prodNavPrcAmt, prodMktPrcAmt, prodBidPrcAmt, prodOffrPrcAmt: At least one of these 4 fields is not null."));
        }
        return errorList;
    }
}
