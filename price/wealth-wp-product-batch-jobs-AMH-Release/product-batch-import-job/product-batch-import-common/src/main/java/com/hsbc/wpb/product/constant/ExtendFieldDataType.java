/*
 */
package com.dummy.wpb.product.constant;

/**
 * <p>
 * <b> Denotes the actions (add, validate add, update, validate update) for the
 * record in the batch import job. </b>
 * </p>
 */
public enum ExtendFieldDataType {

    CHAR("Char"), STRING("String"), INTEGER("Integer"), DECIMAL("Decimal"), DATE("Date"), TIMESTAMP("Timestamp");

    /** The value. */
    private String value;

    ExtendFieldDataType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *        the new value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    public boolean equals(String value){
        return this.value.equals(value);
    }
}
