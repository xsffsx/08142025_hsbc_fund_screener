package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodAsetVoltlClassSeg")
@DocumentObject("asetVoltlClass")
@Data
public class ProductAssetVolatilityClassSegment {
	private String asetVoltlClassCde;
	
	private BigDecimal asetVoltlClassWghtPct;
}
