package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.model.InvestorCharacterUtil;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.model.InvestorCharacter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static com.dummy.wpb.product.model.InvestorCharacterUtil.toMongoQuery;

@Slf4j
@Component
public class InvestCharIndItemProcessor implements ItemProcessor<Object, InvestorCharacter> {

    @Autowired
    MongoTemplate mongoTemplate;


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
            log.error("Missed [Product Code] or [HFI Product Type] for product{}", InvestorCharacterUtil.toProductKeyInfo(character));
            return null;
        }

        if (!mongoTemplate.exists(toMongoQuery(character), CollectionName.product.name())) {
            log.warn("Skip non-existing product{}", InvestorCharacterUtil.toProductKeyInfo(character));
            return null;
        }
        return character;
    }
}
