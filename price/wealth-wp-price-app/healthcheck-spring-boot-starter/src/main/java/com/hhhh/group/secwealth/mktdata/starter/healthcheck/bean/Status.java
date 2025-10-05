/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {

    public static final String SUCCESS = "SUCCESS";

    public static final String FAILURE = "FAILURE";

    private String name;

    private String subName;

    private String statusCode;

    private long costTime;

    private String exception;

    public Status(final String statusCode, final String name, final long costTime) {
        this.statusCode = statusCode;
        this.name = name;
        this.costTime = costTime;
    }

    public Status(final String statusCode, final String name, final String subName, final long costTime) {
        this.statusCode = statusCode;
        this.name = name;
        this.subName = subName;
        this.costTime = costTime;
    }

    public Status(final String statusCode, final String name, final String exception) {
        this.statusCode = statusCode;
        this.name = name;
        this.exception = exception;
    }

    public Status(final String statusCode, final String name, final String subName, final String exception) {
        this.statusCode = statusCode;
        this.name = name;
        this.subName = subName;
        this.exception = exception;
    }

}
