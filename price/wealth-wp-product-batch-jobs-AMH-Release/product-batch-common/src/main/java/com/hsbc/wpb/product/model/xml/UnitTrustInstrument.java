package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "utTrstInstm")
@Data
public class UnitTrustInstrument {

    private ProductKeySegment prodKeySeg;

    private List<ProductAlternativeNumberSegment> prodAltNumSeg;

    private ProductInformationSegment prodInfoSeg;

    private UnitTrustInstrumentSegment utTrstInstmSeg;

    private EsgSegment esgSeg;

    private ProductRecordDateTimeSegment recDtTmSeg;

    public void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
        utTrstInstmSeg.convertTimeValue(recDtTmSeg.getZoneId().getRules().getOffset(Instant.now()));
    }
}
