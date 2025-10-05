package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.etnet;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EtnetResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private Object[] data;

    private String[] fields;

}
