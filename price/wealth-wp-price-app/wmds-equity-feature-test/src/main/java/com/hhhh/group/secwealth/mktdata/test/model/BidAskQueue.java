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
public class BidAskQueue {

    private BigDecimal bidPrice;

    private BigDecimal bidSize;

    private BigDecimal bidOrder;

    private BigDecimal askPrice;

    private BigDecimal askSize;

    private BigDecimal askOrder;
}
