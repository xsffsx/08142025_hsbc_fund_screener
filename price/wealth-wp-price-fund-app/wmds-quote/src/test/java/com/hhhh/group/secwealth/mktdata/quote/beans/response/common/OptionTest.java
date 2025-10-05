package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OptionTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new Option().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new Option().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new Option());
    }

    @Test
    void testSetterAndGetter() {
        Option obj = new Option();
        
        obj.setUnderlyingSymbol("test");
        obj.setUnderlyingMarket("test");
        obj.setUnderlyingProductType("test");
        obj.setRootSymbol("test");
        obj.setDerivativeOpenInterest(new BigDecimal("0"));
        obj.setDerivativeStrike(new BigDecimal("0"));
        obj.setDerivativeContractSize("test");
        obj.setDerivativePeriod("test");
        obj.setDerivativeCallPut("test");
        obj.setDerivativeMoneyness("test");
        obj.setDerivativeContractMonth("test");
        obj.setDerivativeExpiryDate("test");
        obj.setDerivativeDaysToMaturity("test");
        obj.setDerivativeDelta("test");
        obj.setDerivativeRho(new BigDecimal("0"));
        obj.setDerivativeTheta(new BigDecimal("0"));
        obj.setDerivativeGamma(new BigDecimal("0"));
        obj.setDerivativeVega(new BigDecimal("0"));
        obj.setDerivativeContractRangeLow(new BigDecimal("0"));
        obj.setDerivativeContractRangeHigh(new BigDecimal("0"));
        obj.setPrefillExpirationDate("test");
        obj.setPrefillStrikePrice(new BigDecimal("0"));
        obj.setExpirationDates(new String[]{"test"});
        obj.setStrikePrice(new String[]{"test"});
        obj.setExpirationMonths(new String[]{"test"});

        Assertions.assertNotNull(obj.getUnderlyingSymbol());
        Assertions.assertNotNull(obj.getUnderlyingMarket());
        Assertions.assertNotNull(obj.getUnderlyingProductType());
        Assertions.assertNotNull(obj.getRootSymbol());
        Assertions.assertNotNull(obj.getDerivativeOpenInterest());
        Assertions.assertNotNull(obj.getDerivativeStrike());
        Assertions.assertNotNull(obj.getDerivativeContractSize());
        Assertions.assertNotNull(obj.getDerivativePeriod());
        Assertions.assertNotNull(obj.getDerivativeCallPut());
        Assertions.assertNotNull(obj.getDerivativeMoneyness());
        Assertions.assertNotNull(obj.getDerivativeContractMonth());
        Assertions.assertNotNull(obj.getDerivativeExpiryDate());
        Assertions.assertNotNull(obj.getDerivativeDaysToMaturity());
        Assertions.assertNotNull(obj.getDerivativeDelta());
        Assertions.assertNotNull(obj.getDerivativeRho());
        Assertions.assertNotNull(obj.getDerivativeTheta());
        Assertions.assertNotNull(obj.getDerivativeGamma());
        Assertions.assertNotNull(obj.getDerivativeVega());
        Assertions.assertNotNull(obj.getDerivativeContractRangeLow());
        Assertions.assertNotNull(obj.getDerivativeContractRangeHigh());
        Assertions.assertNotNull(obj.getPrefillExpirationDate());
        Assertions.assertNotNull(obj.getPrefillStrikePrice());
        Assertions.assertNotNull(obj.getExpirationDates());
        Assertions.assertNotNull(obj.getStrikePrice());
        Assertions.assertNotNull(obj.getExpirationMonths());
    }
}
