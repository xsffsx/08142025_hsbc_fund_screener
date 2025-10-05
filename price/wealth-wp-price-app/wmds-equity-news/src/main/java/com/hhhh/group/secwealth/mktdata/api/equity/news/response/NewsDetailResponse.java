package com.hhhh.group.secwealth.mktdata.api.equity.news.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsDetailResponse {
    private String id;
    private String headline;
    private String content;
    private Boolean isTableFormat;
    private Number numOfCharPerLine;
    private String asOfDateTime;
    private String asOfDate;
    private String asOfTime;
    private String newsLang;
    private String[] relatedCodes;
}
