package com.dummy.wpb.product.service;

import com.dummy.wpb.product.model.graphql.Operation;
import org.bson.Document;

import java.util.List;

public interface OperationService {
    <T> List<Operation> calcOperations(T originalProduct, T updatedProduct);

    Document decorate(Object product);
}
