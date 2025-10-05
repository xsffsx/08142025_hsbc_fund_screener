package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document(collection = "data_sync_log")
public class ProductSyncLog extends SyncLog {
    private int prodCount;
    private List<Long> prodIdList;

    public ProductSyncLog(ProductEventGroup group) {
        super(group.getEventCount(), group.getEventStartTime(), group.getEventEndTime(), new Date());
        setProdIdList(group.getProdIdList());
    }

    public void setProdIdList(List<Long> prodIdList) {
        this.prodIdList = prodIdList;
        this.prodCount = prodIdList.size();
    }
}
