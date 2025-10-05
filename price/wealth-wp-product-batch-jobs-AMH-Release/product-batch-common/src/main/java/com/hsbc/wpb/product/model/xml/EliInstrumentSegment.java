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
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "eliSeg")
@DocumentObject("eqtyLinkInvst")
@Data
public class EliInstrumentSegment {

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate trdDt;

	private BigDecimal dscntSellPct;

	private Long custSellQtaCnt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate setlDt;

	private String prodExtnlCde;

	private String prodExtnlTypeCde;

	private String eqtyLinkInvstTypeCde;

	private BigDecimal dscntBuyPct;

	private BigDecimal yieldToMturPct;

	private BigDecimal denAmt;

	private BigDecimal trdMinAmt;

	private String suptAonInd;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate pymtDt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate valnDt;

	private String offerTypeCde;

	private String ruleQtaAltmtCde;

	private String lnchProdInd;

	private String rtrvProdExtnlInd;

	private String prodExtnlCatCde;

	private String pdcyCallCde;

	private String pdcyKnockInCde;

	private List<EliUnderlyingStockSegment> eliUndlStockSeg;

	private NartTxtBoxSegment nartTxtBoxSeg;
}
