package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "gnrcProd")
@Data
public class GnrcProductInstrument {

	private ProductKeySegment prodKeySeg;

	private List<ProductAlternativeNumberSegment> prodAltNumSeg;

	private ProductInformationSegment prodInfoSeg;

	private RecordDateTimeSegment recDtTmSeg;
}
