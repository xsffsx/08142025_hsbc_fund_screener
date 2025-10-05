package com.hhhh.group.secwealth.mktdata.elastic.bean.ai.response;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class StockListV2Response {
    private long total;
    private List<String> data;
}
