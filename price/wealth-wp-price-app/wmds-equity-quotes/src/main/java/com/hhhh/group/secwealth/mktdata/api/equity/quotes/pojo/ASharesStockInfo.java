package com.hhhh.group.secwealth.mktdata.api.equity.quotes.pojo;

import com.hhhh.group.secwealth.mktdata.api.equity.quotes.dao.entiry.ASharesStock;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ASharesStockInfo {

    private List<ASharesStock> aSharesStock;

}
