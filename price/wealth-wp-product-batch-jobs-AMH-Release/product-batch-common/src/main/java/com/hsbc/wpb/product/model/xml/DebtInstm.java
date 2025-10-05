package com.dummy.wpb.product.model.xml;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.*;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


import lombok.Data;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "debtInstm")
@XmlType(propOrder = {"recDtTmSeg", "prodKeySeg", "prodAltNumSeg", "prodInfoSeg", "debtInstmSeg"})
@Data
public class DebtInstm {
    private ProductKeySegment prodKeySeg;

    private List<ProductAlternativeNumberSegment> prodAltNumSeg;

    private ProductInformationSegment prodInfoSeg;

    private DebitInstrumentSegment debtInstmSeg;

    private ProductRecordDateTimeSegment recDtTmSeg;

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        prodInfoSeg.convertTimeValue(recDtTmSeg.getZoneId().getRules().getOffset(Instant.now()));
        debtInstmSeg.convertTimeValue(recDtTmSeg.getZoneId().getRules().getOffset(Instant.now()));
    }
}

