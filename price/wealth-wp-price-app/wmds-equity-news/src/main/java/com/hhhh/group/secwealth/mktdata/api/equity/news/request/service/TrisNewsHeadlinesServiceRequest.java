/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.request.service;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrisNewsHeadlinesServiceRequest {

    private String closure;

    private String token;

    private String query;

    private final List<String> filter = new ArrayList<>();

    private Integer numResult;

    private String sortby;

    private Boolean highlight;

    private Boolean totalHitCount;

//    private Date startDtTm;
//    private Date endDtTm;
//    private Date localTm;

//    private String localSeqNo;

    private Boolean teaser;

    private Integer resultOffset;

//    private String topNewsQueryString;
//
//    private Date topNewsStartDtTm;
//
//    private Date topNewsEndDtTm;
//
//    private String analyticQueryString;

    public void addFilter(final List<String> filter) {
        this.filter.addAll(filter);
    }

}
