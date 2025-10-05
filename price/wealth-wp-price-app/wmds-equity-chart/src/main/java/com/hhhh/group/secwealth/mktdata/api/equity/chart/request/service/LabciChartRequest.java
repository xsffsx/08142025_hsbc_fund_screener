package com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


@Getter
@Setter
@ToString
public class LabciChartRequest implements Serializable {

    private static final long serialVersionUID = 5497084552971153819L;

    private String token;

    private String[] item;

    private String intType;

    private Number intCnt;

    private Number per;

    private String startTm;

    private String endTm;

    private Boolean timeZone;

    private String[] filter;

}
