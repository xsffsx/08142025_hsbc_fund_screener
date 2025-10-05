package com.hhhh.group.secwealth.mktdata.api.equity.news.request.service;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EtnetNewsDetailServiceRequest {

    private String closure;

    private String token;

    private List<String> item;

    private List<String> filter;
}
