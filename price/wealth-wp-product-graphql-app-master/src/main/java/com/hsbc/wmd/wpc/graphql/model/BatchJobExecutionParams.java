package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.Date;
@Data
public class BatchJobExecutionParams {
    private Long jobExecutionId;
    private Date createTime;
    private String typeCd;
    private String keyName;
    private String stringVal;
    private Date dateVal;
    private String longVal;
    private String doubleVal;
    private String identifying;

}
