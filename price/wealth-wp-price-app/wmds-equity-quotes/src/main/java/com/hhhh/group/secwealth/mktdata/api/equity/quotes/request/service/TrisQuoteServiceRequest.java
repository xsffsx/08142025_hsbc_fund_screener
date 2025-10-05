/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrisQuoteServiceRequest {

    private String closure;

    private String token;

    private String service;

    private String bqos;

    private String wqos;

    private final List<String> item = new ArrayList<>();

    private final List<String> filter = new ArrayList<>();

    private Boolean expandChain;

    private Boolean emptyLink;

    private Boolean chainLink;

    public void addItem(final String item) {
        this.item.add(item);
    }

    public void addFilter(final String filter) {
        this.filter.add(filter);
    }

    public void addItem(final List<String> item) {
        this.item.addAll(item);
    }

    public void addFilter(final List<String> filter) {
        this.filter.addAll(filter);
    }

}
