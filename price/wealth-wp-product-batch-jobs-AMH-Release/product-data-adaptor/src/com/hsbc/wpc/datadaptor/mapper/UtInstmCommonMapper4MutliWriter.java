package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdListSeg;
import com.dummy.wpc.batch.xml.ProdPerf;
import com.dummy.wpc.batch.xml.ProdPerfmSeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.ResChanSeg;
import com.dummy.wpc.batch.xml.ResChannelDtl;
import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.batch.xml.UtTrstInstmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class UtInstmCommonMapper4MutliWriter extends AbstractFieldSetMapper {
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
		utTrstInstm.setProdKeySeg(prodKey);
		// three segment here, one is for 'P' use PROD_CDE , one is for 'F' use FUND_EXT_CDE and one is for 'M' use PROD_CDE
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
		
		ProdInfoSeg prodInfo = new ProdInfoSeg();
		prodInfo.setProdSubtpCde(StringUtils.trimToEmpty(fieldSet.readString("PROD_SUBTP_CDE")));
		prodInfo.setProdName(StringUtils.trimToEmpty(fieldSet.readString("SHRT_NAME")));
		prodInfo.setProdShrtName(StringUtils.trimToEmpty(fieldSet.readString("SHRT_NAME")));
		prodInfo.setBchmkName(fieldSet.readString("BCHMK_NAME"));
		prodInfo.setProdStatCde(StringUtils.trimToEmpty(fieldSet.readString("STAT_CDE")));
		prodInfo.setCcyProdCde(StringUtils.trimToEmpty(fieldSet.readString("CRNCY_CDE")));
		prodInfo.setRiskLvlCde(fieldSet.readString("RISK_LVL_CDE"));
		prodInfo.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfo.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"), DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfo.setMktInvstCde(fieldSet.readString("MKT_INVST_CDE"));
		prodInfo.setSectInvstCde(fieldSet.readString("SECT_CDE"));
		prodInfo.setInvstInitMinAmt(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("INVST_INIT_MIN_AMT"))));
		
		prodInfo.setAllowBuyProdInd(emptyStr);
		prodInfo.setAllowSellProdInd(emptyStr);
		prodInfo.setAllowBuyUtProdInd(emptyStr);
		prodInfo.setAllowBuyAmtProdInd(emptyStr);
		prodInfo.setAllowSellUtProdInd(emptyStr);
		prodInfo.setAllowSellAmtProdInd(emptyStr);
		prodInfo.setAllowSellMipProdInd(emptyStr);
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
			
		} else if (StringUtils.equalsIgnoreCase(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), "CN") && 
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
		prodInfo.setDcmlPlaceTradeUnitNum(emptyStr);
		//prodInfo.setPldgLimitAssocAcctInd(emptyStr);
		prodInfo.setAumChrgProdInd(emptyStr);
		
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
		if (resChan.getResChannelDtlCount() > 0) {
            prodInfo.setResChanSeg(resChan);
        }
		
		utTrstInstm.setProdInfoSeg(prodInfo);

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
		utTrstInstmSeg.setOfferStartDtTm(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("OFFER_START_DT")+fieldSet.readString("OFFER_START_TM"), DateHelper.DATE_TIME_SHORT_FORMAT), DateHelper.DEFAULT_DATETIME_FORMAT));
		utTrstInstmSeg.setOfferEndDtTm(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("OFFER_END_DT")+fieldSet.readString("OFFER_END_TM"), DateHelper.DATE_TIME_SHORT_FORMAT), DateHelper.DEFAULT_DATETIME_FORMAT));
		
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
		String strNxtScribCtoffDt = fieldSet.readString("NXT_SCRIB_CTOFF_DT");
		if (StringUtils.isBlank(strNxtScribCtoffDt)) {
			utTrstInstmSeg.setScribCtoffNextDtTm(ConstantsPropertiesHelper.getValue(getJobCode(),"scrib_ctoff_next_dt_tm"));
		} else {
			utTrstInstmSeg.setScribCtoffNextDtTm(StringUtils.trimToEmpty(DateHelper.formatDate2String(DateHelper.parseToDate(strNxtScribCtoffDt, DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT)));
		}
		String strNxtRdemCtoffDt = fieldSet.readString("NXT_RDEM_CTOFF_DT");
		if (StringUtils.isBlank(strNxtRdemCtoffDt)) {
			utTrstInstmSeg.setRdmCtoffNextDtTm(ConstantsPropertiesHelper.getValue(getJobCode(),"rdm_ctoff_next_dt_tm"));
		} else {
			utTrstInstmSeg.setRdmCtoffNextDtTm(StringUtils.trimToEmpty(DateHelper.formatDate2String(DateHelper.parseToDate(strNxtRdemCtoffDt, DateHelper.DATE_SHORT_FORMAT), DateHelper.DEFAULT_DATE_FORMAT)));
		}
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
		utTrstInstmSeg.setUtSwOutRtainMinNum(DecimalHelper.trimZero(StringUtils.trimToEmpty(fieldSet.readString("UNIT_ARDM_MIN_NUM"))));
		utTrstInstmSeg.setFundSwOutMaxAmt(emptyStr);
		utTrstInstmSeg.setTranMaxAmt(emptyStr);
		utTrstInstmSeg.setSchemChrgCde(fieldSet.readString("SCHEM_CHRG_CDE"));
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

}
