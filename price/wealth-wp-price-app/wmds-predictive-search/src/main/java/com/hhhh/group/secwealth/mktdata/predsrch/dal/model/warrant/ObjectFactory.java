/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.warrant;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdAsetUndlSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdInfoSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdKeySeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.RecDtTmSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ResChanSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ResChannelDtl;

/**
 * This object contains factory methods for each Java content interface and
 * Java element interface generated in the com.hhhh.compass.domain.sec package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of
 * the Java representation for XML content. The Java representation of XML
 * content can consist of schema derived interfaces and classes representing
 * the binding of schema type definitions, element declarations and model
 * groups. Factory methods for each of these are provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _IncmCharProdInd_QNAME = new QName("", "incmCharProdInd");
    private final static QName _DmyProdSubtpRecInd_QNAME = new QName("", "dmyProdSubtpRecInd");
    private final static QName _PsblProdBorwInd_QNAME = new QName("", "psblProdBorwInd");
    private final static QName _MktProdTrdCde_QNAME = new QName("", "mktProdTrdCde");
    private final static QName _ProdTopYieldRankNum_QNAME = new QName("", "prodTopYieldRankNum");
    private final static QName _CtryRecCde_QNAME = new QName("", "ctryRecCde");
    private final static QName _AsetClassCde_QNAME = new QName("", "asetClassCde");
    private final static QName _AllowProdLendInd_QNAME = new QName("", "allowProdLendInd");
    private final static QName _ProdTopPerfmRankNum_QNAME = new QName("", "prodTopPerfmRankNum");
    private final static QName _BusEndTm_QNAME = new QName("", "busEndTm");
    private final static QName _AllowSwInProdInd_QNAME = new QName("", "allowSwInProdInd");
    private final static QName _AllowSellMipAmtProdInd_QNAME = new QName("", "allowSellMipAmtProdInd");
    private final static QName _TimeZone_QNAME = new QName("", "timeZone");
    private final static QName _ProdPrcUpdtDtTm_QNAME = new QName("", "prodPrcUpdtDtTm");
    private final static QName _AsetUndlCdeSeqNum_QNAME = new QName("", "asetUndlCdeSeqNum");
    private final static QName _MrgnPrcAuctnTrdPct_QNAME = new QName("", "mrgnPrcAuctnTrdPct");
    private final static QName _ProdShrtSllName_QNAME = new QName("", "prodShrtSllName");
    private final static QName _ProdTypeCde_QNAME = new QName("", "prodTypeCde");
    private final static QName _ScripOnlyProdInd_QNAME = new QName("", "scripOnlyProdInd");
    private final static QName _PrdRtrnAvgNum_QNAME = new QName("", "prdRtrnAvgNum");
    private final static QName _ProdListTypeCde_QNAME = new QName("", "prodListTypeCde");
    private final static QName _SprdStopLossMaxCnt_QNAME = new QName("", "sprdStopLossMaxCnt");
    private final static QName _RtrnVoltlAvgPct_QNAME = new QName("", "rtrnVoltlAvgPct");
    private final static QName _ProdSubtpCde_QNAME = new QName("", "prodSubtpCde");
    private final static QName _TermRemainDayCnt_QNAME = new QName("", "termRemainDayCnt");
    private final static QName _DcmlPlaceTradeUnitNum_QNAME = new QName("", "dcmlPlaceTradeUnitNum");
    private final static QName _AllowSwOutProdInd_QNAME = new QName("", "allowSwOutProdInd");
    private final static QName _SuptAtmcTrdInd_QNAME = new QName("", "suptAtmcTrdInd");
    private final static QName _MrkToMktInd_QNAME = new QName("", "mrkToMktInd");
    private final static QName _PrdProdCde_QNAME = new QName("", "prdProdCde");
    private final static QName _PoolProdInd_QNAME = new QName("", "poolProdInd");
    private final static QName _PrtyProdSrchRsultNum_QNAME = new QName("", "prtyProdSrchRsultNum");
    private final static QName _AllowSellProdInd_QNAME = new QName("", "allowSellProdInd");
    private final static QName _ProdCde_QNAME = new QName("", "prodCde");
    private final static QName _ProdListCde_QNAME = new QName("", "prodListCde");
    private final static QName _CcyProdTradeCde_QNAME = new QName("", "ccyProdTradeCde");
    private final static QName _AllowSellMipUtProdInd_QNAME = new QName("", "allowSellMipUtProdInd");
    private final static QName _GrwthCharProdInd_QNAME = new QName("", "grwthCharProdInd");
    private final static QName _AllowSwInAmtProdInd_QNAME = new QName("", "allowSwInAmtProdInd");
    private final static QName _ProdAltNum_QNAME = new QName("", "prodAltNum");
    private final static QName _SuptRcblCashProdInd_QNAME = new QName("", "suptRcblCashProdInd");
    private final static QName _SuptAuctnTrdInd_QNAME = new QName("", "suptAuctnTrdInd");
    private final static QName _SuptNetSetlInd_QNAME = new QName("", "suptNetSetlInd");
    private final static QName _MktInvstCde_QNAME = new QName("", "mktInvstCde");
    private final static QName _CtryProdTrade1Cde_QNAME = new QName("", "ctryProdTrade1Cde");
    private final static QName _SuptStopLossOrdInd_QNAME = new QName("", "suptStopLossOrdInd");
    private final static QName _ProdMturDt_QNAME = new QName("", "prodMturDt");
    private final static QName _AllowBuyUtProdInd_QNAME = new QName("", "allowBuyUtProdInd");
    private final static QName _AllowSellUtProdInd_QNAME = new QName("", "allowSellUtProdInd");
    private final static QName _SuptAllIn1OrdInd_QNAME = new QName("", "suptAllIn1OrdInd");
    private final static QName _SuptMipInd_QNAME = new QName("", "suptMipInd");
    private final static QName _AllowBuyAmtProdInd_QNAME = new QName("", "allowBuyAmtProdInd");
    private final static QName _PrcVarCde_QNAME = new QName("", "prcVarCde");
    private final static QName _AllowSwOutAmtProdInd_QNAME = new QName("", "allowSwOutAmtProdInd");
    private final static QName _ProdName_QNAME = new QName("", "prodName");
    private final static QName _ProdBodLotQtyCnt_QNAME = new QName("", "prodBodLotQtyCnt");
    private final static QName _ProdDesc_QNAME = new QName("", "prodDesc");
    private final static QName _ExclLimitCalcInd_QNAME = new QName("", "exclLimitCalcInd");
    private final static QName _SprdStopLossMinCnt_QNAME = new QName("", "sprdStopLossMinCnt");
    private final static QName _BodLotProdInd_QNAME = new QName("", "bodLotProdInd");
    private final static QName _ProdLnchDt_QNAME = new QName("", "prodLnchDt");
    private final static QName _PldgLimitAssocAcctInd_QNAME = new QName("", "pldgLimitAssocAcctInd");
    private final static QName _MktSegExchgCde_QNAME = new QName("", "mktSegExchgCde");
    private final static QName _GrpMembrRecCde_QNAME = new QName("", "grpMembrRecCde");
    private final static QName _CtryProdRegisCde_QNAME = new QName("", "ctryProdRegisCde");
    private final static QName _ProdStatUpdtDtTm_QNAME = new QName("", "prodStatUpdtDtTm");
    private final static QName _RecCreatDtTm_QNAME = new QName("", "recCreatDtTm");
    private final static QName _ProdTopSellRankNum_QNAME = new QName("", "prodTopSellRankNum");
    private final static QName _PrdProdNum_QNAME = new QName("", "prdProdNum");
    private final static QName _AllowSellMipProdInd_QNAME = new QName("", "allowSellMipProdInd");
    private final static QName _ProdMktStatChngLaDt_QNAME = new QName("", "prodMktStatChngLaDt");
    private final static QName _AllowSellAmtProdInd_QNAME = new QName("", "allowSellAmtProdInd");
    private final static QName _SuptWinWinOrdInd_QNAME = new QName("", "suptWinWinOrdInd");
    private final static QName _MrgnTrdInd_QNAME = new QName("", "mrgnTrdInd");
    private final static QName _DivrNum_QNAME = new QName("", "divrNum");
    private final static QName _ProdCdeAltClassCde_QNAME = new QName("", "prodCdeAltClassCde");
    private final static QName _AllowBuyProdInd_QNAME = new QName("", "allowBuyProdInd");
    private final static QName _YieldEnhnProdInd_QNAME = new QName("", "yieldEnhnProdInd");
    private final static QName _AumChrgProdInd_QNAME = new QName("", "aumChrgProdInd");
    private final static QName _ProdStatCde_QNAME = new QName("", "prodStatCde");
    private final static QName _TrdFirstDt_QNAME = new QName("", "trdFirstDt");
    private final static QName _BusStartTm_QNAME = new QName("", "busStartTm");
    private final static QName _SuptProdSpltInd_QNAME = new QName("", "suptProdSpltInd");
    private final static QName _SuptRcblScripProdInd_QNAME = new QName("", "suptRcblScripProdInd");
    private final static QName _ResChannelCde_QNAME = new QName("", "resChannelCde");
    private final static QName _AsetUndlCde_QNAME = new QName("", "asetUndlCde");
    private final static QName _CptlGurntProdInd_QNAME = new QName("", "cptlGurntProdInd");
    private final static QName _RecUpdtDtTm_QNAME = new QName("", "recUpdtDtTm");
    private final static QName _DispComProdSrchInd_QNAME = new QName("", "dispComProdSrchInd");
    private final static QName _AllowSwOutUtProdInd_QNAME = new QName("", "allowSwOutUtProdInd");
    private final static QName _TrdLimitInd_QNAME = new QName("", "trdLimitInd");
    private final static QName _CcyInvstCde_QNAME = new QName("", "ccyInvstCde");
    private final static QName _CcyProdCde_QNAME = new QName("", "ccyProdCde");
    private final static QName _QtyTypeCde_QNAME = new QName("", "qtyTypeCde");
    private final static QName _AllowSwInUtProdInd_QNAME = new QName("", "allowSwInUtProdInd");
    private final static QName _InvstInitMinAmt_QNAME = new QName("", "invstInitMinAmt");
    private final static QName _ProdShrtName_QNAME = new QName("", "prodShrtName");
    private final static QName _ProdLocCde_QNAME = new QName("", "prodLocCde");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.hhhh.compass.domain.sec
     *
     */
    public ObjectFactory() {}

    /**
     * Create an instance of {@link ProdAsetUndlSeg }
     *
     */
    public ProdAsetUndlSeg createProdAsetUndlSeg() {
        return new ProdAsetUndlSeg();
    }

    /**
     * Create an instance of {@link ResChanSeg }
     *
     */
    public ResChanSeg createResChanSeg() {
        return new ResChanSeg();
    }

    /**
     * Create an instance of {@link ProdListSeg }
     *
     */
    public ProdListSeg createProdListSeg() {
        return new ProdListSeg();
    }

    /**
     * Create an instance of {@link ResChannelDtl }
     *
     */
    public ResChannelDtl createResChannelDtl() {
        return new ResChannelDtl();
    }

    /**
     * Create an instance of {@link StockInstm }
     *
     */
    public StockInstm createStockInstm() {
        return new StockInstm();
    }

    /**
     * Create an instance of {@link StockInstmLst }
     *
     */
    public StockInstmLst createStockInstmLst() {
        return new StockInstmLst();
    }

    /**
     * Create an instance of {@link ProdTradeCcySeg }
     *
     */
    public ProdTradeCcySeg createProdTradeCcySeg() {
        return new ProdTradeCcySeg();
    }

    /**
     * Create an instance of {@link ProdInfoSeg }
     *
     */
    public ProdInfoSeg createProdInfoSeg() {
        return new ProdInfoSeg();
    }

    /**
     * Create an instance of {@link ProdAltNumSeg }
     *
     */
    public ProdAltNumSeg createProdAltNumSeg() {
        return new ProdAltNumSeg();
    }

    /**
     * Create an instance of {@link RecDtTmSeg }
     *
     */
    public RecDtTmSeg createRecDtTmSeg() {
        return new RecDtTmSeg();
    }

    /**
     * Create an instance of {@link StockInstmSeg }
     *
     */
    public StockInstmSeg createStockInstmSeg() {
        return new StockInstmSeg();
    }

    /**
     * Create an instance of {@link MethCalcScripChrgCde }
     *
     */
    public MethCalcScripChrgCde createMethCalcScripChrgCde() {
        return new MethCalcScripChrgCde();
    }

    /**
     * Create an instance of {@link ProdKeySeg }
     *
     */
    public ProdKeySeg createProdKeySeg() {
        return new ProdKeySeg();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "incmCharProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIncmCharProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._IncmCharProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "dmyProdSubtpRecInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDmyProdSubtpRecInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._DmyProdSubtpRecInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "psblProdBorwInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPsblProdBorwInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._PsblProdBorwInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mktProdTrdCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMktProdTrdCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._MktProdTrdCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodTopYieldRankNum")
    public JAXBElement<BigInteger> createProdTopYieldRankNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._ProdTopYieldRankNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ctryRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "asetClassCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAsetClassCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._AsetClassCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowProdLendInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowProdLendInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowProdLendInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodTopPerfmRankNum")
    public JAXBElement<BigInteger> createProdTopPerfmRankNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._ProdTopPerfmRankNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "busEndTm")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createBusEndTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._BusEndTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwInProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "timeZone")
    public JAXBElement<String> createTimeZone(final String value) {
        return new JAXBElement<String>(ObjectFactory._TimeZone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodPrcUpdtDtTm")
    public JAXBElement<String> createProdPrcUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdPrcUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "asetUndlCdeSeqNum")
    public JAXBElement<BigInteger> createAsetUndlCdeSeqNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._AsetUndlCdeSeqNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mrgnPrcAuctnTrdPct")
    public JAXBElement<BigInteger> createMrgnPrcAuctnTrdPct(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._MrgnPrcAuctnTrdPct_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodShrtSllName")
    public JAXBElement<String> createProdShrtSllName(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdShrtSllName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "scripOnlyProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createScripOnlyProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._ScripOnlyProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prdRtrnAvgNum")
    public JAXBElement<BigDecimal> createPrdRtrnAvgNum(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._PrdRtrnAvgNum_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodListTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdListTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdListTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "sprdStopLossMaxCnt")
    public JAXBElement<BigInteger> createSprdStopLossMaxCnt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._SprdStopLossMaxCnt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "rtrnVoltlAvgPct")
    public JAXBElement<BigDecimal> createRtrnVoltlAvgPct(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._RtrnVoltlAvgPct_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodSubtpCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdSubtpCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdSubtpCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "termRemainDayCnt")
    public JAXBElement<BigInteger> createTermRemainDayCnt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._TermRemainDayCnt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "dcmlPlaceTradeUnitNum")
    public JAXBElement<BigInteger> createDcmlPlaceTradeUnitNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._DcmlPlaceTradeUnitNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptAtmcTrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptAtmcTrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptAtmcTrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mrkToMktInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMrkToMktInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._MrkToMktInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prdProdCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPrdProdCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._PrdProdCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "poolProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPoolProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._PoolProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prtyProdSrchRsultNum")
    public JAXBElement<BigInteger> createPrtyProdSrchRsultNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._PrtyProdSrchRsultNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodCde")
    public JAXBElement<BigInteger> createProdCde(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._ProdCde_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodListCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdListCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdListCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ccyProdTradeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCcyProdTradeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CcyProdTradeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "grwthCharProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGrwthCharProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._GrwthCharProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwInAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodAltNum")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdAltNum(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdAltNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptRcblCashProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptRcblCashProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptRcblCashProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptAuctnTrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptAuctnTrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptAuctnTrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptNetSetlInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptNetSetlInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptNetSetlInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mktInvstCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMktInvstCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._MktInvstCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ctryProdTrade1Cde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryProdTrade1Cde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryProdTrade1Cde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptStopLossOrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptStopLossOrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptStopLossOrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodMturDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdMturDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdMturDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowBuyUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptAllIn1OrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptAllIn1OrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptAllIn1OrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptMipInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptMipInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptMipInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowBuyAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prcVarCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPrcVarCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._PrcVarCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodName")
    public JAXBElement<String> createProdName(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodBodLotQtyCnt")
    public JAXBElement<BigInteger> createProdBodLotQtyCnt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._ProdBodLotQtyCnt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodDesc")
    public JAXBElement<String> createProdDesc(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdDesc_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "exclLimitCalcInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createExclLimitCalcInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._ExclLimitCalcInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "sprdStopLossMinCnt")
    public JAXBElement<BigInteger> createSprdStopLossMinCnt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._SprdStopLossMinCnt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "bodLotProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createBodLotProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._BodLotProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodLnchDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdLnchDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdLnchDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "pldgLimitAssocAcctInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPldgLimitAssocAcctInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._PldgLimitAssocAcctInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mktSegExchgCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMktSegExchgCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._MktSegExchgCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "grpMembrRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGrpMembrRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._GrpMembrRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ctryProdRegisCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryProdRegisCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryProdRegisCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodStatUpdtDtTm")
    public JAXBElement<String> createProdStatUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdStatUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "recCreatDtTm")
    public JAXBElement<String> createRecCreatDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecCreatDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodTopSellRankNum")
    public JAXBElement<BigInteger> createProdTopSellRankNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._ProdTopSellRankNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prdProdNum")
    public JAXBElement<BigInteger> createPrdProdNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._PrdProdNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodMktStatChngLaDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdMktStatChngLaDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdMktStatChngLaDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSellAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptWinWinOrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptWinWinOrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptWinWinOrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "mrgnTrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMrgnTrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._MrgnTrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "divrNum")
    public JAXBElement<BigInteger> createDivrNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._DivrNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodCdeAltClassCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdCdeAltClassCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdCdeAltClassCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowBuyProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "yieldEnhnProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createYieldEnhnProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._YieldEnhnProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "aumChrgProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAumChrgProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AumChrgProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodStatCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdStatCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdStatCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "trdFirstDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTrdFirstDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._TrdFirstDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "busStartTm")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createBusStartTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._BusStartTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptProdSpltInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptProdSpltInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptProdSpltInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "suptRcblScripProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptRcblScripProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptRcblScripProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "resChannelCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createResChannelCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ResChannelCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "asetUndlCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAsetUndlCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._AsetUndlCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "cptlGurntProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCptlGurntProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._CptlGurntProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "recUpdtDtTm")
    public JAXBElement<String> createRecUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "dispComProdSrchInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDispComProdSrchInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._DispComProdSrchInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "trdLimitInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTrdLimitInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._TrdLimitInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ccyInvstCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCcyInvstCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CcyInvstCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "ccyProdCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCcyProdCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CcyProdCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "qtyTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createQtyTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._QtyTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "allowSwInUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     *
     */
    @XmlElementDecl(namespace = "", name = "invstInitMinAmt")
    public JAXBElement<BigInteger> createInvstInitMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._InvstInitMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodShrtName")
    public JAXBElement<String> createProdShrtName(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdShrtName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code
     * >}
     *
     */
    @XmlElementDecl(namespace = "", name = "prodLocCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdLocCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdLocCde_QNAME, String.class, null, value);
    }

}
