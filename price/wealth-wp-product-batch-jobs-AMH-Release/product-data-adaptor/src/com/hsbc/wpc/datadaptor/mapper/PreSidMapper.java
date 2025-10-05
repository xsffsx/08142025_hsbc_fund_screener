package com.dummy.wpc.datadaptor.mapper;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.Sid;
import com.dummy.wpc.batch.xml.SidSeg;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class PreSidMapper extends AbstractFieldSetMapper {

	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), "empty_str");

		Sid sid = new Sid();
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
		prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
		prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_type_cde"));
		prodKey.setProdCde(fieldSet.readString("PROD_CDE"));
		sid.setProdKeySeg(prodKey);

		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_p"));
		prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		sid.addProdAltNumSeg(prodAltNumSegP);
		
		ProdAltNumSeg prodAltNumSegM = new ProdAltNumSeg();
		prodAltNumSegM.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_m"));
		prodAltNumSegM.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
        sid.addProdAltNumSeg(prodAltNumSegM);
		
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		prodInfoSeg.setProdSubtpCde(fieldSet.readString("PROD_SUBTP_CDE"));
		prodInfoSeg.setProdName(fieldSet.readString("PROD_NAME"));
		prodInfoSeg.setProdShrtName(StringUtils.substring(fieldSet.readString("PROD_NAME"), 0, 30));
		prodInfoSeg.setProdStatCde(emptyStr);
		prodInfoSeg.setCcyProdCde(fieldSet.readString("CRNCY_CDE"));
		prodInfoSeg.setRiskLvlCde(fieldSet.readString("RISK_LVL_CDE"));
		prodInfoSeg.setPrdProdCde(fieldSet.readString("PRD_PROD_CDE"));
		prodInfoSeg.setPrdProdNum(DecimalHelper.trimZero(fieldSet.readString("PRD_PROD_NUM")));
		prodInfoSeg.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setMktInvstCde(fieldSet.readString("MKT_INVST_CDE"));
		prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_INIT_MIN_AMT")));

		// add mandatory fields
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
		prodInfoSeg.setDivrNum(emptyStr);
		prodInfoSeg.setCtryProdTrade1Cde(emptyStr);
		prodInfoSeg.setCcyInvstCde(fieldSet.readString("CRNCY_CDE"));
		prodInfoSeg.setQtyTypeCde(emptyStr);
		prodInfoSeg.setProdLocCde(emptyStr);
		// prodInfoSeg.setRcblTrdInd(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
		prodInfoSeg.setAumChrgProdInd(emptyStr);

		String asetUndlCde = fieldSet.readString("UDLY_ASET_CDE_1");
		if (StringUtils.isNotBlank(asetUndlCde)) {
			ProdAsetUndlSeg prodAsetUndlSeg = new ProdAsetUndlSeg();
			prodAsetUndlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(getJobCode(), "aset_undl_cde_seq_num_1"));
			prodAsetUndlSeg.setAsetUndlCde(asetUndlCde);
			prodInfoSeg.addProdAsetUndlSeg(prodAsetUndlSeg);
		}

		List<String> names_list = Arrays.asList(fieldSet.getNames());
		for (int i = 1; i <= 15; i++) {
			int index = names_list.indexOf(Constants.USR_DEF_FLD_PREFIX + i);
			if (index >= 0) {
				ProdUserDefSeg prodUserDef = new ProdUserDefSeg();
				prodUserDef.setFieldTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "field_type_cde"));
				String fieldCdePrefix = ConstantsPropertiesHelper.getValue(getJobCode(), "field_cde_prefix");
				String fieldCdeSuffix = String.valueOf(100 + i).substring(1);
				prodUserDef.setFieldCde(fieldCdePrefix + fieldCdeSuffix);
				prodUserDef.setFieldValueText(fieldSet.readString(Constants.USR_DEF_FLD_PREFIX + i));
				prodInfoSeg.addProdUserDefSeg(prodUserDef);
			}
		}
		prodInfoSeg.setAsetText(fieldSet.readString("UDLY_ASET"));
		sid.setProdInfoSeg(prodInfoSeg);

		SidSeg sidSeg = new SidSeg();
		sidSeg.setProdConvCde(fieldSet.readString("CONV_CDE"));
		sidSeg.setMktStartDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRD_MKT_START_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		sidSeg.setMktEndDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRD_MKT_END_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		sidSeg.setYieldAnnlMinPct(DecimalHelper.trimZero(fieldSet.readString("YLD_ANNL_MIN_PCT")));
		sidSeg.setYieldAnnlPotenPct(DecimalHelper.trimZero(fieldSet.readString("YLD_ANNL_POTEN_PCT")));
		
		sidSeg.setOfferTypeCde(fieldSet.readString("OFFER_TYPE_CDE"));
		sidSeg.setCustSellQtaNum(DecimalHelper.trimZero(fieldSet.readString("CUST_QUOT_SELL_CNT")));
		sidSeg.setRuleQtaAltmtCde(fieldSet.readString("QUOT_RULE_TYPE_CDE"));
		sidSeg.setCptlProtcPct(DecimalHelper.trimZero(fieldSet.readString("CPT_PRT_PCT")));
		sidSeg.setLnchProdInd(fieldSet.readString("LAUNCH_STUS"));

		sid.setSidSeg(sidSeg);

		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);

		sid.setRecDtTmSeg(recDtTmSeg);

		return sid;
	}

}
