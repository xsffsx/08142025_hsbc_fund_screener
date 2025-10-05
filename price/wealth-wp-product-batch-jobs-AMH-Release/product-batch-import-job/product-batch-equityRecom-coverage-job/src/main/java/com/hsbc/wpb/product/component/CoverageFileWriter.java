package com.dummy.wpb.product.component;

import com.dummy.wpb.product.jpo.EquityRecommendationsPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@StepScope
public class CoverageFileWriter implements ItemWriter<EquityRecommendationsPo> {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void write(List<? extends EquityRecommendationsPo> items) {
        try {
            mongoTemplate.insertAll(items);
        } catch (Exception e) {
            log.error("Cannot insert records: " + items);
        }
    }
}
