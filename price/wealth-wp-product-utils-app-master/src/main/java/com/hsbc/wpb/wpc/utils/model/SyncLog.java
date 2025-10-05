package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class SyncLog {
    public SyncLog(int eventCount, Date eventStartTime, Date eventEndTime, Date syncStartTime) {
        Objects.requireNonNull(eventStartTime);
        Objects.requireNonNull(syncStartTime);

        this.eventCount = eventCount;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
        this.syncStartTime = syncStartTime;
        this.lagMills = syncStartTime.getTime() - eventStartTime.getTime();
    }

    @Id
    protected String id;
    protected Date syncStartTime;
    protected Date syncEndTime;
    protected Date eventStartTime;
    protected Date eventEndTime;
    protected long lagMills;
    protected long costMills;
    protected int eventCount;

    protected String hostname;
    protected String lockToken;

    public void setSyncEndTime(Date time) {
        this.syncEndTime = time;
        costMills = syncEndTime.getTime() - syncStartTime.getTime();
    }
}
