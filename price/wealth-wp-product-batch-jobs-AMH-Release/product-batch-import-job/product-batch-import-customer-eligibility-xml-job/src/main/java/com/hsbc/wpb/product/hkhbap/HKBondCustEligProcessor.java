package com.dummy.wpb.product.hkhbap;

import com.dummy.wpb.product.condition.ConditionalOnJobParameters;
import com.dummy.wpb.product.configuration.SystemUpdateConfigHolder;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductStreamItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dummy.wpb.product.constant.BatchConstants.BOND_CD;

/**
 * Must after {@link com.dummy.wpb.product.CustEligProcessor}
 */
@ConditionalOnJobParameters({"ctryRecCde", "HK", "grpMembrRecCde", "HBAP"})
@Order(1)
@Component
@StepScope
public class HKBondCustEligProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {

    final List<String> updateAttrs;

    public HKBondCustEligProcessor(SystemUpdateConfigHolder updateConfigHolder, @Value("#{jobParameters['systemCde']}") String systemCde) {
        updateAttrs = updateConfigHolder.getUpdateAttrs(systemCde, "product");
    }

    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) {
        Document importProduct = streamItem.getImportProduct();

        String prodTypeCde = importProduct.getString(Field.prodTypeCde);
        if (!BOND_CD.equals(prodTypeCde) || !updateAttrs.contains(Field.restrCustCtry)) {
            return streamItem;
        }

        List<Document> importRestrCustCtry = importProduct.getList(Field.restrCustCtry, Document.class, Collections.emptyList());
        List<Map<String, Object>> origRestrCustCtry = (List<Map<String, Object>>) streamItem.getOriginalProduct().get(Field.restrCustCtry);
        origRestrCustCtry = ListUtils.emptyIfNull(origRestrCustCtry);
        origRestrCustCtry.removeIf(rcc -> !StringUtils.equalsAny(Objects.toString(rcc.get(Field.restrCtryTypeCde)), "P", "I", "B"));
        streamItem.getUpdatedProduct().put(Field.restrCustCtry, ListUtils.union(importRestrCustCtry, origRestrCustCtry));
        return streamItem;
    }
}
