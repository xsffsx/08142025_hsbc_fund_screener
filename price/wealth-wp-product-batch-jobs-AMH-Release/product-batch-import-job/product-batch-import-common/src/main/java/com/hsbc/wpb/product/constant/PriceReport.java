package com.dummy.wpb.product.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class PriceReport {

    public static final String REPORT_TYPE_TOLERANCE = "TOL";
    public static final String REPORT_TYPE_INVESTIGATION = "INV";
    public static final String REPORT_TYPE_STALE = "STL";
}
