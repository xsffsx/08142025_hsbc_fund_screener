package com.dummy.wpc.datadaptor.beanconverter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.batch.object.castor.CtryCorr;
import com.dummy.wpc.batch.object.castor.CtryRes;
import com.dummy.wpc.batch.object.castor.CustElig;
import com.dummy.wpc.batch.object.castor.Nat;
import com.dummy.wpc.batch.object.castor.RsrCtryInfo;
import com.dummy.wpc.batch.xml.CustFormEligSeg;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdRestCustCtrySeg;
import com.dummy.wpc.batch.xml.ProdTradElg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

public class CustomerEligibilityBeanConverter extends AbstractBeanConverter {

    private static Logger logger = Logger.getLogger(CustomerEligibilityBeanConverter.class);
    
	public Object convert(Object source) {

		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ConstantsPropertiesHelper.getValue(getJobCode(),"ctry_rec_cde"), ConstantsPropertiesHelper
				.getValue(getJobCode(),ConfigConstant.GRP_MEMBR_REC_CDE)));
		
		CustElig cusElig = (CustElig) source;
		MultiWriterObj multiWriterObj = new MultiWriterObj();
		
		
        String supportProdTypeCodeList = ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST);
        
        if (StringUtils.isBlank(supportProdTypeCodeList)) {
            String configKey = getJobCode() + "." + ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST;
            logger.error(configKey + " can't be null, need include export interface type code.");
            throw new IllegalArgumentException(configKey + " can't be null, need include export interface type code.");
        }
        
        String[] typeCodes = supportProdTypeCodeList.split(",");
        List<String> supportTypeList = Arrays.asList(typeCodes);


        String ctryCode = ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.CTRY_REC_CDE);
        String orgnCode = ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.GRP_MEMBR_REC_CDE);
        String prodTypeCode = cusElig.getProdTypeCde();
        String prodCode = cusElig.getProdCde();

        ProdKeySeg prodKeySeg = new ProdKeySeg();
        prodKeySeg.setCtryRecCde(ctryCode);
        prodKeySeg.setGrpMembrRecCde(orgnCode);
        prodKeySeg.setProdTypeCde(prodTypeCode);
        prodKeySeg.setProdCde(prodCode);
        prodKeySeg.setProdCdeAltClassCde("P");
        
        String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
        RecDtTmSeg recDtTmSeg = new RecDtTmSeg();
        recDtTmSeg.setRecCreatDtTm(dateTime);
        recDtTmSeg.setRecUpdtDtTm(dateTime);
        recDtTmSeg.setTimeZone(GMTStr);

        GnrcProd gnrcProd = null;
        ProdTradElg prodTradeElig = null;
        
        if (supportTypeList.contains(prodTypeCode)) {
            gnrcProd = conveterGnrcProd(cusElig, prodKeySeg, recDtTmSeg);
            prodTradeElig = conveterProdTradeElig(cusElig, prodKeySeg, recDtTmSeg);
        }
        
        multiWriterObj.addObj(gnrcProd);
        multiWriterObj.addObj(prodTradeElig);

        logger.debug("Convert time:[" + DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT) + "]");

		return multiWriterObj;
	}
	
	private GnrcProd conveterGnrcProd(CustElig cusElig, ProdKeySeg prodKeySeg, RecDtTmSeg recDtTmSeg) {
	    // ------------------------Product----------------------------//
	    String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.EMPTY_STR);
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        prodInfoSeg.setProdSubtpCde(emptyStr);
        prodInfoSeg.setProdName(emptyStr);
        prodInfoSeg.setProdPllName(emptyStr);
        prodInfoSeg.setProdShrtName(emptyStr);
        prodInfoSeg.setProdShrtPllName(emptyStr);
        prodInfoSeg.setProdStatCde(emptyStr);
        prodInfoSeg.setCcyProdCde(emptyStr);
        prodInfoSeg.setRiskLvlCde(cusElig.getRiskLvlCde());
        prodInfoSeg.setProdLnchDt(emptyStr);
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
        prodInfoSeg.setQtyTypeCde(emptyStr);
        prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
        prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
        prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
        prodInfoSeg.setAumChrgProdInd(emptyStr);

        GnrcProd gnrcProd = new GnrcProd();
        gnrcProd.setProdKeySeg(prodKeySeg);
        gnrcProd.setProdInfoSeg(prodInfoSeg);
        gnrcProd.setRecDtTmSeg(recDtTmSeg);
        // ------------------------Product----------------------------//
	    return gnrcProd;
	}
	
	private ProdTradElg conveterProdTradeElig(CustElig cusElig, ProdKeySeg prodKeySeg, RecDtTmSeg recDtTmSeg) {
	    // ----------------------cust elig-------------------------
	    //R(Restricted) and A(Allowed)
	    String resCdeR = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.RESTR_CDE_R);
        String resCdeA = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.RESTR_CDE_A);
	    
	    ProdTradElg prodTradeElig = new ProdTradElg();
        prodTradeElig.setProdKeySeg(prodKeySeg);
        prodTradeElig.setRecDtTmSeg(recDtTmSeg);

        RsrCtryInfo[] rsrCtryInfo = cusElig.getRsrCtryInfo();
        for (RsrCtryInfo rCtryInfo : rsrCtryInfo) {
            if (StringUtils.equals(rCtryInfo.getTypeCde(), "T")) {
                Nat[] nats = rCtryInfo.getNat();
                for (Nat nat : nats) {
                    if (StringUtils.equals(StringUtils.stripToNull(nat.getInd()), "Y")) {
                        ProdRestCustCtrySeg prodRestCustCtrySeg = new ProdRestCustCtrySeg();
                        prodRestCustCtrySeg.setRestrCtryTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),
                            ConfigConstant.RESTR_CTRY_TYPE_CDE_N));
                        prodRestCustCtrySeg.setCtryIsoCde(StringUtils.stripToNull(nat.getCde()));
                        prodRestCustCtrySeg.setRestrCde(resCdeR);
                        
                        prodTradeElig.addProdRestCustCtrySeg(prodRestCustCtrySeg);
                    }
                }
                CtryRes[] ctryRess = rCtryInfo.getCtryRes();
                for (CtryRes ctryRes : ctryRess) {
                    if (StringUtils.equals(StringUtils.stripToNull(ctryRes.getInd()), "Y")
                        || StringUtils.equals(StringUtils.stripToNull(ctryRes.getInd()), "M")) {
                        ProdRestCustCtrySeg prodRestCustCtrySeg = new ProdRestCustCtrySeg();
                        prodRestCustCtrySeg.setRestrCtryTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),
                            ConfigConstant.RESTR_CTRY_TYPE_CDE_R));
                        prodRestCustCtrySeg.setCtryIsoCde(StringUtils.stripToNull(ctryRes.getCde()));
                        if (StringUtils.equals(StringUtils.stripToNull(ctryRes.getInd()), "Y")) {
                            prodRestCustCtrySeg.setRestrCde(resCdeR);
                        } else if (StringUtils.equals(StringUtils.stripToNull(ctryRes.getInd()), "M")) {
                            prodRestCustCtrySeg.setRestrCde(resCdeA);
                        }
                        prodTradeElig.addProdRestCustCtrySeg(prodRestCustCtrySeg);
                    }
                }
                CtryCorr[] ctryCorrs = rCtryInfo.getCtryCorr();
                for (CtryCorr ctryCorr : ctryCorrs) {
                    if (StringUtils.equals(StringUtils.stripToNull(ctryCorr.getInd()), "Y")
                        || StringUtils.equals(StringUtils.stripToNull(ctryCorr.getInd()), "M")) {
                        ProdRestCustCtrySeg prodRestCustCtrySeg = new ProdRestCustCtrySeg();
                        prodRestCustCtrySeg.setRestrCtryTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(),
                            ConfigConstant.RESTR_CTRY_TYPE_CDE_C));
                        prodRestCustCtrySeg.setCtryIsoCde(StringUtils.stripToNull(ctryCorr.getCde()));
                        if (StringUtils.equals(StringUtils.stripToNull(ctryCorr.getInd()), "Y")) {
                            prodRestCustCtrySeg.setRestrCde(resCdeR);
                        } else if (StringUtils.equals(StringUtils.stripToNull(ctryCorr.getInd()), "M")) {
                            prodRestCustCtrySeg.setRestrCde(resCdeA);
                        }
                        prodTradeElig.addProdRestCustCtrySeg(prodRestCustCtrySeg);
                    }
                }               
            }
        }
        List<String> formReqCdeList = new ArrayList<String>();
        if (cusElig.getFormReqCdeCount() > 0) {
            for (int i = 0; i < cusElig.getFormReqCdeCount(); i++) {
            	if(!formReqCdeList.contains(cusElig.getFormReqCde(i))) {
            		formReqCdeList.add(cusElig.getFormReqCde(i));
            		CustFormEligSeg custFormEligSeg = new CustFormEligSeg();
            		custFormEligSeg.setFormReqCde(cusElig.getFormReqCde(i));
            		prodTradeElig.addCustFormEligSeg(custFormEligSeg);
            	}
            }
        }
        // ----------------------cust elig-------------------------
        return prodTradeElig;
    }
}
