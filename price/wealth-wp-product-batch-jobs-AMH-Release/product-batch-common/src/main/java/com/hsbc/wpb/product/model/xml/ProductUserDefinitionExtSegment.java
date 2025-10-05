package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodUserDefExtSeg")
@DocumentObject("ext")
@Data
public class ProductUserDefinitionExtSegment {
	private String fieldCde;

	private String fieldValue;
}
