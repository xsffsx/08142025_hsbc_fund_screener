package com.dummy.wpc.datadaptor.processor;

import static com.dummy.wpc.datadaptor.util.DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH;
import static com.dummy.wpc.datadaptor.util.DateHelper.DEFAULT_DATE_FORMAT;
import static com.dummy.wpc.datadaptor.util.DateHelper.formatDate2String;
import static com.dummy.wpc.datadaptor.util.DateHelper.parseToDate;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.Eli;
import com.dummy.wpc.batch.xml.EliSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;

public class EliBeanProcessor extends AbstractBeanProcessor<Eli> {

	@Override
	public Eli process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		Eli eli = new Eli();
		// ProdKeySeg
		eli.setProdKeySeg(prodKey);
		// ProdAltNumSeg
		ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
		prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
		prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
		eli.addProdAltNumSeg(prodAltNumSeg_P);
		ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
		prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
		prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
		eli.addProdAltNumSeg(prodAltNumSeg_M);
		// ProdInfoSeg
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		String subtpCde = INCOMING_SUB_TYPE_DEFAULT_VALUE.equals(entity.getProdSubtpCde()) ? EMPTY_STR : entity.getProdSubtpCde();
		prodInfoSeg.setProdSubtpCde(StringUtils.trimToEmpty(subtpCde));
		prodInfoSeg.setProdName(StringUtils.substring(entity.getProdName(), 0, 100));
		prodInfoSeg.setProdPllName(entity.getProdPllName());
		prodInfoSeg.setProdShrtName(StringUtils.trimToEmpty(entity.getShrtName()));
		prodInfoSeg.setProdShrtPllName(entity.getShrtPllName());
		prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(convertProductStatus(entity.getStatCde())));
		prodInfoSeg.setCcyProdCde(StringUtils.trimToEmpty(entity.getCrncyCde()));
		prodInfoSeg.setRiskLvlCde(entity.getRiskLvlCde());
		prodInfoSeg.setProdLnchDt(formatDate2String(parseToDate(entity.getLaunchDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(formatDate2String(parseToDate(entity.getMturDt(), DEFAULT_DATE_FORMAT), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setAllowBuyProdInd(EMPTY_STR);
		// allow buy & sell indicator
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
		prodInfoSeg.setCtryProdTrade1Cde(EMPTY_STR);
		prodInfoSeg.setProdIssueCrosReferDt(formatDate2String(parseToDate(entity.getIssDt(), DATE_SHORT_FORMAT_WITH_NUM_MONTH), DEFAULT_DATE_FORMAT));
		prodInfoSeg.setCcyInvstCde(EMPTY_STR);
		prodInfoSeg.setQtyTypeCde(EMPTY_STR);
		prodInfoSeg.setProdLocCde(EMPTY_STR);
		prodInfoSeg.setSuptRcblCashProdInd(EMPTY_STR);
		prodInfoSeg.setSuptRcblScripProdInd(EMPTY_STR);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(EMPTY_STR);
		prodInfoSeg.setAumChrgProdInd(EMPTY_STR);

		List<String> advcRmk = new ArrayList<String>();
		String getter = null;
		Method method = null;
		for (int i = 1; i < 16; i++) {
			getter = "getAdvcRmk" + i;
			try {
				method = entity.getClass().getMethod(getter);
				Object fieldVal = method.invoke(entity);
				if(fieldVal != null){
					advcRmk.add(String.valueOf(fieldVal));
				}
			} catch (Exception e) {
				logger.error("Exception occured dynamic invoke method: " + "getAdvcRmk" + i);
			}
		}
		prodInfoSeg.setAddInfo(StringUtils.join(advcRmk.toArray(), '\n'));
		// ProdUserDefSeg
		if (StringUtils.equals(entity.getProdTypeCde(), ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.PROD_TYPE_CDE_SN))) {
			ProdUserDefSeg prodUserDef_11 = new ProdUserDefSeg();
			prodUserDef_11.setFieldTypeCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_TYPE_CDE));
			prodUserDef_11.setFieldCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_CDE_PREFIX) + "11");
			ProdUserDefSeg prodUserDef_12 = new ProdUserDefSeg();
			prodUserDef_12.setFieldTypeCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_TYPE_CDE));
			prodUserDef_12.setFieldCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_CDE_PREFIX) + "12");
			ProdUserDefSeg prodUserDef_14 = new ProdUserDefSeg();
			prodUserDef_14.setFieldTypeCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_TYPE_CDE));
			prodUserDef_14.setFieldCde(ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.FIELD_CDE_PREFIX) + "14");
			BigDecimal invstInitMinAmt = entity.getInvstInitMinAmt();
			BigDecimal invstIncrmMinAmt = entity.getInvstIncrmMinAmt();
			if (StringUtils.equals(entity.getCrncyCde(), "JPY")) {
				prodUserDef_11.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstInitMinAmt)));
				prodInfoSeg.addProdUserDefSeg(prodUserDef_11);
				prodUserDef_12.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstIncrmMinAmt)));
				prodInfoSeg.addProdUserDefSeg(prodUserDef_12);
			} else {
				if (invstInitMinAmt != null) {
					prodUserDef_11.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstInitMinAmt.movePointLeft(2))));
					prodInfoSeg.addProdUserDefSeg(prodUserDef_11);
				}
				if (invstIncrmMinAmt != null) {
					prodUserDef_12.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstIncrmMinAmt.movePointLeft(2))));
					prodInfoSeg.addProdUserDefSeg(prodUserDef_12);
				}
			}
			prodUserDef_14.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
			prodInfoSeg.addProdUserDefSeg(prodUserDef_14);
		}
		eli.setProdInfoSeg(prodInfoSeg);
		// EliSeg
		EliSeg eliSeg = new EliSeg();
		String dscntSellPct = DecimalHelper.bigDecimal2String(DecimalHelper.formatDecimalPlace(entity.getOfferPrcAmt(), 4));
		eliSeg.setDscntSellPct(DecimalHelper.trimZero(dscntSellPct));
		eliSeg.setValnDt(entity.getFixDt());
		eli.setEliSeg(eliSeg);
		// RecDtTmSeg
		eli.setRecDtTmSeg(recDtTm);
		return eli;
	}
}
