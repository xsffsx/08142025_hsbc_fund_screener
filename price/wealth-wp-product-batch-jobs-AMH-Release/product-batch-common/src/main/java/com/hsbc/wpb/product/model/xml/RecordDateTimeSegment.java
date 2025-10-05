package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.xmladapter.LocalDateTimeAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recDtTmSeg")
@Data
public class RecordDateTimeSegment {

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime recCreatDtTm;
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime recUpdtDtTm;

    private String timeZone;

    public ZoneId getZoneId() {
        try {
            return ZoneId.of(timeZone);
        } catch (DateTimeException ex) {
            return ZoneId.systemDefault();
        }
    }
}
