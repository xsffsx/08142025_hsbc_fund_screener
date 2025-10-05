package com.dummy.wpb.product.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class ProductBatchUpdateByIdInput {

    private Long prodId;
    private List<Operation> operations;
}
