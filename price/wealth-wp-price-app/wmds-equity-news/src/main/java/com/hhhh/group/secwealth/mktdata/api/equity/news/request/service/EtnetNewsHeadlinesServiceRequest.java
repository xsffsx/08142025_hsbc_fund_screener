/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.request.service;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EtnetNewsHeadlinesServiceRequest {

    private String category;

    private String symbol;

    private Integer recordsPerPage;

    private Integer pageId;

    private Boolean locale;

    private Boolean token;

}
