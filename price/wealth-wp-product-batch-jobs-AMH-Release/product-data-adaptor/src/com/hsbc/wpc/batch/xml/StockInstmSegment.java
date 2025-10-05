/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/xml/StockInstmSegment.java 1.3 2011/12/06 19:08:11CST Navagate Developer 10 (WMDHKG0028) Development  $
 */

package com.dummy.wpc.batch.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class StockInstmSegment.
 * 
 * @version $Revision: 1.3 $ $Date: 2011/12/06 19:08:11CST $
 */
public class StockInstmSegment implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _earnPerShareAnnlAmt.
     */
    private java.lang.String _earnPerShareAnnlAmt;

    /**
     * Field _mrgnTrdInd.
     */
    private java.lang.String _mrgnTrdInd;

    /**
     * Field _mrgnSecODPct.
     */
    private java.lang.String _mrgnSecODPct;

    /**
     * Field _suptAuctnTrdInd.
     */
    private java.lang.String _suptAuctnTrdInd;

    /**
     * Field _stopLossMinPct.
     */
    private java.lang.String _stopLossMinPct;

    /**
     * Field _stopLossMaxPct.
     */
    private java.lang.String _stopLossMaxPct;

    /**
     * Field _sprdStopLossMinCnt.
     */
    private java.lang.String _sprdStopLossMinCnt;

    /**
     * Field _sprdStopLossMaxCnt.
     */
    private java.lang.String _sprdStopLossMaxCnt;

    /**
     * Field _prodBodLotQtyCnt.
     */
    private java.lang.String _prodBodLotQtyCnt;

    /**
     * Field _mktProdTrdCde.
     */
    private java.lang.String _mktProdTrdCde;

    /**
     * Field _suptMipInd.
     */
    private java.lang.String _suptMipInd;

    /**
     * Field _invstMipMinAmt.
     */
    private java.lang.String _invstMipMinAmt;

    /**
     * Field _invstMipIncrmMinAmt.
     */
    private java.lang.String _invstMipIncrmMinAmt;

    /**
     * Field _prodIssQtyTtlCnt.
     */
    private java.lang.String _prodIssQtyTtlCnt;

    /**
     * Field _bodLotProdInd.
     */
    private java.lang.String _bodLotProdInd;

    /**
     * Field _trdLimitInd.
     */
    private java.lang.String _trdLimitInd;

    /**
     * Field _prodMaxIndvOwnrPct.
     */
    private java.lang.String _prodMaxIndvOwnrPct;

    /**
     * Field _prodMaxIndvForgnInvstrPct.
     */
    private java.lang.String _prodMaxIndvForgnInvstrPct;

    /**
     * Field _prodMaxForgnInvstrTtlPct.
     */
    private java.lang.String _prodMaxForgnInvstrTtlPct;

    /**
     * Field _prodExerPrcAmt.
     */
    private java.lang.String _prodExerPrcAmt;

    /**
     * Field _psblProdBorwInd.
     */
    private java.lang.String _psblProdBorwInd;

    /**
     * Field _allowProdLendInd.
     */
    private java.lang.String _allowProdLendInd;

    /**
     * Field _poolProdInd.
     */
    private java.lang.String _poolProdInd;

    /**
     * Field _scripOnlyProdInd.
     */
    private java.lang.String _scripOnlyProdInd;

    /**
     * Field _suptAtmcTrdInd.
     */
    private java.lang.String _suptAtmcTrdInd;

    /**
     * Field _suptNetSetlInd.
     */
    private java.lang.String _suptNetSetlInd;

    /**
     * Field _suptStopLossOrdInd.
     */
    private java.lang.String _suptStopLossOrdInd;

    /**
     * Field _suptWinWinOrdInd.
     */
    private java.lang.String _suptWinWinOrdInd;

    /**
     * Field _suptAllIn1OrdInd.
     */
    private java.lang.String _suptAllIn1OrdInd;

    /**
     * Field _suptProdSpltInd.
     */
    private java.lang.String _suptProdSpltInd;

    /**
     * Field _mrgnPrcAuctnTrdPct.
     */
    private java.lang.String _mrgnPrcAuctnTrdPct;

    /**
     * Field _prodAuctnTrdExpirDt.
     */
    private java.lang.String _prodAuctnTrdExpirDt;

    /**
     * Field _loanProdMrgnTrdPct.
     */
    private java.lang.String _loanProdMrgnTrdPct;

    /**
     * Field _loanBdgtProdIPOAmt.
     */
    private java.lang.String _loanBdgtProdIPOAmt;

    /**
     * Field _loanProdIPOTtlAmt.
     */
    private java.lang.String _loanProdIPOTtlAmt;

    /**
     * Field _exclLimitCalcInd.
     */
    private java.lang.String _exclLimitCalcInd;

    /**
     * Field _prodMktStatChngLaDt.
     */
    private java.lang.String _prodMktStatChngLaDt;

    /**
     * Field _prcVarCde.
     */
    private java.lang.String _prcVarCde;

    /**
     * Field _methCalcScripChrgCde.
     */
    private java.lang.String _methCalcScripChrgCde;

    /**
     * Field _prodSellMaxQtyCnt.
     */
    private java.lang.String _prodSellMaxQtyCnt;

    /**
     * Field _prodBuyMaxQtyCnt.
     */
    private java.lang.String _prodBuyMaxQtyCnt;

    /**
     * Field _prcVarMinAmt.
     */
    private java.lang.String _prcVarMinAmt;

    /**
     * Field _prcVarMinEffDt.
     */
    private java.lang.String _prcVarMinEffDt;

    /**
     * Field _mktSegExchgCde.
     */
    private java.lang.String _mktSegExchgCde;

    /**
     * Field _ctryProdRegisCde.
     */
    private java.lang.String _ctryProdRegisCde;


      //----------------/
     //- Constructors -/
    //----------------/

    public StockInstmSegment() {
        super();
    }


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'allowProdLendInd'.
     * 
     * @return the value of field 'AllowProdLendInd'.
     */
    public java.lang.String getAllowProdLendInd(
    ) {
        return this._allowProdLendInd;
    }

    /**
     * Returns the value of field 'bodLotProdInd'.
     * 
     * @return the value of field 'BodLotProdInd'.
     */
    public java.lang.String getBodLotProdInd(
    ) {
        return this._bodLotProdInd;
    }

    /**
     * Returns the value of field 'ctryProdRegisCde'.
     * 
     * @return the value of field 'CtryProdRegisCde'.
     */
    public java.lang.String getCtryProdRegisCde(
    ) {
        return this._ctryProdRegisCde;
    }

    /**
     * Returns the value of field 'earnPerShareAnnlAmt'.
     * 
     * @return the value of field 'EarnPerShareAnnlAmt'.
     */
    public java.lang.String getEarnPerShareAnnlAmt(
    ) {
        return this._earnPerShareAnnlAmt;
    }

    /**
     * Returns the value of field 'exclLimitCalcInd'.
     * 
     * @return the value of field 'ExclLimitCalcInd'.
     */
    public java.lang.String getExclLimitCalcInd(
    ) {
        return this._exclLimitCalcInd;
    }

    /**
     * Returns the value of field 'invstMipIncrmMinAmt'.
     * 
     * @return the value of field 'InvstMipIncrmMinAmt'.
     */
    public java.lang.String getInvstMipIncrmMinAmt(
    ) {
        return this._invstMipIncrmMinAmt;
    }

    /**
     * Returns the value of field 'invstMipMinAmt'.
     * 
     * @return the value of field 'InvstMipMinAmt'.
     */
    public java.lang.String getInvstMipMinAmt(
    ) {
        return this._invstMipMinAmt;
    }

    /**
     * Returns the value of field 'loanBdgtProdIPOAmt'.
     * 
     * @return the value of field 'LoanBdgtProdIPOAmt'.
     */
    public java.lang.String getLoanBdgtProdIPOAmt(
    ) {
        return this._loanBdgtProdIPOAmt;
    }

    /**
     * Returns the value of field 'loanProdIPOTtlAmt'.
     * 
     * @return the value of field 'LoanProdIPOTtlAmt'.
     */
    public java.lang.String getLoanProdIPOTtlAmt(
    ) {
        return this._loanProdIPOTtlAmt;
    }

    /**
     * Returns the value of field 'loanProdMrgnTrdPct'.
     * 
     * @return the value of field 'LoanProdMrgnTrdPct'.
     */
    public java.lang.String getLoanProdMrgnTrdPct(
    ) {
        return this._loanProdMrgnTrdPct;
    }

    /**
     * Returns the value of field 'methCalcScripChrgCde'.
     * 
     * @return the value of field 'MethCalcScripChrgCde'.
     */
    public java.lang.String getMethCalcScripChrgCde(
    ) {
        return this._methCalcScripChrgCde;
    }

    /**
     * Returns the value of field 'mktProdTrdCde'.
     * 
     * @return the value of field 'MktProdTrdCde'.
     */
    public java.lang.String getMktProdTrdCde(
    ) {
        return this._mktProdTrdCde;
    }

    /**
     * Returns the value of field 'mktSegExchgCde'.
     * 
     * @return the value of field 'MktSegExchgCde'.
     */
    public java.lang.String getMktSegExchgCde(
    ) {
        return this._mktSegExchgCde;
    }

    /**
     * Returns the value of field 'mrgnPrcAuctnTrdPct'.
     * 
     * @return the value of field 'MrgnPrcAuctnTrdPct'.
     */
    public java.lang.String getMrgnPrcAuctnTrdPct(
    ) {
        return this._mrgnPrcAuctnTrdPct;
    }

    /**
     * Returns the value of field 'mrgnSecODPct'.
     * 
     * @return the value of field 'MrgnSecODPct'.
     */
    public java.lang.String getMrgnSecODPct(
    ) {
        return this._mrgnSecODPct;
    }

    /**
     * Returns the value of field 'mrgnTrdInd'.
     * 
     * @return the value of field 'MrgnTrdInd'.
     */
    public java.lang.String getMrgnTrdInd(
    ) {
        return this._mrgnTrdInd;
    }

    /**
     * Returns the value of field 'poolProdInd'.
     * 
     * @return the value of field 'PoolProdInd'.
     */
    public java.lang.String getPoolProdInd(
    ) {
        return this._poolProdInd;
    }

    /**
     * Returns the value of field 'prcVarCde'.
     * 
     * @return the value of field 'PrcVarCde'.
     */
    public java.lang.String getPrcVarCde(
    ) {
        return this._prcVarCde;
    }

    /**
     * Returns the value of field 'prcVarMinAmt'.
     * 
     * @return the value of field 'PrcVarMinAmt'.
     */
    public java.lang.String getPrcVarMinAmt(
    ) {
        return this._prcVarMinAmt;
    }

    /**
     * Returns the value of field 'prcVarMinEffDt'.
     * 
     * @return the value of field 'PrcVarMinEffDt'.
     */
    public java.lang.String getPrcVarMinEffDt(
    ) {
        return this._prcVarMinEffDt;
    }

    /**
     * Returns the value of field 'prodAuctnTrdExpirDt'.
     * 
     * @return the value of field 'ProdAuctnTrdExpirDt'.
     */
    public java.lang.String getProdAuctnTrdExpirDt(
    ) {
        return this._prodAuctnTrdExpirDt;
    }

    /**
     * Returns the value of field 'prodBodLotQtyCnt'.
     * 
     * @return the value of field 'ProdBodLotQtyCnt'.
     */
    public java.lang.String getProdBodLotQtyCnt(
    ) {
        return this._prodBodLotQtyCnt;
    }

    /**
     * Returns the value of field 'prodBuyMaxQtyCnt'.
     * 
     * @return the value of field 'ProdBuyMaxQtyCnt'.
     */
    public java.lang.String getProdBuyMaxQtyCnt(
    ) {
        return this._prodBuyMaxQtyCnt;
    }

    /**
     * Returns the value of field 'prodExerPrcAmt'.
     * 
     * @return the value of field 'ProdExerPrcAmt'.
     */
    public java.lang.String getProdExerPrcAmt(
    ) {
        return this._prodExerPrcAmt;
    }

    /**
     * Returns the value of field 'prodIssQtyTtlCnt'.
     * 
     * @return the value of field 'ProdIssQtyTtlCnt'.
     */
    public java.lang.String getProdIssQtyTtlCnt(
    ) {
        return this._prodIssQtyTtlCnt;
    }

    /**
     * Returns the value of field 'prodMaxForgnInvstrTtlPct'.
     * 
     * @return the value of field 'ProdMaxForgnInvstrTtlPct'.
     */
    public java.lang.String getProdMaxForgnInvstrTtlPct(
    ) {
        return this._prodMaxForgnInvstrTtlPct;
    }

    /**
     * Returns the value of field 'prodMaxIndvForgnInvstrPct'.
     * 
     * @return the value of field 'ProdMaxIndvForgnInvstrPct'.
     */
    public java.lang.String getProdMaxIndvForgnInvstrPct(
    ) {
        return this._prodMaxIndvForgnInvstrPct;
    }

    /**
     * Returns the value of field 'prodMaxIndvOwnrPct'.
     * 
     * @return the value of field 'ProdMaxIndvOwnrPct'.
     */
    public java.lang.String getProdMaxIndvOwnrPct(
    ) {
        return this._prodMaxIndvOwnrPct;
    }

    /**
     * Returns the value of field 'prodMktStatChngLaDt'.
     * 
     * @return the value of field 'ProdMktStatChngLaDt'.
     */
    public java.lang.String getProdMktStatChngLaDt(
    ) {
        return this._prodMktStatChngLaDt;
    }

    /**
     * Returns the value of field 'prodSellMaxQtyCnt'.
     * 
     * @return the value of field 'ProdSellMaxQtyCnt'.
     */
    public java.lang.String getProdSellMaxQtyCnt(
    ) {
        return this._prodSellMaxQtyCnt;
    }

    /**
     * Returns the value of field 'psblProdBorwInd'.
     * 
     * @return the value of field 'PsblProdBorwInd'.
     */
    public java.lang.String getPsblProdBorwInd(
    ) {
        return this._psblProdBorwInd;
    }

    /**
     * Returns the value of field 'scripOnlyProdInd'.
     * 
     * @return the value of field 'ScripOnlyProdInd'.
     */
    public java.lang.String getScripOnlyProdInd(
    ) {
        return this._scripOnlyProdInd;
    }

    /**
     * Returns the value of field 'sprdStopLossMaxCnt'.
     * 
     * @return the value of field 'SprdStopLossMaxCnt'.
     */
    public java.lang.String getSprdStopLossMaxCnt(
    ) {
        return this._sprdStopLossMaxCnt;
    }

    /**
     * Returns the value of field 'sprdStopLossMinCnt'.
     * 
     * @return the value of field 'SprdStopLossMinCnt'.
     */
    public java.lang.String getSprdStopLossMinCnt(
    ) {
        return this._sprdStopLossMinCnt;
    }

    /**
     * Returns the value of field 'stopLossMaxPct'.
     * 
     * @return the value of field 'StopLossMaxPct'.
     */
    public java.lang.String getStopLossMaxPct(
    ) {
        return this._stopLossMaxPct;
    }

    /**
     * Returns the value of field 'stopLossMinPct'.
     * 
     * @return the value of field 'StopLossMinPct'.
     */
    public java.lang.String getStopLossMinPct(
    ) {
        return this._stopLossMinPct;
    }

    /**
     * Returns the value of field 'suptAllIn1OrdInd'.
     * 
     * @return the value of field 'SuptAllIn1OrdInd'.
     */
    public java.lang.String getSuptAllIn1OrdInd(
    ) {
        return this._suptAllIn1OrdInd;
    }

    /**
     * Returns the value of field 'suptAtmcTrdInd'.
     * 
     * @return the value of field 'SuptAtmcTrdInd'.
     */
    public java.lang.String getSuptAtmcTrdInd(
    ) {
        return this._suptAtmcTrdInd;
    }

    /**
     * Returns the value of field 'suptAuctnTrdInd'.
     * 
     * @return the value of field 'SuptAuctnTrdInd'.
     */
    public java.lang.String getSuptAuctnTrdInd(
    ) {
        return this._suptAuctnTrdInd;
    }

    /**
     * Returns the value of field 'suptMipInd'.
     * 
     * @return the value of field 'SuptMipInd'.
     */
    public java.lang.String getSuptMipInd(
    ) {
        return this._suptMipInd;
    }

    /**
     * Returns the value of field 'suptNetSetlInd'.
     * 
     * @return the value of field 'SuptNetSetlInd'.
     */
    public java.lang.String getSuptNetSetlInd(
    ) {
        return this._suptNetSetlInd;
    }

    /**
     * Returns the value of field 'suptProdSpltInd'.
     * 
     * @return the value of field 'SuptProdSpltInd'.
     */
    public java.lang.String getSuptProdSpltInd(
    ) {
        return this._suptProdSpltInd;
    }

    /**
     * Returns the value of field 'suptStopLossOrdInd'.
     * 
     * @return the value of field 'SuptStopLossOrdInd'.
     */
    public java.lang.String getSuptStopLossOrdInd(
    ) {
        return this._suptStopLossOrdInd;
    }

    /**
     * Returns the value of field 'suptWinWinOrdInd'.
     * 
     * @return the value of field 'SuptWinWinOrdInd'.
     */
    public java.lang.String getSuptWinWinOrdInd(
    ) {
        return this._suptWinWinOrdInd;
    }

    /**
     * Returns the value of field 'trdLimitInd'.
     * 
     * @return the value of field 'TrdLimitInd'.
     */
    public java.lang.String getTrdLimitInd(
    ) {
        return this._trdLimitInd;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
     */
    public boolean isValid(
    ) {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(
            final java.io.Writer out)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(
            final org.xml.sax.ContentHandler handler)
    throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'allowProdLendInd'.
     * 
     * @param allowProdLendInd the value of field 'allowProdLendInd'
     */
    public void setAllowProdLendInd(
            final java.lang.String allowProdLendInd) {
        this._allowProdLendInd = allowProdLendInd;
    }

    /**
     * Sets the value of field 'bodLotProdInd'.
     * 
     * @param bodLotProdInd the value of field 'bodLotProdInd'.
     */
    public void setBodLotProdInd(
            final java.lang.String bodLotProdInd) {
        this._bodLotProdInd = bodLotProdInd;
    }

    /**
     * Sets the value of field 'ctryProdRegisCde'.
     * 
     * @param ctryProdRegisCde the value of field 'ctryProdRegisCde'
     */
    public void setCtryProdRegisCde(
            final java.lang.String ctryProdRegisCde) {
        this._ctryProdRegisCde = ctryProdRegisCde;
    }

    /**
     * Sets the value of field 'earnPerShareAnnlAmt'.
     * 
     * @param earnPerShareAnnlAmt the value of field
     * 'earnPerShareAnnlAmt'.
     */
    public void setEarnPerShareAnnlAmt(
            final java.lang.String earnPerShareAnnlAmt) {
        this._earnPerShareAnnlAmt = earnPerShareAnnlAmt;
    }

    /**
     * Sets the value of field 'exclLimitCalcInd'.
     * 
     * @param exclLimitCalcInd the value of field 'exclLimitCalcInd'
     */
    public void setExclLimitCalcInd(
            final java.lang.String exclLimitCalcInd) {
        this._exclLimitCalcInd = exclLimitCalcInd;
    }

    /**
     * Sets the value of field 'invstMipIncrmMinAmt'.
     * 
     * @param invstMipIncrmMinAmt the value of field
     * 'invstMipIncrmMinAmt'.
     */
    public void setInvstMipIncrmMinAmt(
            final java.lang.String invstMipIncrmMinAmt) {
        this._invstMipIncrmMinAmt = invstMipIncrmMinAmt;
    }

    /**
     * Sets the value of field 'invstMipMinAmt'.
     * 
     * @param invstMipMinAmt the value of field 'invstMipMinAmt'.
     */
    public void setInvstMipMinAmt(
            final java.lang.String invstMipMinAmt) {
        this._invstMipMinAmt = invstMipMinAmt;
    }

    /**
     * Sets the value of field 'loanBdgtProdIPOAmt'.
     * 
     * @param loanBdgtProdIPOAmt the value of field
     * 'loanBdgtProdIPOAmt'.
     */
    public void setLoanBdgtProdIPOAmt(
            final java.lang.String loanBdgtProdIPOAmt) {
        this._loanBdgtProdIPOAmt = loanBdgtProdIPOAmt;
    }

    /**
     * Sets the value of field 'loanProdIPOTtlAmt'.
     * 
     * @param loanProdIPOTtlAmt the value of field
     * 'loanProdIPOTtlAmt'.
     */
    public void setLoanProdIPOTtlAmt(
            final java.lang.String loanProdIPOTtlAmt) {
        this._loanProdIPOTtlAmt = loanProdIPOTtlAmt;
    }

    /**
     * Sets the value of field 'loanProdMrgnTrdPct'.
     * 
     * @param loanProdMrgnTrdPct the value of field
     * 'loanProdMrgnTrdPct'.
     */
    public void setLoanProdMrgnTrdPct(
            final java.lang.String loanProdMrgnTrdPct) {
        this._loanProdMrgnTrdPct = loanProdMrgnTrdPct;
    }

    /**
     * Sets the value of field 'methCalcScripChrgCde'.
     * 
     * @param methCalcScripChrgCde the value of field
     * 'methCalcScripChrgCde'.
     */
    public void setMethCalcScripChrgCde(
            final java.lang.String methCalcScripChrgCde) {
        this._methCalcScripChrgCde = methCalcScripChrgCde;
    }

    /**
     * Sets the value of field 'mktProdTrdCde'.
     * 
     * @param mktProdTrdCde the value of field 'mktProdTrdCde'.
     */
    public void setMktProdTrdCde(
            final java.lang.String mktProdTrdCde) {
        this._mktProdTrdCde = mktProdTrdCde;
    }

    /**
     * Sets the value of field 'mktSegExchgCde'.
     * 
     * @param mktSegExchgCde the value of field 'mktSegExchgCde'.
     */
    public void setMktSegExchgCde(
            final java.lang.String mktSegExchgCde) {
        this._mktSegExchgCde = mktSegExchgCde;
    }

    /**
     * Sets the value of field 'mrgnPrcAuctnTrdPct'.
     * 
     * @param mrgnPrcAuctnTrdPct the value of field
     * 'mrgnPrcAuctnTrdPct'.
     */
    public void setMrgnPrcAuctnTrdPct(
            final java.lang.String mrgnPrcAuctnTrdPct) {
        this._mrgnPrcAuctnTrdPct = mrgnPrcAuctnTrdPct;
    }

    /**
     * Sets the value of field 'mrgnSecODPct'.
     * 
     * @param mrgnSecODPct the value of field 'mrgnSecODPct'.
     */
    public void setMrgnSecODPct(
            final java.lang.String mrgnSecODPct) {
        this._mrgnSecODPct = mrgnSecODPct;
    }

    /**
     * Sets the value of field 'mrgnTrdInd'.
     * 
     * @param mrgnTrdInd the value of field 'mrgnTrdInd'.
     */
    public void setMrgnTrdInd(
            final java.lang.String mrgnTrdInd) {
        this._mrgnTrdInd = mrgnTrdInd;
    }

    /**
     * Sets the value of field 'poolProdInd'.
     * 
     * @param poolProdInd the value of field 'poolProdInd'.
     */
    public void setPoolProdInd(
            final java.lang.String poolProdInd) {
        this._poolProdInd = poolProdInd;
    }

    /**
     * Sets the value of field 'prcVarCde'.
     * 
     * @param prcVarCde the value of field 'prcVarCde'.
     */
    public void setPrcVarCde(
            final java.lang.String prcVarCde) {
        this._prcVarCde = prcVarCde;
    }

    /**
     * Sets the value of field 'prcVarMinAmt'.
     * 
     * @param prcVarMinAmt the value of field 'prcVarMinAmt'.
     */
    public void setPrcVarMinAmt(
            final java.lang.String prcVarMinAmt) {
        this._prcVarMinAmt = prcVarMinAmt;
    }

    /**
     * Sets the value of field 'prcVarMinEffDt'.
     * 
     * @param prcVarMinEffDt the value of field 'prcVarMinEffDt'.
     */
    public void setPrcVarMinEffDt(
            final java.lang.String prcVarMinEffDt) {
        this._prcVarMinEffDt = prcVarMinEffDt;
    }

    /**
     * Sets the value of field 'prodAuctnTrdExpirDt'.
     * 
     * @param prodAuctnTrdExpirDt the value of field
     * 'prodAuctnTrdExpirDt'.
     */
    public void setProdAuctnTrdExpirDt(
            final java.lang.String prodAuctnTrdExpirDt) {
        this._prodAuctnTrdExpirDt = prodAuctnTrdExpirDt;
    }

    /**
     * Sets the value of field 'prodBodLotQtyCnt'.
     * 
     * @param prodBodLotQtyCnt the value of field 'prodBodLotQtyCnt'
     */
    public void setProdBodLotQtyCnt(
            final java.lang.String prodBodLotQtyCnt) {
        this._prodBodLotQtyCnt = prodBodLotQtyCnt;
    }

    /**
     * Sets the value of field 'prodBuyMaxQtyCnt'.
     * 
     * @param prodBuyMaxQtyCnt the value of field 'prodBuyMaxQtyCnt'
     */
    public void setProdBuyMaxQtyCnt(
            final java.lang.String prodBuyMaxQtyCnt) {
        this._prodBuyMaxQtyCnt = prodBuyMaxQtyCnt;
    }

    /**
     * Sets the value of field 'prodExerPrcAmt'.
     * 
     * @param prodExerPrcAmt the value of field 'prodExerPrcAmt'.
     */
    public void setProdExerPrcAmt(
            final java.lang.String prodExerPrcAmt) {
        this._prodExerPrcAmt = prodExerPrcAmt;
    }

    /**
     * Sets the value of field 'prodIssQtyTtlCnt'.
     * 
     * @param prodIssQtyTtlCnt the value of field 'prodIssQtyTtlCnt'
     */
    public void setProdIssQtyTtlCnt(
            final java.lang.String prodIssQtyTtlCnt) {
        this._prodIssQtyTtlCnt = prodIssQtyTtlCnt;
    }

    /**
     * Sets the value of field 'prodMaxForgnInvstrTtlPct'.
     * 
     * @param prodMaxForgnInvstrTtlPct the value of field
     * 'prodMaxForgnInvstrTtlPct'.
     */
    public void setProdMaxForgnInvstrTtlPct(
            final java.lang.String prodMaxForgnInvstrTtlPct) {
        this._prodMaxForgnInvstrTtlPct = prodMaxForgnInvstrTtlPct;
    }

    /**
     * Sets the value of field 'prodMaxIndvForgnInvstrPct'.
     * 
     * @param prodMaxIndvForgnInvstrPct the value of field
     * 'prodMaxIndvForgnInvstrPct'.
     */
    public void setProdMaxIndvForgnInvstrPct(
            final java.lang.String prodMaxIndvForgnInvstrPct) {
        this._prodMaxIndvForgnInvstrPct = prodMaxIndvForgnInvstrPct;
    }

    /**
     * Sets the value of field 'prodMaxIndvOwnrPct'.
     * 
     * @param prodMaxIndvOwnrPct the value of field
     * 'prodMaxIndvOwnrPct'.
     */
    public void setProdMaxIndvOwnrPct(
            final java.lang.String prodMaxIndvOwnrPct) {
        this._prodMaxIndvOwnrPct = prodMaxIndvOwnrPct;
    }

    /**
     * Sets the value of field 'prodMktStatChngLaDt'.
     * 
     * @param prodMktStatChngLaDt the value of field
     * 'prodMktStatChngLaDt'.
     */
    public void setProdMktStatChngLaDt(
            final java.lang.String prodMktStatChngLaDt) {
        this._prodMktStatChngLaDt = prodMktStatChngLaDt;
    }

    /**
     * Sets the value of field 'prodSellMaxQtyCnt'.
     * 
     * @param prodSellMaxQtyCnt the value of field
     * 'prodSellMaxQtyCnt'.
     */
    public void setProdSellMaxQtyCnt(
            final java.lang.String prodSellMaxQtyCnt) {
        this._prodSellMaxQtyCnt = prodSellMaxQtyCnt;
    }

    /**
     * Sets the value of field 'psblProdBorwInd'.
     * 
     * @param psblProdBorwInd the value of field 'psblProdBorwInd'.
     */
    public void setPsblProdBorwInd(
            final java.lang.String psblProdBorwInd) {
        this._psblProdBorwInd = psblProdBorwInd;
    }

    /**
     * Sets the value of field 'scripOnlyProdInd'.
     * 
     * @param scripOnlyProdInd the value of field 'scripOnlyProdInd'
     */
    public void setScripOnlyProdInd(
            final java.lang.String scripOnlyProdInd) {
        this._scripOnlyProdInd = scripOnlyProdInd;
    }

    /**
     * Sets the value of field 'sprdStopLossMaxCnt'.
     * 
     * @param sprdStopLossMaxCnt the value of field
     * 'sprdStopLossMaxCnt'.
     */
    public void setSprdStopLossMaxCnt(
            final java.lang.String sprdStopLossMaxCnt) {
        this._sprdStopLossMaxCnt = sprdStopLossMaxCnt;
    }

    /**
     * Sets the value of field 'sprdStopLossMinCnt'.
     * 
     * @param sprdStopLossMinCnt the value of field
     * 'sprdStopLossMinCnt'.
     */
    public void setSprdStopLossMinCnt(
            final java.lang.String sprdStopLossMinCnt) {
        this._sprdStopLossMinCnt = sprdStopLossMinCnt;
    }

    /**
     * Sets the value of field 'stopLossMaxPct'.
     * 
     * @param stopLossMaxPct the value of field 'stopLossMaxPct'.
     */
    public void setStopLossMaxPct(
            final java.lang.String stopLossMaxPct) {
        this._stopLossMaxPct = stopLossMaxPct;
    }

    /**
     * Sets the value of field 'stopLossMinPct'.
     * 
     * @param stopLossMinPct the value of field 'stopLossMinPct'.
     */
    public void setStopLossMinPct(
            final java.lang.String stopLossMinPct) {
        this._stopLossMinPct = stopLossMinPct;
    }

    /**
     * Sets the value of field 'suptAllIn1OrdInd'.
     * 
     * @param suptAllIn1OrdInd the value of field 'suptAllIn1OrdInd'
     */
    public void setSuptAllIn1OrdInd(
            final java.lang.String suptAllIn1OrdInd) {
        this._suptAllIn1OrdInd = suptAllIn1OrdInd;
    }

    /**
     * Sets the value of field 'suptAtmcTrdInd'.
     * 
     * @param suptAtmcTrdInd the value of field 'suptAtmcTrdInd'.
     */
    public void setSuptAtmcTrdInd(
            final java.lang.String suptAtmcTrdInd) {
        this._suptAtmcTrdInd = suptAtmcTrdInd;
    }

    /**
     * Sets the value of field 'suptAuctnTrdInd'.
     * 
     * @param suptAuctnTrdInd the value of field 'suptAuctnTrdInd'.
     */
    public void setSuptAuctnTrdInd(
            final java.lang.String suptAuctnTrdInd) {
        this._suptAuctnTrdInd = suptAuctnTrdInd;
    }

    /**
     * Sets the value of field 'suptMipInd'.
     * 
     * @param suptMipInd the value of field 'suptMipInd'.
     */
    public void setSuptMipInd(
            final java.lang.String suptMipInd) {
        this._suptMipInd = suptMipInd;
    }

    /**
     * Sets the value of field 'suptNetSetlInd'.
     * 
     * @param suptNetSetlInd the value of field 'suptNetSetlInd'.
     */
    public void setSuptNetSetlInd(
            final java.lang.String suptNetSetlInd) {
        this._suptNetSetlInd = suptNetSetlInd;
    }

    /**
     * Sets the value of field 'suptProdSpltInd'.
     * 
     * @param suptProdSpltInd the value of field 'suptProdSpltInd'.
     */
    public void setSuptProdSpltInd(
            final java.lang.String suptProdSpltInd) {
        this._suptProdSpltInd = suptProdSpltInd;
    }

    /**
     * Sets the value of field 'suptStopLossOrdInd'.
     * 
     * @param suptStopLossOrdInd the value of field
     * 'suptStopLossOrdInd'.
     */
    public void setSuptStopLossOrdInd(
            final java.lang.String suptStopLossOrdInd) {
        this._suptStopLossOrdInd = suptStopLossOrdInd;
    }

    /**
     * Sets the value of field 'suptWinWinOrdInd'.
     * 
     * @param suptWinWinOrdInd the value of field 'suptWinWinOrdInd'
     */
    public void setSuptWinWinOrdInd(
            final java.lang.String suptWinWinOrdInd) {
        this._suptWinWinOrdInd = suptWinWinOrdInd;
    }

    /**
     * Sets the value of field 'trdLimitInd'.
     * 
     * @param trdLimitInd the value of field 'trdLimitInd'.
     */
    public void setTrdLimitInd(
            final java.lang.String trdLimitInd) {
        this._trdLimitInd = trdLimitInd;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * com.dummy.wpc.batch.xml.StockInstmSegment
     */
    public static com.dummy.wpc.batch.xml.StockInstmSegment unmarshal(
            final java.io.Reader reader)
    throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.dummy.wpc.batch.xml.StockInstmSegment) Unmarshaller.unmarshal(com.dummy.wpc.batch.xml.StockInstmSegment.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate(
    )
    throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }

}
