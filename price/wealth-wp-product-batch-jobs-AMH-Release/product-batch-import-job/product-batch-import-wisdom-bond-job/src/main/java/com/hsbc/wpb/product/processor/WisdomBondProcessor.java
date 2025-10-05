package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.exception.InvalidRecordException;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.util.ProductKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import static com.dummy.wpb.product.constant.BatchConstants.EXPIRED;
import static com.dummy.wpb.product.constant.BatchConstants.INDICATOR_YES;
import static com.dummy.wpb.product.constant.BatchImportAction.ADD;

@Slf4j
@Component
public class WisdomBondProcessor implements ItemProcessor<ProductStreamItem, ProductStreamItem> {
    @Override
    public ProductStreamItem process(ProductStreamItem streamItem) {
        BatchImportAction actionCode = streamItem.getActionCode();
        Document importProduct = streamItem.getImportProduct();

        String prodKeyInfo = ProductKeyUtils.buildProdKeyInfo(importProduct);

        if (ADD == actionCode) {
            log.error("Can not find product {}.", prodKeyInfo);
            return null;
        }

        if (null == importProduct.get(Field.prodTopSellRankNum)) {
            String msg = String.format("Top Seller Rank Number cannot be null %s.", prodKeyInfo);
            log.error(msg);
            throw new InvalidRecordException(msg);
        }

        Document origProduct = streamItem.getOriginalProduct();
        if (EXPIRED.equals(origProduct.getString(Field.prodStatCde))) {
            log.warn("Can not update expired product {}.", prodKeyInfo);
            return null;
        }

        String allowBuyProdInd = origProduct.getString(Field.allowBuyProdInd);
        if (!INDICATOR_YES.equals(allowBuyProdInd)) {
            log.warn("Can not update product with allowBuyProdInd equal {}  {} .", allowBuyProdInd, prodKeyInfo);
            return null;
        }

        return streamItem;
    }
}
