package com.dummy.wpb.product.model.xml;


import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodPrcSeg")
@DocumentObject("priceHistory")
@Data
public class ProductPriceSegment {

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate prcEffDt;

	private String pdcyPrcCde;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate prcInpDt;

	private String ccyProdMktPrcCde;

	private BigDecimal prodBidPrcAmt;

	private BigDecimal prodOffrPrcAmt;

	private BigDecimal prodNavPrcAmt;

	private RecordDateTimeSegment recDtTmSeg;

	private StockPriceSegment stockPrcSeg;

	private BigDecimal prodMktPrcAmt;
}
