package com.dummy.wpb.product.model.graphql;

import lombok.Data;

import java.util.List;

@Data
public class InvalidReferData {
    private ReferenceData referData;
    private List<InvalidProductError> errors;
}
