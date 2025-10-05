package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TopMoverTable implements Serializable {

    private static final long serialVersionUID = 8482281124124820670L;

    private String chainKey;

    private String tableKey;

    private List<TopMoverProduct> products;

    // private String exchangeTimezone;
    @Override
    public String toString() {
        return "TopTenTable [chainKey=" + this.chainKey + ", tableKey=" + this.tableKey + ", product=" + this.products + "]";
    }

}
