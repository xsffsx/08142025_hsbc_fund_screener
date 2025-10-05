package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "asetSicAllocSeg")
@DocumentObject("asetSicAlloc")
@Data
public class AssetStandardIndustryClassificationAllocationSegment {
	private String sicCde;

	private BigDecimal asetAllocWghtPct;
}
