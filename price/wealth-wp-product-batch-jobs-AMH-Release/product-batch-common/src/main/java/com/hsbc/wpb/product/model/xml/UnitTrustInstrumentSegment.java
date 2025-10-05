package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.utils.DateUtils;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import com.dummy.wpb.product.xmladapter.LocalDateTimeAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "utTrstInstmSeg")
@DocumentObject("utTrstInstm")
@Data
public class UnitTrustInstrumentSegment {

	private String fundHouseCde;

	private String amcmInd;

	private String closeEndFundInd;

	private BigDecimal invstIncrmMinAmt;

	private BigDecimal rdmMinAmt;

	private Long utRtainMinNum;

	private BigDecimal fundRtainMinAmt;

	private String suptMipInd;

	private BigDecimal invstMipIncrmMinAmt;

	private BigDecimal chrgFundSwPct;

	private BigDecimal chrgInitSalesPct;

	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	private LocalDateTime scribCtoffNextDtTm;

	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	private LocalDateTime rdmCtoffNextDtTm;

	private String fundClassCde;

	private String insuLinkUtTrstInd;

	private BigDecimal fundSwInMinAmt;

	private BigDecimal fundSwOutMinAmt;

	private BigDecimal fundSwOutRtainMinAmt;

	private Long utSwOutRtainMinNum;

	private BigDecimal fundSwOutMaxAmt;

	private BigDecimal tranMaxAmt;

	private String incmHandlOptCde;

	private BigDecimal divYieldPct;

	private BigDecimal utPrcProdLnchAmt;

	private BigDecimal fundAmt;

	private Long utRdmMinNum;

	private BigDecimal invstMipMinAmt;

	private Long utMipRdmMinNum;

	private BigDecimal rdmMipMinAmt;

	private Long utMipRtainMinNum;

	private BigDecimal fundMipRtainMinAmt;

	private BigDecimal rtrnGurntPct;

	private String schemChrgCde;

	private BigDecimal dscntMaxPct;

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime offerStartDtTm;

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime offerEndDtTm;

	private String payCashDivInd;


	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate hldayFundNextDt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate dealNextDt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate divDclrDt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate divPymtDt;

	private String autoSweepFundInd;

	private String spclFundInd;

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime prodTrmtDt;

	private String autoRenewFundInd;

	@XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
	private LocalDateTime fundValnTm;

	private String prodShoreLocCde;

	private List<FundUnswithSegment> fundUnswithSeg;

	public void convertTimeValue(ZoneOffset zoneOffset) {
		scribCtoffNextDtTm = DateUtils.toSystemLocalDateTime(scribCtoffNextDtTm, zoneOffset);
		rdmCtoffNextDtTm = DateUtils.toSystemLocalDateTime(rdmCtoffNextDtTm, zoneOffset);
		offerStartDtTm = DateUtils.toSystemLocalDateTime(offerStartDtTm, zoneOffset);
		offerEndDtTm = DateUtils.toSystemLocalDateTime(offerEndDtTm, zoneOffset);
		prodTrmtDt = DateUtils.toSystemLocalDateTime(prodTrmtDt, zoneOffset);
	}
}
