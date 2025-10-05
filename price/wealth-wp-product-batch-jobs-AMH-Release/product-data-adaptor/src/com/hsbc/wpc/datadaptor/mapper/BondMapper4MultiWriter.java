package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.CreditRtingSeg;
import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.DebtInstmSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdListSeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.YieldHistSeg;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class BondMapper4MultiWriter extends AbstractFieldSetMapper {

	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), "empty_str");

		MultiWriterObj arr = new MultiWriterObj();
		DebtInstm bond = new DebtInstm();
		arr.addObj(bond);

		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(fieldSet.readString("CTRY_CDE"));
		prodKey.setGrpMembrRecCde(fieldSet.readString("ORGN_CDE"));
		prodKey.setProdTypeCde(fieldSet.readString("PROD_TYPE_CDE"));
		prodKey.setProdCde(fieldSet.readString("PROD_CDE"));
		bond.setProdKeySeg(prodKey);

		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_cde_alt_class_cde_p"));
		prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		bond.addProdAltNumSeg(prodAltNumSegP);
		
		ProdAltNumSeg prodAltNumSegM = new ProdAltNumSeg();
		prodAltNumSegM.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_m"));
		prodAltNumSegM.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		bond.addProdAltNumSeg(prodAltNumSegM);

		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		prodInfoSeg.setProdSubtpCde(fieldSet.readString("PROD_SUBTP_CDE"));
		prodInfoSeg.setProdName(fieldSet.readString("PROD_NAME"));
		prodInfoSeg.setProdPllName(fieldSet.readString("PROD_PLL_NAME"));
		prodInfoSeg.setProdSllName(fieldSet.readString("PROD_SLL_NAME"));
		prodInfoSeg.setProdShrtName(fieldSet.readString("SHRT_NAME"));
		prodInfoSeg.setProdShrtPllName(fieldSet.readString("SHRT_PLL_NAME"));
		prodInfoSeg.setProdShrtSllName(fieldSet.readString("SHRT_SLL_NAME"));
		prodInfoSeg.setProdDesc(fieldSet.readString("PROD_DESC"));
		prodInfoSeg.setProdPllDesc(fieldSet.readString("PROD_PLL_DESC"));
		prodInfoSeg.setProdSllDesc(fieldSet.readString("PROD_SLL_DESC"));
		prodInfoSeg.setAsetClassCde(fieldSet.readString("ASET_CLASS_CDE_1"));
		prodInfoSeg.setProdStatCde(fieldSet.readString("STAT_CDE"));
		prodInfoSeg.setCcyProdCde(fieldSet.readString("CRNCY_CDE"));
		prodInfoSeg.setRiskLvlCde(fieldSet.readString("RISK_LVL_CDE"));
		prodInfoSeg.setPrdProdCde(fieldSet.readString("PRD_PROD_CDE"));
		prodInfoSeg.setPrdProdNum(DecimalHelper.trimZero(fieldSet.readString("PRD_PROD_NUM")));
		prodInfoSeg.setTermRemainDayCnt(DecimalHelper.trimZero(fieldSet.readString("TENOR_DAY_NUM")));
		prodInfoSeg.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setMktInvstCde(fieldSet.readString("MKT_INVST_CDE"));
		prodInfoSeg.setSectInvstCde(fieldSet.readString("SECT_CDE"));

		prodInfoSeg.setAllowBuyProdInd(fieldSet.readString("ALOW_BUY_IND"));
		prodInfoSeg.setAllowSellProdInd(fieldSet.readString("ALOW_SELL_IND"));
		prodInfoSeg.setAllowBuyUtProdInd(fieldSet.readString("ALOW_BUY_UNIT_IND"));
		prodInfoSeg.setAllowBuyAmtProdInd(fieldSet.readString("ALOW_BUY_AMT_IND"));
		prodInfoSeg.setAllowSellUtProdInd(fieldSet.readString("ALOW_SELL_UNIT_IND"));
		prodInfoSeg.setAllowSellAmtProdInd(fieldSet.readString("ALOW_SELL_AMT_IND"));
		prodInfoSeg.setAllowSellMipProdInd(fieldSet.readString("ALOW_MIP_IND"));
		prodInfoSeg.setAllowSellMipUtProdInd(fieldSet.readString("ALOW_MIP_UNIT"));
		prodInfoSeg.setAllowSellMipAmtProdInd(fieldSet.readString("ALOW_MIP_AMT"));
		prodInfoSeg.setAllowSwInProdInd(fieldSet.readString("ALOW_SI_IND"));
		prodInfoSeg.setAllowSwInUtProdInd(fieldSet.readString("ALOW_SI_UNIT"));
		prodInfoSeg.setAllowSwInAmtProdInd(fieldSet.readString("ALOW_SI_AMT"));
		prodInfoSeg.setAllowSwOutProdInd(fieldSet.readString("ALOW_SO_IND"));
		prodInfoSeg.setAllowSwOutUtProdInd(fieldSet.readString("ALOW_SO_UNIT"));
		prodInfoSeg.setAllowSwOutAmtProdInd(fieldSet.readString("ALOW_SO_AMT"));
		prodInfoSeg.setIncmCharProdInd(fieldSet.readString("INCOME_IND"));
		prodInfoSeg.setCptlGurntProdInd(fieldSet.readString("CAPITAL_GURN_IND"));
		prodInfoSeg.setYieldEnhnProdInd(fieldSet.readString("YLD_EHANCE_IND"));
		prodInfoSeg.setGrwthCharProdInd(fieldSet.readString("GROWTH_IND"));
		prodInfoSeg.setPrtyProdSrchRsultNum(DecimalHelper.trimZero(fieldSet.readString("SRCH_PRTY_NUM")));
		prodInfoSeg.setAvailMktInfoInd(fieldSet.readString("MKT_INFO_IND"));
		prodInfoSeg.setPrdRtrnAvgNum(DecimalHelper.trimZero(fieldSet.readString("AVG_PRD_RTN")));
		prodInfoSeg.setRtrnVoltlAvgPct(DecimalHelper.trimZero(fieldSet.readString("AVE_VOLAT")));
		prodInfoSeg.setDmyProdSubtpRecInd(fieldSet.readString("SUB_TYPE_IND"));
		prodInfoSeg.setDispComProdSrchInd(fieldSet.readString("CMN_SRCH_IND"));
		prodInfoSeg.setDivrNum(DecimalHelper.trimZero(fieldSet.readString("PRC_DIVR_NUM")));
		prodInfoSeg.setMrkToMktInd(fieldSet.readString("MK_TO_MKT_IND"));
		prodInfoSeg.setCtryProdTrade1Cde(emptyStr);
		prodInfoSeg.setProdIssueCrosReferDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("INT_ISS_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setCcyInvstCde(fieldSet.readString("INVST_INIT_MIN_CCY"));
		prodInfoSeg.setQtyTypeCde(emptyStr);
		prodInfoSeg.setProdLocCde(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
		prodInfoSeg.setLoanProdOdMrgnPct(DecimalHelper.trimZero(fieldSet.readString("OVDFT_SEC_PCT")));
		prodInfoSeg.setAumChrgProdInd(emptyStr);
		prodInfoSeg.setAddInfo(fieldSet.readString("ADD_INFO"));
		prodInfoSeg.setAddPLLInfo(fieldSet.readString("ADD_PLL_INFO"));
		prodInfoSeg.setAddSLLInfo(fieldSet.readString("ADD_SLL_INFO"));
		prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_INIT_MIN_AMT")));

		for (int i = 1; i <= 20; i++) {
			String ccyProdTradeCde = fieldSet.readString("CRNCY_TRADE_CDE_" + i);
			if (StringUtils.isNotEmpty(ccyProdTradeCde)) {
				ProdTradeCcySeg prodTradeCcy = new ProdTradeCcySeg();
				prodTradeCcy.setCcyProdTradeCde(ccyProdTradeCde);
				prodInfoSeg.addProdTradeCcySeg(prodTradeCcy);
			}
		}

		for (int i = 1; i <= 3; i++) {
			String asetUndlCde = fieldSet.readString("UDLY_ASET_CDE_" + i);
			if (StringUtils.isNotEmpty(asetUndlCde)) {
				ProdAsetUndlSeg prodAsetUndl = new ProdAsetUndlSeg();
				prodAsetUndl.setAsetUndlCdeSeqNum("" + i);
				prodAsetUndl.setAsetUndlCde(asetUndlCde);
				prodInfoSeg.addProdAsetUndlSeg(prodAsetUndl);
			}
		}

		for (int i = 1; i <= 3; i++) {
			String slst_ind = fieldSet.readString("SLST_" + i + "_IND");
			if ("Y".equals(slst_ind)) {
				ProdListSeg prodList = new ProdListSeg();
				String prodListTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(), "prod_list_type_cde");
				prodList.setProdListTypeCde(prodListTypeCde);
				prodList.setProdListCde(prodListTypeCde + "_" + i);
				prodList.setProdEffSlstDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("SLST_EFF_" + i + "_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
				prodInfoSeg.addProdListSeg(prodList);
			}
		}

		for (int i = 1; i <= 15; i++) {
			ProdUserDefSeg prodUserDef = new ProdUserDefSeg();
			prodUserDef.setFieldTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "field_type_cde"));
			String fieldCdePrefix = ConstantsPropertiesHelper.getValue(getJobCode(), "field_cde_prefix");
			String fieldCdeSuffix = String.valueOf(100 + i).substring(1);
			prodUserDef.setFieldCde(fieldCdePrefix + fieldCdeSuffix);
			prodUserDef.setFieldValueText(fieldSet.readString(Constants.USR_DEF_FLD_PREFIX + i));
			prodInfoSeg.addProdUserDefSeg(prodUserDef);
		}

		bond.setProdInfoSeg(prodInfoSeg);

		DebtInstmSeg bondSeg = new DebtInstmSeg();
		bondSeg.setIsrBndNme(fieldSet.readString("ISS_CDE"));
		bondSeg.setIssueNum(fieldSet.readString("ISS_NUM"));
		bondSeg.setProdIssDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("ISS_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		bondSeg.setPdcyCoupnPymtCd(fieldSet.readString("FREQ_COUPN_CDE"));
		bondSeg.setCoupnAnnlRt(DecimalHelper.trimZero(fieldSet.readString("COUPN_ANNL_PCT")));
		bondSeg.setCoupnExtInstmRt(DecimalHelper.trimZero(fieldSet.readString("EXT_COUPN_ANNL_TXT")));
		bondSeg.setPymtCoupnNextDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PAY_NEXT_COUPN_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		bondSeg.setFlexMatOptInd(fieldSet.readString("EXBL_CLBL_IND"));
		bondSeg.setIntIndAccrAmt(DecimalHelper.trimZero(fieldSet.readString("INT_IND_ACCR_AMT")));
		bondSeg.setInvstIncMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_ICT_MIN_AMT")));
		bondSeg.setProdBodLotQtyCnt(DecimalHelper.trimZero(fieldSet.readString("LOT_SIZE_NUM")));
		bondSeg.setMturExtDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_EXT_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		bondSeg.setSubDebtInd(fieldSet.readString("SUB_ORDNT"));
		bondSeg.setYieldOfferText(fieldSet.readString("OFFER_YLD_TEXT"));
		bondSeg.setCoupnAnnlText(fieldSet.readString("COUPN_ANNL_PCT_TXT"));

		for (int i = 1; i <= 3; i++) {
			String creditRtingCde = fieldSet.readString("CRED_RTNG_" + i + "_CDE");
			if (StringUtils.isNotEmpty(creditRtingCde)) {
				CreditRtingSeg creditRtingSeg = new CreditRtingSeg();
				creditRtingSeg.setCreditRtingAgcyCde(ConstantsPropertiesHelper.getValue(getJobCode(), "bond_credit_rtng_agcy_cde_" + i));
				creditRtingSeg.setCreditRtingCde(creditRtingCde);
				bondSeg.addCreditRtingSeg(creditRtingSeg);
			}
		}

		YieldHistSeg yieldHistSeg = new YieldHistSeg();
		yieldHistSeg.setYieldTypeCde(emptyStr);
		yieldHistSeg.setYieldDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		yieldHistSeg.setYieldEffDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		yieldHistSeg.setYieldBidPct(DecimalHelper.trimZero(fieldSet.readString("BID_YLD_TEXT")));
		yieldHistSeg.setYieldToCallBidPct(DecimalHelper.trimZero(fieldSet.readString("BID_YTC_TEXT")));
		yieldHistSeg.setYieldToMturBidPct(DecimalHelper.trimZero(fieldSet.readString("BID_YTM_TEXT")));
		yieldHistSeg.setYieldOfferPct(DecimalHelper.trimZero(fieldSet.readString("OFFER_YLD")));
		yieldHistSeg.setYieldToCallOfferPct(DecimalHelper.trimZero(fieldSet.readString("OFFER_YTC_TXT")));
		yieldHistSeg.setYieldToMturOfferText(fieldSet.readString("OFFER_YTM_TXT"));
		// when, yield effective not allow  and  any percentage having value, then will set this record
        if (StringUtils.isNotBlank(yieldHistSeg.getYieldEffDt())
            && (StringUtils.isNotBlank(yieldHistSeg.getYieldBidClosePct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldBidPct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldOfferClosePct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldOfferPct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldToCallBidPct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldToCallOfferPct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldToMturBidPct())
                || StringUtils.isNotBlank(yieldHistSeg.getYieldToMturOfferText()))) {
            bondSeg.addYieldHistSeg(yieldHistSeg);
        }

		bond.setDebtInstmSeg(bondSeg);

		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setProdStatUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);
		bond.setRecDtTmSeg(recDtTmSeg);

		// RecDtTmSeg for ProdPrcSeg use.
		RecDtTmSeg recDtTm = new RecDtTmSeg();
		recDtTm.setRecCreatDtTm(dateTime);
		recDtTm.setRecUpdtDtTm(dateTime);
		recDtTm.setTimeZone(GMTStr);

		// ProdPrc begin
		ProdPrc prodPrc = new ProdPrc();
		prodPrc.setProdKeySeg(prodKey);

		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), "pdcy_prc_cde"));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		prodPrcSeg.setCcyProdMktPrcCde(fieldSet.readString("BID_PRC_CCY"));
		prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(fieldSet.readString("BID_PRC_AMT")));
		prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(fieldSet.readString("OFFER_PRC_AMT")));

		prodPrcSeg.setRecDtTmSeg(recDtTm);
		prodPrc.addProdPrcSeg(prodPrcSeg);
		
		if (StringUtils.isNotBlank(prodPrcSeg.getPrcEffDt())
            && StringUtils.isNotBlank(prodPrcSeg.getCcyProdMktPrcCde())
            && (StringUtils.isNotBlank(prodPrcSeg.getProdBidPrcAmt())
                    || StringUtils.isNotBlank(prodPrcSeg.getProdOffrPrcAmt())
                    || StringUtils.isNotBlank(prodPrcSeg.getProdMktPrcAmt())
                    || StringUtils.isNotBlank(prodPrcSeg.getProdNavPrcAmt()))) {
            arr.addObj(prodPrc);
        }else {
            arr.addObj(null);
        }
		
		// ProdPrc end

		return arr;
	}

}
