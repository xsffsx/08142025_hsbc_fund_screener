package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.Sid;
import com.dummy.wpc.batch.xml.SidProdPrcSeg;
import com.dummy.wpc.batch.xml.SidRtrnSeg;
import com.dummy.wpc.batch.xml.SidSeg;
import com.dummy.wpc.datadaptor.util.Constants;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class AfterSidMapper extends AbstractFieldSetMapper{

	public Object mapLine(FieldSet fieldSet) {
	    MultiWriterObj multiObject = new MultiWriterObj();
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(),"empty_str");
		
		Sid sid = new Sid();
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "ctry_rec_cde"));
        prodKey.setGrpMembrRecCde(ConstantsPropertiesHelper.getValue(getJobCode(), "grp_membr_rec_cde"));
        prodKey.setProdTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "prod_type_cde"));
		prodKey.setProdCde(fieldSet.readString("PROD_CDE"));
		sid.setProdKeySeg(prodKey);
		
		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_p"));
		prodAltNumSegP.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
		sid.addProdAltNumSeg(prodAltNumSegP);
		
		ProdAltNumSeg prodAltNumSegM = new ProdAltNumSeg();
		prodAltNumSegM.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_m"));
		prodAltNumSegM.setProdAltNum(StringUtils.trimToEmpty(fieldSet.readString("PROD_CDE")));
        sid.addProdAltNumSeg(prodAltNumSegM);
		
		ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
		prodInfoSeg.setProdSubtpCde(fieldSet.readString("PROD_SUBTP_CDE"));
		prodInfoSeg.setProdName(fieldSet.readString("PROD_NAME"));
		prodInfoSeg.setProdShrtName(fieldSet.readString("SHRT_NAME"));
		prodInfoSeg.setProdStatCde(fieldSet.readString("STAT_CDE"));
		prodInfoSeg.setCcyProdCde(fieldSet.readString("CRNCY_CDE"));
		prodInfoSeg.setRiskLvlCde(fieldSet.readString("RISK_LVL_CDE"));
		prodInfoSeg.setPrdProdCde(fieldSet.readString("PRD_PROD_CDE"));
		prodInfoSeg.setPrdProdNum(DecimalHelper.trimZero(fieldSet.readString("PRD_PROD_NUM")));
		prodInfoSeg.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(fieldSet.readString("INVST_INIT_MIN_AMT")));
		
		//add mandatory fields
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
//		prodInfoSeg.setRcblTrdInd(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);		
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);		
		prodInfoSeg.setLoanProdOdMrgnPct(DecimalHelper.trimZero(fieldSet.readString("OVDFT_SEC_PCT")));
		prodInfoSeg.setAumChrgProdInd(emptyStr);
		sid.setProdInfoSeg(prodInfoSeg);
		
		SidSeg sidSeg = new SidSeg();
		sidSeg.setProdConvCde(fieldSet.readString("CONV_CDE"));
		sidSeg.setCcyLinkDepstCde(fieldSet.readString("CRNCY_LINK_CDE"));
		sidSeg.setMktStartDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRD_MKT_START_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		sidSeg.setMktEndDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("PRD_MKT_END_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		sidSeg.setYieldAnnlMinPct(DecimalHelper.trimZero(fieldSet.readString("YLD_ANNL_MIN_PCT")));
		sidSeg.setAllowEarlyRdmInd(fieldSet.readString("ALLOW_ERDM_IND"));
		sidSeg.setRdmEarlyDalwText(fieldSet.readString("PRD_ERDM_DALW_TEXT"));
		sidSeg.setRdmEarlyIndAmt(DecimalHelper.trimZero(fieldSet.readString("ERDM_PCT")));
		sidSeg.setOfferTypeCde(fieldSet.readString("OFFER_TYPE_CDE"));
		sidSeg.setBonusIntCalcTypeCde(fieldSet.readString("BONUS_RATE_TYPE"));
		sidSeg.setBonusIntDtTypeCde(fieldSet.readString("BONUS_DATE_TYPE"));
		sidSeg.setLnchProdInd(fieldSet.readString("LAUNCH_STUS"));
		
		for (int i=1; i<=24; i++) {
			String rtrnIntrmPaidDt = DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString(Constants.PYMT_INTRM_RTRN_DT+i),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT);
			if(rtrnIntrmPaidDt != null){
				SidRtrnSeg sidRtrnSeg = new SidRtrnSeg();
				sidRtrnSeg.setRtrnIntrmPaidDt(rtrnIntrmPaidDt);
				sidRtrnSeg.setRtrnIntrmPct(DecimalHelper.trimZero(fieldSet.readString(Constants.RTRN_INTRM_PCT+i)));
				sidSeg.addSidRtrnSeg(sidRtrnSeg);
			}
		}
		sid.setSidSeg(sidSeg);
		
		RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTmSeg.setRecCreatDtTm(dateTime);
		recDtTmSeg.setRecUpdtDtTm(dateTime);
		recDtTmSeg.setTimeZone(GMTStr);	
		sid.setRecDtTmSeg(recDtTmSeg);
        multiObject.addObj(sid);
        // Mapping to sidProductPriceSegment
        ProdPrc prodPrc = new ProdPrc();
        prodPrc.setProdKeySeg(prodKey);

        SidProdPrcSeg sidProdPrcSeg = new SidProdPrcSeg();
        // Read String blank, set ""
        String prcEarlyRdmPct = fieldSet.readString("ERDM_PCT");
        if (StringUtils.isNotBlank(prcEarlyRdmPct)) {
            sidProdPrcSeg.setPrcEarlyRdmPct(DecimalHelper.trimZero(prcEarlyRdmPct));
        } else {
            sidProdPrcSeg.setPrcEarlyRdmPct(StringUtils.EMPTY);
        }

        sidProdPrcSeg.setPrcEarlyRdmEffDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        sidProdPrcSeg.setRecDtTmSeg(recDtTmSeg);

        prodPrc.setSidProdPrcSeg(sidProdPrcSeg);

        multiObject.addObj(prodPrc);

        return multiObject;

        
	}
}
