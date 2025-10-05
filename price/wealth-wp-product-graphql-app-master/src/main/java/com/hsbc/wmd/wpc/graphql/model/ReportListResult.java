package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.util.List;

@Data
public class ReportListResult {
    private int total;
    private int skip;
    private int limit;
    private List<Report> list;
}
