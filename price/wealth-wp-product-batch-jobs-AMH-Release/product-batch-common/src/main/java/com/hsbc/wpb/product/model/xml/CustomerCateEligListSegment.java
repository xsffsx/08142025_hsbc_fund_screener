package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "custCateEligSeg")
@DocumentObject("restrCustGroup")
@Data
public class CustomerCateEligListSegment {

	@DocumentField("custProdCatCde")
	private String cuspCatCde;

	private String eligCuspCatInd;
}
