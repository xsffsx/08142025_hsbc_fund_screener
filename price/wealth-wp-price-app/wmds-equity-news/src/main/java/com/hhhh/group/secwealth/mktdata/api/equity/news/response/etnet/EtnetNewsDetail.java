package com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EtnetNewsDetail extends EtnetErrResponse {

    private String id;
    private String headline;
    private String content;
    private String asOfDateTime;

}
