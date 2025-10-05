/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.chart.request;
import org.hibernate.validator.constraints.NotEmpty;

import com.hhhh.group.secwealth.mktdata.starter.validation.annotation.RegEx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductKey {

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.productKey.prodCdeAltClassCde.message}")
    @RegEx(regexp = "{validator.regex.quotesRequest.productKey.prodCdeAltClassCde}", message = "{validator.regex.quotesRequest.productKey.prodCdeAltClassCde.message}")
    private String prodCdeAltClassCde;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.productKey.prodAltNum.message}")
    private String prodAltNum;

    @NotEmpty(message = "{validator.notEmpty.quotesRequest.productKey.productType.message}")
    private String productType;
}
