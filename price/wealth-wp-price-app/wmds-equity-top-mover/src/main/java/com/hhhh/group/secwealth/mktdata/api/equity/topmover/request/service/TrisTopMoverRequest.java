package com.hhhh.group.secwealth.mktdata.api.equity.topmover.request.service;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrisTopMoverRequest implements Serializable {

    private static final long serialVersionUID = 4924763788463173498L;

    private String closure;

    private String token;

    private String service;

    private String bqos;

    private String wqos;

    private List<String> item;

    private List<String> filter;

    private Boolean expandChain;

    private Boolean emptyLink;

    private Boolean chainLink;
}