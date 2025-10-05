package com.hhhh.group.secwealth.mktdata.api.equity.topmover.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.request.EquityCommonRequest;
import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @ClassName: topmoverRequest
 * @Description: TODO(Receive user requests)
 * @author 45027090
 * @date Jan 16, 2019
 *
 */
@Getter
@Setter
public class TopMoverRequest extends EquityCommonRequest implements Serializable {

    private static final long serialVersionUID = 5497084552971153819L;

    private String locale;

    @NotEmpty(message = "{validator.notEmpty.topmoverRequest.market.message}")
    @RegEx(regexp = "{validator.regex.topmoverRequest.market}", message = "{validator.regex.topmoverRequest.market.message}")
    private String market;

    private String exchangeCode;

//    @NotNull(message = "{validator.notNull.topmoverRequest.delay.message}")
    private Boolean delay;

    @NotEmpty(message = "{validator.notEmpty.topmoverRequest.productType.message}")
    @RegEx(regexp = "{validator.regex.topmoverRequest.productType}", message = "{validator.regex.topmoverRequest.productType.message}")
    private String productType;

    private Integer topNum;

    private String token;

    private String countryCode;

    private String groupMember;

    private String appCode;

    private String requestType;
    
    private String moverType;
    
    private String boardType;

    @Override
    public String toString() {
        return "topmoverRequest [locale=" + this.locale + ", market=" + this.market + ", exchangeCode=" + this.exchangeCode
            + ", delay=" + this.delay + ", productType=" + this.productType + ", topNum=" + this.topNum + ", token=" + this.token
            + ", countryCode=" + this.countryCode + ", groupMember=" + this.groupMember + ", appCode=" + this.appCode + "]";
    }

}
