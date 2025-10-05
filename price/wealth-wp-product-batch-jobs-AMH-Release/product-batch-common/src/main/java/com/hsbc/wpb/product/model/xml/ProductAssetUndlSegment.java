package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodAsetUndlSeg")
@DocumentObject("undlAset")
@Data
public class ProductAssetUndlSegment {
	@DocumentField("seqNum")
	private Long asetUndlCdeSeqNum;
	
	private String asetUndlCde;
}
