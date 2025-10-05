package com.dummy.wpb.product;

import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.util.ProductKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

@Slf4j
public class SnProductValidator implements Validator<ProductStreamItem> {
    @Override
    public void validate(ProductStreamItem streamItem) throws ValidationException {
        if (streamItem.getActionCode() != BatchImportAction.UPDATE) {
            String prodKeyInfo = ProductKeyUtils.buildProdKeyInfo(streamItem.getImportProduct());
            String message = String.format("Can not find origin product%s", prodKeyInfo);
            log.warn(message);
            throw new ValidationException(message);
        }
    }
}
