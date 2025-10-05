package com.dummy.wpc.datadaptor.mapper;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ChanlAttrSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdListSeg;
import com.dummy.wpc.batch.xml.ProdPerf;
import com.dummy.wpc.batch.xml.ProdPerfmSeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.ProdTradeCcySeg;
import com.dummy.wpc.batch.xml.ProdUserDefEGExtSeg;
import com.dummy.wpc.batch.xml.ProdUserDefExtSeg;
import com.dummy.wpc.batch.xml.ProdUserDefOPExtSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.ResChanSeg;
import com.dummy.wpc.batch.xml.ResChannelDtl;
import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.batch.xml.UtTrstInstmSeg;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class UTWithCUTASMapper4MutliWriter extends UtInstmCommonMapper4MutliWriter {

	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(),"empty_str");
		
		MultiWriterObj arr = new MultiWriterObj();
		
		//UtTrstInstm begin
		UtTrstInstm utTrstInstm = new UtTrstInstm();
		arr.addObj(utTrstInstm);
		
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"));
		prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde"));
		prodKey.setProdCde(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_type_cde"));
		prodKey.setProdCdeAltClassCde("P");
		utTrstInstm.setProdKeySeg(prodKey);
		// four segment here, one is for 'P' use PROD_CDE , one is for 'F' use FUND_EXT_CDE and one is for 'M' use PROD_CDE and one is for 'I' use ISIN_CDE
		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_p"));
		prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		utTrstInstm.addProdAltNumSeg(prodAltNumSegP);
		
		ProdAltNumSeg prodAltNumSegF = new ProdAltNumSeg();
		prodAltNumSegF.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_f"));
		prodAltNumSegF.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("FUND_EXT_CDE")));
		utTrstInstm.addProdAltNumSeg(prodAltNumSegF);
		
		ProdAltNumSeg prodAltNumSegM= new ProdAltNumSeg();
		prodAltNumSegM.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_m"));
		prodAltNumSegM.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		utTrstInstm.addProdAltNumSeg(prodAltNumSegM);
		
		if(StringUtils.isNotBlank(fieldSet.readString("ISIN_CDE"))){
			ProdAltNumSeg prodAltNumSegI = new ProdAltNumSeg();
			prodAltNumSegI.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_i"));
			prodAltNumSegI.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("ISIN_CDE")));
			utTrstInstm.addProdAltNumSeg(prodAltNumSegI);
		}
		
		ProdInfoSeg prodInfo = new ProdInfoSeg();
		prodInfo.setProdSubtpCde(StringUtils.trimToEmpty(fieldSet.readString("PROD_SUBTP_CDE")));
		prodInfo.setProdName(StringUtils.trimToEmpty(fieldSet.readString("SHRT_NAME")));
		prodInfo.setProdShrtName(StringUtils.trimToEmpty(fieldSet.readString("SHRT_NAME")));
		prodInfo.setBchmkName(fieldSet.readString("BCHMK_NAME"));
		prodInfo.setProdStatCde(StringUtils.trimToEmpty(fieldSet.readString("STAT_CDE")));
		prodInfo.setCcyProdCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_CDE")));
		prodInfo.setRiskLvlCde(fieldSet.readString("RISK_LVL_CDE"));
		prodInfo.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		//prodInfo.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfo.setMktInvstCde(fieldSet.readString("MKT_INVST_CDE"));
		prodInfo.setSectInvstCde(fieldSet.readString("SECT_CDE"));
		prodInfo.setInvstInitMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("INVST_INIT_MIN_AMT"))));
		
		prodInfo.setAllowBuyProdInd(emptyStr);
		prodInfo.setAllowSellProdInd(emptyStr);
		prodInfo.setAllowBuyUtProdInd(emptyStr);
		prodInfo.setAllowBuyAmtProdInd(emptyStr);
		prodInfo.setAllowSellUtProdInd(emptyStr);
		prodInfo.setAllowSellAmtProdInd(emptyStr);
		prodInfo.setAllowSellMipProdInd(fieldSet.readString("MIP_IND"));
		prodInfo.setAllowSellMipUtProdInd(emptyStr);
		prodInfo.setAllowSellMipAmtProdInd(emptyStr);
		prodInfo.setAllowSwInProdInd(emptyStr);
		prodInfo.setAllowSwInUtProdInd(emptyStr);
		prodInfo.setAllowSwInAmtProdInd(emptyStr);
		prodInfo.setAllowSwOutProdInd(emptyStr);
		prodInfo.setAllowSwOutUtProdInd(emptyStr);
		prodInfo.setAllowSwOutAmtProdInd(emptyStr);
		//prodInfo.setIncmCharProdInd(emptyStr);
		//prodInfo.setCptlGurntProdInd(emptyStr);
		//prodInfo.setYieldEnhnProdInd(emptyStr);
		//prodInfo.setGrwthCharProdInd(emptyStr);
		//prodInfo.setDispComProdSrchInd(emptyStr);
		prodInfo.setDivrNum(emptyStr);
		//prodInfo.setMrkToMktInd(emptyStr);
		prodInfo.setCtryProdTrade1Cde(emptyStr);
		// 'Currency Investment Code' use 'Fund Currency Code'
		// special handle for HK CUTAS, 'Currency Investment Code' = INVST_INIT_MIN_CCY from cutas
		if (StringUtils.equalsIgnoreCase(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), "HK") && 
				StringUtils.isNotBlank(StringUtils.trimToEmpty(fieldSet.readString("INVST_INIT_MIN_CCY")))) {
			
			prodInfo.setCcyInvstCde(StringUtils.trimToEmpty(fieldSet.readString("INVST_INIT_MIN_CCY")));
			
		} else {
			prodInfo.setCcyInvstCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_CDE")));
		}
		
		prodInfo.setQtyTypeCde(emptyStr);
		prodInfo.setProdLocCde(emptyStr);
//		prodInfo.setRcblTrdInd(emptyStr);
		prodInfo.setSuptRcblCashProdInd(emptyStr);
		prodInfo.setSuptRcblScripProdInd(emptyStr);
		prodInfo.setDcmlPlaceTradeUnitNum(StringUtils.trimToEmpty(fieldSet.readString("DCML_PLACE_TRADE_UNIT_NUM")));
		//prodInfo.setPldgLimitAssocAcctInd(emptyStr);
		prodInfo.setAumChrgProdInd(emptyStr);
		
		ProdTradeCcySeg prodTradeCcy = new ProdTradeCcySeg();
		prodTradeCcy.setCcyProdTradeCde(prodInfo.getCcyProdCde());
		if(!prodInfo.getCcyProdCde().equals(Constants.HKD_FUNDS)){
			ProdTradeCcySeg prodTradeCcy2 = new ProdTradeCcySeg();
			prodTradeCcy2.setCcyProdTradeCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_trade_ccy"));
			prodInfo.addProdTradeCcySeg(prodTradeCcy2);
		}
		prodInfo.addProdTradeCcySeg(prodTradeCcy);
		
		
		String slst_1_ind = fieldSet.readString("SLST_1_IND");
		if ("Y".equals(slst_1_ind)) {
			ProdListSeg prodList = new ProdListSeg();
			String prodListTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(),"prod_list_type_cde");
			prodList.setProdListTypeCde(prodListTypeCde);
			prodList.setProdListCde(prodListTypeCde + "_1");
			prodList.setProdEffSlstDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("SLST_EFF_1_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
			prodInfo.addProdListSeg(prodList);
		}
		
		ResChanSeg resChan = new ResChanSeg();
		String alowCasChnlInd = fieldSet.readString("ALOW_CAS_CHNL_IND");
		if (!"Y".equals(alowCasChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_cas"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowPfsfpChnlInd = fieldSet.readString("ALOW_PFSFP_CHNL_IND");
		if (!"Y".equals(alowPfsfpChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_pfsfp"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowPfspdChnlInd = fieldSet.readString("ALOW_PFSPD_CHNL_IND");
		if (!"Y".equals(alowPfspdChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_pfspd"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowPwspbChnlInd = fieldSet.readString("ALOW_PWSPB_CHNL_IND");
		if (!"Y".equals(alowPwspbChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_pwspb"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowPwscbChnlInd = fieldSet.readString("ALOW_PWSCB_CHNL_IND");
		if (!"Y".equals(alowPwscbChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_pwscb"));
			resChan.addResChannelDtl(resChannelDtl);
		}		
		String alowPibChnlInd = fieldSet.readString("ALOW_PIB_CHNL_IND");
		if (!"Y".equals(alowPibChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_pib"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowWexChnlInd = fieldSet.readString("ALOW_WEX_CHNL_IND");
		if (!"Y".equals(alowWexChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_wex"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String alowIvrsChnlInd = fieldSet.readString("ALOW_IVRS_CHNL_IND");
		if (!"Y".equals(alowIvrsChnlInd)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"res_channel_cde_ivrs"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		String sweepInvestIndicator = fieldSet.readString("SWEEP_INVEST_FUND_IND");
		if (!"Y".equals(sweepInvestIndicator)) {
			ResChannelDtl resChannelDtl = new ResChannelDtl();
			resChannelDtl.setResChannelCde(ConstantsPropertiesHelper.getValue(getJobCode(),"sweep_invest_chanl"));
			resChan.addResChannelDtl(resChannelDtl);
		}
		
		
		if (resChan.getResChannelDtlCount() > 0) {
            prodInfo.setResChanSeg(resChan);
        }
		
		utTrstInstm.setProdInfoSeg(prodInfo);

		// AMHUTB, adding new field for CUTAS file which will be stored in PROD_USER_DEFIN_EXT_FIELD
		List<ProdUserDefExtSeg> userExtFieldList = new ArrayList<ProdUserDefExtSeg>();
		//userExtFieldList.add(extractUserDefExtField(fieldSet, "fundUnitDecimal", "FUND_UNIT_DECIMAL"));
		userExtFieldList.add(extractUserDefExtField(fieldSet, "clearFundInd", "CLEAR_FUND_IND"));
		userExtFieldList.add(extractUserDefExtField(fieldSet, "gteFundInd", "GTE_FUND_IND"));
		userExtFieldList.add(extractUserDefExtField(fieldSet, "annMgmtFeePct", "ANN_MGMT_FEE_PCT"));
		userExtFieldList.add(extractUserDefExtField(fieldSet, "setlLeadTmScrib", "SET_LEAD_TIME_SUB"));
		userExtFieldList.add(extractUserDefExtField(fieldSet, "setlLeadTmRdm", "SET_LEAD_TIME_RDM"));
		String gbaAcctTrdb = StringUtils.trimToEmpty(fieldSet.readString("GBA_ACCT_RDB"));
		if(StringUtils.isBlank(gbaAcctTrdb)) {
			ProdUserDefExtSeg prodFundIndField = new ProdUserDefExtSeg();
			prodFundIndField.setFieldCde("gbaAcctTrdb");
			prodFundIndField.setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"gbaAcctTrdb"));
			userExtFieldList.add(prodFundIndField);
		}else {
			userExtFieldList.add(extractUserDefExtField(fieldSet, "gbaAcctTrdb", "GBA_ACCT_RDB"));
		}
		userExtFieldList.add(extractUserDefExtField(fieldSet, "gnrAcctTrdb", "GNR_ACCT_RDB"));
		prodInfo.setProdUserDefExtSeg(userExtFieldList.toArray(new ProdUserDefExtSeg[] {}));
		
		// AMHUTB, adding new field for CUTAS file which will be stored in PROD_USER_DEFIN_OP_EXT_FIEL
		List<ProdUserDefOPExtSeg> userOPExtFieldList = new ArrayList<ProdUserDefOPExtSeg>();
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "dummyMaxInitchrgPct", "dummy_CHRG_MAX_INIT_PCT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inDealBefDt", "IN_DEAL_BEF_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inScribStlBefDt", "IN_SCRIB_STL_BEF_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inRedempStlBefDt", "IN_REDEMP_STL_BEF_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inDealAftDt", "IN_DEAL_AFT_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inScribStlAftDt", "IN_SCRIB_STL_AFT_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "inRedempStlAftDt", "IN_REDEMP_STL_AFT_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "infcDt", "INFC_DT"));
		userOPExtFieldList.add(extractUserDefOPExtField(fieldSet, "siFundInd", "SWEEP_INVEST_FUND_IND"));
		prodInfo.setProdUserDefOPExtSeg(userOPExtFieldList.toArray(new ProdUserDefOPExtSeg[] {}));
		
		// for Sweep Invest requirement
		ChanlAttrSeg[] chanlAttrSegList = {};
		if("Y".equalsIgnoreCase(sweepInvestIndicator)){
			chanlAttrSegList = buildChanlAttr(prodInfo);
		}
		prodInfo.setChanlAttrSeg(chanlAttrSegList);
		
		// AMHUTB, adding new field for CUTAS file which will be stored in PROD_USER_DEFIN_EG_EXT_FIEL
//		List<ProdUserDefEGExtSeg> userEGExtFieldList = new ArrayList<ProdUserDefEGExtSeg>();
//		prodInfo.setProdUserDefEGExtSeg(userEGExtFieldList.toArray(new ProdUserDefEGExtSeg[] {}));
		
		UtTrstInstmSeg utTrstInstmSeg = new UtTrstInstmSeg();
		utTrstInstmSeg.setFundHouseCde(StringUtils.trimToEmpty(fieldSet.readString("FUND_HOUSE_CDE")));
	    if (StringUtils.isBlank(utTrstInstmSeg.getFundHouseCde())) {
            utTrstInstmSeg.setFundHouseCde(ConstantsPropertiesHelper.getValue(getJobCode(), "fund_house_cde"));
        }
		utTrstInstmSeg.setCloseEndFundInd(emptyStr);
		utTrstInstmSeg.setInvstIncrmMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("INVST_ICT_MIN_AMT"))));
		if (StringUtils.isBlank(utTrstInstmSeg.getInvstIncrmMinAmt())) {
            utTrstInstmSeg.setInvstIncrmMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), "invst_incrm_min_amt"));
        }
		
		utTrstInstmSeg.setRdmMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("RDEM_MIN_AMT"))));
		if (StringUtils.isBlank(utTrstInstmSeg.getRdmMinAmt())) {
            utTrstInstmSeg.setRdmMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), "rdm_min_amt"));
        }
		
		utTrstInstmSeg.setUtRtainMinNum(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("UNIT_ARDM_MIN_NUM"))));
		if (StringUtils.isBlank(utTrstInstmSeg.getUtRtainMinNum())) {
            utTrstInstmSeg.setUtRtainMinNum(ConstantsPropertiesHelper.getValue(getJobCode(), "ut_rtain_min_num"));
        }
		
		utTrstInstmSeg.setFundRtainMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("FUND_ARDM_MIN_AMT"))));
		if (StringUtils.isBlank(utTrstInstmSeg.getFundRtainMinAmt())) {
            utTrstInstmSeg.setFundRtainMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), "fund_rtain_min_amt"));
        }
		
		utTrstInstmSeg.setSuptMipInd(fieldSet.readString("MIP_IND"));
		utTrstInstmSeg.setInvstMipMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_MIP_MIN_AMT")));
		utTrstInstmSeg.setInvstMipIncrmMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_INCR_MIP_AMT")));
		utTrstInstmSeg.setRdmMipMinAmt(DecimalHelper.trimZero(fieldSet.readString("FN_MIP_RDM_MIN_AMT")));
		utTrstInstmSeg.setUtMipRtainMinNum(DecimalHelper.trimZero(fieldSet.readString("UT_MIP_ARD_MIN_NUM")));
		utTrstInstmSeg.setFundMipRtainMinAmt(DecimalHelper.trimZero(fieldSet.readString("UT_MIP_ARD_MIN_AMT")));
		utTrstInstmSeg.setChrgInitSalesPct(DecimalHelper.trimZero(fieldSet.readString("CHRG_INIT_PCT")));
		utTrstInstmSeg.setDscntMaxPct(DecimalHelper.trimZero(fieldSet.readString("DSCNT_MAX_PCT")));
//		utTrstInstmSeg.setOfferStartDtTm(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("OFFER_START_DT")+fieldSet.readString("OFFER_START_TM"), DateHelper.DATE_TIME_SHORT_FORMAT), DateHelper.DEFAULT_DATETIME_FORMAT));
//		utTrstInstmSeg.setOfferEndDtTm(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("OFFER_END_DT")+fieldSet.readString("OFFER_END_TM"), DateHelper.DATE_TIME_SHORT_FORMAT), DateHelper.DEFAULT_DATETIME_FORMAT));
		
		//special handle for HK CUTAS
		if (StringUtils.equalsIgnoreCase(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), "HK")) {
			String payCashDivInd = null;
			
			if (StringUtils.isBlank(fieldSet.readString("DVD_MTHD"))){
				payCashDivInd = null;
			} else if (StringUtils.equalsIgnoreCase(fieldSet.readString("DVD_MTHD"), "C")){
				payCashDivInd = "Y";
			} else {
				payCashDivInd = "N";
			}
			
			utTrstInstmSeg.setPayCashDivInd(payCashDivInd);
			
		} else {
			utTrstInstmSeg.setPayCashDivInd(fieldSet.readString("DVD_FLG"));
		}
		
		
		utTrstInstmSeg.setHldayFundNextDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("NXT_HLDAY_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		
		//stop update and create this two date from CUTAS
		String scribCtOffDtTmStr = ConstantsPropertiesHelper.getValue(getJobCode(),"scrib_ctoff_next_dt_tm")+fieldSet.readString("CUTOFF_TM");
		String rdmCtOffDtTmStr = ConstantsPropertiesHelper.getValue(getJobCode(),"scrib_ctoff_next_dt_tm")+fieldSet.readString("CUTOFF_TM");
		Date cutOffDtTm = DateHelper.getDateTm(scribCtOffDtTmStr, DateHelper.DATE_TIME_DTHHMM_FORMAT);
		Date rdmCtOffDtTm = DateHelper.getDateTm(rdmCtOffDtTmStr, DateHelper.DATE_TIME_DTHHMM_FORMAT);
		
		scribCtOffDtTmStr = DateHelper.formatDate2String(new java.sql.Date(cutOffDtTm.getTime()), DateHelper.DEFAULT_DATETIME_FORMAT);
		rdmCtOffDtTmStr = DateHelper.formatDate2String(new java.sql.Date(rdmCtOffDtTm.getTime()), DateHelper.DEFAULT_DATETIME_FORMAT);
		
		utTrstInstmSeg.setScribCtoffNextDtTm(scribCtOffDtTmStr);
		utTrstInstmSeg.setRdmCtoffNextDtTm(rdmCtOffDtTmStr);

		utTrstInstmSeg.setDealNextDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("DEAL_NEXT_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		
		String field_fund_class_cde = ConstantsPropertiesHelper.getValue(getJobCode(), "replace_fund_class_cde");
        if (StringUtils.isNotBlank(field_fund_class_cde)) {
            utTrstInstmSeg.setFundClassCde(StringUtils.trimToEmpty(fieldSet.readString(field_fund_class_cde)));
        } else {
            utTrstInstmSeg.setFundClassCde(StringUtils.trimToEmpty(fieldSet.readString("FUND_CLASS_CDE")));
        }
        if (StringUtils.isBlank(utTrstInstmSeg.getFundClassCde())) {
            utTrstInstmSeg.setFundClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), "fund_class_cde"));
        }
		
		utTrstInstmSeg.setAmcmInd(fieldSet.readString("AMCM_IND"));
		utTrstInstmSeg.setSpclFundInd(fieldSet.readString("SPEC_FUND_IND"));
		utTrstInstmSeg.setFundSwInMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("INVST_ICT_MIN_AMT"))));
		utTrstInstmSeg.setFundSwOutMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("RDEM_MIN_AMT"))));
		utTrstInstmSeg.setFundSwOutRtainMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("FUND_ARDM_MIN_AMT"))));
		if(StringUtils.isBlank(utTrstInstmSeg.getFundSwOutRtainMinAmt())){
			utTrstInstmSeg.setFundSwOutRtainMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), "fund_sw_out_rtain_min_amt"));
		}
		utTrstInstmSeg.setUtSwOutRtainMinNum(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("UNIT_ARDM_MIN_NUM"))));
		if(StringUtils.isBlank(utTrstInstmSeg.getUtSwOutRtainMinNum())){
			utTrstInstmSeg.setUtSwOutRtainMinNum(ConstantsPropertiesHelper.getValue(getJobCode(), "ut_sw_out_rtain_min_num"));
		}
		utTrstInstmSeg.setFundSwOutMaxAmt(emptyStr);
		utTrstInstmSeg.setTranMaxAmt(emptyStr);
		utTrstInstmSeg.setSchemChrgCde(fieldSet.readString("SCHEM_CHRG_CDE"));
			if(StringUtils.isNotBlank(utTrstInstmSeg.getSchemChrgCde()) && Constants.NIC.equals(utTrstInstmSeg.getSchemChrgCde())){
				prodInfo.setNoScribFeeProdInd(Constants.Y);
			}else{
				prodInfo.setNoScribFeeProdInd(Constants.N);
			}
		utTrstInstm.setUtTrstInstmSeg(utTrstInstmSeg);

		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setProdStatUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);

		utTrstInstm.setRecDtTmSeg(recDtTmSeg);
		//UtTrstInstm end
		
		//RecDtTmSeg for ProdPrcSeg and ProdPerfmSeg use.
		RecDtTmSeg recDtTm = new RecDtTmSeg();
		recDtTm.setRecCreatDtTm(dateTime);
		recDtTm.setRecUpdtDtTm(dateTime);
		recDtTm.setTimeZone(GMTStr);
		
		//ProdPrc begin
		ProdPrc prodPrc = new ProdPrc();
		arr.addObj(prodPrc);
		
		prodPrc.setProdKeySeg(prodKey);
		
		ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
		prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRC_EFF_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT)));
		prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(),"pdcy_prc_cde"));
		prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
		// 'Currency Product Market Price Code' use 'Currency Product Code'
		prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_CDE")));
		prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(fieldSet.readString("BID_DLY_PRC_AMT")));
		prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(fieldSet.readString("OFFER_DLY_PRC_AMT")));
		prodPrcSeg.setProdNavPrcAmt(DecimalHelper.trimZero(fieldSet.readString("NAV_DLY_PRC_AMT")));
		setMktPrice(prodPrcSeg);
		prodPrcSeg.setRecDtTmSeg(recDtTm);
		prodPrc.addProdPrcSeg(prodPrcSeg);
		//ProdPrc end
		
		//ProdPerf begin
		ProdPerf prodPerf = new ProdPerf();
		arr.addObj(prodPerf);
		
		prodPerf.setProdKeySeg(prodKey);
		
		ProdPerfmSeg prodPerfmSeg_BCHMK = new ProdPerfmSeg();
		prodPerfmSeg_BCHMK.setPerfmTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),"perfm_type_cde_b"));
		prodPerfmSeg_BCHMK.setPerfmCalcDt(StringUtils.trimToEmpty(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("BCHMK_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT)));
		prodPerfmSeg_BCHMK.setPerfmYrToDtPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_YTD_PCT")));
		prodPerfmSeg_BCHMK.setPerfm6moPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_6MO_PCT")));
		prodPerfmSeg_BCHMK.setPerfm1yrPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_1YR_PCT")));
		prodPerfmSeg_BCHMK.setPerfm3yrPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_3YR_PCT")));
		prodPerfmSeg_BCHMK.setPerfm5yrPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_5YR_PCT")));
		prodPerfmSeg_BCHMK.setPerfmSinceLnchPct(DecimalHelper.trimZero(fieldSet.readString("BCHMK_SNC_LNCH_PCT")));
		prodPerfmSeg_BCHMK.setRecDtTmSeg(recDtTm);
		prodPerf.addProdPerfmSeg(prodPerfmSeg_BCHMK);
		
		ProdPerfmSeg prodPerfmSeg_PRFM = new ProdPerfmSeg();
		prodPerfmSeg_PRFM.setPerfmTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),"perfm_type_cde_p"));
		prodPerfmSeg_PRFM.setPerfmCalcDt(StringUtils.trimToEmpty(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("BCHMK_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT)));
		prodPerfmSeg_PRFM.setPerfmYrToDtPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_YTD_PCT")));
		prodPerfmSeg_PRFM.setPerfm6moPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_6MO_PCT")));
		prodPerfmSeg_PRFM.setPerfm1yrPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_1YR_PCT")));
		prodPerfmSeg_PRFM.setPerfm3yrPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_3YR_PCT")));
		prodPerfmSeg_PRFM.setPerfm5yrPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_5YR_PCT")));
		prodPerfmSeg_PRFM.setPerfmSinceLnchPct(DecimalHelper.trimZero(fieldSet.readString("PRFM_SNC_LNCH_PCT")));
		prodPerfmSeg_PRFM.setRecDtTmSeg(recDtTm);
		prodPerf.addProdPerfmSeg(prodPerfmSeg_PRFM);
		//ProdPerf end
		
		return arr;
	}

	/**
	 * CUTAS update earlier than GSOPS,
	 * after CUTAS update, market price still old and RBP get the market price.
	 * So need to update the mkt price in CUTAS adaptor
	 * if has NAV, set market price = Nav
	 * if no NAV, set Bid = market pric
	 * @param prodPrcSeg
	 */
	private void setMktPrice(ProdPrcSeg prodPrcSeg) {
		String prodNavPrcAmt = prodPrcSeg.getProdNavPrcAmt();
		if (StringUtils.isNotEmpty(prodNavPrcAmt) && !"0".equals(prodNavPrcAmt)) {
			prodPrcSeg.setProdMktPrcAmt(prodNavPrcAmt);
		} else {
			String prodBidPrcAmt = prodPrcSeg.getProdBidPrcAmt();
			if (StringUtils.isNotEmpty(prodBidPrcAmt) && !"0".equals(prodBidPrcAmt)) {
				prodPrcSeg.setProdMktPrcAmt(prodBidPrcAmt);
			}
		}
	}
	
	/**
	 * @param ProdUserDefExtSeg fieldSet
	 * @return
	 */
	private ProdUserDefExtSeg extractUserDefExtField(FieldSet fieldSet, String fieldCde, String readStr) {
		ProdUserDefExtSeg bondFundIndField = new ProdUserDefExtSeg();
		bondFundIndField.setFieldCde(fieldCde);
		bondFundIndField.setFieldValue(StringUtils.trimToEmpty(fieldSet.readString(readStr)));
		return bondFundIndField;
	}
	
	/**
	 * @param ProdUserDefOPExtSeg fieldSet
	 * @return
	 */
	private ProdUserDefOPExtSeg extractUserDefOPExtField(FieldSet fieldSet, String fieldCde, String readStr) {
		ProdUserDefOPExtSeg prodUserDefOPExtSeg = new ProdUserDefOPExtSeg();
		prodUserDefOPExtSeg.setFieldCde(fieldCde);
		prodUserDefOPExtSeg.setFieldValue(StringUtils.trimToEmpty(fieldSet.readString(readStr)));
		return prodUserDefOPExtSeg;
	}
	
	/**
	 * @param ProdUserDefEGExtSeg fieldSet
	 * @return
	 */
	private ProdUserDefEGExtSeg extractUserDefEGExtField(FieldSet fieldSet, String fieldCde, String readStr) {
		ProdUserDefEGExtSeg prodUserDefEGExtSeg = new ProdUserDefEGExtSeg();
		prodUserDefEGExtSeg.setFieldCde(fieldCde);
		prodUserDefEGExtSeg.setFieldValue(StringUtils.trimToEmpty(fieldSet.readString(readStr)));
		return prodUserDefEGExtSeg;
	}
	
	/**
	 * build channel attribute tags
	 */
	private ChanlAttrSeg[]  buildChanlAttr(ProdInfoSeg prodInfo) {
		
		// set value for tb_chanl_attr
		ChanlAttrSeg[] chanlAttrSegList = new ChanlAttrSeg[15];
		String chanlCde = ConstantsPropertiesHelper.getValue(getJobCode(),"sweep_invest_chanl");
		//ALLOW_BUY_PROD_IND
		chanlAttrSegList[0] = new ChanlAttrSeg();		
		chanlAttrSegList[0].setChanlCde(chanlCde);
		chanlAttrSegList[0].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_buy_cde"));
		chanlAttrSegList[0].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"si_allow_buy_ind"));
		//ALLOW_SELL_PROD_IND
		chanlAttrSegList[1] = new ChanlAttrSeg();		
		chanlAttrSegList[1].setChanlCde(chanlCde);
		chanlAttrSegList[1].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sell_cde"));
		chanlAttrSegList[1].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"si_allow_sell_ind"));
		//ALLOW_SELL_MIP_PROD_IND
		chanlAttrSegList[2] = new ChanlAttrSeg();		
		chanlAttrSegList[2].setChanlCde(chanlCde);
		chanlAttrSegList[2].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_mip_cde"));
		chanlAttrSegList[2].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"si_allow_mip_ind"));
		//ALLOW_SW_IN_PROD_IND
		chanlAttrSegList[3] = new ChanlAttrSeg();		
		chanlAttrSegList[3].setChanlCde(chanlCde);
		chanlAttrSegList[3].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_in_cde"));
		chanlAttrSegList[3].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"si_allow_sw_in_ind"));
		//ALLOW_SW_OUT_PROD_IND
		chanlAttrSegList[4] = new ChanlAttrSeg();		
		chanlAttrSegList[4].setChanlCde(chanlCde);
		chanlAttrSegList[4].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_out_cde"));
		chanlAttrSegList[4].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"si_allow_sw_out_ind"));
		
		chanlCde = ConstantsPropertiesHelper.getValue(getJobCode(),"casc_sweep_invest_chanl");
		//ALLOW_BUY_PROD_IND
		chanlAttrSegList[5] = new ChanlAttrSeg();		
		chanlAttrSegList[5].setChanlCde(chanlCde);
		chanlAttrSegList[5].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_buy_cde"));
		chanlAttrSegList[5].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casc_allow_buy_ind"));
		//ALLOW_SELL_PROD_IND
		chanlAttrSegList[6] = new ChanlAttrSeg();		
		chanlAttrSegList[6].setChanlCde(chanlCde);
		chanlAttrSegList[6].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sell_cde"));
		chanlAttrSegList[6].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casc_allow_sell_ind"));
		//ALLOW_SELL_MIP_PROD_IND
		chanlAttrSegList[7] = new ChanlAttrSeg();		
		chanlAttrSegList[7].setChanlCde(chanlCde);
		chanlAttrSegList[7].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_mip_cde"));
		chanlAttrSegList[7].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casc_allow_mip_ind"));
		//ALLOW_SW_IN_PROD_IND
		chanlAttrSegList[8] = new ChanlAttrSeg();		
		chanlAttrSegList[8].setChanlCde(chanlCde);
		chanlAttrSegList[8].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_in_cde"));
		chanlAttrSegList[8].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casc_allow_sw_in_ind"));
		//ALLOW_SW_OUT_PROD_IND
		chanlAttrSegList[9] = new ChanlAttrSeg();		
		chanlAttrSegList[9].setChanlCde(chanlCde);
		chanlAttrSegList[9].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_out_cde"));
		chanlAttrSegList[9].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casc_allow_sw_out_ind"));
		
		chanlCde = ConstantsPropertiesHelper.getValue(getJobCode(),"casb_sweep_invest_chanl");
		//ALLOW_BUY_PROD_IND
		chanlAttrSegList[10] = new ChanlAttrSeg();		
		chanlAttrSegList[10].setChanlCde(chanlCde);
		chanlAttrSegList[10].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_buy_cde"));
		chanlAttrSegList[10].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casb_allow_buy_ind"));
		//ALLOW_SELL_PROD_IND
		chanlAttrSegList[11] = new ChanlAttrSeg();		
		chanlAttrSegList[11].setChanlCde(chanlCde);
		chanlAttrSegList[11].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sell_cde"));
		chanlAttrSegList[11].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casb_allow_sell_ind"));
		//ALLOW_SELL_MIP_PROD_IND
		chanlAttrSegList[12] = new ChanlAttrSeg();		
		chanlAttrSegList[12].setChanlCde(chanlCde);
		chanlAttrSegList[12].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_mip_cde"));
		chanlAttrSegList[12].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casb_allow_mip_ind"));
		//ALLOW_SW_IN_PROD_IND
		chanlAttrSegList[13] = new ChanlAttrSeg();		
		chanlAttrSegList[13].setChanlCde(chanlCde);
		chanlAttrSegList[13].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_in_cde"));
		chanlAttrSegList[13].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casb_allow_sw_in_ind"));
		//ALLOW_SW_OUT_PROD_IND
		chanlAttrSegList[14] = new ChanlAttrSeg();		
		chanlAttrSegList[14].setChanlCde(chanlCde);
		chanlAttrSegList[14].setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(),"allow_sw_out_cde"));
		chanlAttrSegList[14].setFieldValue(ConstantsPropertiesHelper.getValue(getJobCode(),"casb_allow_sw_out_ind"));
		
		// set value for tb_prod
		String prodDfltValue = ConstantsPropertiesHelper.getValue(getJobCode(),"si_prod_allow_buy_sell_delt_val");		
		prodInfo.setAllowBuyProdInd(prodDfltValue);
		prodInfo.setAllowSellProdInd(prodDfltValue);
		prodInfo.setAllowSellMipProdInd(prodDfltValue);
		prodInfo.setAllowSwInProdInd(prodDfltValue);
		prodInfo.setAllowSwOutProdInd(prodDfltValue);
				
		return chanlAttrSegList;
	}
	
}
