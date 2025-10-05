package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.CollectionName;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.util.ProductKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.joda.time.LocalDateTime;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@StepScope
public class UtPerfmXmlProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {

    private static final String PERFORMANCE_TYPE = "P";
    private static final String BENCHMARK_TYPE = "B";

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Autowired
    private SystemUpdateConfigHolder systemUpdateConfigHolder;


    @Override
    public ProductStreamItem process(ProductStreamItem streamItem){
        BatchImportAction actionCode = streamItem.getActionCode();
        Document importProduct = streamItem.getImportProduct();
        if (actionCode == BatchImportAction.ADD) {
            log.warn("Can not find origin performance product {}", ProductKeyUtils.buildProdKeyInfo(importProduct));
            return null;
        }
        List<Document> performanceList = importProduct.getList("performance", Document.class);

        Map<String, Object> performance = filterPerformance(performanceList, PERFORMANCE_TYPE);
        Map<String, Object> benchmark = filterPerformance(performanceList, BENCHMARK_TYPE);
        if (ObjectUtils.allNull(performance, benchmark)) {
            log.warn("Skip empty performance record {}.", ProductKeyUtils.buildProdKeyInfo(importProduct));
            return null;
        }

        Document updateProd = new Document(streamItem.getOriginalProduct());
        updateProd.compute("performance", (key, oldValue) -> merge((Map<String, Object>) oldValue, performance));
        updateProd.compute("benchmark", (key, oldValue) -> merge((Map<String, Object>) oldValue, benchmark));
        streamItem.setUpdatedProduct(updateProd);
        return streamItem;
    }

    private Map<String, Object> merge(Map<String, Object> oldPerformance, Map<String, Object> newPerformance) {
        if (MapUtils.isEmpty(newPerformance)) {
            return oldPerformance;
        }

        newPerformance.put(Field.recUpdtDtTm, LocalDateTime.now().toString());
        if (MapUtils.isEmpty(oldPerformance)) {
            newPerformance.put(Field.recCreatDtTm, LocalDateTime.now().toString());
            return newPerformance;
        }

        List<String> updateAttrs = systemUpdateConfigHolder.getUpdateAttrs(systemCde, CollectionName.product.name());
        LinkedHashMap<String, Object> mergePerformance = new LinkedHashMap<>(oldPerformance);
        updateAttrs.forEach(attr -> mergePerformance.put(attr, newPerformance.get(attr)));
        return mergePerformance;
    }

    private Document filterPerformance(List<Document> performanceList, String perfmTypeCde) {
        Document performance = performanceList
                .stream()
                .filter(item -> StringUtils.equals(item.getString("perfmTypeCde"), perfmTypeCde))
                .findFirst()
                .orElse(null);

        if (null == performance || isEmpty(performance)) {
            return new Document();
        }

        return performance;
    }

    private boolean isEmpty(Document performance) {
        return Objects.isNull(performance.get("perfmYrToDtPct")) &&
                ObjectUtils.allNull(performance.get("perfm1moPct"), performance.get("perfm3moPct"), performance.get("perfm6moPct"), performance.get("perfm1yrPct"),
                        performance.get("perfm3yrPct"), performance.get("perfm5yrPct"), performance.get("perfmExt1YrPct"), performance.get("perfmSinceLnchPct"),
                        performance.get("rtrnVoltl1YrPct"), performance.get("rtrnVoltlExt1YrPct"), performance.get("rtrnVoltl3YrPct"), performance.get("rtrnVoltlExt3YrPct"),
                        performance.get("perfm6moAmt"), performance.get("perfmYrToDtAmt"), performance.get("perfm1yrAmt"), performance.get("perfm3yrAmt"));
    }
}
