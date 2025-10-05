
package com.hhhh.group.secwealth.mktdata.quote.beans.response.common;

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * <b> Response fields for Option. </b>
 * </p>
 */
public class Option {

    private String underlyingSymbol;

    private String underlyingMarket;

    private String underlyingProductType;

    private String rootSymbol;

    private BigDecimal derivativeOpenInterest;

    private BigDecimal derivativeStrike;

    private String derivativeContractSize;

    private String derivativePeriod;

    private String derivativeCallPut;

    private String derivativeMoneyness;

    private String derivativeContractMonth;

    private String derivativeExpiryDate;

    private String derivativeDaysToMaturity;

    private String derivativeDelta;

    private BigDecimal derivativeRho;

    private BigDecimal derivativeTheta;

    private BigDecimal derivativeGamma;

    private BigDecimal derivativeVega;

    private BigDecimal derivativeContractRangeLow;

    private BigDecimal derivativeContractRangeHigh;

    private String prefillExpirationDate;

    private BigDecimal prefillStrikePrice;

    private String[] expirationDates;

    private String[] strikePrice;

    private String[] expirationMonths;


    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("underlyingSymbol", this.expirationMonths);
        builder.append("underlyingMarket", this.underlyingMarket);
        builder.append("underlyingProductType", this.underlyingProductType);
        builder.append("rootSymbol", this.rootSymbol);
        builder.append("derivativeOpenInterest", this.derivativeOpenInterest);
        builder.append("derivativeStrike", this.derivativeStrike);
        builder.append("derivativeContractSize", this.derivativeContractSize);
        builder.append("derivativePeriod", this.derivativePeriod);
        builder.append("derivativeCallPut", this.derivativeCallPut);
        builder.append("derivativeMoneyness", this.derivativeMoneyness);
        builder.append("derivativeContractMonth", this.derivativeContractMonth);
        builder.append("derivativeExpiryDate", this.derivativeExpiryDate);
        builder.append("derivativeDaysToMaturity", this.derivativeDaysToMaturity);
        builder.append("derivativeDelta", this.derivativeDelta);
        builder.append("derivativeRho", this.derivativeRho);
        builder.append("derivativeTheta", this.derivativeTheta);
        builder.append("derivativeGamma", this.derivativeGamma);
        builder.append("derivativeVega", this.derivativeVega);
        builder.append("derivativeContractRangeLow", this.derivativeContractRangeLow);
        builder.append("derivativeContractRangeHigh", this.derivativeContractRangeHigh);
        builder.append("prefillExpirationDate", this.prefillExpirationDate);
        builder.append("prefillStrikePrice", this.prefillStrikePrice);
        builder.append("expirationDate", Arrays.toString(this.expirationDates));
        builder.append("strikePrice", Arrays.toString(this.strikePrice));
        builder.append("expirationMonth", Arrays.toString(this.expirationMonths));
        return builder.toString();
    }

    public String getUnderlyingSymbol() {
        return this.underlyingSymbol;
    }


    public void setUnderlyingSymbol(final String underlyingSymbol) {
        this.underlyingSymbol = underlyingSymbol;
    }

    public String getUnderlyingMarket() {
        return this.underlyingMarket;
    }


    public void setUnderlyingMarket(final String underlyingMarket) {
        this.underlyingMarket = underlyingMarket;
    }

    public String getUnderlyingProductType() {
        return this.underlyingProductType;
    }


    public void setUnderlyingProductType(final String underlyingProductType) {
        this.underlyingProductType = underlyingProductType;
    }


    public String getRootSymbol() {
        return this.rootSymbol;
    }


    public void setRootSymbol(final String rootSymbol) {
        this.rootSymbol = rootSymbol;
    }


    public BigDecimal getDerivativeOpenInterest() {
        return this.derivativeOpenInterest;
    }


    public void setDerivativeOpenInterest(final BigDecimal derivativeOpenInterest) {
        this.derivativeOpenInterest = derivativeOpenInterest;
    }

    public BigDecimal getDerivativeStrike() {
        return this.derivativeStrike;
    }

    public void setDerivativeStrike(final BigDecimal derivativeStrike) {
        this.derivativeStrike = derivativeStrike;
    }


    public String getDerivativeContractSize() {
        return this.derivativeContractSize;
    }


    public void setDerivativeContractSize(final String derivativeContractSize) {
        this.derivativeContractSize = derivativeContractSize;
    }


    public String getDerivativePeriod() {
        return this.derivativePeriod;
    }


    public void setDerivativePeriod(final String derivativePeriod) {
        this.derivativePeriod = derivativePeriod;
    }

    public String getDerivativeCallPut() {
        return this.derivativeCallPut;
    }


    public void setDerivativeCallPut(final String derivativeCallPut) {
        this.derivativeCallPut = derivativeCallPut;
    }


    public String getDerivativeMoneyness() {
        return this.derivativeMoneyness;
    }


    public void setDerivativeMoneyness(final String derivativeMoneyness) {
        this.derivativeMoneyness = derivativeMoneyness;
    }


    public String getDerivativeContractMonth() {
        return this.derivativeContractMonth;
    }


    public void setDerivativeContractMonth(final String derivativeContractMonth) {
        this.derivativeContractMonth = derivativeContractMonth;
    }

    public String getDerivativeExpiryDate() {
        return this.derivativeExpiryDate;
    }


    public void setDerivativeExpiryDate(final String derivativeExpiryDate) {
        this.derivativeExpiryDate = derivativeExpiryDate;
    }


    public String getDerivativeDaysToMaturity() {
        return this.derivativeDaysToMaturity;
    }


    public void setDerivativeDaysToMaturity(final String derivativeDaysToMaturity) {
        this.derivativeDaysToMaturity = derivativeDaysToMaturity;
    }


    public String getDerivativeDelta() {
        return this.derivativeDelta;
    }


    public void setDerivativeDelta(final String derivativeDelta) {
        this.derivativeDelta = derivativeDelta;
    }


    public BigDecimal getDerivativeRho() {
        return this.derivativeRho;
    }


    public void setDerivativeRho(final BigDecimal derivativeRho) {
        this.derivativeRho = derivativeRho;
    }


    public BigDecimal getDerivativeTheta() {
        return this.derivativeTheta;
    }


    public void setDerivativeTheta(final BigDecimal derivativeTheta) {
        this.derivativeTheta = derivativeTheta;
    }

    public BigDecimal getDerivativeGamma() {
        return this.derivativeGamma;
    }


    public void setDerivativeGamma(final BigDecimal derivativeGamma) {
        this.derivativeGamma = derivativeGamma;
    }


    public BigDecimal getDerivativeVega() {
        return this.derivativeVega;
    }


    public void setDerivativeVega(final BigDecimal derivativeVega) {
        this.derivativeVega = derivativeVega;
    }


    public BigDecimal getDerivativeContractRangeLow() {
        return this.derivativeContractRangeLow;
    }


    public void setDerivativeContractRangeLow(final BigDecimal derivativeContractRangeLow) {
        this.derivativeContractRangeLow = derivativeContractRangeLow;
    }


    public BigDecimal getDerivativeContractRangeHigh() {
        return this.derivativeContractRangeHigh;
    }


    public void setDerivativeContractRangeHigh(final BigDecimal derivativeContractRangeHigh) {
        this.derivativeContractRangeHigh = derivativeContractRangeHigh;
    }


    public String getPrefillExpirationDate() {
        return this.prefillExpirationDate;
    }


    public void setPrefillExpirationDate(final String prefillExpirationDate) {
        this.prefillExpirationDate = prefillExpirationDate;
    }


    public BigDecimal getPrefillStrikePrice() {
        return this.prefillStrikePrice;
    }


    public void setPrefillStrikePrice(final BigDecimal prefillStrikePrice) {
        this.prefillStrikePrice = prefillStrikePrice;
    }


    public String[] getExpirationDates() {
        return this.expirationDates;
    }


    public void setExpirationDates(final String[] expirationDates) {
        this.expirationDates = expirationDates;
    }


    public String[] getStrikePrice() {
        return this.strikePrice;
    }


    public void setStrikePrice(final String[] strikePrice) {
        this.strikePrice = strikePrice;
    }


    public String[] getExpirationMonths() {
        return this.expirationMonths;
    }

    public void setExpirationMonths(final String[] expirationMonths) {
        this.expirationMonths = expirationMonths;
    }

}
