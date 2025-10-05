package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodUserDefSeg")
@Data
public class ProductUserDefinitionSegment {
	@XmlElement(name = "fieldTypeCde")
	private String fieldTypeCde;

	@XmlElement(name = "fieldCde")
	private String fieldCde;

	@XmlElement(name = "fieldValueText")
	private String fieldValueText;

}
