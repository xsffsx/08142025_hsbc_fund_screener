package com.dummy.wpb.product.thymeleaf.expression;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TimeZones {

    @Value("${#{exportRequest.timeZone}:${batch.timeZone}}")
    String zoneId;

    private static final String GMT= "GMT";

    public String get() {
        return String.format("%s%s",GMT,OffsetDateTime.now(ZoneId.of(zoneId)).format(DateTimeFormatter.ofPattern("Z")));
    }

    public String format(Date date,String pattern) {
        if (null == date) return null;
        ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.of(zoneId));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zonedDateTime.format(formatter);
    }
}
