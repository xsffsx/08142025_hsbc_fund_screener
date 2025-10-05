package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "global_esg_data")
public class EsgDataItem {
    @Id
    private String id;
    private Long prodId;
    private String isin;
    private String productClassification;
    private String classificationName;
    private EsgClassification classificationCode;
    private String securityName;
    private Integer year;
    private Integer quarter;
    private Date recCreatDtTm;
}
