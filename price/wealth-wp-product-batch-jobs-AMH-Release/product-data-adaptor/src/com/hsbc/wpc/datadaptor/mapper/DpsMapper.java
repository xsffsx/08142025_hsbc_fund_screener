package com.dummy.wpc.datadaptor.mapper;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.file.mapping.FieldSet;

import com.dummy.wpc.batch.xml.Dps;
import com.dummy.wpc.batch.xml.DpsSeg;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class DpsMapper extends AbstractFieldSetMapper{

	public Object mapLine(FieldSet fieldSet) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper.getValue(getJobCode(),"grp_membr_rec_cde")));
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(),"empty_str");
		
		// build Product Code
        String prodCde = null;
        String prodTypeCde = fieldSet.readString("PROD_TYPE_CDE");
        
        String crncyCde = fieldSet.readString("CRNCY_CDE");
        crncyCde = StringUtils.isBlank(crncyCde) ? StringUtils.leftPad("", 3, "0") : crncyCde;
        
        String prdProdCde = fieldSet.readString("PRD_PROD_CDE");
        prdProdCde = StringUtils.isBlank(prdProdCde) ? StringUtils.leftPad("", 15, "0") : prdProdCde;
        
        String prdProdNum = DecimalHelper.trimZero(fieldSet.readString("PRD_PROD_NUM"));
        prdProdNum = StringUtils.isBlank(prdProdNum) ? StringUtils.leftPad("", 5, "0") : StringUtils.leftPad(prdProdNum, 5, "0");
        
        String dpsTypeCde = fieldSet.readString("DPS_TYPE_CDE");
        dpsTypeCde = StringUtils.isBlank(dpsTypeCde) ? StringUtils.leftPad("", 2, "0") : dpsTypeCde;
        
        String prodSfx = fieldSet.readString("PROD_SFX");
        prodSfx = StringUtils.isBlank(prodSfx) ? StringUtils.leftPad("", 1, "0") : prodSfx;
        
        String crncyLinkCde = fieldSet.readString("CRNCY_LINK_CDE");
        crncyLinkCde = StringUtils.isBlank(crncyLinkCde) ? StringUtils.leftPad("", 3, "0") : crncyLinkCde;
        
        // tmdProdCde - ProdExtnlCde, interface NOT provide     
        String tmdProdCde = StringUtils.equals(dpsTypeCde, "01") ? "BASIC" : StringUtils.leftPad("", 5, "0");
        
//        if (StringUtils.equals(dpsTypeCde, "01")) {
//            prodInfoSeg.setProdSubtpCde("NORM");
//            tmdProdCde = "BASIC";
//            prodName = "DPS" + crncyCde + "/" + crncyLinkCde + prdProdNum + prdProdCde;
//        } else if (StringUtils.equals(dpsTypeCde, "02")) {
//            prodInfoSeg.setProdSubtpCde("EHIT");
//            prodName = "Early Hit " + "DPS" + crncyCde + "/" + crncyLinkCde + prdProdNum + prdProdCde;
//        } else if (StringUtils.equals(dpsTypeCde, "03")) {
//            prodInfoSeg.setProdSubtpCde("KNIN");
//            prodName = "Knock In " + "DPS" + crncyCde + "/" + crncyLinkCde + prdProdNum + prdProdCde + matDt;
//        }
        prodCde = prodTypeCde + dpsTypeCde + tmdProdCde + prodSfx + crncyCde + crncyLinkCde + prdProdNum + prdProdCde;
        
        Dps dps = new Dps();
        
        ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(fieldSet.readString("CTRY_CDE"));
		prodKey.setGrpMembrRecCde(fieldSet.readString("ORGN_CDE"));
		prodKey.setProdTypeCde(prodTypeCde);
		prodKey.setProdCde(prodCde);
		dps.setProdKeySeg(prodKey);
		
		ProdAltNumSeg prodAltNumSegP = new ProdAltNumSeg();
		prodAltNumSegP.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_p"));
		prodAltNumSegP.setProdAltNum(prodCde);
		dps.addProdAltNumSeg(prodAltNumSegP);
		ProdAltNumSeg prodAltNumSegM = new ProdAltNumSeg();
        prodAltNumSegM.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),"prod_cde_alt_class_cde_m"));
        prodAltNumSegM.setProdAltNum(prodCde);
        dps.addProdAltNumSeg(prodAltNumSegM);
		
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        
		prodInfoSeg.setProdSubtpCde(fieldSet.readString("PROD_SUBTP_CDE"));
		prodInfoSeg.setProdName(emptyStr);
		prodInfoSeg.setProdShrtName(emptyStr);
		prodInfoSeg.setProdStatCde(emptyStr);
		prodInfoSeg.setCcyProdCde(crncyCde);
		prodInfoSeg.setPrdProdCde(prdProdCde);
		prodInfoSeg.setPrdProdNum(prdProdNum);
		prodInfoSeg.setProdLnchDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("LAUNCH_DT"), DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setProdMturDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("MAT_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(fieldSet.readString("DEPST_MIN_AMT")));
		
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
		prodInfoSeg.setCcyInvstCde(emptyStr);
		prodInfoSeg.setQtyTypeCde(emptyStr);
		prodInfoSeg.setProdLocCde(emptyStr);
		prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
		prodInfoSeg.setSuptRcblScripProdInd(emptyStr);		
		prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);	
		prodInfoSeg.setAumChrgProdInd(emptyStr);
		
		ProdAsetUndlSeg prodAsetUndlSeg = new ProdAsetUndlSeg();
		prodAsetUndlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(getJobCode(), "aset_undl_cde_seq_num_1"));
		prodAsetUndlSeg.setAsetUndlCde(ConstantsPropertiesHelper.getValue(getJobCode(), "aset_undl_cde"));
		prodInfoSeg.addProdAsetUndlSeg(prodAsetUndlSeg);
		
		dps.setProdInfoSeg(prodInfoSeg);
		
		DpsSeg dpsSeg = new DpsSeg();
		dpsSeg.setDepstPlusTypeCde(dpsTypeCde);
		dpsSeg.setProdExtnlSuffNum(prodSfx);
		dpsSeg.setCcyLinkDepstCde(crncyLinkCde);
		dpsSeg.setTailrMadeDepstInd(fieldSet.readString("TLR_MADE_DPS_IND"));
		dpsSeg.setKnockTypeCde(fieldSet.readString("KNOCK_TYPE_CDE"));
		dpsSeg.setExchgRateFixDt(DateHelper.formatDate2String(DateHelper.parseToDate(fieldSet.readString("EXCHG_RATE_FIX_DT"),DateHelper.DATE_SHORT_FORMAT_WITH_NUM_MONTH), DateHelper.DEFAULT_DATE_FORMAT));
		dpsSeg.setFactrConvRate(DecimalHelper.trimZero(fieldSet.readString("CONV_FACTR_RATE")));
		dpsSeg.setIntRate(DecimalHelper.trimZero(String.valueOf(fieldSet.readString("INT_PCT"))));
		dpsSeg.setExchgSpotRate(DecimalHelper.trimZero(fieldSet.readString("EXCHG_SPOT_RATE")));
		dpsSeg.setExchgInitRate(DecimalHelper.trimZero(fieldSet.readString("EXCHG_INIT_RATE")));
		dpsSeg.setExchgBreakEvenRate(DecimalHelper.trimZero(fieldSet.readString("EXCHG_BKVN_RATE")));
		dpsSeg.setRtnOptCde(fieldSet.readString("RTRN_OPTN"));
		dpsSeg.setCcyFromCde(fieldSet.readString("SRC_CRNCY"));
		dpsSeg.setCcyToCde(fieldSet.readString("TRG_CRNCY"));
		dps.setDpsSeg(dpsSeg);
		
		RecDtTmSeg recDtTm = new RecDtTmSeg();
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		recDtTm.setRecCreatDtTm(dateTime);
		recDtTm.setRecUpdtDtTm(dateTime);
		recDtTm.setTimeZone(GMTStr);
		dps.setRecDtTmSeg(recDtTm);
		
		return dps;
	}
}
