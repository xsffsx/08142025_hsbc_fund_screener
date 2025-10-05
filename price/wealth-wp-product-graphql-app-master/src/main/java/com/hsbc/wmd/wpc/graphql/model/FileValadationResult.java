package com.dummy.wmd.wpc.graphql.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileValadationResult {
    private String fileName;
    private Boolean isUpload;
    private String reason;
}
