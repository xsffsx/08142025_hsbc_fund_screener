package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodUserDefOPExtSeg")
@Data
@DocumentObject("extOp")
public class ProductUserDefinitionOPExtSegment {

	private String fieldCde;

	private String fieldValue;
}
