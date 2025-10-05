package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.labciPortal.performance.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Data {

    @SerializedName("nb_ric")
    private String nbRic;

    @SerializedName("1WPC")
    private String WPC1;

    @SerializedName("1MPC")
    private String MPC1;

    @SerializedName("3MPC")
    private String MPC3;

    @SerializedName("1YPC")
    private String YPC1;

    @SerializedName("3YPC")
    private String YPC3;

    @Override
    public String toString() {
        return "Data{" +
                "nbRic='" + nbRic + '\'' +
                ", WPC1='" + WPC1 + '\'' +
                ", MPC1='" + MPC1 + '\'' +
                ", MPC3='" + MPC3 + '\'' +
                ", YPC1='" + YPC1 + '\'' +
                ", YPC3='" + YPC3 + '\'' +
                '}';
    }
}
