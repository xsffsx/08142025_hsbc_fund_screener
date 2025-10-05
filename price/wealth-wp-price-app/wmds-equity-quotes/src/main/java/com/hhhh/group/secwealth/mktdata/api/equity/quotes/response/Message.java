/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {

    private String reasonCode;

    private String text;

    private String traceCode;

    private String productType;

    private String prodCdeAltClassCde;

    private String prodAltNum;

}
