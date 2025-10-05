package com.dummy.wpb.product.constant;

import java.util.Arrays;
import java.util.List;

public class DataType {

    public final static String CONSTANT = "Constant";
    public final static String STRING = "String";
    public final static String STRING_ARRAY = "[String]";
    public final static String LONG = "Long";
    public final static String FLOAT = "Float";
    public final static String DATE = "Date";
    public final static String DATE_TIME = "DateTime";
    public final static String LOCAL_DATE_TIME = "LocalDateTime";
    public final static String LOCAL_TIME = "LocalTime";

    public final static String BIGDECIMAL = "BigDecimal";

    public final static List<String> DATE_TYPE_LIST = Arrays.asList(DATE,DATE_TIME,LOCAL_DATE_TIME,LOCAL_TIME);
}
