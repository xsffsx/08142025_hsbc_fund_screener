package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;

@XmlRootElement(name = "prodPerfmSeg")
@XmlAccessorType(XmlAccessType.FIELD)
@DocumentObject("performance")
@Data
public class ProductPerformanceSegment {

	private String perfmTypeCde;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate perfmCalcDt;

	private BigDecimal perfmYrToDtPct;

	@DocumentField("perfm1MoPct")
	private BigDecimal perfm1moPct;

	@DocumentField("perfm3MoPct")
	private BigDecimal perfm3moPct;

	@DocumentField("perfm6MoPct")
	private BigDecimal perfm6moPct;

	@DocumentField("perfm1YrPct")
	private BigDecimal perfm1yrPct;

	@DocumentField("perfm3YrPct")
	private BigDecimal perfm3yrPct;

	@DocumentField("perfm5YrPct")
	private BigDecimal perfm5yrPct;

	private BigDecimal perfmExt1YrPct;

	private BigDecimal perfmSinceLnchPct;

	private BigDecimal rtrnVoltl1YrPct;

	private BigDecimal rtrnVoltlExt1YrPct;

	private BigDecimal rtrnVoltl3YrPct;

	private BigDecimal rtrnVoltlExt3YrPct;

	@DocumentField("perfm6MoAmt")
	private BigDecimal perfm6moAmt;

	private BigDecimal perfmYrToDtAmt;

	@DocumentField("perfm1YrAmt")
	private BigDecimal perfm1yrAmt;

	@DocumentField("perfm3YrAmt")
	private BigDecimal perfm3yrAmt;

	private RecordDateTimeSegment recDtTmSeg;

}
