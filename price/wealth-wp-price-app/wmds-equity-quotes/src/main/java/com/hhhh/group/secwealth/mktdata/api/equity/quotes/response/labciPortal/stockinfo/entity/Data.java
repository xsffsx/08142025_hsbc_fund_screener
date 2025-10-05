package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.stockinfo.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {

    @SerializedName("nbric")
    private String nbRic;

    private String symbol;

    private String name;

    private String isADR;

    private String isETF;

    private String PER;

    private String ePER;

    private String mktcap;

    private String divYield;

    private String eDivYield;

    private String ccy;

    private String industry;

    private String exDivDate;

    private String ls;

    private String nc;

    private String pc;

    private String prevClose;

    private String ask;

    private String askSize;

    private String bid;

    private String bidSize;

    private String open;

    private String high;

    private String low;

    private String high52W;

    private String low52W;

    private String volume;

    private String turnover;

    private String adrRatio;

    private String adrPrice;

    private String adrCcy;

    private String symbolHK;

    private String nameHK;

    private String fdType;

    private String fdSubType;

    private String fdRegion;

    private String beta6m;

    private String beta1y;

    private String beta3y;

    private String beta5y;

    private String beta10y;

    private String aum;

    private String aumCcy;

    private String aumDate;

    private String expenseRatio;
}
