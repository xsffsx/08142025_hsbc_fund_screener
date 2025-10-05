package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodPrc")
@Data
public class ProductPrice {

	private ProductKeySegment prodKeySeg;

	private List<ProductPriceSegment> prodPrcSeg;

	private EliDctSellPctSegment eliDctSellPctSeg;
}
