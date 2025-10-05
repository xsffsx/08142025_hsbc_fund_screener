package com.hhhh.group.secwealth.mktdata.api.equity.news.response.etnet;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EtnetNewsHeadlines extends EtnetErrResponse{

    private List<EtnetNews> newsList;

}
