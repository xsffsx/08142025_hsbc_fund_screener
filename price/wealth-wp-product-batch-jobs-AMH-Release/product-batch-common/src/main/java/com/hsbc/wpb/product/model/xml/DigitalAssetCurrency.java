package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "digtlAssetCcy")
public class DigitalAssetCurrency {

    private ProductKeySegment prodKeySeg;

    private List<ProductAlternativeNumberSegment> prodAltNumSeg;

    private ProductInformationSegment prodInfoSeg;

    private DigitalAssetCurrencySegment digtlAssetCcySeg;

    private RecordDateTimeSegment recDtTmSeg;
}
