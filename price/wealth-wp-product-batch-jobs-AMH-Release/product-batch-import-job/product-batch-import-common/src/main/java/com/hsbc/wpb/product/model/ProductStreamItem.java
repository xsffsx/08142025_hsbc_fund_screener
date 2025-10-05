package com.dummy.wpb.product.model;

import com.dummy.wpb.product.constant.BatchImportAction;
import lombok.Data;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/*
 * Save the product in three different states and some parameters
 */
@Data
public class ProductStreamItem {
    /**
     * Product converted from file (lile excel, csv or xml)
     */
    private Document importProduct;

    /**
     * The original product saved in the database
     */
    private Document originalProduct;

    /**
     * The product after value update
     */
    private Document updatedProduct;
    private BatchImportAction actionCode;

    private Map<String, Object> params;

    public synchronized void putParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }
}
