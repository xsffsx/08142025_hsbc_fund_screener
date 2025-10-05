package com.dummy.wpb.product.component;

import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.dummy.wpb.product.ImportGacJsonJobApplication.JOB_NAME;

@StepScope
@Service
@Slf4j
public class GacService {

    @Value("#{stepExecution}")
    private StepExecution stepExecution;

    @Value("#{jobExecutionContext['prodNotFound']}")
    private Integer prodNotFoundNum;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void updateProducts(Map<String,Object> item) {
        // update product with gac data
        Update update = new Update();
        update.set(Field.gac, item.get(Field.gac));
        update.set(Field.volatility, item.get(Field.volatility));
        update.set(Field.lastUpdatedBy, JOB_NAME);
        update.set(Field.recUpdtDtTm, new LocalDateTime());

        Query query = new Query();
        query.addCriteria(Criteria.where(Field.prodId).is(item.get(Field.prodId)));

        UpdateResult result = mongoTemplate.updateFirst(query, update, String.valueOf(CollectionName.product));
        if(result.getModifiedCount() == 0) {
            log.error("Product not found. Product id: " + item.get(Field.prodId));
            prodNotFoundNum ++;
            stepExecution.getJobExecution().getExecutionContext().put("prodNotFound", prodNotFoundNum);
        }
    }

}
