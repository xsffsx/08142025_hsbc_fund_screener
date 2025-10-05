package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static com.dummy.wpb.product.constant.BatchConstants.*;

public class ProductPriceValidatorTest {

    @Test
    public void test_validate() {
        ProductPriceValidator priceValidator = new ProductPriceValidator();
        ReflectionTestUtils.setField(priceValidator, "systemCde", "AMHGSOPS.CP");

        ProductPriceStreamItem streamItem = new ProductPriceStreamItem();
        Document importProduct = new Document();
        Document originalProduct = new Document();
        importProduct.put(Field.prodTypeCde, UNIT_TRUST);
        originalProduct.put(Field.debtInstm, Collections.singletonMap(Field.isAlgoInd, INDICATOR_NO));
        streamItem.setActionCode(BatchImportAction.UPDATE);
        streamItem.setImportProduct(importProduct);
        streamItem.setOriginalProduct(originalProduct);
        streamItem.setImportPriceHistory(Collections.emptyList());
        priceValidator.validate(streamItem);

        importProduct.put(Field.prodTypeCde, BOND_CD);
        priceValidator.validate(streamItem);

        originalProduct.put(Field.debtInstm, Collections.singletonMap(Field.isAlgoInd, INDICATOR_YES));
        Assert.assertThrows(ValidationException.class, () -> priceValidator.validate(streamItem));

        ReflectionTestUtils.setField(priceValidator, "systemCde", "MDSBE");
        priceValidator.validate(streamItem);
    }
}