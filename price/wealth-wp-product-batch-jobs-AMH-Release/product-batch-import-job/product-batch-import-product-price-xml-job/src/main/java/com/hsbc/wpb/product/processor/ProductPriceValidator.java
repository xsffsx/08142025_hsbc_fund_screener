package com.dummy.wpb.product.processor;

import com.dummy.wpb.product.constant.BatchConstants;
import com.dummy.wpb.product.constant.BatchImportAction;
import com.dummy.wpb.product.constant.Field;
import com.dummy.wpb.product.model.ProductPriceStreamItem;
import com.dummy.wpb.product.model.ProductStreamItem;
import com.dummy.wpb.product.util.ProductKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.bson.Document;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.dummy.wpb.product.constant.BatchConstants.BOND_CD;
import static com.dummy.wpb.product.constant.BatchConstants.EQUITY_LINKED_INVESTMENT;

@Slf4j
public class ProductPriceValidator implements Validator<ProductStreamItem> {

    @Value("#{jobParameters['systemCde']}")
    private String systemCde;

    @Override
    public void validate(ProductStreamItem streamItem) throws ValidationException {
        Document importProduct = streamItem.getImportProduct();

        try {
            ProductPriceStreamItem priceStreamItem = (ProductPriceStreamItem) streamItem;
            if (streamItem.getActionCode() != BatchImportAction.UPDATE) {
                throw new ValidationException("Can not find origin product");
            }

            List<Document> importPriceHistoryList = priceStreamItem.getImportPriceHistory();

            if (EQUITY_LINKED_INVESTMENT.equals(importProduct.get(Field.prodTypeCde))) {
                validateEliPrice(importPriceHistoryList);
            } else if (BOND_CD.equals(importProduct.get(Field.prodTypeCde))) {
               // do nothing currently
            } else {
                validateOtherTypePrice(importPriceHistoryList);
            }

            validateNegativePrice(importPriceHistoryList);
        } catch (ValidationException e) {
            String prodKeyInfo = ProductKeyUtils.buildProdKeyInfo(importProduct);
            String message = String.format("Failed to process input product price%s: %s", prodKeyInfo, e.getMessage());
            log.warn(message);
            throw new ValidationException(message);
        }
    }

    private void validateNegativePrice(List<Document> importPriceHistoryList) {
        Document maxPriceHistory = Collections.max(importPriceHistoryList, (priceHistory1, priceHistory2) ->
                ObjectUtils.compare(priceHistory1.get(Field.prcEffDt, LocalDate.class), priceHistory2.get(Field.prcEffDt, LocalDate.class))
        );

        boolean containNegativePrice = Stream.of(
                        maxPriceHistory.get(Field.prodBidPrcAmt, Number.class), maxPriceHistory.get(Field.prodOffrPrcAmt, Number.class),
                        maxPriceHistory.get(Field.prodMktPrcAmt, Number.class), maxPriceHistory.get(Field.prodNavPrcAmt, Number.class))
                .anyMatch(amount -> null != amount && amount.doubleValue() < 0);
        if (containNegativePrice) {
            throw new ValidationException("Contain negative price");
        }
    }

    private void validateOtherTypePrice(List<Document> importPriceHistoryList) {
        for (Document priceHistory : importPriceHistoryList) {
            LocalDate prcEffDt = priceHistory.get(Field.prcEffDt, LocalDate.class);

            if (ObjectUtils.compare(prcEffDt, LocalDate.now()) > 0) {
                throw new ValidationException(String.format("The prcEffDt %s can not greater than current day", prcEffDt));
            }

            if (ObjectUtils.anyNull(
                    priceHistory.get(Field.prcEffDt),
                    priceHistory.get(Field.ccyProdMktPrcCde))
                    || ObjectUtils.allNull(
                    priceHistory.get(Field.prodBidPrcAmt),
                    priceHistory.get(Field.prodMktPrcAmt),
                    priceHistory.get(Field.prodNavPrcAmt),
                    priceHistory.get(Field.prodOffrPrcAmt))) {
                throw new ValidationException("Have a invalid product price.");
            }
        }
    }

    private void validateEliPrice(List<Document> importPriceHistoryList) {
        boolean eliInvalid = importPriceHistoryList.stream().noneMatch(
                item -> ObjectUtils.allNotNull(
                        item.get(Field.prcEffDt),
                        item.get(Field.prcInpDt))
                        && ObjectUtils.allNull(
                        item.get(Field.prodBidPrcAmt),
                        item.get(Field.prodMktPrcAmt),
                        item.get(Field.prodNavPrcAmt),
                        item.get(Field.prodOffrPrcAmt))
        );

        if (eliInvalid) {
            throw new ValidationException("Eli Product mush only contains prcEffDt and PrcInpDt and all other fields are empty");
        }
    }

    public void validBondPrice(ProductStreamItem streamItem) {
        Document originalProduct = streamItem.getOriginalProduct();
        Map<String, Object> debtInstm = originalProduct.get(Field.debtInstm, Map.class);
        if ("AMHGSOPS.CP".equals(systemCde) && BatchConstants.INDICATOR_YES.equals(debtInstm.get(Field.isAlgoInd))) {
            throw new ValidationException("AMH GSOPS.CP could only update the non-algo product");
        }
    }
}
