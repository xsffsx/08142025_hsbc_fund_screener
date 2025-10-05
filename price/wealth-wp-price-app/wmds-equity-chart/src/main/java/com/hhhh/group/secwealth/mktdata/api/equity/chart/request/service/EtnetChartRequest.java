package com.hhhh.group.secwealth.mktdata.api.equity.chart.request.service;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * for HK market
 */
@Getter
@Setter
@ToString
public class EtnetChartRequest implements Serializable  {

    private static final long serialVersionUID = 5497084552971153819L;

    private String[] symbol;

    private Boolean realTime;

    private Number timePeriod;

    private String timeInterval;

    private String locale;

    private String token;
}
