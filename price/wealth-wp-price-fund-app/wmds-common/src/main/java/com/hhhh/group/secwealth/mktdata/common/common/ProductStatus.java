
package com.hhhh.group.secwealth.mktdata.common.common;

import java.util.HashMap;
import java.util.Map;


public enum ProductStatus {

    
    NORMAL("normal", "000", "The symbol contains live data"),

    
    SUSPENDED("halted"),

    
    STALE("stale", "001", "The symbol data are stale"),

    
    TIMEOUT("timeout"),

    
    CLOSED("CLOSED", "002", "The symbol is dropped by RMDS or is not permissioned"),

    
    CLOSED_RECOVER("CLOSED_RECOVER", "003",
        "The symbol is dropped by RMDS or is not permissioned. User may try to request the symbol sometime later"),

    SYMBOL_INVALID("Symbol invalid", "004", "The symbol is invalid");

    
    private static final Map<String, ProductStatus> stringToEnum = new HashMap<String, ProductStatus>();

    
    private String value;

    
    private String statusCode;

    
    private String message;

    
    ProductStatus(final String value, final String code) {
        this.value = value;
        this.statusCode = code;
    }

    ProductStatus(final String value) {
        this.value = value;
    }

    ProductStatus(final String value, final String code, final String msg) {
        this.value = value;
        this.statusCode = code;
        this.message = msg;
    }

    static {
        for (ProductStatus e : values()) {
            ProductStatus.stringToEnum.put(e.getValue(), e);
        }
    }

    
    public static ProductStatus fromString(final String str) {
        return ProductStatus.stringToEnum.get(str);
    }

    
    public String getValue() {
        return this.value;
    }

    
    public void setValue(final String value) {
        this.value = value;
    }

    
    public String getStatusCode() {
        return this.statusCode;
    }

    
    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    
    public String getMessage() {
        return this.message;
    }

    
    public void setMessage(final String message) {
        this.message = message;
    }

}
