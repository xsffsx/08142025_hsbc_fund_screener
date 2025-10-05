package com.dummy.wpb.product.writer;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.InvestorCharacter;
import com.dummy.wpb.product.model.graphql.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class InvestCharIndRetryItemWriter extends InvestCharIndCommonItemWriter {

    @Override
    protected List<Operation> doWrite(InvestorCharacter character) {
        List<Operation> operations = super.doWrite(character);

        Query query = new Query()
                .addCriteria(Criteria.where(Field.ctryRecCde).is(character.getCtryRecCde()))
                .addCriteria(Criteria.where(Field.grpMembrRecCde).is(character.getGrpMembrRecCde()))
                .addCriteria(Criteria.where(Field.prodTypeCde).is(character.getProdTypeCde()))
                .addCriteria(Criteria.where(Field.prodCde).is(character.getProdAltPrimNum()));
        mongoTemplate.remove(query, CollectionName.prod_dervt_handl_reqmt.name());

        return operations;
    }
}
