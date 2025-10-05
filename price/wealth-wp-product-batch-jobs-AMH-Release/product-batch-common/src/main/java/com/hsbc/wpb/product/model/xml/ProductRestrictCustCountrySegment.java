package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodRestCustCtrySeg")
@DocumentObject("restrCustCtry")
@Data
public class ProductRestrictCustCountrySegment {

	private String restrCtryTypeCde;

	private String ctryIsoCde;

	private String restrCde;
}
