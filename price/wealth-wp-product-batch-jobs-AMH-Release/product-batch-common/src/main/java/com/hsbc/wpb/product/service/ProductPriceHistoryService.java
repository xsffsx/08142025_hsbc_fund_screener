package com.dummy.wpb.product.service;

import com.dummy.wpb.product.model.graphql.ProductPriceHistory;

import java.util.List;
import java.util.Map;

public interface ProductPriceHistoryService {
    List<ProductPriceHistory> productPriceHistoryByFilter(Map<String, Object> filter);
}
