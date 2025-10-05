package com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

/**
 * @author 45247946
 */
@Setter
@Getter
public class StockListRequest {

    private String market;
    private String[] assetClasses;
    private String scrollId;
    @Min(0)
    private int pageNum;
    @Min(1)
    private int pageSize;
}
