package com.dummy.wpb.wpc.utils.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataChangeEvent {
    // since the ts keep only ms level, not enough to indicate the order of the events, use a sequence number to indicate that
    private int order;
    private Timestamp ts;
    private String tableName;
    private String op;
    /**
     * The ROWID of the changed row in named table
     */
    private String rowId;
    /**
     * The ROWID of DATA_CHANGE_EVENT table
     */
    private String id;

    /**
     * prodId for product related table, null for non-product table
     */
    private Long prodId;

    /**
     * The key combine with rowid@table|operation
     * @return
     */
    public String asKey3() {
        return String.format("%s@%s|%s", rowId, tableName, op);
    }
}
