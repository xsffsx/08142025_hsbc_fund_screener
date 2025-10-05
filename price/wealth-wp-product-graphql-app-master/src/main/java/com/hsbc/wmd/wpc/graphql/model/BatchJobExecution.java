package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.Date;

@Data
public class BatchJobExecution {
    private String shortContext;
    private String serializedContext;
    private Long jobExecutionId;
    private Long jobInstanceId;
    private Date createTime;
    private Date startTime;
    private Date endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private Date lastUpdated;
    private String jobName;
    private String systemCde;
}
