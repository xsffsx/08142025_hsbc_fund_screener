package com.dummy.wpb.product.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
@StepScope
@Slf4j
public class GacItemWriter implements ItemWriter<Map<String, Object>> {

    @Autowired
    private GacService gacService;

    @Override
    public void write(List<? extends Map<String, Object>> items) {
        if (CollectionUtils.isEmpty(items)) {
            log.error("No records can be processed.");
        }

        for (Map<String,Object> item : items) {
            try {
                //update in product by mongoTemplate
                gacService.updateProducts(item);
            } catch (Exception e) {
                log.error(String.format("Cannot process this record: %s. Error message: %s", item, e.getMessage()));
            }
        }
    }
}
