/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean;

import java.util.List;

public class ProdInfoSeg {
	private String prodSubtpCde;
	private String prodName;
	private String prodPllName;
	private String prodSllName;
	private String prodShrtName;
	private String prodShrtPllName;
	private String prodShrtSllName;
	private String[] switchableGroups;
	private String prodDesc;
	private String prodPllDesc;
	private String asetClassCde;
	private String prodStatCde;
	private String ccyProdCde;
	private String riskLvlCde;
	private String prdProdCde;
	private String prdProdNum;
	private String termRemainDayCnt;
	private String prodMturDt;
	private String prodLnchDt;
	private String mktInvstCde;
	private String islmProdInd;
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
	private String prtyProdSrchRsultNum;
	private String availMktInfoInd;
	private String prdRtrnAvgNum;
	private String rtrnVoltlAvgPct;
	private String dmyProdSubtpRecInd;
	private String dispComProdSrchInd;
	private String divrNum;
	private String mrkToMktInd;
	private String ctryProdTrade1Cde;
	private String busStartTm;
	private String busEndTm;
	private String prodTopSellRankNum;
	private String prodTopPerfmRankNum;
	private String prodTopYieldRankNum;
	private String ccyInvstCde;
	private String qtyTypeCde;
	private String prodLocCde;
	private String prodTaxFreeWrapActStaCde;
	private String trdFirstDt;
	private String suptRcblCashProdInd;
	private String suptRcblScripProdInd;
	private String dcmlPlaceTradeUnitNum;
	private String pldgLimitAssocAcctInd;
	private String aumChrgProdInd;
	private String invstInitMinAmt;
	private String prdInvstTnorNum;
	private String asetVoltlClassMajrPrntCde;
	private ProdTradeCcySeg prodTradeCcySeg;
	private ProdAsetUndlSeg prodAsetUndlSeg;
	private ProdListSeg prodListSeg;
	private List<ProdUserDefExtSeg> prodUserDefExtSeg;
	private List<ProdAsetVoltlClassSeg> prodAsetVoltlClassSeg;
	private ResChanSeg resChanSeg;
	private List<AsetSicAllocSeg> asetSicAllocSeg;
	private List<AsetGeoLocAllocSeg> asetGeoLocAllocSeg;
	
	//add
	private List<ChanlAttrSeg> chanlAttrSeg;

	
	public List<ChanlAttrSeg> getChanlAttrSeg() {
		return chanlAttrSeg;
	}

	public void setChanlAttrSeg(List<ChanlAttrSeg> chanlAttrSeg) {
		this.chanlAttrSeg = chanlAttrSeg;
	}

	public String getProdSubtpCde() {
		return prodSubtpCde;
	}

	public void setProdSubtpCde(String prodSubtpCde) {
		this.prodSubtpCde = prodSubtpCde;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdPllName() {
		return prodPllName;
	}

	public void setProdPllName(String prodPllName) {
		this.prodPllName = prodPllName;
	}

	public String getProdSllName() {
		return prodSllName;
	}

	public void setProdSllName(String prodSllName) {
		this.prodSllName = prodSllName;
	}

	public String getProdShrtName() {
		return prodShrtName;
	}

	public void setProdShrtName(String prodShrtName) {
		this.prodShrtName = prodShrtName;
	}

	public String getProdShrtPllName() {
		return prodShrtPllName;
	}

	public void setProdShrtPllName(String prodShrtPllName) {
		this.prodShrtPllName = prodShrtPllName;
	}

	public String getProdShrtSllName() {
		return prodShrtSllName;
	}

	public void setProdShrtSllName(String prodShrtSllName) {
		this.prodShrtSllName = prodShrtSllName;
	}

	public String[] getSwitchableGroups() {
		return switchableGroups;
	}

	public void setSwitchableGroups(String[] switchableGroups) {
		this.switchableGroups = switchableGroups;
	}

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public String getProdPllDesc() {
		return prodPllDesc;
	}

	public void setProdPllDesc(String prodPllDesc) {
		this.prodPllDesc = prodPllDesc;
	}

	public String getAsetClassCde() {
		return asetClassCde;
	}

	public void setAsetClassCde(String asetClassCde) {
		this.asetClassCde = asetClassCde;
	}

	public String getProdStatCde() {
		return prodStatCde;
	}

	public void setProdStatCde(String prodStatCde) {
		this.prodStatCde = prodStatCde;
	}

	public String getCcyProdCde() {
		return ccyProdCde;
	}

	public void setCcyProdCde(String ccyProdCde) {
		this.ccyProdCde = ccyProdCde;
	}

	public String getRiskLvlCde() {
		return riskLvlCde;
	}

	public void setRiskLvlCde(String riskLvlCde) {
		this.riskLvlCde = riskLvlCde;
	}

	public String getPrdProdCde() {
		return prdProdCde;
	}

	public void setPrdProdCde(String prdProdCde) {
		this.prdProdCde = prdProdCde;
	}

	public String getPrdProdNum() {
		return prdProdNum;
	}

	public void setPrdProdNum(String prdProdNum) {
		this.prdProdNum = prdProdNum;
	}

	public String getTermRemainDayCnt() {
		return termRemainDayCnt;
	}

	public void setTermRemainDayCnt(String termRemainDayCnt) {
		this.termRemainDayCnt = termRemainDayCnt;
	}

	public String getProdMturDt() {
		return prodMturDt;
	}

	public void setProdMturDt(String prodMturDt) {
		this.prodMturDt = prodMturDt;
	}

	public String getProdLnchDt() {
		return prodLnchDt;
	}

	public void setProdLnchDt(String prodLnchDt) {
		this.prodLnchDt = prodLnchDt;
	}

	public String getMktInvstCde() {
		return mktInvstCde;
	}

	public void setMktInvstCde(String mktInvstCde) {
		this.mktInvstCde = mktInvstCde;
	}
	
	public String getIslmProdInd() {
        return islmProdInd;
    }

    public void setIslmProdInd(String islmProdInd) {
        this.islmProdInd = islmProdInd;
    }

    public String getAllowBuyProdInd() {
		return allowBuyProdInd;
	}

	public void setAllowBuyProdInd(String allowBuyProdInd) {
		this.allowBuyProdInd = allowBuyProdInd;
	}

	public String getAllowSellProdInd() {
		return allowSellProdInd;
	}

	public void setAllowSellProdInd(String allowSellProdInd) {
		this.allowSellProdInd = allowSellProdInd;
	}

	public String getAllowBuyUtProdInd() {
		return allowBuyUtProdInd;
	}

	public void setAllowBuyUtProdInd(String allowBuyUtProdInd) {
		this.allowBuyUtProdInd = allowBuyUtProdInd;
	}

	public String getAllowBuyAmtProdInd() {
		return allowBuyAmtProdInd;
	}

	public void setAllowBuyAmtProdInd(String allowBuyAmtProdInd) {
		this.allowBuyAmtProdInd = allowBuyAmtProdInd;
	}

	public String getAllowSellUtProdInd() {
		return allowSellUtProdInd;
	}

	public void setAllowSellUtProdInd(String allowSellUtProdInd) {
		this.allowSellUtProdInd = allowSellUtProdInd;
	}

	public String getAllowSellAmtProdInd() {
		return allowSellAmtProdInd;
	}

	public void setAllowSellAmtProdInd(String allowSellAmtProdInd) {
		this.allowSellAmtProdInd = allowSellAmtProdInd;
	}

	public String getAllowSellMipProdInd() {
		return allowSellMipProdInd;
	}

	public void setAllowSellMipProdInd(String allowSellMipProdInd) {
		this.allowSellMipProdInd = allowSellMipProdInd;
	}

	public String getAllowSellMipUtProdInd() {
		return allowSellMipUtProdInd;
	}

	public void setAllowSellMipUtProdInd(String allowSellMipUtProdInd) {
		this.allowSellMipUtProdInd = allowSellMipUtProdInd;
	}

	public String getAllowSellMipAmtProdInd() {
		return allowSellMipAmtProdInd;
	}

	public void setAllowSellMipAmtProdInd(String allowSellMipAmtProdInd) {
		this.allowSellMipAmtProdInd = allowSellMipAmtProdInd;
	}

	public String getAllowSwInProdInd() {
		return allowSwInProdInd;
	}

	public void setAllowSwInProdInd(String allowSwInProdInd) {
		this.allowSwInProdInd = allowSwInProdInd;
	}

	public String getAllowSwInUtProdInd() {
		return allowSwInUtProdInd;
	}

	public void setAllowSwInUtProdInd(String allowSwInUtProdInd) {
		this.allowSwInUtProdInd = allowSwInUtProdInd;
	}

	public String getAllowSwInAmtProdInd() {
		return allowSwInAmtProdInd;
	}

	public void setAllowSwInAmtProdInd(String allowSwInAmtProdInd) {
		this.allowSwInAmtProdInd = allowSwInAmtProdInd;
	}

	public String getAllowSwOutProdInd() {
		return allowSwOutProdInd;
	}

	public void setAllowSwOutProdInd(String allowSwOutProdInd) {
		this.allowSwOutProdInd = allowSwOutProdInd;
	}

	public String getAllowSwOutUtProdInd() {
		return allowSwOutUtProdInd;
	}

	public void setAllowSwOutUtProdInd(String allowSwOutUtProdInd) {
		this.allowSwOutUtProdInd = allowSwOutUtProdInd;
	}

	public String getAllowSwOutAmtProdInd() {
		return allowSwOutAmtProdInd;
	}

	public void setAllowSwOutAmtProdInd(String allowSwOutAmtProdInd) {
		this.allowSwOutAmtProdInd = allowSwOutAmtProdInd;
	}

	public String getIncmCharProdInd() {
		return incmCharProdInd;
	}

	public void setIncmCharProdInd(String incmCharProdInd) {
		this.incmCharProdInd = incmCharProdInd;
	}

	public String getCptlGurntProdInd() {
		return cptlGurntProdInd;
	}

	public void setCptlGurntProdInd(String cptlGurntProdInd) {
		this.cptlGurntProdInd = cptlGurntProdInd;
	}

	public String getYieldEnhnProdInd() {
		return yieldEnhnProdInd;
	}

	public void setYieldEnhnProdInd(String yieldEnhnProdInd) {
		this.yieldEnhnProdInd = yieldEnhnProdInd;
	}

	public String getGrwthCharProdInd() {
		return grwthCharProdInd;
	}

	public void setGrwthCharProdInd(String grwthCharProdInd) {
		this.grwthCharProdInd = grwthCharProdInd;
	}

	public String getPrtyProdSrchRsultNum() {
		return prtyProdSrchRsultNum;
	}

	public void setPrtyProdSrchRsultNum(String prtyProdSrchRsultNum) {
		this.prtyProdSrchRsultNum = prtyProdSrchRsultNum;
	}

	public String getAvailMktInfoInd() {
		return availMktInfoInd;
	}

	public void setAvailMktInfoInd(String availMktInfoInd) {
		this.availMktInfoInd = availMktInfoInd;
	}

	public String getPrdRtrnAvgNum() {
		return prdRtrnAvgNum;
	}

	public void setPrdRtrnAvgNum(String prdRtrnAvgNum) {
		this.prdRtrnAvgNum = prdRtrnAvgNum;
	}

	public String getRtrnVoltlAvgPct() {
		return rtrnVoltlAvgPct;
	}

	public void setRtrnVoltlAvgPct(String rtrnVoltlAvgPct) {
		this.rtrnVoltlAvgPct = rtrnVoltlAvgPct;
	}

	public String getDmyProdSubtpRecInd() {
		return dmyProdSubtpRecInd;
	}

	public void setDmyProdSubtpRecInd(String dmyProdSubtpRecInd) {
		this.dmyProdSubtpRecInd = dmyProdSubtpRecInd;
	}

	public String getDispComProdSrchInd() {
		return dispComProdSrchInd;
	}

	public void setDispComProdSrchInd(String dispComProdSrchInd) {
		this.dispComProdSrchInd = dispComProdSrchInd;
	}

	public String getDivrNum() {
		return divrNum;
	}

	public void setDivrNum(String divrNum) {
		this.divrNum = divrNum;
	}

	public String getMrkToMktInd() {
		return mrkToMktInd;
	}

	public void setMrkToMktInd(String mrkToMktInd) {
		this.mrkToMktInd = mrkToMktInd;
	}

	public String getCtryProdTrade1Cde() {
		return ctryProdTrade1Cde;
	}

	public void setCtryProdTrade1Cde(String ctryProdTrade1Cde) {
		this.ctryProdTrade1Cde = ctryProdTrade1Cde;
	}

	public String getBusStartTm() {
		return busStartTm;
	}

	public void setBusStartTm(String busStartTm) {
		this.busStartTm = busStartTm;
	}

	public String getBusEndTm() {
		return busEndTm;
	}

	public void setBusEndTm(String busEndTm) {
		this.busEndTm = busEndTm;
	}

	public String getProdTopSellRankNum() {
		return prodTopSellRankNum;
	}

	public void setProdTopSellRankNum(String prodTopSellRankNum) {
		this.prodTopSellRankNum = prodTopSellRankNum;
	}

	public String getProdTopPerfmRankNum() {
		return prodTopPerfmRankNum;
	}

	public void setProdTopPerfmRankNum(String prodTopPerfmRankNum) {
		this.prodTopPerfmRankNum = prodTopPerfmRankNum;
	}

	public String getProdTopYieldRankNum() {
		return prodTopYieldRankNum;
	}

	public void setProdTopYieldRankNum(String prodTopYieldRankNum) {
		this.prodTopYieldRankNum = prodTopYieldRankNum;
	}

	public String getCcyInvstCde() {
		return ccyInvstCde;
	}

	public void setCcyInvstCde(String ccyInvstCde) {
		this.ccyInvstCde = ccyInvstCde;
	}

	public String getQtyTypeCde() {
		return qtyTypeCde;
	}

	public void setQtyTypeCde(String qtyTypeCde) {
		this.qtyTypeCde = qtyTypeCde;
	}

	public String getProdLocCde() {
		return prodLocCde;
	}

	public void setProdLocCde(String prodLocCde) {
		this.prodLocCde = prodLocCde;
	}

	public String getProdTaxFreeWrapActStaCde() {
		return prodTaxFreeWrapActStaCde;
	}

	public void setProdTaxFreeWrapActStaCde(String prodTaxFreeWrapActStaCde) {
		this.prodTaxFreeWrapActStaCde = prodTaxFreeWrapActStaCde;
	}

	public String getTrdFirstDt() {
		return trdFirstDt;
	}

	public void setTrdFirstDt(String trdFirstDt) {
		this.trdFirstDt = trdFirstDt;
	}

	public String getSuptRcblCashProdInd() {
		return suptRcblCashProdInd;
	}

	public void setSuptRcblCashProdInd(String suptRcblCashProdInd) {
		this.suptRcblCashProdInd = suptRcblCashProdInd;
	}

	public String getSuptRcblScripProdInd() {
		return suptRcblScripProdInd;
	}

	public void setSuptRcblScripProdInd(String suptRcblScripProdInd) {
		this.suptRcblScripProdInd = suptRcblScripProdInd;
	}

	public String getDcmlPlaceTradeUnitNum() {
		return dcmlPlaceTradeUnitNum;
	}

	public void setDcmlPlaceTradeUnitNum(String dcmlPlaceTradeUnitNum) {
		this.dcmlPlaceTradeUnitNum = dcmlPlaceTradeUnitNum;
	}

	public String getPldgLimitAssocAcctInd() {
		return pldgLimitAssocAcctInd;
	}

	public void setPldgLimitAssocAcctInd(String pldgLimitAssocAcctInd) {
		this.pldgLimitAssocAcctInd = pldgLimitAssocAcctInd;
	}

	public String getAumChrgProdInd() {
		return aumChrgProdInd;
	}

	public void setAumChrgProdInd(String aumChrgProdInd) {
		this.aumChrgProdInd = aumChrgProdInd;
	}

	public String getInvstInitMinAmt() {
		return invstInitMinAmt;
	}

	public void setInvstInitMinAmt(String invstInitMinAmt) {
		this.invstInitMinAmt = invstInitMinAmt;
	}

	public String getPrdInvstTnorNum() {
		return prdInvstTnorNum;
	}

	public void setPrdInvstTnorNum(String prdInvstTnorNum) {
		this.prdInvstTnorNum = prdInvstTnorNum;
	}

	public String getAsetVoltlClassMajrPrntCde() {
		return asetVoltlClassMajrPrntCde;
	}

	public void setAsetVoltlClassMajrPrntCde(String asetVoltlClassMajrPrntCde) {
		this.asetVoltlClassMajrPrntCde = asetVoltlClassMajrPrntCde;
	}

	public ProdTradeCcySeg getProdTradeCcySeg() {
		return prodTradeCcySeg;
	}

	public void setProdTradeCcySeg(ProdTradeCcySeg prodTradeCcySeg) {
		this.prodTradeCcySeg = prodTradeCcySeg;
	}

	public ProdAsetUndlSeg getProdAsetUndlSeg() {
		return prodAsetUndlSeg;
	}

	public void setProdAsetUndlSeg(ProdAsetUndlSeg prodAsetUndlSeg) {
		this.prodAsetUndlSeg = prodAsetUndlSeg;
	}

	public ProdListSeg getProdListSeg() {
		return prodListSeg;
	}

	public void setProdListSeg(ProdListSeg prodListSeg) {
		this.prodListSeg = prodListSeg;
	}

	public List<ProdUserDefExtSeg> getProdUserDefExtSeg() {
		return prodUserDefExtSeg;
	}

	public void setProdUserDefExtSeg(List<ProdUserDefExtSeg> prodUserDefExtSeg) {
		this.prodUserDefExtSeg = prodUserDefExtSeg;
	}

	public List<ProdAsetVoltlClassSeg> getProdAsetVoltlClassSeg() {
		return prodAsetVoltlClassSeg;
	}

	public void setProdAsetVoltlClassSeg(List<ProdAsetVoltlClassSeg> prodAsetVoltlClassSeg) {
		this.prodAsetVoltlClassSeg = prodAsetVoltlClassSeg;
	}

	public ResChanSeg getResChanSeg() {
		return resChanSeg;
	}

	public void setResChanSeg(ResChanSeg resChanSeg) {
		this.resChanSeg = resChanSeg;
	}

	public List<AsetSicAllocSeg> getAsetSicAllocSeg() {
		return asetSicAllocSeg;
	}

	public void setAsetSicAllocSeg(List<AsetSicAllocSeg> asetSicAllocSeg) {
		this.asetSicAllocSeg = asetSicAllocSeg;
	}

	public List<AsetGeoLocAllocSeg> getAsetGeoLocAllocSeg() {
		return asetGeoLocAllocSeg;
	}

	public void setAsetGeoLocAllocSeg(List<AsetGeoLocAllocSeg> asetGeoLocAllocSeg) {
		this.asetGeoLocAllocSeg = asetGeoLocAllocSeg;
	}

}
