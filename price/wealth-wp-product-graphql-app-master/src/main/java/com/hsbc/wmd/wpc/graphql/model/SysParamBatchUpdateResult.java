package com.dummy.wmd.wpc.graphql.model;

import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class SysParamBatchUpdateResult {
    private List<Document> updatedSysParams;
    private List<Document> notExistingSysParams;
}
