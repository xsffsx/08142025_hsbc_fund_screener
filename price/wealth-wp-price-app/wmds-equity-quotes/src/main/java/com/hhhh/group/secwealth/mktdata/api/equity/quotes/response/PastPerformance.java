/*
 */

package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class PastPerformance {

    private String nbRic;
    private BigDecimal WPC1;
    private BigDecimal MPC1;
    private BigDecimal MPC3;
    private BigDecimal YPC1;
    private BigDecimal YPC3;

    public String getNbRic() {
        return nbRic;
    }

    public void setNbRic(String nbRic) {
        this.nbRic = nbRic;
    }

    @JsonProperty( "1WPC")
    public BigDecimal getWPC1() {
        return WPC1;
    }

    public void setWPC1(BigDecimal WPC1) {
        this.WPC1 = WPC1;
    }

    @JsonProperty( "1MPC")
    public BigDecimal getMPC1() {
        return MPC1;
    }

    public void setMPC1(BigDecimal MPC1) {
        this.MPC1 = MPC1;
    }

    @JsonProperty( "3MPC")
    public BigDecimal getMPC3() {
        return MPC3;
    }

    public void setMPC3(BigDecimal MPC3) {
        this.MPC3 = MPC3;
    }

    @JsonProperty( "1YPC")
    public BigDecimal getYPC1() {
        return YPC1;
    }

    public void setYPC1(BigDecimal YPC1) {
        this.YPC1 = YPC1;
    }

    @JsonProperty( "3YPC")
    public BigDecimal getYPC3() {
        return YPC3;
    }

    public void setYPC3(BigDecimal YPC3) {
        this.YPC3 = YPC3;
    }
}
