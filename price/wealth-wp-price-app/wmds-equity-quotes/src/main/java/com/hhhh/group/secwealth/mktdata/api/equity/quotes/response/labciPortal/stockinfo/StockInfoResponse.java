package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo;

import com.google.gson.annotations.SerializedName;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo.entity.Result;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockInfoResponse {

    @SerializedName("stsCode")
    private String statusCode;

    @SerializedName("stsTxt")
    private String statusText;

    private Result result;

    @Override
    public String toString() {
        return "StockInfoResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", statusText='" + statusText + '\'' +
                ", result=" + result +
                '}';
    }
}
