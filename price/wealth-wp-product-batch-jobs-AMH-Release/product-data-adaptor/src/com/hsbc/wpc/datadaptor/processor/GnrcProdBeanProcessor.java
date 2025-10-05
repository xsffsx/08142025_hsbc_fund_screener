package com.dummy.wpc.datadaptor.processor;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import static com.dummy.wpc.datadaptor.util.DateHelper.*;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class GnrcProdBeanProcessor extends AbstractBeanProcessor<GnrcProd> {

	@Override
	public GnrcProd process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		GnrcProd gnrcProd = new GnrcProd();
		// ProdKeySeg
		gnrcProd.setProdKeySeg(prodKey);
		// ProdAltNumSeg
		ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
		prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
		prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
		gnrcProd.addProdAltNumSeg(prodAltNumSeg_P);
		ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
		prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
		prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
		gnrcProd.addProdAltNumSeg(prodAltNumSeg_M);
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
		prodInfoSeg.setDivrNum(StringUtils.trimToEmpty(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()))));
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
		prodInfoSeg.setQtyTypeCde(EMPTY_STR);
		prodInfoSeg.setProdLocCde(EMPTY_STR);
		prodInfoSeg.setSuptRcblCashProdInd(EMPTY_STR);
		prodInfoSeg.setSuptRcblScripProdInd(EMPTY_STR);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(EMPTY_STR);
		prodInfoSeg.setPldgLimitAssocAcctInd(EMPTY_STR);
		prodInfoSeg.setAumChrgProdInd(EMPTY_STR);
		gnrcProd.setProdInfoSeg(prodInfoSeg);
		// RecDtTmSeg
		gnrcProd.setRecDtTmSeg(recDtTm);
		return gnrcProd;
	}

}
