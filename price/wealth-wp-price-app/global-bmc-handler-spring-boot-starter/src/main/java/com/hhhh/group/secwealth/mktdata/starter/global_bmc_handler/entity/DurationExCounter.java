/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_bmc_handler.entity;

import java.util.GregorianCalendar;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DurationExCounter {

    private GregorianCalendar time;

    private Throwable exception;

    public DurationExCounter(final Throwable exception) {
        this.time = new GregorianCalendar();
        this.exception = exception;
    }

}
