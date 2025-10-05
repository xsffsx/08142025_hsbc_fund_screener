package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.model.InvestorCharacterUtil;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Date;

import static com.dummy.wpb.product.model.InvestorCharacterUtil.*;

@Slf4j
public class InvestCharMonthlyReconItemProcessor implements ItemProcessor<Object, InvestorCharacter> {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public InvestorCharacter process(Object item) {
        if (item instanceof String) {
            log.info(item.toString());
            return null;
        }

        InvestorCharacter character = (InvestorCharacter) item;

        Query query = toMongoQuery(character);
        query.fields().include(Field.prodDervtCde, Field.prodDervtRvsCde, Field.prdDervRvsEffDt);
        Document history = mongoTemplate.findOne(query, Document.class, CollectionName.product.name());
        if (null == history) {
            log.warn("Skip non-existing product{}", InvestorCharacterUtil.toProductKeyInfo(character));
            return null;
        }

        if (compareICFlag(character, history)) {
            log.info("Record is matched for product {}.", toProductKeyInfo(character));
            return null;
        }

        return character;
    }

    private boolean compareICFlag(final InvestorCharacter character, final Document history) {
        Date historyEffDt = history.getDate(Field.prdDervRvsEffDt);
        String historyEffDtStr = null;
        if (null != historyEffDt) {
            historyEffDtStr = DateFormatUtils.format(historyEffDt, EFFECTIVE_DATE_FORMAT);
        }

        return StringUtils.equals(character.getProdDervtCde(), history.getString(Field.prodDervtCde))
                && StringUtils.equals(character.getProdDervtRvsCde(), history.getString(Field.prodDervtRvsCde))
                && (StringUtils.equals(character.getProdDervRvsEffDt(), historyEffDtStr)
                || StringUtils.isAllBlank(character.getProdDervRvsEffDt(), historyEffDtStr)
                || (EFFECTIVE_DATE.equals(character.getProdDervRvsEffDt()) && StringUtils.isBlank(historyEffDtStr)));

    }
}
