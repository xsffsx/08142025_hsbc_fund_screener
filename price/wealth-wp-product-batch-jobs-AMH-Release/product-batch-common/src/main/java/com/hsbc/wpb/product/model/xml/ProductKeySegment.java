package com.dummy.wpb.product.model.xml;


import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.constant.Field;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "prodKeySeg")
@DocumentObject("$")
@Data
public class ProductKeySegment {
	private String ctryRecCde;

	private String grpMembrRecCde;

	private String prodTypeCde;

	@DocumentField(Field.prodAltPrimNum)
	private String prodCde;

	private String prodCdeAltClassCde;

	private String ctryProdTradeCde;

	private String ccyProdCde;
}
