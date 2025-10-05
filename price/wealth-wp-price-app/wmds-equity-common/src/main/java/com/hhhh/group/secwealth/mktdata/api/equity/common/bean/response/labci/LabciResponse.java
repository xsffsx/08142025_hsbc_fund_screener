/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;


@Getter
@Setter
public class LabciResponse {

    private String symbol;

    private String parent;

    private int status;

    private Map<String, Object> fields = new HashMap<String, Object>();

}
