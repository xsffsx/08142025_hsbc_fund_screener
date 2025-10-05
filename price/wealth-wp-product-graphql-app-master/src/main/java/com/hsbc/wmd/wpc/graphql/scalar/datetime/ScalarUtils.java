package com.dummy.wmd.wpc.graphql.scalar.datetime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class ScalarUtils {
    public static OffsetDateTime dateToOffsetDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toOffsetDateTime();
    }
}
