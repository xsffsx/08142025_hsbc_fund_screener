package com.dummy.wpb.product.thymeleaf.expression;

import java.math.BigDecimal;

public class Doubles {

    public String stripTrailingZeros(final Object number) {
        if (null == number) {
            return null;
        }
        return new BigDecimal(number.toString()).stripTrailingZeros().toPlainString();
    }

    public String stripDeciaml(final Object number) {
        if (null == number) {
            return null;
        }
        return this.stripTrailingZeros(number).replace(".", "");
    }

    public String decimalPlace(final Object number) {
        if (null == number) {
            return null;
        }
        return String.valueOf(new BigDecimal(this.stripTrailingZeros(number)).scale());
    }
}
