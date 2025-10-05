package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
public class Report {
    private String ctryRecCde;
    private String grpMembrRecCde;
    private String filename;
    private String reportCode;
    private String ext;
    private LocalDate reportDate;
    private OffsetDateTime lastModifiedTime;
    private long size;
}
