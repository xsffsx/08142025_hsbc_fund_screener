package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.util.ProductKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@StepScope
@Slf4j
public class SecXmlFileValidator implements Validator<ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void validate(ProductStreamItem streamItem) throws ValidationException {
        Document importProd = streamItem.getImportProduct();
        if (BatchConstants.SYS_CDE_AMHGSOPS_AS.equals(systemCde)
                && streamItem.getActionCode() == BatchImportAction.ADD
                && BatchConstants.DELISTED.equals(importProd.getString(Field.prodStatCde))) {
            Query query = new Query();
            query.addCriteria(Criteria.where(Field.ctryRecCde).is(importProd.getString(Field.ctryRecCde)));
            query.addCriteria(Criteria.where(Field.grpMembrRecCde).is(importProd.getString(Field.grpMembrRecCde)));
            query.addCriteria(Criteria.where(Field.prodAltPrimNum).is(importProd.getString(Field.prodAltPrimNum)));
            query.addCriteria(Criteria.where(Field.prodTypeCde).is(importProd.getString(Field.prodTypeCde)));
            query.addCriteria(Criteria.where(Field.prodStatCde).is(importProd.getString(Field.prodStatCde)));
            if (mongoTemplate.exists(query, CollectionName.product.name())) {
                String message = String.format("The system %s not support to create deleted product%s.", systemCde, ProductKeyUtils.buildProdKeyInfo(importProd));
                log.error(message);
                throw new ValidationException(message);
            }
        }
    }
}
