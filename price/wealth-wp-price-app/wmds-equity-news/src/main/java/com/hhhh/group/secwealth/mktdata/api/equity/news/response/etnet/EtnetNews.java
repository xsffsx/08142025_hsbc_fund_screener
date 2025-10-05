package com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EtnetNews {

    private String id;
    private String source;
    private String headline;
    private String[] relatedCodes;
    private String brief;
    private String asOfDateTime;

}
