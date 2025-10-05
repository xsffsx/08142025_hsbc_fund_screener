/*
 */

package com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut;

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
 * Java element interface generated in the com.hhhh.compass.domain.ut package.
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
    private final static QName _ProdTopYieldRankNum_QNAME = new QName("", "prodTopYieldRankNum");
    private final static QName _CtryRecCde_QNAME = new QName("", "ctryRecCde");
    private final static QName _AsetClassCde_QNAME = new QName("", "asetClassCde");
    private final static QName _ProdTopPerfmRankNum_QNAME = new QName("", "prodTopPerfmRankNum");
    private final static QName _SectInvstCde_QNAME = new QName("", "sectInvstCde");
    private final static QName _AllowSwInProdInd_QNAME = new QName("", "allowSwInProdInd");
    private final static QName _AllowSellMipAmtProdInd_QNAME = new QName("", "allowSellMipAmtProdInd");
    private final static QName _TimeZone_QNAME = new QName("", "timeZone");
    private final static QName _ProdPrcUpdtDtTm_QNAME = new QName("", "prodPrcUpdtDtTm");
    private final static QName _AsetUndlCdeSeqNum_QNAME = new QName("", "asetUndlCdeSeqNum");
    private final static QName _AvailMktInfoInd_QNAME = new QName("", "availMktInfoInd");
    private final static QName _FundMipRtainMinAmt_QNAME = new QName("", "fundMipRtainMinAmt");
    private final static QName _ProdTypeCde_QNAME = new QName("", "prodTypeCde");
    private final static QName _FundSwOutRtainMinAmt_QNAME = new QName("", "fundSwOutRtainMinAmt");
    private final static QName _ProdSubtpCde_QNAME = new QName("", "prodSubtpCde");
    private final static QName _ScribCtoffNextDtTm_QNAME = new QName("", "scribCtoffNextDtTm");
    private final static QName _HldayFundNextDt_QNAME = new QName("", "hldayFundNextDt");
    private final static QName _UtMipRtainMinNum_QNAME = new QName("", "utMipRtainMinNum");
    private final static QName _TermRemainDayCnt_QNAME = new QName("", "termRemainDayCnt");
    private final static QName _AllowSwOutProdInd_QNAME = new QName("", "allowSwOutProdInd");
    private final static QName _DcmlPlaceTradeUnitNum_QNAME = new QName("", "dcmlPlaceTradeUnitNum");
    private final static QName _MrkToMktInd_QNAME = new QName("", "mrkToMktInd");
    private final static QName _PrtyProdSrchRsultNum_QNAME = new QName("", "prtyProdSrchRsultNum");
    private final static QName _AllowSellProdInd_QNAME = new QName("", "allowSellProdInd");
    private final static QName _ProdCde_QNAME = new QName("", "prodCde");
    private final static QName _RdmMipMinAmt_QNAME = new QName("", "rdmMipMinAmt");
    private final static QName _AllowSellMipUtProdInd_QNAME = new QName("", "allowSellMipUtProdInd");
    private final static QName _BchmkName_QNAME = new QName("", "bchmkName");
    private final static QName _UtRdmMinNum_QNAME = new QName("", "utRdmMinNum");
    private final static QName _RiskLvlCde_QNAME = new QName("", "riskLvlCde");
    private final static QName _GrwthCharProdInd_QNAME = new QName("", "grwthCharProdInd");
    private final static QName _AllowSwInAmtProdInd_QNAME = new QName("", "allowSwInAmtProdInd");
    private final static QName _ProdAltNum_QNAME = new QName("", "prodAltNum");
    private final static QName _SuptRcblCashProdInd_QNAME = new QName("", "suptRcblCashProdInd");
    private final static QName _MktInvstCde_QNAME = new QName("", "mktInvstCde");
    private final static QName _CtryProdTrade1Cde_QNAME = new QName("", "ctryProdTrade1Cde");
    private final static QName _ChrgCatCde_QNAME = new QName("", "chrgCatCde");
    private final static QName _PayCashDivInd_QNAME = new QName("", "payCashDivInd");
    private final static QName _AmcmInd_QNAME = new QName("", "amcmInd");
    private final static QName _ProdMturDt_QNAME = new QName("", "prodMturDt");
    private final static QName _AllowBuyUtProdInd_QNAME = new QName("", "allowBuyUtProdInd");
    private final static QName _AllowSellUtProdInd_QNAME = new QName("", "allowSellUtProdInd");
    private final static QName _DealNextDt_QNAME = new QName("", "dealNextDt");
    private final static QName _RdmMinAmt_QNAME = new QName("", "rdmMinAmt");
    private final static QName _SuptMipInd_QNAME = new QName("", "suptMipInd");
    private final static QName _FundClassCde_QNAME = new QName("", "fundClassCde");
    private final static QName _SpclFundInd_QNAME = new QName("", "spclFundInd");
    private final static QName _UtMipRdmMinNum_QNAME = new QName("", "utMipRdmMinNum");
    private final static QName _AllowBuyAmtProdInd_QNAME = new QName("", "allowBuyAmtProdInd");
    private final static QName _RdmCtoffNextDtTm_QNAME = new QName("", "rdmCtoffNextDtTm");
    private final static QName _InsuLinkUtTrstInd_QNAME = new QName("", "insuLinkUtTrstInd");
    private final static QName _AllowSwOutAmtProdInd_QNAME = new QName("", "allowSwOutAmtProdInd");
    private final static QName _ProdName_QNAME = new QName("", "prodName");
    private final static QName _SchemChrgCde_QNAME = new QName("", "schemChrgCde");
    private final static QName _ProdLnchDt_QNAME = new QName("", "prodLnchDt");
    private final static QName _PldgLimitAssocAcctInd_QNAME = new QName("", "pldgLimitAssocAcctInd");
    private final static QName _GrpMembrRecCde_QNAME = new QName("", "grpMembrRecCde");
    private final static QName _CloseEndFundInd_QNAME = new QName("", "closeEndFundInd");
    private final static QName _InvstMipMinAmt_QNAME = new QName("", "invstMipMinAmt");
    private final static QName _UtRtainMinNum_QNAME = new QName("", "utRtainMinNum");
    private final static QName _InvstIncrmMinAmt_QNAME = new QName("", "invstIncrmMinAmt");
    private final static QName _ProdStatUpdtDtTm_QNAME = new QName("", "prodStatUpdtDtTm");
    private final static QName _RecCreatDtTm_QNAME = new QName("", "recCreatDtTm");
    private final static QName _FundUnswCde_QNAME = new QName("", "fundUnswCde");
    private final static QName _RecOnlnUpdtDtTm_QNAME = new QName("", "recOnlnUpdtDtTm");
    private final static QName _OfferEndDtTm_QNAME = new QName("", "offerEndDtTm");
    private final static QName _FundSwOutMaxAmt_QNAME = new QName("", "fundSwOutMaxAmt");
    private final static QName _ProdTopSellRankNum_QNAME = new QName("", "prodTopSellRankNum");
    private final static QName _PrdProdNum_QNAME = new QName("", "prdProdNum");
    private final static QName _AllowSellMipProdInd_QNAME = new QName("", "allowSellMipProdInd");
    private final static QName _RestrOnlScribInd_QNAME = new QName("", "restrOnlScribInd");
    private final static QName _AllowTradeProdInd_QNAME = new QName("", "allowTradeProdInd");
    private final static QName _ProdTaxFreeWrapActStaCde_QNAME = new QName("", "prodTaxFreeWrapActStaCde");
    private final static QName _OfferStartDtTm_QNAME = new QName("", "offerStartDtTm");
    private final static QName _AllowSellAmtProdInd_QNAME = new QName("", "allowSellAmtProdInd");
    private final static QName _FundSwInMinAmt_QNAME = new QName("", "fundSwInMinAmt");
    private final static QName _DivrNum_QNAME = new QName("", "divrNum");
    private final static QName _ProdCdeAltClassCde_QNAME = new QName("", "prodCdeAltClassCde");
    private final static QName _FundHouseCde_QNAME = new QName("", "fundHouseCde");
    private final static QName _AllowBuyProdInd_QNAME = new QName("", "allowBuyProdInd");
    private final static QName _YieldEnhnProdInd_QNAME = new QName("", "yieldEnhnProdInd");
    private final static QName _UtSwOutRtainMinNum_QNAME = new QName("", "utSwOutRtainMinNum");
    private final static QName _InvstMipIncrmMinAmt_QNAME = new QName("", "invstMipIncrmMinAmt");
    private final static QName _TranMaxAmt_QNAME = new QName("", "tranMaxAmt");
    private final static QName _AumChrgProdInd_QNAME = new QName("", "aumChrgProdInd");
    private final static QName _ProdStatCde_QNAME = new QName("", "prodStatCde");
    private final static QName _ChrgInitSalesPct_QNAME = new QName("", "chrgInitSalesPct");
    private final static QName _SuptRcblScripProdInd_QNAME = new QName("", "suptRcblScripProdInd");
    private final static QName _ResChannelCde_QNAME = new QName("", "resChannelCde");
    private final static QName _AsetUndlCde_QNAME = new QName("", "asetUndlCde");
    private final static QName _CptlGurntProdInd_QNAME = new QName("", "cptlGurntProdInd");
    private final static QName _RecUpdtDtTm_QNAME = new QName("", "recUpdtDtTm");
    private final static QName _DispComProdSrchInd_QNAME = new QName("", "dispComProdSrchInd");
    private final static QName _AllowSwOutUtProdInd_QNAME = new QName("", "allowSwOutUtProdInd");
    private final static QName _CcyInvstCde_QNAME = new QName("", "ccyInvstCde");
    private final static QName _IntroProdCurrPrdInd_QNAME = new QName("", "introProdCurrPrdInd");
    private final static QName _CcyProdCde_QNAME = new QName("", "ccyProdCde");
    private final static QName _QtyTypeCde_QNAME = new QName("", "qtyTypeCde");
    private final static QName _FundRtainMinAmt_QNAME = new QName("", "fundRtainMinAmt");
    private final static QName _AllowSwInUtProdInd_QNAME = new QName("", "allowSwInUtProdInd");
    private final static QName _DscntMaxPct_QNAME = new QName("", "dscntMaxPct");
    private final static QName _InvstInitMinAmt_QNAME = new QName("", "invstInitMinAmt");
    private final static QName _ProdShrtName_QNAME = new QName("", "prodShrtName");
    private final static QName _ProdLocCde_QNAME = new QName("", "prodLocCde");
    private final static QName _FundSwOutMinAmt_QNAME = new QName("", "fundSwOutMinAmt");
    private final static QName _PiFundInd_QNAME = new QName("", "piFundInd");
    private final static QName _DeAuthFundInd_QNAME = new QName("", "deAuthFundInd");

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package: com.hhhh.compass.domain.ut
     * 
     */
    public ObjectFactory() {}

    /**
     * Create an instance of {@link ResChanSeg }
     * 
     */
    public ResChanSeg createResChanSeg() {
        return new ResChanSeg();
    }

    /**
     * Create an instance of {@link ProdAsetUndlSeg }
     * 
     */
    public ProdAsetUndlSeg createProdAsetUndlSeg() {
        return new ProdAsetUndlSeg();
    }

    /**
     * Create an instance of {@link UtTrstInstmLst }
     * 
     */
    public UtTrstInstmLst createUtTrstInstmLst() {
        return new UtTrstInstmLst();
    }

    /**
     * Create an instance of {@link RecDtTmSeg }
     * 
     */
    public RecDtTmSeg createRecDtTmSeg() {
        return new RecDtTmSeg();
    }

    /**
     * Create an instance of {@link FundUnswithSeg }
     * 
     */
    public FundUnswithSeg createFundUnswithSeg() {
        return new FundUnswithSeg();
    }

    /**
     * Create an instance of {@link ProdKeySeg }
     * 
     */
    public ProdKeySeg createProdKeySeg() {
        return new ProdKeySeg();
    }

    /**
     * Create an instance of {@link UtTrstInstmSeg }
     * 
     */
    public UtTrstInstmSeg createUtTrstInstmSeg() {
        return new UtTrstInstmSeg();
    }

    /**
     * Create an instance of {@link ProdAltNumSeg }
     * 
     */
    public ProdAltNumSeg createProdAltNumSeg() {
        return new ProdAltNumSeg();
    }

    /**
     * Create an instance of {@link ResChannelDtl }
     * 
     */
    public ResChannelDtl createResChannelDtl() {
        return new ResChannelDtl();
    }

    /**
     * Create an instance of {@link ProdInfoSeg }
     * 
     */
    public ProdInfoSeg createProdInfoSeg() {
        return new ProdInfoSeg();
    }

    /**
     * Create an instance of {@link UtTrstInstm }
     * 
     */
    public UtTrstInstm createUtTrstInstm() {
        return new UtTrstInstm();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "incmCharProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIncmCharProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._IncmCharProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dmyProdSubtpRecInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDmyProdSubtpRecInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._DmyProdSubtpRecInd_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ctryRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "asetClassCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAsetClassCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._AsetClassCde_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "sectInvstCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSectInvstCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._SectInvstCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwInProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "timeZone")
    public JAXBElement<String> createTimeZone(final String value) {
        return new JAXBElement<String>(ObjectFactory._TimeZone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "availMktInfoInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAvailMktInfoInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AvailMktInfoInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundMipRtainMinAmt")
    public JAXBElement<BigInteger> createFundMipRtainMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._FundMipRtainMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundSwOutRtainMinAmt")
    public JAXBElement<BigInteger> createFundSwOutRtainMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._FundSwOutRtainMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodSubtpCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdSubtpCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdSubtpCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "scribCtoffNextDtTm")
    public JAXBElement<String> createScribCtoffNextDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._ScribCtoffNextDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "hldayFundNextDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createHldayFundNextDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._HldayFundNextDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "utMipRtainMinNum")
    public JAXBElement<BigInteger> createUtMipRtainMinNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._UtMipRtainMinNum_QNAME, BigInteger.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutProdInd_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mrkToMktInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMrkToMktInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._MrkToMktInd_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
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
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rdmMipMinAmt")
    public JAXBElement<BigInteger> createRdmMipMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._RdmMipMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "bchmkName")
    public JAXBElement<String> createBchmkName(final String value) {
        return new JAXBElement<String>(ObjectFactory._BchmkName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "utRdmMinNum")
    public JAXBElement<BigInteger> createUtRdmMinNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._UtRdmMinNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "riskLvlCde")
    public JAXBElement<BigInteger> createRiskLvlCde(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._RiskLvlCde_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "grwthCharProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGrwthCharProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._GrwthCharProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwInAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodAltNum")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdAltNum(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdAltNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "suptRcblCashProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptRcblCashProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptRcblCashProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "mktInvstCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createMktInvstCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._MktInvstCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ctryProdTrade1Cde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCtryProdTrade1Cde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CtryProdTrade1Cde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "chrgCatCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createChrgCatCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ChrgCatCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "payCashDivInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPayCashDivInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._PayCashDivInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "amcmInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAmcmInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AmcmInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodMturDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdMturDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdMturDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowBuyUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSellUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dealNextDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDealNextDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._DealNextDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rdmMinAmt")
    public JAXBElement<BigInteger> createRdmMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._RdmMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "suptMipInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptMipInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptMipInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundClassCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFundClassCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._FundClassCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "spclFundInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSpclFundInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SpclFundInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "utMipRdmMinNum")
    public JAXBElement<BigInteger> createUtMipRdmMinNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._UtMipRdmMinNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowBuyAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "rdmCtoffNextDtTm")
    public JAXBElement<String> createRdmCtoffNextDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RdmCtoffNextDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "insuLinkUtTrstInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createInsuLinkUtTrstInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._InsuLinkUtTrstInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodName")
    public JAXBElement<String> createProdName(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "schemChrgCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSchemChrgCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._SchemChrgCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodLnchDt")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdLnchDt(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdLnchDt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pldgLimitAssocAcctInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPldgLimitAssocAcctInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._PldgLimitAssocAcctInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "grpMembrRecCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createGrpMembrRecCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._GrpMembrRecCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "closeEndFundInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCloseEndFundInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._CloseEndFundInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "invstMipMinAmt")
    public JAXBElement<BigInteger> createInvstMipMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._InvstMipMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "utRtainMinNum")
    public JAXBElement<BigInteger> createUtRtainMinNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._UtRtainMinNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "invstIncrmMinAmt")
    public JAXBElement<BigInteger> createInvstIncrmMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._InvstIncrmMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodStatUpdtDtTm")
    public JAXBElement<String> createProdStatUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdStatUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "recCreatDtTm")
    public JAXBElement<String> createRecCreatDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecCreatDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundUnswCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFundUnswCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._FundUnswCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "recOnlnUpdtDtTm")
    public JAXBElement<String> createRecOnlnUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecOnlnUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "offerEndDtTm")
    public JAXBElement<String> createOfferEndDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._OfferEndDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundSwOutMaxAmt")
    public JAXBElement<BigDecimal> createFundSwOutMaxAmt(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._FundSwOutMaxAmt_QNAME, BigDecimal.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSellMipProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellMipProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellMipProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "restrOnlScribInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRestrOnlScribInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._RestrOnlScribInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowTradeProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowTradeProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowTradeProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodTaxFreeWrapActStaCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdTaxFreeWrapActStaCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdTaxFreeWrapActStaCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "offerStartDtTm")
    public JAXBElement<String> createOfferStartDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._OfferStartDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSellAmtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSellAmtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSellAmtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundSwInMinAmt")
    public JAXBElement<BigInteger> createFundSwInMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._FundSwInMinAmt_QNAME, BigInteger.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodCdeAltClassCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdCdeAltClassCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdCdeAltClassCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundHouseCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createFundHouseCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._FundHouseCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowBuyProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowBuyProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowBuyProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "yieldEnhnProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createYieldEnhnProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._YieldEnhnProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "utSwOutRtainMinNum")
    public JAXBElement<BigInteger> createUtSwOutRtainMinNum(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._UtSwOutRtainMinNum_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "invstMipIncrmMinAmt")
    public JAXBElement<BigInteger> createInvstMipIncrmMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._InvstMipIncrmMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "tranMaxAmt")
    public JAXBElement<BigDecimal> createTranMaxAmt(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._TranMaxAmt_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "aumChrgProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAumChrgProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AumChrgProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodStatCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdStatCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdStatCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "chrgInitSalesPct")
    public JAXBElement<BigDecimal> createChrgInitSalesPct(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._ChrgInitSalesPct_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "suptRcblScripProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createSuptRcblScripProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._SuptRcblScripProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "resChannelCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createResChannelCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ResChannelCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "asetUndlCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAsetUndlCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._AsetUndlCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "cptlGurntProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCptlGurntProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._CptlGurntProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "recUpdtDtTm")
    public JAXBElement<String> createRecUpdtDtTm(final String value) {
        return new JAXBElement<String>(ObjectFactory._RecUpdtDtTm_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dispComProdSrchInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createDispComProdSrchInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._DispComProdSrchInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwOutUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwOutUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwOutUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ccyInvstCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCcyInvstCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CcyInvstCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "introProdCurrPrdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIntroProdCurrPrdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._IntroProdCurrPrdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ccyProdCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createCcyProdCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._CcyProdCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "qtyTypeCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createQtyTypeCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._QtyTypeCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundRtainMinAmt")
    public JAXBElement<BigInteger> createFundRtainMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._FundRtainMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "allowSwInUtProdInd")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAllowSwInUtProdInd(final String value) {
        return new JAXBElement<String>(ObjectFactory._AllowSwInUtProdInd_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dscntMaxPct")
    public JAXBElement<BigDecimal> createDscntMaxPct(final BigDecimal value) {
        return new JAXBElement<BigDecimal>(ObjectFactory._DscntMaxPct_QNAME, BigDecimal.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodShrtName")
    public JAXBElement<String> createProdShrtName(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdShrtName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "prodLocCde")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createProdLocCde(final String value) {
        return new JAXBElement<String>(ObjectFactory._ProdLocCde_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "fundSwOutMinAmt")
    public JAXBElement<BigInteger> createFundSwOutMinAmt(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._FundSwOutMinAmt_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PiFundInd")
    public JAXBElement<BigInteger> createPiFundInd(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._PiFundInd_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
     * {@code >}
     * 
     */
    @XmlElementDecl(namespace = "", name = "deAuthFundInd")
    public JAXBElement<BigInteger> createDeAuthFundInd(final BigInteger value) {
        return new JAXBElement<BigInteger>(ObjectFactory._DeAuthFundInd_QNAME, BigInteger.class, null, value);
    }

}
