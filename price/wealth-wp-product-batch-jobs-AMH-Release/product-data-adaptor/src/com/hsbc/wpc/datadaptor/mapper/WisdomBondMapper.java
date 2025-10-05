package com.dummy.wpc.datadaptor.mapper;

import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.DebtInstmSeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class WisdomBondMapper extends AbstractFieldSetMapper {

	@Override
	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), "empty_str");
		String currentDateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);

		DebtInstm bond = new DebtInstm();

		// ProdKeySeg
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(StringUtils.trimToEmpty(fieldSet.readString("CTRY_REC_CDE")));
		prodKey.setGrpMembrRecCde(StringUtils.trimToEmpty(fieldSet.readString("GRP_MEMBR_REC_CDE")));
		prodKey.setProdTypeCde(StringUtils.trimToEmpty(fieldSet.readString("PROD_TYPE_CDE")));
		prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		prodKey.setProdCdeAltClassCde("P");
		bond.setProdKeySeg(prodKey);

		// ProdAltNumSeg
		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
		prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		bond.addProdAltNumSeg(prodAltNumSegP);

		// ProdInfoSeg
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		prodInfoSeg.setProdSubtpCde(fieldSet.readString("PROD_SUBTP_CDE"));
		prodInfoSeg.setProdName(emptyStr);
		prodInfoSeg.setProdPllName(emptyStr);
		prodInfoSeg.setProdShrtName(emptyStr);
		prodInfoSeg.setProdShrtPllName(emptyStr);
		prodInfoSeg.setProdStatCde(emptyStr);
		prodInfoSeg.setCcyProdCde(emptyStr);
		prodInfoSeg.setRiskLvlCde(emptyStr);
		prodInfoSeg.setProdLnchDt(emptyStr);
		prodInfoSeg.setProdMturDt(emptyStr);
		prodInfoSeg.setDivrNum(emptyStr);
		prodInfoSeg.setProdIssueCrosReferDt(emptyStr);

		prodInfoSeg.setAllowBuyProdInd(emptyStr);
		prodInfoSeg.setAllowSellProdInd(emptyStr);
		prodInfoSeg.setAllowBuyUtProdInd(emptyStr);
		prodInfoSeg.setAllowBuyAmtProdInd(emptyStr);
		prodInfoSeg.setAllowSellUtProdInd(emptyStr);
		prodInfoSeg.setAllowSellAmtProdInd(emptyStr);
		prodInfoSeg.setAllowSellMipProdInd(emptyStr);
		prodInfoSeg.setAllowSellMipUtProdInd(emptyStr);
		prodInfoSeg.setAllowSellMipAmtProdInd(emptyStr);
		prodInfoSeg.setAllowSwInProdInd(emptyStr);
		prodInfoSeg.setAllowSwInUtProdInd(emptyStr);
		prodInfoSeg.setAllowSwInAmtProdInd(emptyStr);
		prodInfoSeg.setAllowSwOutProdInd(emptyStr);
		prodInfoSeg.setAllowSwOutUtProdInd(emptyStr);
		prodInfoSeg.setAllowSwOutAmtProdInd(emptyStr);
		prodInfoSeg.setCtryProdTrade1Cde(emptyStr);
		prodInfoSeg.setCcyInvstCde(prodInfoSeg.getCcyProdCde());
		prodInfoSeg.setQtyTypeCde(emptyStr);
		prodInfoSeg.setProdLocCde(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
		prodInfoSeg.setPldgLimitAssocAcctInd(emptyStr);
		prodInfoSeg.setAumChrgProdInd(emptyStr);

		ProdTradeCcySeg prodTradeCcy = new ProdTradeCcySeg();
		prodTradeCcy.setCcyProdTradeCde(prodInfoSeg.getCcyProdCde());

		prodInfoSeg.setProdTradeCcySeg(new ProdTradeCcySeg[]{
				prodTradeCcy
		});
		prodInfoSeg.setProdTopSellRankNum(fieldSet.readString("PROD_TOP_SELL_RANK_NUM"));
		bond.setProdInfoSeg(prodInfoSeg);

		// DebtInstmSeg
		DebtInstmSeg debtInstm = new DebtInstmSeg();
		bond.setDebtInstmSeg(debtInstm);

		// RecDtTmSeg
		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		recDtTmSeg.setRecCreatDtTm(currentDateTime);
		recDtTmSeg.setRecUpdtDtTm(currentDateTime);
		recDtTmSeg.setProdStatUpdtDtTm(currentDateTime);
		recDtTmSeg.setTimeZone(GMTStr);
		bond.setRecDtTmSeg(recDtTmSeg);

		// return bond;
		return bond;
	}

}
