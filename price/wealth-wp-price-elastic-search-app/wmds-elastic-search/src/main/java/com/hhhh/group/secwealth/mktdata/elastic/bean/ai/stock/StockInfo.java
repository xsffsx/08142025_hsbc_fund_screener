package com.hhhh.group.secwealth.mktdata.elastic.bean.ai.stock;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * @author 45247946
 */
@Document(indexName = "testutb", createIndex = false, shards = 1, replicas = 0)
@Getter
@Setter
@Builder
public class StockInfo {
    private List<ProdAltNumSeg> prodAltNumSegs;
    private String productType;
    private String productSubType;
    private String countryTradableCode;
    private String productName;
    private String productShortName;
    private String productCcy;
    private String market;
    private String symbol;
    private String productCode;
}
