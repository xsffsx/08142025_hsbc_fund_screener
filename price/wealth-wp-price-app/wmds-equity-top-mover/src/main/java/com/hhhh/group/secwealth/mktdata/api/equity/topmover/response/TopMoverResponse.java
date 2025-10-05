package com.hhhh.group.secwealth.mktdata.api.equity.topmover.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopMoverResponse implements Serializable {

    private static final long serialVersionUID = 1817215332757514880L;

    private List<TopMoverTable> topMovers;
}
