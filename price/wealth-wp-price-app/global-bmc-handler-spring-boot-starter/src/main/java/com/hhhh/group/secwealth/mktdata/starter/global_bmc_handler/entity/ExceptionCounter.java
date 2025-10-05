/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionCounter {

    private String exceptionName;

    private String key;

    private int timeDuration;

    private int maxNum;

    private String bmcExCde;

    private String bmcExMsg;

    private List<DurationExCounter> durationExceptionList = new ArrayList<DurationExCounter>();

    public boolean ignoreException() {
        return this.maxNum == -1;
    }

}
