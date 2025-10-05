package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.xmladapter.LocalDateTimeAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Market Rate Segment
 */
@Data
@DocumentObject("marketRate")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "marketRateSeg")
public class MarketRateSegment {

    private String currencyPair;

    private String curveType;

    private BigDecimal midRate;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime xpeTime;
}
