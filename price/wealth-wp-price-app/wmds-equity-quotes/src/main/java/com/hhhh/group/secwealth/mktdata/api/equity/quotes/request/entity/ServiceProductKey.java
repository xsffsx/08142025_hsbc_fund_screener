/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceProductKey {

    private String prodCdeAltClassCde;

    private String prodAltNum;

    private String productType;

    private String market;

    private String exchange;

}
