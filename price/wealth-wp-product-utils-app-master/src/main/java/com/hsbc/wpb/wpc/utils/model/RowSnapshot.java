package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "data_sync_row")
public class RowSnapshot {
    @Id
    private String id;
    private org.bson.Document data;
    private String lastOp;
    private Date lastTs;
    private String rowid;
    private String table;
}
