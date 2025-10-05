package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "asetGeoLocAllocSeg")
@DocumentObject("asetGeoLocAlloc")
@Data
public class AssetGeographicLocationAllocationSegment {

	private String geoLocCde;

	private BigDecimal asetAllocWghtPct;
}
