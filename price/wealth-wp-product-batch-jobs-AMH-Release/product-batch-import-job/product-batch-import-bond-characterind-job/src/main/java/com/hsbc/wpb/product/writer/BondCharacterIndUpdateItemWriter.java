package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.BondCharacter;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.Updates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dummy.wpb.product.ImportBondCharacterIndJonApplication.JOB_NAME;
import static com.dummy.wpb.product.constant.BatchConstants.DELISTED;
import static com.dummy.wpb.product.constant.BatchConstants.dummy;

@Component
@StepScope
@Slf4j
public class BondCharacterIndUpdateItemWriter implements ItemWriter<BondCharacter> {

    @Value("#{jobParameters['ctryRecCde']}")
    private String ctryRecCde;

    @Value("#{jobParameters['grpMembrRecCde']}")
    private String grpMembrRecCde;

    MongoCollection<Document> productCollection;

    public BondCharacterIndUpdateItemWriter(MongoDatabase mongoDatabase) {
        this.productCollection = mongoDatabase.getCollection(CollectionName.product.toString());
    }

    private static final String RBP_MIG_IND = "debtInstm.rbpMigrInd";

    @Override
    public void write(List<? extends BondCharacter> bondCharacterList) {
        if (bondCharacterList.isEmpty()) {return;}
        List<String> bondCharProdAltPrimNums = bondCharacterList.stream().map(BondCharacter::getProdAltPrimNum).collect(Collectors.toList());
        List<Document> prodDocs = new ArrayList<>(findExistProduct(grpMembrRecCde, bondCharProdAltPrimNums));
        logNonExistRecord(bondCharProdAltPrimNums, prodDocs);

        if (!prodDocs.isEmpty()) {
            List<UpdateOneModel<Document>> udblist = new ArrayList<>();
            List<String> prodAltPrimNums = prodDocs.stream().map(prodDoc -> prodDoc.getString(Field.prodAltPrimNum)).collect(Collectors.toList());
            if (!dummy.equals(grpMembrRecCde)) {
                List<Document> cmbProducts = findExistProduct(dummy, prodAltPrimNums);
                prodDocs.addAll(cmbProducts);
            }
            for (Document product : prodDocs) {
                bondCharacterList.stream()
                        .filter(bond -> product.getString(Field.prodAltPrimNum).equals(bond.getProdAltPrimNum()))
                        .findFirst().ifPresent(bondCharacter -> {
                            if (!valiEqualField(product, bondCharacter)) {
                                udblist.add(update(product, bondCharacter));
                            } else {
                                log.info("Skip no need to update record.(ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                                        product.getString(Field.ctryRecCde), product.getString(Field.grpMembrRecCde), "BOND", product.getString(Field.prodAltPrimNum));
                            }
                        });
            }
            batchWrite(udblist, prodDocs);
        }
    }

    private void batchWrite(List<UpdateOneModel<Document>> udblist, List<Document> prodDocs) {
        if (!udblist.isEmpty()) {
            BulkWriteResult bulkWriteResult = productCollection.bulkWrite(udblist);
            logSuccessRecord(udblist.size(), bulkWriteResult.getModifiedCount(), prodDocs);
        }
    }

    private boolean valiEqualField(Document product, BondCharacter bondCharacter) {
        return Objects.equals(product.getString(Field.qtyUnitProdCde), bondCharacter.getQtyTypeCde())
                && Objects.equals(product.getString(Field.prodLocCde), bondCharacter.getProdLocCde())
                && Objects.equals(JsonPathUtils.readValue(product, RBP_MIG_IND), bondCharacter.getRbpMigrInd());
    }

    private void logSuccessRecord(int updateListSize, int modifiedCount, List<Document> prodDocs) {
        if (Objects.equals(modifiedCount, updateListSize)) {
            prodDocs.forEach(product ->  log.info("Product has been updated.(ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                    product.getString(Field.ctryRecCde), product.getString(Field.grpMembrRecCde), "BOND", product.getString(Field.prodAltPrimNum)));
        }else {
            log.error("Expected update count: " + updateListSize + ". Actually updated count: " + modifiedCount);
        }
    }

    private void logNonExistRecord(List<String> prodAltPrimNums, List<Document> prodDocs) {
        prodAltPrimNums.removeIf(bondCharacterAltPrimNum -> prodDocs.stream().anyMatch(prodDoc -> StringUtils.equals(prodDoc.getString(Field.prodAltPrimNum), bondCharacterAltPrimNum)));
        prodAltPrimNums.forEach(bondCharacterAltPrimNum -> log.info("Skip non-existing product(ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                ctryRecCde, grpMembrRecCde, "BOND", bondCharacterAltPrimNum));
    }

    private List<Document> findExistProduct(String grpMembrRecCde, List<String> prodAltPrimNums) {
        List<Bson> query = Arrays.asList(Filters.eq(Field.ctryRecCde, ctryRecCde),
                Filters.eq(Field.grpMembrRecCde, grpMembrRecCde),
                Filters.in(Field.prodAltPrimNum, prodAltPrimNums),
                Filters.ne(Field.prodStatCde, DELISTED));
        return productCollection.find(Filters.and(query))
                .projection(Projections.include(Field.prodId, Field.prodAltPrimNum, RBP_MIG_IND, Field.grpMembrRecCde, Field.ctryRecCde, Field.qtyUnitProdCde, Field.prodLocCde))
                .into(new ArrayList<>());
    }

    private UpdateOneModel<Document> update(Document product, BondCharacter bondCharacter) {
        Long prodId = product.getLong(Field.prodId);
        List<Bson> updateList = Stream.of(Updates.set(Field.qtyUnitProdCde, bondCharacter.getQtyTypeCde()),
                Updates.set(Field.prodLocCde, bondCharacter.getProdLocCde()),
                Updates.set(Field.recUpdtDtTm, LocalDateTime.now()),
                Updates.set(Field.lastUpdatedBy, JOB_NAME)).collect(Collectors.toList());
        if (JsonPathUtils.readValue(product, RBP_MIG_IND) != null) {
            updateList.add(Updates.set(RBP_MIG_IND, bondCharacter.getRbpMigrInd()));
        }
        Bson updates = Updates.combine(updateList);
        return new UpdateOneModel<>(Filters.eq(Field.prodId, prodId), updates);
    }
}
