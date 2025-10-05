package com.dummy.wpb.wpc.utils.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@Document("lock")
public class productLock {
    public static final String DATA_UTILS_TASK_LOCK = "DATA_UTILS_TASK_LOCK";

    @Id
    protected String id;
    protected Date startTime;
    protected Date endTime;
    protected Date lastHeartbeatTime;
    protected int heartbeatTimes;
    protected long timeout;
    protected String token;
    protected int lock;
    protected String lockFrom;
}
