/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.midfs;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatus {

    private String key;

    private Map<String, String> value = new HashMap<>();

    private String correctStatus;

}
