package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Result {

    private Data data;

    @Override
    public String toString() {
        return "Result{" +
                "data=" + data +
                '}';
    }
}
