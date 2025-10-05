/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BMCEntity {

    private final Map<String, ExceptionCounter> exceptionCounterMap = new HashMap<String, ExceptionCounter>();
    private List<ExceptionCounter> exceptionCounterList = new ArrayList<>();

    private String prefix;
    private String countryCode;
    private String groupMember;

    private int exTimeDurationThrownPastSec;
    private int exNumThrownPastSec;
    private String exCdeThrownPastSec;
    private String exMgsThrownPastSec;
    private int exNumExceedLimit;
    private String exCdeExceedLimit;
    private String exMgsExceedLimit;

    private final List<DurationExCounter> durationExceptionList = new ArrayList<>();
    private final AtomicInteger totalExceptionNum = new AtomicInteger(0);

    public void initExceptionCounterMap() {
        if (this.exceptionCounterList != null && !this.exceptionCounterList.isEmpty()) {
            for (int i = 0; i < this.exceptionCounterList.size(); i++) {
                final ExceptionCounter counter = this.exceptionCounterList.get(i);
                this.exceptionCounterMap.put(counter.getExceptionName() + counter.getKey(), counter);
            }
        }
    }

}
