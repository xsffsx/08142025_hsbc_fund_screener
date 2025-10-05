package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "resChanSeg")
@DocumentObject
@Data
public class ResChanSegment {

	@XmlElement(name = "resChannelDtl")
	private List<ResChannelDtl> resChannelDtls;
}
