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


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "stockInstmSeg")
@DocumentObject("stockInstm")
@Data
public class StockInstrumentSegment {

	private String mrgnTrdInd;

	@DocumentField("mrgnSecOdPct")
	private BigDecimal mrgnSecODPct;

	private String suptAuctnTrdInd;

	private BigDecimal stopLossMinPct;

	private BigDecimal stopLossMaxPct;

	private Long sprdStopLossMinCnt;

	private Long sprdStopLossMaxCnt;

	private BigDecimal prodBodLotQtyCnt;

	private String mktProdTrdCde;

	private String suptMipInd;

	private String bodLotProdInd;

	private String trdLimitInd;

	private String psblProdBorwInd;

	private String allowProdLendInd;

	private String poolProdInd;

	private String scripOnlyProdInd;

	private String suptAtmcTrdInd;

	private String suptNetSetlInd;

	private String suptStopLossOrdInd;

	private String suptWinWinOrdInd;

	private String suptAllIn1OrdInd;

	private String suptProdSpltInd;

	private BigDecimal mrgnPrcAuctnTrdPct;

	private String exclLimitCalcInd;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate prodMktStatChngLaDt;

	private String prcVarCde;

	private String methCalcScripChrgCde;

	private String mktSegExchgCde;

	private String ctryProdRegisCde;

	private BigDecimal earnPerShareAnnlAmt;

	private BigDecimal invstMipMinAmt;

	private BigDecimal invstMipIncrmMinAmt;

	private Long prodIssQtyTtlCnt;

	private BigDecimal prodMaxIndvOwnrPct;

	private BigDecimal prodMaxIndvForgnInvstrPct;

	private BigDecimal prodMaxForgnInvstrTtlPct;

	private BigDecimal prodExerPrcAmt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate prodAuctnTrdExpirDt;

	private BigDecimal loanProdMrgnTrdPct;

	private BigDecimal loanBdgtProdIPOAmt;

	private BigDecimal loanProdIPOTtlAmt;

	private Long prodSellMaxQtyCnt;

	private Long prodBuyMaxQtyCnt;

	private BigDecimal prcVarMinAmt;

	@XmlJavaTypeAdapter(value = LocalDateAdapter.class)
	private LocalDate prcVarMinEffDt;

	private String miFirIdentifier;

	private String miFirIdentifierDes;

	private BigDecimal miFidAvgDailyNumEsma;

	private BigDecimal miFidAvgDailyNumFca;

	private String vaEtfIndicator;
}
