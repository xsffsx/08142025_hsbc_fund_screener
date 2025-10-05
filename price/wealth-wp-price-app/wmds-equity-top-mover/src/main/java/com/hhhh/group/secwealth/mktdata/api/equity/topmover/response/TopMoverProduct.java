package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class TopMoverProduct implements Serializable {

    private static final long serialVersionUID = 3116603200672306042L;

    private String unsignedAgreementId;

    private String ric;

    private String symbol;

    private String market;

    private String name;

    private BigDecimal price;

    private Boolean delay;

    private BigDecimal change;

    private BigDecimal changePercent;

    private BigDecimal volume;

    private String productType;

    private String currency;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS Z")
    private Date exchangeUpdatedTime;

    private List<ProdAltNumSeg> prodAltNumSegs;

}
