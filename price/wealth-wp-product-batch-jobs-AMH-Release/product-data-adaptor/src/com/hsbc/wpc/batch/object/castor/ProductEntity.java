/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: src/com/dummy/wpc/batch/object/castor/ProductEntity.java 1.3.1.1 2012/10/16 17:53:50CST CHRIS CUI (43601081) Development  $
 */

package com.dummy.wpc.batch.object.castor;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ProductEntity.
 * 
 * @version $Revision: 1.3.1.1 $ $Date: 2012/10/16 17:53:50CST $
 */
public class ProductEntity implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ctryCde
     */
    private java.lang.String _ctryCde;

    /**
     * Field _orgnCde
     */
    private java.lang.String _orgnCde;

    /**
     * Field _prodTypeCde
     */
    private java.lang.String _prodTypeCde;

    /**
     * Field _prodSubtpCde
     */
    private java.lang.String _prodSubtpCde;

    /**
     * Field _prodCde
     */
    private java.lang.String _prodCde;

    /**
     * Field _prodName
     */
    private java.lang.String _prodName;

    /**
     * Field _prodPllName
     */
    private java.lang.String _prodPllName;

    /**
     * Field _shrtName
     */
    private java.lang.String _shrtName;

    /**
     * Field _shrtPllName
     */
    private java.lang.String _shrtPllName;

    /**
     * Field _statCde
     */
    private java.lang.String _statCde;

    /**
     * Field _crncyCde
     */
    private java.lang.String _crncyCde;

    /**
     * Field _riskLvlCde
     */
    private java.lang.String _riskLvlCde;

    /**
     * Field _launchDt
     */
    private java.lang.String _launchDt;

    /**
     * Field _exprDt
     */
    private java.lang.String _exprDt;

    /**
     * Field _issTtlNum
     */
    private java.math.BigDecimal _issTtlNum;

    /**
     * Field _prcDivrNum
     */
    private java.math.BigDecimal _prcDivrNum;

    /**
     * Field _nomlPrcAmt
     */
    private java.math.BigDecimal _nomlPrcAmt;

    /**
     * Field _bidPrcAmt
     */
    private java.math.BigDecimal _bidPrcAmt;

    /**
     * Field _offerPrcAmt
     */
    private java.math.BigDecimal _offerPrcAmt;

    /**
     * Field _prodPrcChngPct
     */
    private java.math.BigDecimal _prodPrcChngPct;

    /**
     * Field _prodPrcChngAmt
     */
    private java.math.BigDecimal _prodPrcChngAmt;

    /**
     * Field _prodPrcMaxAmt
     */
    private java.math.BigDecimal _prodPrcMaxAmt;

    /**
     * Field _prodPrcMinAmt
     */
    private java.math.BigDecimal _prodPrcMinAmt;

    /**
     * Field _shareTradeCnt
     */
    private java.math.BigDecimal _shareTradeCnt;

    /**
     * Field _shareTradeAmt
     */
    private java.math.BigDecimal _shareTradeAmt;

    /**
     * Field _prodPrcCloseAmt
     */
    private java.math.BigDecimal _prodPrcCloseAmt;

    /**
     * Field _ISINCde
     */
    private java.lang.String _ISINCde;

    /**
     * Field _ISINCde
     */
    private java.lang.String _GISINCde;

    /**
     * Field _ISINCde
     */
    private java.lang.String _TISINCde;
    
    /**
     * Field _RICCde
     */
    private java.lang.String _RICCde;

    /**
     * Field _MKTCde
     */
    private java.lang.String _MKTCde;
    
    /**
     * Field _mrgnTrdInd
     */
    private java.lang.String _mrgnTrdInd;

    /**
     * Field _mrgnSecOvdftPct
     */
    private java.math.BigDecimal _mrgnSecOvdftPct;

    /**
     * Field _auctnTrdInd
     */
    private java.lang.String _auctnTrdInd;

    /**
     * Field _stopLossMinPct
     */
    private java.math.BigDecimal _stopLossMinPct;

    /**
     * Field _stopLossMaxPct
     */
    private java.math.BigDecimal _stopLossMaxPct;

    /**
     * Field _sprdSplsMinCnt
     */
    private java.math.BigDecimal _sprdSplsMinCnt;

    /**
     * Field _sprdSplsMaxCnt
     */
    private java.math.BigDecimal _sprdSplsMaxCnt;

    /**
     * Field _lotSizeNum
     */
    private java.math.BigDecimal _lotSizeNum;
    
    /**
     * Field _eLIPrcCcy
     */
    private java.lang.String _eLIPrcCcy;

    /**
     * Field _sprdPrcAmt
     */
    private java.math.BigDecimal _sprdPrcAmt;

    /**
     * Field _ovdftSecPct
     */
    private java.math.BigDecimal _ovdftSecPct;

    /**
     * Field _exchgCde
     */
    private java.lang.String _exchgCde;

    /**
     * Field _stkPrcCcy
     */
    private java.lang.String _stkPrcCcy;
    
    /**
     * Field _stkPrcDt
     */
    private java.lang.String _stkPrcDt;
    
    /**
     * Field _mktPrcCcy
     */
    private java.lang.String _mktPrcCcy;
    
    /**
     * Field _marketPrc
     */
    private java.math.BigDecimal _marketPrc;
    
    /**
     * Field _mktPrcDt
     */
    private java.lang.String _mktPrcDt;
    
    /**
     * Field _qtyType
     */
    private java.lang.String _qtyType;
    
    /**
     * Field _prodLocCode
     */
    private java.lang.String _prodLocCode;
    
    /**
     * Field _cpnFreq
     */
    private java.lang.String _cpnFreq;
    
    /**
     * Field _cpnRte
     */
    private java.math.BigDecimal _cpnRte;
    
    /**
     * Field _prdProdCde
     */
    private java.lang.String _prdProdCde;

    /**
     * Field _prdProdNum
     */
    private java.math.BigDecimal _prdProdNum;

    /**
     * Field _invstInitMinAmt
     */
    private java.math.BigDecimal _invstInitMinAmt;

    /**
     * Field _invstIncrmMinAmt
     */
    private java.math.BigDecimal _invstIncrmMinAmt;

    /**
     * Field _redeemMinAmt
     */
    private java.math.BigDecimal _redeemMinAmt;

    /**
     * Field _priceDt
     */
    private java.lang.String _priceDt;

    /**
     * Field _fixDt
     */
    private java.lang.String _fixDt;

    /**
     * Field _matDt
     */
    private java.lang.String _matDt;

    /**
     * Field _mturDt
     */
    private java.lang.String _mturDt;

    /**
     * Field _advcRmk1
     */
    private java.lang.String _advcRmk1;

    /**
     * Field _advcRmk2
     */
    private java.lang.String _advcRmk2;

    /**
     * Field _advcRmk3
     */
    private java.lang.String _advcRmk3;

    /**
     * Field _advcRmk4
     */
    private java.lang.String _advcRmk4;

    /**
     * Field _advcRmk5
     */
    private java.lang.String _advcRmk5;

    /**
     * Field _advcRmk6
     */
    private java.lang.String _advcRmk6;

    /**
     * Field _advcRmk7
     */
    private java.lang.String _advcRmk7;

    /**
     * Field _advcRmk8
     */
    private java.lang.String _advcRmk8;

    /**
     * Field _advcRmk9
     */
    private java.lang.String _advcRmk9;

    /**
     * Field _advcRmk10
     */
    private java.lang.String _advcRmk10;

    /**
     * Field _advcRmk11
     */
    private java.lang.String _advcRmk11;

    /**
     * Field _advcRmk12
     */
    private java.lang.String _advcRmk12;

    /**
     * Field _advcRmk13
     */
    private java.lang.String _advcRmk13;

    /**
     * Field _advcRmk14
     */
    private java.lang.String _advcRmk14;

    /**
     * Field _advcRmk15
     */
    private java.lang.String _advcRmk15;

    /**
     * Field _offerStdt
     */
    private java.lang.String _offerStdt;

    /**
     * Field _offerEndt
     */
    private java.lang.String _offerEndt;

    /**
     * Field _issDt
     */
    private java.lang.String _issDt;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProductEntity() 
     {
        super();
    } //-- com.dummy.hfi.batch.object.castor.ProductEntity()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'advcRmk1'.
     * 
     * @return String
     * @return the value of field 'advcRmk1'.
     */
    public java.lang.String getAdvcRmk1()
    {
        return this._advcRmk1;
    } //-- java.lang.String getAdvcRmk1() 

    /**
     * Returns the value of field 'advcRmk10'.
     * 
     * @return String
     * @return the value of field 'advcRmk10'.
     */
    public java.lang.String getAdvcRmk10()
    {
        return this._advcRmk10;
    } //-- java.lang.String getAdvcRmk10() 

    /**
     * Returns the value of field 'advcRmk11'.
     * 
     * @return String
     * @return the value of field 'advcRmk11'.
     */
    public java.lang.String getAdvcRmk11()
    {
        return this._advcRmk11;
    } //-- java.lang.String getAdvcRmk11() 

    /**
     * Returns the value of field 'advcRmk12'.
     * 
     * @return String
     * @return the value of field 'advcRmk12'.
     */
    public java.lang.String getAdvcRmk12()
    {
        return this._advcRmk12;
    } //-- java.lang.String getAdvcRmk12() 

    /**
     * Returns the value of field 'advcRmk13'.
     * 
     * @return String
     * @return the value of field 'advcRmk13'.
     */
    public java.lang.String getAdvcRmk13()
    {
        return this._advcRmk13;
    } //-- java.lang.String getAdvcRmk13() 

    /**
     * Returns the value of field 'advcRmk14'.
     * 
     * @return String
     * @return the value of field 'advcRmk14'.
     */
    public java.lang.String getAdvcRmk14()
    {
        return this._advcRmk14;
    } //-- java.lang.String getAdvcRmk14() 

    /**
     * Returns the value of field 'advcRmk15'.
     * 
     * @return String
     * @return the value of field 'advcRmk15'.
     */
    public java.lang.String getAdvcRmk15()
    {
        return this._advcRmk15;
    } //-- java.lang.String getAdvcRmk15() 

    /**
     * Returns the value of field 'advcRmk2'.
     * 
     * @return String
     * @return the value of field 'advcRmk2'.
     */
    public java.lang.String getAdvcRmk2()
    {
        return this._advcRmk2;
    } //-- java.lang.String getAdvcRmk2() 

    /**
     * Returns the value of field 'advcRmk3'.
     * 
     * @return String
     * @return the value of field 'advcRmk3'.
     */
    public java.lang.String getAdvcRmk3()
    {
        return this._advcRmk3;
    } //-- java.lang.String getAdvcRmk3() 

    /**
     * Returns the value of field 'advcRmk4'.
     * 
     * @return String
     * @return the value of field 'advcRmk4'.
     */
    public java.lang.String getAdvcRmk4()
    {
        return this._advcRmk4;
    } //-- java.lang.String getAdvcRmk4() 

    /**
     * Returns the value of field 'advcRmk5'.
     * 
     * @return String
     * @return the value of field 'advcRmk5'.
     */
    public java.lang.String getAdvcRmk5()
    {
        return this._advcRmk5;
    } //-- java.lang.String getAdvcRmk5() 

    /**
     * Returns the value of field 'advcRmk6'.
     * 
     * @return String
     * @return the value of field 'advcRmk6'.
     */
    public java.lang.String getAdvcRmk6()
    {
        return this._advcRmk6;
    } //-- java.lang.String getAdvcRmk6() 

    /**
     * Returns the value of field 'advcRmk7'.
     * 
     * @return String
     * @return the value of field 'advcRmk7'.
     */
    public java.lang.String getAdvcRmk7()
    {
        return this._advcRmk7;
    } //-- java.lang.String getAdvcRmk7() 

    /**
     * Returns the value of field 'advcRmk8'.
     * 
     * @return String
     * @return the value of field 'advcRmk8'.
     */
    public java.lang.String getAdvcRmk8()
    {
        return this._advcRmk8;
    } //-- java.lang.String getAdvcRmk8() 

    /**
     * Returns the value of field 'advcRmk9'.
     * 
     * @return String
     * @return the value of field 'advcRmk9'.
     */
    public java.lang.String getAdvcRmk9()
    {
        return this._advcRmk9;
    } //-- java.lang.String getAdvcRmk9() 

    /**
     * Returns the value of field 'auctnTrdInd'.
     * 
     * @return String
     * @return the value of field 'auctnTrdInd'.
     */
    public java.lang.String getAuctnTrdInd()
    {
        return this._auctnTrdInd;
    } //-- java.lang.String getAuctnTrdInd() 

    /**
     * Returns the value of field 'bidPrcAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'bidPrcAmt'.
     */
    public java.math.BigDecimal getBidPrcAmt()
    {
        return this._bidPrcAmt;
    } //-- java.math.BigDecimal getBidPrcAmt() 

    /**
     * Returns the value of field 'crncyCde'.
     * 
     * @return String
     * @return the value of field 'crncyCde'.
     */
    public java.lang.String getCrncyCde()
    {
        return this._crncyCde;
    } //-- java.lang.String getCrncyCde() 

    /**
     * Returns the value of field 'ctryCde'.
     * 
     * @return String
     * @return the value of field 'ctryCde'.
     */
    public java.lang.String getCtryCde()
    {
        return this._ctryCde;
    } //-- java.lang.String getCtryCde() 

    /**
     * Returns the value of field 'exchgCde'.
     * 
     * @return String
     * @return the value of field 'exchgCde'.
     */
    public java.lang.String getExchgCde()
    {
        return this._exchgCde;
    } //-- java.lang.String getExchgCde() 

    /**
     * Returns the value of field 'eLIPrcCcy'.
     * 
     * @return String
     * @return the value of field 'eLIPrcCcy'.
     */
    public java.lang.String getELIPrcCcy()
    {
        return this._eLIPrcCcy;
    }
    
    /**
     * Returns the value of field 'stkPrcDt'.
     * 
     * @return String
     * @return the value of field 'stkPrcDt'.
     */
    public java.lang.String getStkPrcDt()
    {
        return this._stkPrcDt;
    }
    
    /**
     * Returns the value of field 'mktPrcCcy'.
     * 
     * @return String
     * @return the value of field 'mktPrcCcy'.
     */
    public java.lang.String getMktPrcCcy()
    {
        return this._mktPrcCcy;
    }
    
    /**
     * Returns the value of field 'marketPrc'.
     * 
     * @return String
     * @return the value of field 'marketPrc'.
     */
    public java.math.BigDecimal getMarketPrc()
    {
        return this._marketPrc;
    }
    
    /**
     * Returns the value of field 'mktPrcDt'.
     * 
     * @return String
     * @return the value of field 'mktPrcDt'.
     */
    public java.lang.String getMktPrcDt()
    {
        return this._mktPrcDt;
    }
    
    /**
     * Returns the value of field 'qtyType'.
     * 
     * @return String
     * @return the value of field 'qtyType'.
     */
    public java.lang.String getQtyType() {
        return this._qtyType;
    }

    /**
     * Returns the value of field 'prodLocCode'.
     * 
     * @return String
     * @return the value of field 'prodLocCode'.
     */
    public java.lang.String getProdLocCode() {
        return this._prodLocCode;
    }
    
    /**
     * Returns the value of field 'cpnFreq'.
     * 
     * @return String
     * @return the value of field 'cpnFreq'.
     */
    public java.lang.String getCpnFreq() {
        return this._cpnFreq;
    }

    /**
     * Returns the value of field 'cpnRte'.
     * 
     * @return BigDecimal
     * @return the value of field 'cpnRte'.
     */
    public java.math.BigDecimal getCpnRte() {
        return this._cpnRte;
    }


    /**
     * Returns the value of field 'exprDt'.
     * 
     * @return String
     * @return the value of field 'exprDt'.
     */
    public java.lang.String getExprDt()
    {
        return this._exprDt;
    } //-- java.lang.String getExprDt() 

    /**
     * Returns the value of field 'fixDt'.
     * 
     * @return String
     * @return the value of field 'fixDt'.
     */
    public java.lang.String getFixDt()
    {
        return this._fixDt;
    } //-- java.lang.String getFixDt() 

    /**
     * Returns the value of field 'ISINCde'.
     * 
     * @return String
     * @return the value of field 'ISINCde'.
     */
    public java.lang.String getISINCde()
    {
        return this._ISINCde;
    } //-- java.lang.String getISINCde() 

    /**
     * Returns the value of field 'GISINCde'.
     *
     * @return String
     * @return the value of field 'GISINCde'.
     */
    public java.lang.String getGISINCde()
    {
        return this._GISINCde;
    }

    /**
     * Returns the value of field 'TISINCde'.
     *
     * @return String
     * @return the value of field 'TISINCde'.
     */
    public java.lang.String getTISINCde()
    {
        return this._TISINCde;
    }

    /**
     * Returns the value of field 'RICCde'.
     * 
     * @return String
     * @return the value of field 'RICCde'.
     */
    public java.lang.String getRICCde()
    {
        return this._RICCde;
    } //-- java.lang.String getRICCde() 
    
    /**
     * Returns the value of field 'MKTCde'.
     * 
     * @return String
     * @return the value of field 'MKTCde'.
     */
    public java.lang.String getMKTCde()
    {
    	return this._MKTCde;
    }//-- java.lang.String getMKTCde() 
    
    /**
     * Returns the value of field 'invstIncrmMinAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'invstIncrmMinAmt'.
     */
    public java.math.BigDecimal getInvstIncrmMinAmt()
    {
        return this._invstIncrmMinAmt;
    } //-- java.math.BigDecimal getInvstIncrmMinAmt() 

    /**
     * Returns the value of field 'invstInitMinAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'invstInitMinAmt'.
     */
    public java.math.BigDecimal getInvstInitMinAmt()
    {
        return this._invstInitMinAmt;
    } //-- java.math.BigDecimal getInvstInitMinAmt() 

    /**
     * Returns the value of field 'issDt'.
     * 
     * @return String
     * @return the value of field 'issDt'.
     */
    public java.lang.String getIssDt()
    {
        return this._issDt;
    } //-- java.lang.String getIssDt() 

    /**
     * Returns the value of field 'issTtlNum'.
     * 
     * @return BigDecimal
     * @return the value of field 'issTtlNum'.
     */
    public java.math.BigDecimal getIssTtlNum()
    {
        return this._issTtlNum;
    } //-- java.math.BigDecimal getIssTtlNum() 

    /**
     * Returns the value of field 'launchDt'.
     * 
     * @return String
     * @return the value of field 'launchDt'.
     */
    public java.lang.String getLaunchDt()
    {
        return this._launchDt;
    } //-- java.lang.String getLaunchDt() 

    /**
     * Returns the value of field 'lotSizeNum'.
     * 
     * @return BigDecimal
     * @return the value of field 'lotSizeNum'.
     */
    public java.math.BigDecimal getLotSizeNum()
    {
        return this._lotSizeNum;
    } //-- java.math.BigDecimal getLotSizeNum() 

    /**
     * Returns the value of field 'stkPrcCcy'.
     * 
     * @return String
     * @return the value of field 'stkPrcCcy'.
     */
    public java.lang.String getStkPrcCcy()
    {
        return this._stkPrcCcy;
    }
    
    /**
     * Returns the value of field 'matDt'.
     * 
     * @return String
     * @return the value of field 'matDt'.
     */
    public java.lang.String getMatDt()
    {
        return this._matDt;
    } //-- java.lang.String getMatDt() 

    /**
     * Returns the value of field 'mrgnSecOvdftPct'.
     * 
     * @return BigDecimal
     * @return the value of field 'mrgnSecOvdftPct'.
     */
    public java.math.BigDecimal getMrgnSecOvdftPct()
    {
        return this._mrgnSecOvdftPct;
    } //-- java.math.BigDecimal getMrgnSecOvdftPct() 

    /**
     * Returns the value of field 'mrgnTrdInd'.
     * 
     * @return String
     * @return the value of field 'mrgnTrdInd'.
     */
    public java.lang.String getMrgnTrdInd()
    {
        return this._mrgnTrdInd;
    } //-- java.lang.String getMrgnTrdInd() 

    /**
     * Returns the value of field 'mturDt'.
     * 
     * @return String
     * @return the value of field 'mturDt'.
     */
    public java.lang.String getMturDt()
    {
        return this._mturDt;
    } //-- java.lang.String getMturDt() 

    /**
     * Returns the value of field 'nomlPrcAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'nomlPrcAmt'.
     */
    public java.math.BigDecimal getNomlPrcAmt()
    {
        return this._nomlPrcAmt;
    } //-- java.math.BigDecimal getNomlPrcAmt() 

    /**
     * Returns the value of field 'offerEndt'.
     * 
     * @return String
     * @return the value of field 'offerEndt'.
     */
    public java.lang.String getOfferEndt()
    {
        return this._offerEndt;
    } //-- java.lang.String getOfferEndt() 

    /**
     * Returns the value of field 'offerPrcAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'offerPrcAmt'.
     */
    public java.math.BigDecimal getOfferPrcAmt()
    {
        return this._offerPrcAmt;
    } //-- java.math.BigDecimal getOfferPrcAmt() 

    /**
     * Returns the value of field 'offerStdt'.
     * 
     * @return String
     * @return the value of field 'offerStdt'.
     */
    public java.lang.String getOfferStdt()
    {
        return this._offerStdt;
    } //-- java.lang.String getOfferStdt() 

    /**
     * Returns the value of field 'orgnCde'.
     * 
     * @return String
     * @return the value of field 'orgnCde'.
     */
    public java.lang.String getOrgnCde()
    {
        return this._orgnCde;
    } //-- java.lang.String getOrgnCde() 

    /**
     * Returns the value of field 'ovdftSecPct'.
     * 
     * @return BigDecimal
     * @return the value of field 'ovdftSecPct'.
     */
    public java.math.BigDecimal getOvdftSecPct()
    {
        return this._ovdftSecPct;
    } //-- java.math.BigDecimal getOvdftSecPct() 

    /**
     * Returns the value of field 'prcDivrNum'.
     * 
     * @return BigDecimal
     * @return the value of field 'prcDivrNum'.
     */
    public java.math.BigDecimal getPrcDivrNum()
    {
        return this._prcDivrNum;
    } //-- java.math.BigDecimal getPrcDivrNum() 

    /**
     * Returns the value of field 'prdProdCde'.
     * 
     * @return String
     * @return the value of field 'prdProdCde'.
     */
    public java.lang.String getPrdProdCde()
    {
        return this._prdProdCde;
    } //-- java.lang.String getPrdProdCde() 

    /**
     * Returns the value of field 'prdProdNum'.
     * 
     * @return BigDecimal
     * @return the value of field 'prdProdNum'.
     */
    public java.math.BigDecimal getPrdProdNum()
    {
        return this._prdProdNum;
    } //-- java.math.BigDecimal getPrdProdNum() 

    /**
     * Returns the value of field 'priceDt'.
     * 
     * @return String
     * @return the value of field 'priceDt'.
     */
    public java.lang.String getPriceDt()
    {
        return this._priceDt;
    } //-- java.lang.String getPriceDt() 

    /**
     * Returns the value of field 'prodCde'.
     * 
     * @return String
     * @return the value of field 'prodCde'.
     */
    public java.lang.String getProdCde()
    {
        return this._prodCde;
    } //-- java.lang.String getProdCde() 

    /**
     * Returns the value of field 'prodName'.
     * 
     * @return String
     * @return the value of field 'prodName'.
     */
    public java.lang.String getProdName()
    {
        return this._prodName;
    } //-- java.lang.String getProdName() 

    /**
     * Returns the value of field 'prodPllName'.
     * 
     * @return String
     * @return the value of field 'prodPllName'.
     */
    public java.lang.String getProdPllName()
    {
        return this._prodPllName;
    } //-- java.lang.String getProdPllName() 

    /**
     * Returns the value of field 'prodPrcChngAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'prodPrcChngAmt'.
     */
    public java.math.BigDecimal getProdPrcChngAmt()
    {
        return this._prodPrcChngAmt;
    } //-- java.math.BigDecimal getProdPrcChngAmt() 

    /**
     * Returns the value of field 'prodPrcChngPct'.
     * 
     * @return BigDecimal
     * @return the value of field 'prodPrcChngPct'.
     */
    public java.math.BigDecimal getProdPrcChngPct()
    {
        return this._prodPrcChngPct;
    } //-- java.math.BigDecimal getProdPrcChngPct() 

    /**
     * Returns the value of field 'prodPrcCloseAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'prodPrcCloseAmt'.
     */
    public java.math.BigDecimal getProdPrcCloseAmt()
    {
        return this._prodPrcCloseAmt;
    } //-- java.math.BigDecimal getProdPrcCloseAmt() 

    /**
     * Returns the value of field 'prodPrcMaxAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'prodPrcMaxAmt'.
     */
    public java.math.BigDecimal getProdPrcMaxAmt()
    {
        return this._prodPrcMaxAmt;
    } //-- java.math.BigDecimal getProdPrcMaxAmt() 

    /**
     * Returns the value of field 'prodPrcMinAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'prodPrcMinAmt'.
     */
    public java.math.BigDecimal getProdPrcMinAmt()
    {
        return this._prodPrcMinAmt;
    } //-- java.math.BigDecimal getProdPrcMinAmt() 

    /**
     * Returns the value of field 'prodSubtpCde'.
     * 
     * @return String
     * @return the value of field 'prodSubtpCde'.
     */
    public java.lang.String getProdSubtpCde()
    {
        return this._prodSubtpCde;
    } //-- java.lang.String getProdSubtpCde() 

    /**
     * Returns the value of field 'prodTypeCde'.
     * 
     * @return String
     * @return the value of field 'prodTypeCde'.
     */
    public java.lang.String getProdTypeCde()
    {
        return this._prodTypeCde;
    } //-- java.lang.String getProdTypeCde() 

    /**
     * Returns the value of field 'redeemMinAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'redeemMinAmt'.
     */
    public java.math.BigDecimal getRedeemMinAmt()
    {
        return this._redeemMinAmt;
    } //-- java.math.BigDecimal getRedeemMinAmt() 

    /**
     * Returns the value of field 'riskLvlCde'.
     * 
     * @return String
     * @return the value of field 'riskLvlCde'.
     */
    public java.lang.String getRiskLvlCde()
    {
        return this._riskLvlCde;
    } //-- java.lang.String getRiskLvlCde() 

    /**
     * Returns the value of field 'shareTradeAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'shareTradeAmt'.
     */
    public java.math.BigDecimal getShareTradeAmt()
    {
        return this._shareTradeAmt;
    } //-- java.math.BigDecimal getShareTradeAmt() 

    /**
     * Returns the value of field 'shareTradeCnt'.
     * 
     * @return BigDecimal
     * @return the value of field 'shareTradeCnt'.
     */
    public java.math.BigDecimal getShareTradeCnt()
    {
        return this._shareTradeCnt;
    } //-- java.math.BigDecimal getShareTradeCnt() 

    /**
     * Returns the value of field 'shrtName'.
     * 
     * @return String
     * @return the value of field 'shrtName'.
     */
    public java.lang.String getShrtName()
    {
        return this._shrtName;
    } //-- java.lang.String getShrtName() 

    /**
     * Returns the value of field 'shrtPllName'.
     * 
     * @return String
     * @return the value of field 'shrtPllName'.
     */
    public java.lang.String getShrtPllName()
    {
        return this._shrtPllName;
    } //-- java.lang.String getShrtPllName() 

    /**
     * Returns the value of field 'sprdPrcAmt'.
     * 
     * @return BigDecimal
     * @return the value of field 'sprdPrcAmt'.
     */
    public java.math.BigDecimal getSprdPrcAmt()
    {
        return this._sprdPrcAmt;
    } //-- java.math.BigDecimal getSprdPrcAmt() 

    /**
     * Returns the value of field 'sprdSplsMaxCnt'.
     * 
     * @return BigDecimal
     * @return the value of field 'sprdSplsMaxCnt'.
     */
    public java.math.BigDecimal getSprdSplsMaxCnt()
    {
        return this._sprdSplsMaxCnt;
    } //-- java.math.BigDecimal getSprdSplsMaxCnt() 

    /**
     * Returns the value of field 'sprdSplsMinCnt'.
     * 
     * @return BigDecimal
     * @return the value of field 'sprdSplsMinCnt'.
     */
    public java.math.BigDecimal getSprdSplsMinCnt()
    {
        return this._sprdSplsMinCnt;
    } //-- java.math.BigDecimal getSprdSplsMinCnt() 

    /**
     * Returns the value of field 'statCde'.
     * 
     * @return String
     * @return the value of field 'statCde'.
     */
    public java.lang.String getStatCde()
    {
        return this._statCde;
    } //-- java.lang.String getStatCde() 

    /**
     * Returns the value of field 'stopLossMaxPct'.
     * 
     * @return BigDecimal
     * @return the value of field 'stopLossMaxPct'.
     */
    public java.math.BigDecimal getStopLossMaxPct()
    {
        return this._stopLossMaxPct;
    } //-- java.math.BigDecimal getStopLossMaxPct() 

    /**
     * Returns the value of field 'stopLossMinPct'.
     * 
     * @return BigDecimal
     * @return the value of field 'stopLossMinPct'.
     */
    public java.math.BigDecimal getStopLossMinPct()
    {
        return this._stopLossMinPct;
    } //-- java.math.BigDecimal getStopLossMinPct() 

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'advcRmk1'.
     * 
     * @param advcRmk1 the value of field 'advcRmk1'.
     */
    public void setAdvcRmk1(java.lang.String advcRmk1)
    {
        this._advcRmk1 = advcRmk1;
    } //-- void setAdvcRmk1(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk10'.
     * 
     * @param advcRmk10 the value of field 'advcRmk10'.
     */
    public void setAdvcRmk10(java.lang.String advcRmk10)
    {
        this._advcRmk10 = advcRmk10;
    } //-- void setAdvcRmk10(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk11'.
     * 
     * @param advcRmk11 the value of field 'advcRmk11'.
     */
    public void setAdvcRmk11(java.lang.String advcRmk11)
    {
        this._advcRmk11 = advcRmk11;
    } //-- void setAdvcRmk11(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk12'.
     * 
     * @param advcRmk12 the value of field 'advcRmk12'.
     */
    public void setAdvcRmk12(java.lang.String advcRmk12)
    {
        this._advcRmk12 = advcRmk12;
    } //-- void setAdvcRmk12(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk13'.
     * 
     * @param advcRmk13 the value of field 'advcRmk13'.
     */
    public void setAdvcRmk13(java.lang.String advcRmk13)
    {
        this._advcRmk13 = advcRmk13;
    } //-- void setAdvcRmk13(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk14'.
     * 
     * @param advcRmk14 the value of field 'advcRmk14'.
     */
    public void setAdvcRmk14(java.lang.String advcRmk14)
    {
        this._advcRmk14 = advcRmk14;
    } //-- void setAdvcRmk14(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk15'.
     * 
     * @param advcRmk15 the value of field 'advcRmk15'.
     */
    public void setAdvcRmk15(java.lang.String advcRmk15)
    {
        this._advcRmk15 = advcRmk15;
    } //-- void setAdvcRmk15(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk2'.
     * 
     * @param advcRmk2 the value of field 'advcRmk2'.
     */
    public void setAdvcRmk2(java.lang.String advcRmk2)
    {
        this._advcRmk2 = advcRmk2;
    } //-- void setAdvcRmk2(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk3'.
     * 
     * @param advcRmk3 the value of field 'advcRmk3'.
     */
    public void setAdvcRmk3(java.lang.String advcRmk3)
    {
        this._advcRmk3 = advcRmk3;
    } //-- void setAdvcRmk3(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk4'.
     * 
     * @param advcRmk4 the value of field 'advcRmk4'.
     */
    public void setAdvcRmk4(java.lang.String advcRmk4)
    {
        this._advcRmk4 = advcRmk4;
    } //-- void setAdvcRmk4(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk5'.
     * 
     * @param advcRmk5 the value of field 'advcRmk5'.
     */
    public void setAdvcRmk5(java.lang.String advcRmk5)
    {
        this._advcRmk5 = advcRmk5;
    } //-- void setAdvcRmk5(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk6'.
     * 
     * @param advcRmk6 the value of field 'advcRmk6'.
     */
    public void setAdvcRmk6(java.lang.String advcRmk6)
    {
        this._advcRmk6 = advcRmk6;
    } //-- void setAdvcRmk6(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk7'.
     * 
     * @param advcRmk7 the value of field 'advcRmk7'.
     */
    public void setAdvcRmk7(java.lang.String advcRmk7)
    {
        this._advcRmk7 = advcRmk7;
    } //-- void setAdvcRmk7(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk8'.
     * 
     * @param advcRmk8 the value of field 'advcRmk8'.
     */
    public void setAdvcRmk8(java.lang.String advcRmk8)
    {
        this._advcRmk8 = advcRmk8;
    } //-- void setAdvcRmk8(java.lang.String) 

    /**
     * Sets the value of field 'advcRmk9'.
     * 
     * @param advcRmk9 the value of field 'advcRmk9'.
     */
    public void setAdvcRmk9(java.lang.String advcRmk9)
    {
        this._advcRmk9 = advcRmk9;
    } //-- void setAdvcRmk9(java.lang.String) 

    /**
     * Sets the value of field 'auctnTrdInd'.
     * 
     * @param auctnTrdInd the value of field 'auctnTrdInd'.
     */
    public void setAuctnTrdInd(java.lang.String auctnTrdInd)
    {
        this._auctnTrdInd = auctnTrdInd;
    } //-- void setAuctnTrdInd(java.lang.String) 

    /**
     * Sets the value of field 'bidPrcAmt'.
     * 
     * @param bidPrcAmt the value of field 'bidPrcAmt'.
     */
    public void setBidPrcAmt(java.math.BigDecimal bidPrcAmt)
    {
        this._bidPrcAmt = bidPrcAmt;
    } //-- void setBidPrcAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'crncyCde'.
     * 
     * @param crncyCde the value of field 'crncyCde'.
     */
    public void setCrncyCde(java.lang.String crncyCde)
    {
        this._crncyCde = crncyCde;
    } //-- void setCrncyCde(java.lang.String) 

    /**
     * Sets the value of field 'ctryCde'.
     * 
     * @param ctryCde the value of field 'ctryCde'.
     */
    public void setCtryCde(java.lang.String ctryCde)
    {
        this._ctryCde = ctryCde;
    } //-- void setCtryCde(java.lang.String) 

    /**
     * Sets the value of field 'exchgCde'.
     * 
     * @param exchgCde the value of field 'exchgCde'.
     */
    public void setExchgCde(java.lang.String exchgCde)
    {
        this._exchgCde = exchgCde;
    } //-- void setExchgCde(java.lang.String) 

    /**
     * Sets the value of field 'eLIPrcCcy'.
     * 
     * @param stkPrcCcy the value of field 'eLIPrcCcy'.
     */
    public void setELIPrcCcy(java.lang.String eLIPrcCcy)
    {
        this._eLIPrcCcy = eLIPrcCcy;
    }
    
    /**
     * Sets the value of field 'stkPrcDt'.
     * 
     * @param stkPrcDt the value of field 'stkPrcDt'.
     */
    public void setStkPrcDt(java.lang.String stkPrcDt)
    {
        this._stkPrcDt = stkPrcDt;
    }
    
    /**
     * Sets the value of field 'mktPrcCcy'.
     * 
     * @param mktPrcCcy the value of field 'mktPrcCcy'.
     */
    public void setMktPrcCcy(java.lang.String mktPrcCcy)
    {
        this._mktPrcCcy = mktPrcCcy;
    }
    
    /**
     * Sets the value of field 'marketPrc'.
     * 
     * @param marketPrc the value of field 'marketPrc'.
     */
    public void setMarketPrc(java.math.BigDecimal marketPrc)
    {
        this._marketPrc = marketPrc;
    }
    
    /**
     * Sets the value of field 'mktPrcDt'.
     * 
     * @param mktPrcDt the value of field 'mktPrcDt'.
     */
    public void setMktPrcDt(java.lang.String mktPrcDt)
    {
        this._mktPrcDt = mktPrcDt;
    }
    
    /**
     * Sets the value of field 'qtyType'.
     * 
     * @param qtyType the value of field 'qtyType'.
     */
    public void setQtyType(java.lang.String qtyType) {
        this._qtyType = qtyType;
    }

    /**
     * Sets the value of field 'prodLocCode'.
     * 
     * @param prodLocCode the value of field 'prodLocCode'.
     */
    public void setProdLocCode(java.lang.String prodLocCode) {
        this._prodLocCode = prodLocCode;
    }
    
    /**
     * Sets the value of field 'cpnFreq'.
     * 
     * @param cpnFreq the value of field 'cpnFreq'.
     */
    public void setCpnFreq(java.lang.String cpnFreq) {
        this._cpnFreq = cpnFreq;
    }

    /**
     * Sets the value of field 'cpnRte'.
     * 
     * @param cpnRte the value of field 'cpnRte'.
     */
    public void setCpnRte(java.math.BigDecimal cpnRte) {
        this._cpnRte = cpnRte;
    }


    /**
     * Sets the value of field 'exprDt'.
     * 
     * @param exprDt the value of field 'exprDt'.
     */
    public void setExprDt(java.lang.String exprDt)
    {
        this._exprDt = exprDt;
    } //-- void setExprDt(java.lang.String) 

    /**
     * Sets the value of field 'fixDt'.
     * 
     * @param fixDt the value of field 'fixDt'.
     */
    public void setFixDt(java.lang.String fixDt)
    {
        this._fixDt = fixDt;
    } //-- void setFixDt(java.lang.String) 

    /**
     * Sets the value of field 'ISINCde'.
     * 
     * @param ISINCde the value of field 'ISINCde'.
     */
    public void setISINCde(java.lang.String ISINCde)
    {
        this._ISINCde = ISINCde;
    } //-- void setISINCde(java.lang.String) 

    /**
     * Sets the value of field 'GISINCde'.
     *
     * @param GISINCde the value of field 'GISINCde'.
     */
    public void setGISINCde(java.lang.String GISINCde)
    {
        this._GISINCde = GISINCde;
    }

    /**
     * Sets the value of field 'ISINCde'.
     *
     * @param TISINCde the value of field 'TISINCde'.
     */
    public void setTISINCde(java.lang.String TISINCde)
    {
        this._TISINCde = TISINCde;
    }

    /**
     * Sets the value of field 'RICCde'.
     * 
     * @param RICCde the value of field 'RICCde'.
     */
    public void setRICCde(java.lang.String RICCde)
    {
        this._RICCde = RICCde;
    } //-- void setRICCde(java.lang.String) 
    
    /**
     * Sets the value of field 'MKTCde'.
     * 
     * @param MKTCde the value of field 'MKTCde'.
     */
    public void setMKTCde(java.lang.String MKTCde) 
    {
    	this._MKTCde = MKTCde;
    }//-- void setMKTCde(java.lang.String) 
    
    /**
     * Sets the value of field 'invstIncrmMinAmt'.
     * 
     * @param invstIncrmMinAmt the value of field 'invstIncrmMinAmt'
     */
    public void setInvstIncrmMinAmt(java.math.BigDecimal invstIncrmMinAmt)
    {
        this._invstIncrmMinAmt = invstIncrmMinAmt;
    } //-- void setInvstIncrmMinAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'invstInitMinAmt'.
     * 
     * @param invstInitMinAmt the value of field 'invstInitMinAmt'.
     */
    public void setInvstInitMinAmt(java.math.BigDecimal invstInitMinAmt)
    {
        this._invstInitMinAmt = invstInitMinAmt;
    } //-- void setInvstInitMinAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'issDt'.
     * 
     * @param issDt the value of field 'issDt'.
     */
    public void setIssDt(java.lang.String issDt)
    {
        this._issDt = issDt;
    } //-- void setIssDt(java.lang.String) 

    /**
     * Sets the value of field 'issTtlNum'.
     * 
     * @param issTtlNum the value of field 'issTtlNum'.
     */
    public void setIssTtlNum(java.math.BigDecimal issTtlNum)
    {
        this._issTtlNum = issTtlNum;
    } //-- void setIssTtlNum(java.math.BigDecimal) 

    /**
     * Sets the value of field 'launchDt'.
     * 
     * @param launchDt the value of field 'launchDt'.
     */
    public void setLaunchDt(java.lang.String launchDt)
    {
        this._launchDt = launchDt;
    } //-- void setLaunchDt(java.lang.String) 

    /**
     * Sets the value of field 'lotSizeNum'.
     * 
     * @param lotSizeNum the value of field 'lotSizeNum'.
     */
    public void setLotSizeNum(java.math.BigDecimal lotSizeNum)
    {
        this._lotSizeNum = lotSizeNum;
    } //-- void setLotSizeNum(java.math.BigDecimal) 

    /**
     * Sets the value of field 'stkPrcCcy'.
     * 
     * @param stkPrcCcy the value of field 'stkPrcCcy'.
     */
    public void setStkPrcCcy(java.lang.String stkPrcCcy)
    {
        this._stkPrcCcy = stkPrcCcy;
    }
    
    /**
     * Sets the value of field 'matDt'.
     * 
     * @param matDt the value of field 'matDt'.
     */
    public void setMatDt(java.lang.String matDt)
    {
        this._matDt = matDt;
    } //-- void setMatDt(java.lang.String) 

    /**
     * Sets the value of field 'mrgnSecOvdftPct'.
     * 
     * @param mrgnSecOvdftPct the value of field 'mrgnSecOvdftPct'.
     */
    public void setMrgnSecOvdftPct(java.math.BigDecimal mrgnSecOvdftPct)
    {
        this._mrgnSecOvdftPct = mrgnSecOvdftPct;
    } //-- void setMrgnSecOvdftPct(java.math.BigDecimal) 

    /**
     * Sets the value of field 'mrgnTrdInd'.
     * 
     * @param mrgnTrdInd the value of field 'mrgnTrdInd'.
     */
    public void setMrgnTrdInd(java.lang.String mrgnTrdInd)
    {
        this._mrgnTrdInd = mrgnTrdInd;
    } //-- void setMrgnTrdInd(java.lang.String) 

    /**
     * Sets the value of field 'mturDt'.
     * 
     * @param mturDt the value of field 'mturDt'.
     */
    public void setMturDt(java.lang.String mturDt)
    {
        this._mturDt = mturDt;
    } //-- void setMturDt(java.lang.String) 

    /**
     * Sets the value of field 'nomlPrcAmt'.
     * 
     * @param nomlPrcAmt the value of field 'nomlPrcAmt'.
     */
    public void setNomlPrcAmt(java.math.BigDecimal nomlPrcAmt)
    {
        this._nomlPrcAmt = nomlPrcAmt;
    } //-- void setNomlPrcAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'offerEndt'.
     * 
     * @param offerEndt the value of field 'offerEndt'.
     */
    public void setOfferEndt(java.lang.String offerEndt)
    {
        this._offerEndt = offerEndt;
    } //-- void setOfferEndt(java.lang.String) 

    /**
     * Sets the value of field 'offerPrcAmt'.
     * 
     * @param offerPrcAmt the value of field 'offerPrcAmt'.
     */
    public void setOfferPrcAmt(java.math.BigDecimal offerPrcAmt)
    {
        this._offerPrcAmt = offerPrcAmt;
    } //-- void setOfferPrcAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'offerStdt'.
     * 
     * @param offerStdt the value of field 'offerStdt'.
     */
    public void setOfferStdt(java.lang.String offerStdt)
    {
        this._offerStdt = offerStdt;
    } //-- void setOfferStdt(java.lang.String) 

    /**
     * Sets the value of field 'orgnCde'.
     * 
     * @param orgnCde the value of field 'orgnCde'.
     */
    public void setOrgnCde(java.lang.String orgnCde)
    {
        this._orgnCde = orgnCde;
    } //-- void setOrgnCde(java.lang.String) 

    /**
     * Sets the value of field 'ovdftSecPct'.
     * 
     * @param ovdftSecPct the value of field 'ovdftSecPct'.
     */
    public void setOvdftSecPct(java.math.BigDecimal ovdftSecPct)
    {
        this._ovdftSecPct = ovdftSecPct;
    } //-- void setOvdftSecPct(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prcDivrNum'.
     * 
     * @param prcDivrNum the value of field 'prcDivrNum'.
     */
    public void setPrcDivrNum(java.math.BigDecimal prcDivrNum)
    {
        this._prcDivrNum = prcDivrNum;
    } //-- void setPrcDivrNum(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prdProdCde'.
     * 
     * @param prdProdCde the value of field 'prdProdCde'.
     */
    public void setPrdProdCde(java.lang.String prdProdCde)
    {
        this._prdProdCde = prdProdCde;
    } //-- void setPrdProdCde(java.lang.String) 

    /**
     * Sets the value of field 'prdProdNum'.
     * 
     * @param prdProdNum the value of field 'prdProdNum'.
     */
    public void setPrdProdNum(java.math.BigDecimal prdProdNum)
    {
        this._prdProdNum = prdProdNum;
    } //-- void setPrdProdNum(java.math.BigDecimal) 

    /**
     * Sets the value of field 'priceDt'.
     * 
     * @param priceDt the value of field 'priceDt'.
     */
    public void setPriceDt(java.lang.String priceDt)
    {
        this._priceDt = priceDt;
    } //-- void setPriceDt(java.lang.String) 

    /**
     * Sets the value of field 'prodCde'.
     * 
     * @param prodCde the value of field 'prodCde'.
     */
    public void setProdCde(java.lang.String prodCde)
    {
        this._prodCde = prodCde;
    } //-- void setProdCde(java.lang.String) 

    /**
     * Sets the value of field 'prodName'.
     * 
     * @param prodName the value of field 'prodName'.
     */
    public void setProdName(java.lang.String prodName)
    {
        this._prodName = prodName;
    } //-- void setProdName(java.lang.String) 

    /**
     * Sets the value of field 'prodPllName'.
     * 
     * @param prodPllName the value of field 'prodPllName'.
     */
    public void setProdPllName(java.lang.String prodPllName)
    {
        this._prodPllName = prodPllName;
    } //-- void setProdPllName(java.lang.String) 

    /**
     * Sets the value of field 'prodPrcChngAmt'.
     * 
     * @param prodPrcChngAmt the value of field 'prodPrcChngAmt'.
     */
    public void setProdPrcChngAmt(java.math.BigDecimal prodPrcChngAmt)
    {
        this._prodPrcChngAmt = prodPrcChngAmt;
    } //-- void setProdPrcChngAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prodPrcChngPct'.
     * 
     * @param prodPrcChngPct the value of field 'prodPrcChngPct'.
     */
    public void setProdPrcChngPct(java.math.BigDecimal prodPrcChngPct)
    {
        this._prodPrcChngPct = prodPrcChngPct;
    } //-- void setProdPrcChngPct(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prodPrcCloseAmt'.
     * 
     * @param prodPrcCloseAmt the value of field 'prodPrcCloseAmt'.
     */
    public void setProdPrcCloseAmt(java.math.BigDecimal prodPrcCloseAmt)
    {
        this._prodPrcCloseAmt = prodPrcCloseAmt;
    } //-- void setProdPrcCloseAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prodPrcMaxAmt'.
     * 
     * @param prodPrcMaxAmt the value of field 'prodPrcMaxAmt'.
     */
    public void setProdPrcMaxAmt(java.math.BigDecimal prodPrcMaxAmt)
    {
        this._prodPrcMaxAmt = prodPrcMaxAmt;
    } //-- void setProdPrcMaxAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prodPrcMinAmt'.
     * 
     * @param prodPrcMinAmt the value of field 'prodPrcMinAmt'.
     */
    public void setProdPrcMinAmt(java.math.BigDecimal prodPrcMinAmt)
    {
        this._prodPrcMinAmt = prodPrcMinAmt;
    } //-- void setProdPrcMinAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'prodSubtpCde'.
     * 
     * @param prodSubtpCde the value of field 'prodSubtpCde'.
     */
    public void setProdSubtpCde(java.lang.String prodSubtpCde)
    {
        this._prodSubtpCde = prodSubtpCde;
    } //-- void setProdSubtpCde(java.lang.String) 

    /**
     * Sets the value of field 'prodTypeCde'.
     * 
     * @param prodTypeCde the value of field 'prodTypeCde'.
     */
    public void setProdTypeCde(java.lang.String prodTypeCde)
    {
        this._prodTypeCde = prodTypeCde;
    } //-- void setProdTypeCde(java.lang.String) 

    /**
     * Sets the value of field 'redeemMinAmt'.
     * 
     * @param redeemMinAmt the value of field 'redeemMinAmt'.
     */
    public void setRedeemMinAmt(java.math.BigDecimal redeemMinAmt)
    {
        this._redeemMinAmt = redeemMinAmt;
    } //-- void setRedeemMinAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'riskLvlCde'.
     * 
     * @param riskLvlCde the value of field 'riskLvlCde'.
     */
    public void setRiskLvlCde(java.lang.String riskLvlCde)
    {
        this._riskLvlCde = riskLvlCde;
    } //-- void setRiskLvlCde(java.lang.String) 

    /**
     * Sets the value of field 'shareTradeAmt'.
     * 
     * @param shareTradeAmt the value of field 'shareTradeAmt'.
     */
    public void setShareTradeAmt(java.math.BigDecimal shareTradeAmt)
    {
        this._shareTradeAmt = shareTradeAmt;
    } //-- void setShareTradeAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'shareTradeCnt'.
     * 
     * @param shareTradeCnt the value of field 'shareTradeCnt'.
     */
    public void setShareTradeCnt(java.math.BigDecimal shareTradeCnt)
    {
        this._shareTradeCnt = shareTradeCnt;
    } //-- void setShareTradeCnt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'shrtName'.
     * 
     * @param shrtName the value of field 'shrtName'.
     */
    public void setShrtName(java.lang.String shrtName)
    {
        this._shrtName = shrtName;
    } //-- void setShrtName(java.lang.String) 

    /**
     * Sets the value of field 'shrtPllName'.
     * 
     * @param shrtPllName the value of field 'shrtPllName'.
     */
    public void setShrtPllName(java.lang.String shrtPllName)
    {
        this._shrtPllName = shrtPllName;
    } //-- void setShrtPllName(java.lang.String) 

    /**
     * Sets the value of field 'sprdPrcAmt'.
     * 
     * @param sprdPrcAmt the value of field 'sprdPrcAmt'.
     */
    public void setSprdPrcAmt(java.math.BigDecimal sprdPrcAmt)
    {
        this._sprdPrcAmt = sprdPrcAmt;
    } //-- void setSprdPrcAmt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'sprdSplsMaxCnt'.
     * 
     * @param sprdSplsMaxCnt the value of field 'sprdSplsMaxCnt'.
     */
    public void setSprdSplsMaxCnt(java.math.BigDecimal sprdSplsMaxCnt)
    {
        this._sprdSplsMaxCnt = sprdSplsMaxCnt;
    } //-- void setSprdSplsMaxCnt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'sprdSplsMinCnt'.
     * 
     * @param sprdSplsMinCnt the value of field 'sprdSplsMinCnt'.
     */
    public void setSprdSplsMinCnt(java.math.BigDecimal sprdSplsMinCnt)
    {
        this._sprdSplsMinCnt = sprdSplsMinCnt;
    } //-- void setSprdSplsMinCnt(java.math.BigDecimal) 

    /**
     * Sets the value of field 'statCde'.
     * 
     * @param statCde the value of field 'statCde'.
     */
    public void setStatCde(java.lang.String statCde)
    {
        this._statCde = statCde;
    } //-- void setStatCde(java.lang.String) 

    /**
     * Sets the value of field 'stopLossMaxPct'.
     * 
     * @param stopLossMaxPct the value of field 'stopLossMaxPct'.
     */
    public void setStopLossMaxPct(java.math.BigDecimal stopLossMaxPct)
    {
        this._stopLossMaxPct = stopLossMaxPct;
    } //-- void setStopLossMaxPct(java.math.BigDecimal) 

    /**
     * Sets the value of field 'stopLossMinPct'.
     * 
     * @param stopLossMinPct the value of field 'stopLossMinPct'.
     */
    public void setStopLossMinPct(java.math.BigDecimal stopLossMinPct)
    {
        this._stopLossMinPct = stopLossMinPct;
    } //-- void setStopLossMinPct(java.math.BigDecimal) 

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ProductEntity
     */
    public static com.dummy.wpc.batch.object.castor.ProductEntity unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.dummy.wpc.batch.object.castor.ProductEntity) Unmarshaller.unmarshal(com.dummy.wpc.batch.object.castor.ProductEntity.class, reader);
    } //-- com.dummy.hfi.batch.object.castor.ProductEntity unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
