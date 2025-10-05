package com.dummy.wpc.datadaptor.processor;

import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.SUB_DEBT_IND;
import static com.dummy.wpc.datadaptor.util.DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH;
import static com.dummy.wpc.datadaptor.util.DateHelper.DEFAULT_DATE_FORMAT;
import static com.dummy.wpc.datadaptor.util.DateHelper.formatDate2String;
import static com.dummy.wpc.datadaptor.util.DateHelper.parseToDate;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.DebtInstmSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class DebtInstmBeanProcessor extends AbstractBeanProcessor<DebtInstm> {

	@Override
	public DebtInstm process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		DebtInstm bond = new DebtInstm();
		// ProdKeySeg
		bond.setProdKeySeg(prodKey);
		// ProdAltNumSeg
		ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
		prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, PROD_CDE_ALT_CLASS_CDE_P));
		prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
		bond.addProdAltNumSeg(prodAltNumSeg_P);
		ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
		prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, PROD_CDE_ALT_CLASS_CDE_M));
		prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
		bond.addProdAltNumSeg(prodAltNumSeg_M);
		// ProdInfoSeg
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		String subtpCde = INCOMING_SUB_TYPE_DEFAULT_VALUE.equals(entity.getProdSubtpCde()) ? EMPTY_STR : entity.getProdSubtpCde();
		prodInfoSeg.setProdSubtpCde(StringUtils.trimToEmpty(subtpCde));
		prodInfoSeg.setProdName(StringUtils.substring(entity.getProdName(), 0, 100));
		prodInfoSeg.setProdPllName(StringUtils.trimToEmpty(entity.getProdPllName()));
		prodInfoSeg.setProdShrtName(StringUtils.trimToEmpty(entity.getShrtName()));
		prodInfoSeg.setProdShrtPllName(StringUtils.trimToEmpty(entity.getShrtPllName()));
		prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(convertProductStatus(entity.getStatCde())));
		prodInfoSeg.setCcyProdCde(StringUtils.trimToEmpty(entity.getCrncyCde()));
		prodInfoSeg.setRiskLvlCde(StringUtils.trimToEmpty(entity.getRiskLvlCde()));
		prodInfoSeg.setProdLnchDt(formatDate2String(parseToDate(entity.getLaunchDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(formatDate2String(parseToDate(entity.getExprDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setDivrNum(StringUtils.trimToEmpty(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()))));
		prodInfoSeg.setProdIssueCrosReferDt(formatDate2String(parseToDate(entity.getIssDt(), DATE_SHORT_FORMAT_WITH_NUM_MONTH), DEFAULT_DATE_FORMAT));
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
		
		prodInfoSeg.setCtryProdTrade1Cde(EMPTY_STR);
		prodInfoSeg.setCcyInvstCde(prodInfoSeg.getCcyProdCde());
		prodInfoSeg.setQtyTypeCde(StringUtils.trimToEmpty(entity.getQtyType()));
		prodInfoSeg.setProdLocCde(StringUtils.trimToEmpty(entity.getProdLocCode()));
		prodInfoSeg.setSuptRcblCashProdInd(EMPTY_STR);
		prodInfoSeg.setSuptRcblScripProdInd(EMPTY_STR);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(EMPTY_STR);
		prodInfoSeg.setPldgLimitAssocAcctInd(EMPTY_STR);
		prodInfoSeg.setAumChrgProdInd(EMPTY_STR);
		ProdTradeCcySeg prodTradeCcy = new ProdTradeCcySeg();
		prodTradeCcy.setCcyProdTradeCde(prodInfoSeg.getCcyProdCde());
		prodInfoSeg.setProdTradeCcySeg(new ProdTradeCcySeg[] { prodTradeCcy });
		bond.setProdInfoSeg(prodInfoSeg);
		DebtInstmSeg debtInstm = new DebtInstmSeg();
		debtInstm.setPdcyCoupnPymtCd(StringUtils.trimToEmpty(entity.getCpnFreq()));
		debtInstm.setCoupnAnnlRt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getCpnRte())));
		debtInstm.setSubDebtInd(ConstantsPropertiesHelper.getValue(jobCode, SUB_DEBT_IND));
		bond.setDebtInstmSeg(debtInstm);
		// RecDtTmSeg
		bond.setRecDtTmSeg(recDtTm);
		return bond;
	}
}
