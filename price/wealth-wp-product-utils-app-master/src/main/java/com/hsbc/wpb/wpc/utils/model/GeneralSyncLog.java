package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Document(collection = "data_sync_log")
public class GeneralSyncLog extends SyncLog {
    private final List<DataChangeEvent> changeEvents;
    private Map<String, Set<Map<String, Object>>> masterKeys;
    private Map<String, Set<String>> deletedSetMap;

    public GeneralSyncLog(List<DataChangeEvent> changeEvents, Date eventStartTime, Date eventEndTime, Date syncStartTime) {
        super(changeEvents.size(), eventStartTime, eventEndTime, syncStartTime);
        this.changeEvents = changeEvents;
    }
}
