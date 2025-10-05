package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Digital Asset Currency Segment
 */
@Data
@DocumentObject("digtlAssetCcy")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "digtlAssetCcySeg")
public class DigitalAssetCurrencySegment {

    private String digtlCcy;

    private List<MarketRateSegment> marketRateSeg;

}
