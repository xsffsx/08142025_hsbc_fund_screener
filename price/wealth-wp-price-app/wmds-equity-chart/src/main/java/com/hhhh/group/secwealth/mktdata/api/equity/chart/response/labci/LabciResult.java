package com.hhhh.group.secwealth.mktdata.api.equity.chart.response.labci;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LabciResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String item;

    private Object timeZone;
    
    private Object[] data;

    private String[] field;

}