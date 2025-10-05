package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.model.InvestorCharacterUtil;
import com.dummy.wpb.product.model.InvestorCharacter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class InvestCharIndDailyUpdateItemProcessor implements ItemProcessor<InvestorCharacter, InvestorCharacter> {
    @Override
    public InvestorCharacter process(InvestorCharacter character) {
        if (StringUtils.equals(character.getProdDervtCde(), character.getProdDervtRvsCde())) {
            log.info("Skip no change record {}", InvestorCharacterUtil.toProductKeyInfo(character));
            return null;
        }
        return character;
    }
}
