package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.utils.DateUtils;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import com.dummy.wpb.product.xmladapter.LocalDateTimeAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "debtInstmSeg")
@DocumentObject("debtInstm")
@Data
public class DebitInstrumentSegment {
    @DocumentField("isrBondName")
    private String isrBndNme;

    private String issueNum;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate prodIssDt;

    @XmlElement(name="pdcyCoupnPymtCd")
    private String pdcyCoupnPymtCde;

    @XmlElement(name="coupnAnnlRt")
    private BigDecimal coupnAnnlRate;

    @XmlElement(name="coupnExtInstmRt")
    private BigDecimal coupnExtInstmRate;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate pymtCoupnNextDt;

    private String flexMatOptInd;

    @XmlElement(name="IntIndAccrAmt")
    private BigDecimal intIndAccrAmt;

    @XmlElement(name="InvstIncMinAmt")
    private BigDecimal invstIncrmMinAmt;

    private BigDecimal prodBodLotQtyCnt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    @XmlElement(name="MturExtDt")
    private LocalDate mturExtDt;

    @XmlElement(name="SubDebtInd")
    private String subDebtInd;

    private String bondStatCde;


    private String ctryBondIssueCde;

  //  private String intBasisCalcText;

    private String grntrName;

    private String cptlTierText;

    private String coupnType;

    @XmlElement(name="coupnPrevRt")
    private String coupnPrevRate;

    @XmlElement(name="indexFltRtNme")
    private String indexFltRateName;

    @XmlElement(name="bondFltSprdRt")
    private String bondFltSprdRate;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate coupnCurrStartDt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate coupnPrevStartDt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate bondCallNextDt;

    @XmlElement(name="intBassiCalcText")
    private String intBasisCalcText;

    private BigDecimal invstSoldLestAmt;

    private BigDecimal invstIncrmSoldAmt;

    private Long intAccrDayCnt;
    
    private BigDecimal shrBidCnt;
    
    private BigDecimal shrOffrCnt;

    @XmlElement(name="prodClsBidPrcAmt")
    private String prodCloseBidPrcAmt;

    @XmlElement(name="prodClsOffrPrcAmt")
    private String prodCloseOffrPrcAmt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate bondCloseDt;
    
    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate bondSetlDt;

    private BigDecimal dscntMrgnBselPct;
    
    private BigDecimal dscntMrgnBbuyPct;

    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime prcBondRecvDtTm;
    
    private String yieldOfferText;
    
    private String coupnAnnlText;
    
    private String isrDesc;

    @DocumentField("trnsl.isrPllDesc")
    private String isrPllDesc;

    @DocumentField("trnsl.isrSllDesc")
    private String isrSllDesc;
    
    private String srTypeCde;
    
    private List<CreditRatingSegment> creditRtingSeg;

    public void convertTimeValue(ZoneOffset zoneOffset) {
        prcBondRecvDtTm = DateUtils.toSystemLocalDateTime(prcBondRecvDtTm, zoneOffset);
    }

    private List<YieldHistorySegment> yieldHistSeg;
}
