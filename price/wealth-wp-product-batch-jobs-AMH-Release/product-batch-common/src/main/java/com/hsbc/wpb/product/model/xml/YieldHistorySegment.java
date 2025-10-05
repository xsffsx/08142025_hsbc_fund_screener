package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "yieldHistSeg")
@DocumentObject("debtInstm.yieldHist")
@Getter
@Setter
@ToString
public class YieldHistorySegment {

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate yieldDt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate yieldEffDt;

    private String yieldTypeCde;
    private BigDecimal yieldBidPct;
    private BigDecimal yieldOfferPct;
}
