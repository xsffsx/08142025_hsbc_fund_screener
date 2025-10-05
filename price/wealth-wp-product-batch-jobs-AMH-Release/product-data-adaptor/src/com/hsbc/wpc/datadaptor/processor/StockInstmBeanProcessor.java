package com.dummy.wpc.datadaptor.processor;

import static com.dummy.wpc.datadaptor.util.DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH;
import static com.dummy.wpc.datadaptor.util.DateHelper.DEFAULT_DATE_FORMAT;
import static com.dummy.wpc.datadaptor.util.DateHelper.formatDate2String;
import static com.dummy.wpc.datadaptor.util.DateHelper.parseToDate;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.ResChanSeg;
import com.dummy.wpc.batch.xml.ResChannelDtl;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.batch.xml.StockInstmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.ProductTypeEnum;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class StockInstmBeanProcessor extends AbstractBeanProcessor<StockInstm> {
	
	@Override
	public StockInstm process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		String prodTypeCde = getInternalProdTypeCde(entity.getProdTypeCde());
		StockInstm stockInstm = null;
		switch (ProductTypeEnum.retrieveProdType(prodTypeCde)) {
			case SEC:
				stockInstm = convertSec(entity, prodKey, recDtTm);
				break;
			case WRTS:
				stockInstm = convertSec(entity, prodKey, recDtTm);
				break;
			default:
				break;
		}
		return stockInstm;
	}

	private StockInstm convertSec(final ProductEntity entity, final ProdKeySeg prodKey, final RecDtTmSeg recDtTm) {
		StockInstm sec = new StockInstm();
		// ProdKeySeg
		sec.setProdKeySeg(prodKey);
		// ProdAltNumSeg
		ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
		prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
		prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
		sec.addProdAltNumSeg(prodAltNumSeg_P);
		ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
		prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
		// if MKTCde is not empty set it as M code , else set as P code
		if (StringUtils.isNotEmpty(entity.getMKTCde())) {
			prodAltNumSeg_M.setProdAltNum(entity.getMKTCde());
		} else {
			prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
		}
		sec.addProdAltNumSeg(prodAltNumSeg_M);
		String isinCode = entity.getISINCde();
		if (StringUtils.isNotBlank(isinCode)) {
			ProdAltNumSeg prodAltNumSeg_I = new ProdAltNumSeg();
			prodAltNumSeg_I.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_I));
			prodAltNumSeg_I.setProdAltNum(isinCode);
			sec.addProdAltNumSeg(prodAltNumSeg_I);
		}
		String gisinCode = entity.getGISINCde();
		if (StringUtils.isNotBlank(gisinCode)) {
			ProdAltNumSeg prodAltNumSeg_G = new ProdAltNumSeg();
			prodAltNumSeg_G.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_G));
			prodAltNumSeg_G.setProdAltNum(gisinCode);
			sec.addProdAltNumSeg(prodAltNumSeg_G);
		}
		String tisinCode = entity.getTISINCde();
		if (StringUtils.isNotBlank(tisinCode)) {
			ProdAltNumSeg prodAltNumSeg_X = new ProdAltNumSeg();
			prodAltNumSeg_X.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_X));
			prodAltNumSeg_X.setProdAltNum(tisinCode);
			sec.addProdAltNumSeg(prodAltNumSeg_X);
		}
		// RIC Code for A-Share
		String ricCode = entity.getRICCde();
		if (StringUtils.isNotBlank(ricCode)) {
			// For AMH StockConnect, if the ric code matches *.SH , *.ZK， replace .SH as .SS replace .ZK as .SZ
			if (ricCode.matches("[0-9]{6}\\.SH") || ricCode.matches("[0-9]{6}\\.ZK")) {
				// replace and map it as R code
				String ricCodeNew = null;
				if (ricCode.matches("[0-9]{6}\\.SH")) {
					ricCodeNew = ricCode.replace("SH", "SS");
				} else if (ricCode.matches("[0-9]{6}\\.ZK")) {
					ricCodeNew = ricCode.replace("ZK", "SZ");
				}
				ProdAltNumSeg prodAltNumSeg_R = new ProdAltNumSeg();
				prodAltNumSeg_R.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_R));
				prodAltNumSeg_R.setProdAltNum(ricCodeNew);
				sec.addProdAltNumSeg(prodAltNumSeg_R);
				// duplicate one more set as C code using incoming Ric code
				ProdAltNumSeg prodAltNumSeg_C = new ProdAltNumSeg();
				prodAltNumSeg_C.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_C));
				prodAltNumSeg_C.setProdAltNum(ricCode);
				sec.addProdAltNumSeg(prodAltNumSeg_C);
			} else {
				// Directly map the ric code as R code
				ProdAltNumSeg prodAltNumSeg_R = new ProdAltNumSeg();
				prodAltNumSeg_R.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_R));
				prodAltNumSeg_R.setProdAltNum(ricCode);
				sec.addProdAltNumSeg(prodAltNumSeg_R);
			}
		}
		// ProdInfoSeg
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		String subtpCde = entity.getProdSubtpCde();
		subtpCde = INCOMING_SUB_TYPE_DEFAULT_VALUE.equals(subtpCde) ? EMPTY_STR : subtpCde;
		prodInfoSeg.setProdSubtpCde(StringUtils.trimToEmpty(subtpCde));
		prodInfoSeg.setProdName(StringUtils.substring(entity.getProdName(), 0, 100));
		prodInfoSeg.setProdPllName(entity.getProdPllName());
		prodInfoSeg.setProdShrtName(StringUtils.trimToEmpty(entity.getShrtName()));
		prodInfoSeg.setProdShrtPllName(entity.getShrtPllName());
		prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(convertProductStatus(entity.getStatCde())));
		prodInfoSeg.setCcyProdCde(StringUtils.trimToEmpty(entity.getCrncyCde()));
		prodInfoSeg.setRiskLvlCde(entity.getRiskLvlCde());
		prodInfoSeg.setProdLnchDt(formatDate2String(parseToDate(entity.getLaunchDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(formatDate2String(parseToDate(entity.getExprDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		// allow buy & sell indicator
		prodInfoSeg.setAllowBuyProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellProdInd(EMPTY_STR);
		prodInfoSeg.setAllowBuyUtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowBuyAmtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellUtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellAmtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellMipProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellMipUtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSellMipAmtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwInProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwInUtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwInAmtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwOutProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwOutUtProdInd(EMPTY_STR);
		prodInfoSeg.setAllowSwOutAmtProdInd(EMPTY_STR);
		prodInfoSeg.setDivrNum(StringUtils.trimToEmpty(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()))));
		// change country trade code depends on exchange code( config in constants.properties )
		prodInfoSeg.setCtryProdTrade1Cde(getCtryTradeCdeByExchange(entity.getExchgCde()));
		setSllNameForHK(prodInfoSeg);
		prodInfoSeg.setProdIssueCrosReferDt(formatDate2String(parseToDate(entity.getIssDt(), DATE_SHORT_FORMAT_WITH_NUM_MONTH), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setCcyInvstCde(prodInfoSeg.getCcyProdCde());
		prodInfoSeg.setQtyTypeCde(EMPTY_STR);
		prodInfoSeg.setProdLocCde(EMPTY_STR);
		prodInfoSeg.setSuptRcblCashProdInd(EMPTY_STR);
		prodInfoSeg.setSuptRcblScripProdInd(EMPTY_STR);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(EMPTY_STR);
		prodInfoSeg.setAumChrgProdInd(EMPTY_STR);
		// ProdAsetUndlSeg
		ProdAsetUndlSeg prodAsetUndlSeg = new ProdAsetUndlSeg();
		prodAsetUndlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.ASET_UNDL_CDE_SEQ_NUM_1));
		prodAsetUndlSeg.setAsetUndlCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.ASET_UNDL_CDE));
		prodInfoSeg.addProdAsetUndlSeg(prodAsetUndlSeg);
		// ResChanSeg
		String restrictChannelCode = ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.RES_CHANNEL_CDE);
		if (StringUtils.isNotBlank(restrictChannelCode)) {
			ResChanSeg resChan = new ResChanSeg();
			for (String resChnCde : restrictChannelCode.split("\\,")) {
				ResChannelDtl resChannelDtl = new ResChannelDtl();
				resChannelDtl.setResChannelCde(resChnCde);
				resChan.addResChannelDtl(resChannelDtl);
			}
			prodInfoSeg.setResChanSeg(resChan);
		}
		ProdTradeCcySeg prodTradeCcy = new ProdTradeCcySeg();
		prodTradeCcy.setCcyProdTradeCde(prodInfoSeg.getCcyProdCde());
		prodInfoSeg.setProdTradeCcySeg(new ProdTradeCcySeg[] { prodTradeCcy });
		sec.setProdInfoSeg(prodInfoSeg);
		// StockInstmSeg
		StockInstmSeg stockInstm = new StockInstmSeg();
		stockInstm.setMrgnTrdInd(entity.getMrgnTrdInd());
		stockInstm.setMrgnSecODPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getMrgnSecOvdftPct())));
		stockInstm.setSuptAuctnTrdInd(entity.getAuctnTrdInd());
		stockInstm.setStopLossMinPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getStopLossMinPct())));
		stockInstm.setStopLossMaxPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getStopLossMaxPct())));
		stockInstm.setSprdStopLossMinCnt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getSprdSplsMinCnt())));
		stockInstm.setSprdStopLossMaxCnt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getSprdSplsMaxCnt())));
		stockInstm.setProdBodLotQtyCnt(StringUtils.trimToEmpty(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getLotSizeNum()))));
		stockInstm.setMktProdTrdCde(StringUtils.trimToEmpty(entity.getExchgCde()));
		stockInstm.setBodLotProdInd(EMPTY_STR);
		stockInstm.setTrdLimitInd(EMPTY_STR);
		stockInstm.setPsblProdBorwInd(EMPTY_STR);
		stockInstm.setAllowProdLendInd(EMPTY_STR);
		stockInstm.setPoolProdInd(EMPTY_STR);
		stockInstm.setScripOnlyProdInd(EMPTY_STR);
		stockInstm.setSuptAtmcTrdInd(EMPTY_STR);
		stockInstm.setSuptNetSetlInd(EMPTY_STR);
		stockInstm.setSuptStopLossOrdInd(EMPTY_STR);
		stockInstm.setSuptWinWinOrdInd(EMPTY_STR);
		stockInstm.setSuptAllIn1OrdInd(EMPTY_STR);
		stockInstm.setSuptProdSpltInd(EMPTY_STR);
		stockInstm.setExclLimitCalcInd(EMPTY_STR);
		stockInstm.setProdMktStatChngLaDt(EMPTY_STR);
		stockInstm.setPrcVarCde(EMPTY_STR);
		stockInstm.setPrcVarMinAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getSprdPrcAmt())));
		sec.setStockInstmSeg(stockInstm);
		// RecDtTmSeg
		sec.setRecDtTmSeg(recDtTm);
		return sec;
	}
}
