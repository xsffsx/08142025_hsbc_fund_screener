package com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LastTradeRecord {

    private BigDecimal executionPrice;

    private BigDecimal quantity;
}
