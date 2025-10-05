package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.performance;

import com.google.gson.annotations.SerializedName;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.performance.entity.Result;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PastPerformanceResponse {

    @SerializedName("stsCode")
    private String statusCode;

    @SerializedName("stsTxt")
    private String statusText;

    private Result result;

    @Override
    public String toString() {
        return "PastPerformanceResponse{" +
                "statusCode='" + statusCode + '\'' +
                ", statusText='" + statusText + '\'' +
                ", result=" + result +
                '}';
    }
}
