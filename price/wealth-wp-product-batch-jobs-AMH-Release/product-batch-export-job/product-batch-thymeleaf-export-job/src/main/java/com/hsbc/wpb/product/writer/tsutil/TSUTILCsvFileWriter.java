package com.dummy.wpb.product.writer.tsutil;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.writer.CsvTemplateWriter;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.dummy.wpb.product.constant.BatchConstants.REUTERS_PRICE_STATUS_POPULATED;

@Slf4j
public class TSUTILCsvFileWriter extends CsvTemplateWriter {

    protected int invalidCount = 0;

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.setWriteSkipCount(invalidCount);
        stepExecution.setWriteCount(stepExecution.getWriteCount() - invalidCount);
        return super.afterStep(stepExecution);
    }

    @Override
    protected String doWrite(List<? extends Document> products) {
        if (!products.isEmpty()) {
            List<Object> prodIdList = new ArrayList<>();
            for (Document product : products) {
                prodIdList.add(product.get(Field.prodId));
                if (product.get("prices", Collections.emptyList()).isEmpty()) {
                    invalidCount++;
                }
            }

            Query query = new Query(Criteria.where(Field.prodId).in(prodIdList));
            Update update = new Update()
                    .set(Field.priceHistSrceRdyStatCde, REUTERS_PRICE_STATUS_POPULATED)
                    .set("priceHistDelvrDtTm", LocalDateTime.now())
                    .set(Field.recUpdtDtTm, LocalDateTime.now());
            UpdateResult updateResult = mongoTemplate.updateMulti(query, update, CollectionName.product.name());

            if (updateResult.getModifiedCount() == products.size()) {
                products.forEach(product -> log.info(
                        "Product(ctryRecCde: {}, grpMembrRecCde: {}, prodAltNum(T): {}) has been updated , exported {} prices.",
                        ctryRecCde,
                        grpMembrRecCde,
                        product.get("prodAltNumT"),
                        product.get("prices", Collections.emptyList()).size()
                ));
            } else {
                log.info("Failed to update products(ctryRecCde: {}, grpMembrRecCde: {}, prodAltNums(T): {}).",
                        ctryRecCde,
                        grpMembrRecCde,
                        products.stream().map(product -> product.get("prodAltNumT")).collect(Collectors.toList()));
            }
        }

        return super.doWrite(products);
    }
}
