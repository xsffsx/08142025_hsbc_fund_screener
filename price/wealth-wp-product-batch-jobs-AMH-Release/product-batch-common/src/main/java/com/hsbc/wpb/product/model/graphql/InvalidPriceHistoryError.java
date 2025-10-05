package com.dummy.wpb.product.model.graphql;

import lombok.Data;

@Data
public class InvalidPriceHistoryError {
	
	private String jsonPath;

	private String code;

	private String message;
}
