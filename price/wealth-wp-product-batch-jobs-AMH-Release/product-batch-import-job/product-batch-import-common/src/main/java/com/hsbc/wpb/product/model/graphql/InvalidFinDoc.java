package com.dummy.wpb.product.model.graphql;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class InvalidFinDoc {

    private Document finDoc;

    private List<InvalidProductError> errors;
}
