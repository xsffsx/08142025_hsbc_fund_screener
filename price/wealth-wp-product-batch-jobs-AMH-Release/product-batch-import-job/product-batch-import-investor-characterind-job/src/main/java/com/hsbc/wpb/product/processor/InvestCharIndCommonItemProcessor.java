package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.InvestorCharacter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;

import static com.dummy.wpb.product.model.InvestorCharacterUtil.toMongoQuery;
import static com.dummy.wpb.product.model.InvestorCharacterUtil.toProductKeyInfo;

@Slf4j
public class InvestCharIndCommonItemProcessor implements ItemProcessor<Object, InvestorCharacter> {

    public static final String PRODUCT_DELETION = "R";
    public static final String ADD_CHANGE_MARKET_CODE = "M";

    private final boolean exRetry;

    private final MongoTemplate mongoTemplate;

    public InvestCharIndCommonItemProcessor(MongoTemplate mongoTemplate, boolean exRetry) {
        this.mongoTemplate = mongoTemplate;
        this.exRetry = exRetry;
    }

    @Override
    public InvestorCharacter process(Object item) throws Exception {
        if (item instanceof String) {
            log.info(item.toString());
            return null;
        }

        InvestorCharacter character = (InvestorCharacter) item;

        String prodTypeCde = character.getInternalProdTypeCde();
        String prodAltPrimNum = character.getProdAltPrimNum();

        if (StringUtils.isAnyBlank(prodTypeCde, prodAltPrimNum)) {
            String message = String.format("Missed [Product Code] or [HFI Product Type] for product{%s}", toProductKeyInfo(character));
            log.error(message);
            throw new InvalidRecordException(message);
        }

        String mainCode = character.getMaintCde();
        if (StringUtils.isBlank(mainCode)) {
            log.info("Missed [Maintanence Code] for product{}", toProductKeyInfo(character));
            return null;
        }

        if (!exRetry && StringUtils.equalsAny(mainCode, PRODUCT_DELETION, ADD_CHANGE_MARKET_CODE)) {
            log.info("Maintanence code is R/M, no need update for product{}", toProductKeyInfo(character));
            return null;
        }

        if (!mongoTemplate.exists(toMongoQuery(character), CollectionName.product.name())) {
            if (exRetry) {
                String message = String.format("Reject non-existing product and keep record in exception table %s", toProductKeyInfo(character));
                log.error(message);
                throw new InvalidRecordException(message);
            } else {
                addNonExistProdToExceptionTable(character);
                log.info("Skip non-existing product and add to exception table {}", toProductKeyInfo(character));
            }
            return null;
        }

        return character;
    }

    private void addNonExistProdToExceptionTable(InvestorCharacter character) {
        Query query = new Query()
                .addCriteria(Criteria.where(Field.ctryRecCde).is(character.getCtryRecCde()))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(character.getGrpMembrRecCde()))
                .addCriteria(Criteria.where(Field.prodTypeCde).is(character.getProdTypeCde()))
                .addCriteria(Criteria.where(Field.prodCde).is(character.getProdAltPrimNum()));

        Update update = new Update()
                .setOnInsert(Field.ctryRecCde, character.getCtryRecCde())
                .setOnInsert(Field.grpMembrRecCde, character.getGrpMembrRecCde())
                .setOnInsert(Field.prodTypeCde, character.getInternalProdTypeCde())
                .setOnInsert(Field.prodCde, character.getProdAltPrimNum())
                .setOnInsert(Field.recCreatDtTm, new Date())
                .set(Field.prodSubtpCde, character.getInternalProdSubTypeCde())
                .set("recMaintActnCde", character.getMaintCde())
                .set("mktProdTrdCde", character.getProdMktCde())
                .set(Field.prodDervtCde, character.getProdDervtCde())
                .set(Field.prodDervtRvsCde, character.getProdDervtRvsCde())
                .set("prdDervRvsEffIntfDt", character.getProdDervRvsEffDt())
                .set(Field.recUpdtDtTm, new Date());

        mongoTemplate.upsert(query, update, CollectionName.prod_dervt_handl_reqmt.name());
    }
}
