package com.dummy.wpb.product.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WrtsUndlStockRecordType {
    public static final String HEADER_RECORD_TYPE = "0";
    public static final String DETAIL_RECORD_TYPE = "1";
    public static final String TRAILER_RECORD_TYPE = "9";
}
