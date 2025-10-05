package com.dummy.wpb.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrtsUndlStockRecord {
    private String recordType;
    private String headerOrTrailerContent;
    private String securityCode;
    private String securityType;
    private String underlyingCode;
}
