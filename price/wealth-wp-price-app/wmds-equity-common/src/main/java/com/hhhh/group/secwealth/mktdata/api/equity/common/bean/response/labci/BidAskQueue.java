/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidAskQueue {

    private BigDecimal bidPrice;

    private BigDecimal bidSize;

    private BigDecimal bidOrder;

    private BigDecimal askPrice;

    private BigDecimal askSize;

    private BigDecimal askOrder;

}
