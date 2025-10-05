package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.labciportal.stockinfo;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockInfoRequest {

    private String token;

    @SerializedName("nbric")
    private String nbRic;

    private String lang;
}
