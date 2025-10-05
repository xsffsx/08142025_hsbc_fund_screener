package com.hhhh.group.secwealth.mktdata.test.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PastPerformance {

    private String nbRic;
    private BigDecimal WPC1;
    private BigDecimal MPC1;
    private BigDecimal MPC3;
    private BigDecimal YPC1;
    private BigDecimal YPC3;
}
