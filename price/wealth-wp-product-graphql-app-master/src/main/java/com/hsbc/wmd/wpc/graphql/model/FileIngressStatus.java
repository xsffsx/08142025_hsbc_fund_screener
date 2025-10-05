package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@NoArgsConstructor
@Data
@Document(collection = "tt_file_ingress_stat")
public class FileIngressStatus {
    @Id
    private Long id;
    @Field("fileName")
    private String filename;
    private String md5;
    @Field("isAllRecordLoadedToDatabase")
    private Boolean allLoaded;
    @Field("noOfRecordsToBeProcess")
    private Long recordCount;
    @Field("crtDtTm")
    private Date createTime;
    @Field("updtDtTm")
    private Date lastUpdateTime;
    private Boolean isExcelUploadForm;
    private Long pendingCount;
    private Long finishedCount;
    private Long errorCount;
    private Long skippedCount;
    @Field("stat")
    private IngressFileStatus status;
    private String errorMessage;
}
