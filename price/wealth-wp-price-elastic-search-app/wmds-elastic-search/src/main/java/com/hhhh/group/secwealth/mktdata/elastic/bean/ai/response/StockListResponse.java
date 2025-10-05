package com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock.StockInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author 45247946
 */
@Setter
@Getter
public class StockListResponse {
    private int pageNumber;
    private int recordsPerPage;
    private long total;
    private String scrollId;
    private boolean hasMore;
    private List<StockInfo> data;
}
