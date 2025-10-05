package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.labciportal.performance;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PastPerformanceRequest {

    private String token;

    @SerializedName("nb_ric")
    private String nbRic;
}
