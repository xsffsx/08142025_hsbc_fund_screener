package com.dummy.wpb.product.service;

import org.bson.Document;

import java.util.Collection;

public interface ProductFormatService {
    void formatByUpdateAttrs(Document importProduct, Document updatedProduct, Collection<String> updateAttrs);

    void formatExtendField(Document product, String fieldCde, Object fieldValue);

    /**
     * Create product that allows incoming in graphql. The type corresponding to graphql is [ProductInput].
     * Please see the product-metadata.xml for specific fields.
     *
     * @param sourceProd Documents that need to be formatted
     * @return Products that can be imported to grpahql
     */
    Document initProduct(Document sourceProd);
}
