package com.hhhh.group.secwealth.mktdata.api.equity.news.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;


@Getter
@Setter
public class NewsDetailRequest extends EquityCommonRequest {
    @NotEmpty(message = "{validator.notEmpty.trisNewsDetailRequest.market.message}")
    private String market;

    @NotEmpty(message = "{validator.notEmpty.trisNewsDetailRequest.id.message}")
    private String id;

    private String source;

    private boolean translate;
}
