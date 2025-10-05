/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdAltNumSeg {

    @NotEmpty(message = "{validator.notEmpty.quotesResponse.quotesPriceQuote.prodAltNumSeg.prodCdeAltClassCde}")
    private String prodCdeAltClassCde;

    @NotEmpty(message = "{validator.notEmpty.quotesResponse.quotesPriceQuote.prodAltNumSeg.prodAltNum}")
    private String prodAltNum;


}
