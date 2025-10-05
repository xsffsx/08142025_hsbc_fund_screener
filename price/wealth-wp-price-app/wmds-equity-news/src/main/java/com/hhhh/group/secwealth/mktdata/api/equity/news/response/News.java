package com.hhhh.group.secwealth.mktdata.api.equity.news.response;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class News {

    private String id;

    private String source;

    private String headline;

    private String[] relatedCodes;

    private String brief;

    private String newsLang;

    private String asOfDateTime;

    private String asOfDate;

    private String asOfTime;

}
