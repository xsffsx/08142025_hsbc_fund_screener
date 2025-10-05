/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuoteListServiceHKRequest {

    private boolean delay;

    private String locale;

    private String stock;

}
