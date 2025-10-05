package com.dummy.wpb.product;

import com.dummy.wpb.product.configuration.SystemDefaultValuesHolder;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.service.ProductFormatService;
import com.dummy.wpb.product.utils.JsonPathUtils;
import com.dummy.wpb.product.utils.JsonUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.USER_DEFINED_FIELDS;

@Component
@StepScope
@Order(0)
public class CustEligProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {
    @Autowired
    private SystemUpdateConfigHolder updateConfigHolder;

    @Autowired
    private SystemDefaultValuesHolder defaultValuesHolder;

    @Autowired
    ProductFormatService productFormatService;

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) throws Exception {
        Document importProd = streamItem.getImportProduct();
        Document origProduct = streamItem.getOriginalProduct();
        Document updatedProd = JsonUtil.deepCopy(origProduct);
        streamItem.setUpdatedProduct(updatedProd);

        Map<String, Object> defaultValues = defaultValuesHolder.getDefaultValues(systemCde);
        defaultValues.forEach((attr, defaultValue) -> JsonPathUtils.setValueIfAbsent(importProd, attr, defaultValue));

        List<String> updateAttrs = updateConfigHolder.getUpdateAttrs(systemCde, "product");
        Stream.of(Field.restrCustGroup, Field.formReqmt, Field.restrCustCtry).forEach(attr -> {
            if (!updateAttrs.remove(attr)) {
                return;
            }

            List<Document> targetValue = importProd.getList(attr, Document.class, Collections.emptyList());
            if (CollectionUtils.isNotEmpty(targetValue)) {
                updatedProd.put(attr, targetValue);
            }
        });

        if (updateAttrs.remove(USER_DEFINED_FIELDS)) {
            productFormatService.formatByUpdateAttrs(importProd, updatedProd, Collections.singleton(USER_DEFINED_FIELDS));
        }

        if (MapUtils.isNotEmpty(origProduct.get(Field.tradeElig, Collections.emptyMap()))) {
            productFormatService.formatByUpdateAttrs(importProd, updatedProd, updateAttrs);
        } else {
            updatedProd.put(Field.tradeElig, importProd.get(Field.tradeElig));
        }

        return streamItem;
    }
}
