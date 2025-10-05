package com.dummy.wpb.product.model;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

import static com.dummy.wpb.product.constant.ConfigConstants.PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET;

@Data
@Document
public class Product {
    private Long prodId;
    private String prodTypeCde;
    private String prodAltPrimNum;
    private List<AltId> altId;
    private StockInstm stockInstm;

    public String getMCode() {
        AltId mCodeAltId = altId.stream()
                                .filter(altIdObj -> PRODUCT_CODE_ALTERNATED_CLASS_CODE_MARKET.equals(
                                        altIdObj.getProdCdeAltClassCde()))
                                .findFirst()
                                .orElse(null);
        assert mCodeAltId != null;
        return mCodeAltId.getProdAltNum();
    }

    public UndlStock getUndlStock() {
        assert stockInstm != null;
        return Optional.ofNullable(stockInstm.getUndlStock())
                       .filter(CollectionUtils::isNotEmpty)
                       .map(undlStockList -> undlStockList.get(0))
                       .orElse(null);
    }
}
