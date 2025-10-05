package com.dummy.wpb.product.model.graphql;

import lombok.Data;

@Data
public class InvalidProductError {
	
	private String jsonPath;

	private String code;

	private String message;
}
