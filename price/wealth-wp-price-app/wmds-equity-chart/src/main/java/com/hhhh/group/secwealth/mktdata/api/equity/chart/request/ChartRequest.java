package com.hhhh.group.secwealth.mktdata.api.equity.chart.request;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ChartRequest extends EquityCommonRequest implements Serializable {

    private static final long serialVersionUID = 5497084552971153819L;
    @NotEmpty(message = "{validator.notEmpty.ChartRequest.market.message}")
    private String market;

    private String productType;

    private String[] symbol;

    private List<ProductKey> productKeys;
    
    private Boolean delay;

    private String[] filters;

    private Number period;

    private Number intCnt;

    @NotEmpty(message = "{validator.notEmpty.chartRequest.intType.message}")
    @RegEx(regexp = "{validator.regex.chartRequest.intType}", message = "{validator.regex.chartRequest.intType.message}")
    private String intType;

    private String startTime;
    
    private String endTime;

    private Number maxPts;

    private Boolean timeZoneRequired;
}
