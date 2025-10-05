package com.dummy.wpb.product.model.graphql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBatchUpdateInput {
    Map<String,Object> filter;

    List<Operation> operations;
}
