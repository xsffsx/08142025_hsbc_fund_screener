package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPORequest extends EquityCommonRequest{
	
	@NotEmpty(message = "{validator.notEmpty.ipoRequest.market.message}")
    @RegEx(regexp = "{validator.regex.ipoRequest.market}", message = "{validator.regex.ipoRequest.market.message}")
    private String market;

    private Integer recordsPerPage = 10;

    private Integer pageId = 1;
}
