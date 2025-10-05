package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "eliUndlStockSeg")
@DocumentObject("eqtyLinkInvst.undlStock")
@Data
public class EliUnderlyingStockSegment {

	private String instmUndlCde;

	private String instmUndlText;

	private String crncyInstmUndlPriceCde;

	private BigDecimal prodStrkPrcAmt;

	private BigDecimal prodStrkCallPrcAmt;

	private BigDecimal prodStrkPutPrcAmt;

	private BigDecimal prodClosePrcAmt;

	private BigDecimal prodCloseLowPrcAmt;

	private BigDecimal prodCloseUpprPrcAmt;

	private BigDecimal prodClosePutPrcAmt;

	private BigDecimal prodCloseCallPrcAmt;

	private BigDecimal prodExerPrcAmt;

	private BigDecimal prodBreakEvenPrcAmt;

	private BigDecimal prodBreakEvenLowPrcAmt;

	private BigDecimal prodBreakEvenUpprPrcAmt;

	private BigDecimal prodBreakEvenPutPrcAmt;

	private BigDecimal prodBreakEvenCallPrcAmt;

	private Long sprdCnt;

	private Long instmEntlCnt;

	private BigDecimal prodKnockInPriceAmt;

	private BigDecimal prodStrkPriceInitPct;

	private BigDecimal prodStrkCallPriceInitPct;

	private BigDecimal prodKnockInPriceInitPct;

	private Long prodIdUndlInstm;

	private BigDecimal prodInitSpotPriceAmt;

	private BigDecimal prodFloorPriceAmt;

	private BigDecimal prodBarrierPriceAmt;

	private BigDecimal prodDownOutBarPrcAmt;
}
