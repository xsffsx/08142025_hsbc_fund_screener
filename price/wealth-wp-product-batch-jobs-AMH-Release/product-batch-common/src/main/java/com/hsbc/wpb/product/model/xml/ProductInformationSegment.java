package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.utils.DateUtils;
import com.dummy.wpb.product.xmladapter.LocalDateAdapter;
import com.dummy.wpb.product.xmladapter.LocalTimeAdapter;
import lombok.Data;

import java.time.LocalTime;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodInfoSeg")
@DocumentObject("$")
@Data
public class ProductInformationSegment {
    private String prodSubtpCde;

    private String prodName;

    private String prodPllName;

    private String prodSllName;

    private String prodShrtName;

    private String prodStatCde;

    private String ccyProdCde;

    private String riskLvlCde;

    private String prdProdCde;

    private Long prdProdNum;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate prodLnchDt;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate prodMturDt;

    private String allowBuyProdInd;

    private String allowSellProdInd;

    private String allowBuyUtProdInd;

    private String allowBuyAmtProdInd;

    private String allowSellUtProdInd;

    private String allowSellAmtProdInd;

    private String allowSellMipProdInd;

    private String allowSellMipUtProdInd;

    private String allowSellMipAmtProdInd;

    private String allowSwInProdInd;

    private String allowSwInUtProdInd;

    private String allowSwInAmtProdInd;

    private String allowSwOutProdInd;

    private String allowSwOutUtProdInd;

    private String allowSwOutAmtProdInd;

    private String incmCharProdInd;

    private String cptlGurntProdInd;

    private String yieldEnhnProdInd;

    private String grwthCharProdInd;

    private Long prtyProdSrchRsultNum;

    private BigDecimal rtrnVoltlAvgPct;

    private String dispComProdSrchInd;

    private BigDecimal divrNum;

    private String mrkToMktInd;

    @DocumentField("ctryProdTradeCde[0]")
    private String ctryProdTrade1Cde;

    @DocumentField("ctryProdTradeCde[1]")
    private String ctryProdTrade2Cde;

    @DocumentField("ctryProdTradeCde[2]")
    private String ctryProdTrade3Cde;

    @DocumentField("ctryProdTradeCde[3]")
    private String ctryProdTrade4Cde;

    @DocumentField("ctryProdTradeCde[4]")
    private String ctryProdTrade5Cde;

    private String introProdCurrPrdInd;

    private String ccyInvstCde;

    private String qtyTypeCde;

    private String prodLocCde;

    private String suptRcblCashProdInd;

    private String suptRcblScripProdInd;

    private Long dcmlPlaceTradeUnitNum;

    private String pldgLimitAssocAcctInd;

    private String aumChrgProdInd;

    private String invstImigProdInd;

    private String restrInvstrProdInd;

    private BigDecimal invstInitMinAmt;

    private String noScribFeeProdInd;

    private String topSellProdInd;

    private String topPerfmProdInd;

    private String mktInvstCde;

    private String esgInd;

    private String esgTheme;

    private List<ProductAssetVolatilityClassSegment> prodAsetVoltlClassSeg;

    private List<AssetStandardIndustryClassificationAllocationSegment> asetSicAllocSeg;

    private List<AssetGeographicLocationAllocationSegment> asetGeoLocAllocSeg;

    private List<ProductUserDefinitionSegment> prodUserDefSeg;

    private List<ProductUserDefinitionExtSegment> prodUserDefExtSeg;

    private List<ProductUserDefinitionOPExtSegment> prodUserDefOPExtSeg;

    private List<ProductUserDefinitionEGExtSegment> prodUserDefEGExtSeg;

    private List<ProductAssetUndlSegment> prodAsetUndlSeg;

    private List<ProductTradeCcySegment> prodTradeCcySeg;

    private String prodShrtPllName;

    private String prodShrtSllName;

    private String prodDesc;

    private String prodPllDesc;

    private String prodSllDesc;

    private String bchmkName;

    private String bchmkPllName;

    private String bchmkSllName;

    private String asetClassCde;

    private Long termRemainDayCnt;

    private String sectInvstCde;

    private String availMktInfoInd;

    private Long prdRtrnAvgNum;

    private String dmyProdSubtpRecInd;

    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    private LocalTime busStartTm;

    @XmlJavaTypeAdapter(value = LocalTimeAdapter.class)
    private LocalTime busEndTm;

    private Long prodTopSellRankNum;

    private Long prodTopPerfmRankNum;

    private Long prodTopYieldRankNum;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate prodIssueCrosReferDt;

    private String prodTaxFreeWrapActStaCde;

    @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
    private LocalDate trdFirstDt;

    private BigDecimal loanProdOdMrgnPct;

    private String prodOwnCde;

    private String forgnProdInd;

    private String asetCatExtnlCde;

    private String chrgCatCde;

    private String addInfo;

    private String addPLLInfo;

    private String addSLLInfo;

    private String prodInvstObjText;

    private String prodInvstObjPllText;

    private String prodInvstObjSllText;

    private BigDecimal invstInitMaxAmt;

    private String rcmndProdDecsnCde;

    private String asetText;

    private String prodDervtCde;

    private String eqtyUndlInd;

    private String wlthAccumGoalInd;

    private String prtyWlthAccumGoalCde;

    private String planForRtireGoalInd;

    private String prtyPlnForRtireCde;

    private String educGoalInd;

    private String prtyEducCde;

    private String liveInRtireGoalInd;

    private String prtyLiveInRtireCde;

    private String protcGoalInd;

    private String prtyProtcCde;

    private String mngSolnInd;

    private Long prdInvstTnorNum;

    private String geoRiskInd;

    private String prodLqdyInd;

    private String rvrseEnqProdInd;

    private String prodInvstTypeCde;

    private String islmProdInd;

    private String cntlAdvcInd;

    private String asetVoltlClassMajrPrntCde;

    private ResChanSegment resChanSeg;

    private List<ChanlAttrSegment> chanlAttrSeg;

    private List<ProductListSegment> prodListSeg;

    private String ccyProdMktPrcCde;

    public void convertTimeValue(ZoneOffset zoneOffset) {
        busStartTm = DateUtils.toSystemLocalTime(busStartTm, zoneOffset);
        busEndTm = DateUtils.toSystemLocalTime(busEndTm, zoneOffset);
    }
}
