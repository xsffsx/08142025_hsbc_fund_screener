package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.entity.MarketRate;
import com.dummy.wpb.product.model.ProductStreamItem;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@StepScope
public class GoldPriceUpdateJobProcessor implements ItemProcessor<Document, ProductStreamItem> {

    @Autowired
    private GoldPriceUpdateJobService goldPriceUpdateJobService;

    @Override
    public ProductStreamItem process(Document product) {
        log.info("Updating product (ctryRecCde: {}, grpMembrRecCde: {}, prodTypeCde: {}, prodAltPrimNum: {})",
                product.getString(Field.ctryRecCde),
                product.getString(Field.grpMembrRecCde),
                product.getString(Field.prodTypeCde),
                product.getString(Field.prodAltPrimNum));
        ProductStreamItem productStreamItem = new ProductStreamItem();
        productStreamItem.setActionCode(BatchImportAction.UPDATE);
        productStreamItem.setOriginalProduct(product);
        // get market rate
        List<MarketRate> marketRates = goldPriceUpdateJobService.getMarketRate(product.getInteger(Field.prodId));
        // update product
        Document updatedProduct = goldPriceUpdateJobService.updateProductMarketRate(product, marketRates);
        productStreamItem.setUpdatedProduct(updatedProduct);
        return productStreamItem;
    }
}
