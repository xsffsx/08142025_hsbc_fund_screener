package com.dummy.wmd.wpc.graphql.model;

import com.dummy.wmd.wpc.graphql.utils.DataProcessingUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;

import java.util.Date;

@NoArgsConstructor
@Data
@Document(collection = "tt_dat_proc_stat")
public class FileIngressRecord {
    @Id
    private Long id;
    private Long fisid;
    private Long recordNum;
    private String identifier;
    private String payload;
    private String rawData;
    @Field("stat")
    private IngressRecordStatus status;
    private String sourceSystem;
    @Field("crtDtTm")
    private Date createTime;
    @Field("updtDtTm")
    private Date lastUpdateTime;
    @Field("errorMessage")
    private String rawErrorMessage;

    public String getErrorMessage() {
        if (StringUtils.hasText(rawErrorMessage)) {
            return DataProcessingUtils.simplifyErrorMessage(rawErrorMessage);
        }
        return rawErrorMessage;
    }
}
