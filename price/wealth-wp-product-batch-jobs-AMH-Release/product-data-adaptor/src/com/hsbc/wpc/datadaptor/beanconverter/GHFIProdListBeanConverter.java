package com.dummy.wpc.datadaptor.beanconverter;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.Eli;
import com.dummy.wpc.batch.xml.EliSeg;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdAltNumSeg;
import com.dummy.wpc.batch.xml.ProdAsetUndlSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.ProdPrcSeg;
import com.dummy.wpc.batch.xml.ProdUserDefSeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.batch.xml.StockInstmSeg;
import com.dummy.wpc.batch.xml.StockPrcSeg;
import com.dummy.wpc.batch.xml.Warr;
import com.dummy.wpc.batch.xml.WarrSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.DecimalHelper;
import com.dummy.wpc.datadaptor.util.ProdTypCdeMappingHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;


public class GHFIProdListBeanConverter extends AbstractBeanConverter {
    
    private String ctryCde;
    
    private String orgnCde;
    
    private String GMTStr;
    
    private String emptyStr;
    
    private String dateTime;
    
    private String internalProdTypeCode;
    
    private String externalProdTypeCode;
    
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String DATE_FORMAT = "yyyyMMdd";
    
    public Object convert(Object source) {
        ctryCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CTRY_REC_CDE);
        orgnCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRP_MEMBR_REC_CDE);
        GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ctryCde, orgnCde));
        emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.EMPTY_STR);
        dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
        
        ProductEntity entity = (ProductEntity) source;
        MultiWriterObj multiWriterObj = new MultiWriterObj();
        
        externalProdTypeCode = entity.getProdTypeCde();
        internalProdTypeCode = ProdTypCdeMappingHelper.getInternalProdTypCde(ctryCde, orgnCde, externalProdTypeCode);
        
        ProdKeySeg prodKey = new ProdKeySeg();
        prodKey.setCtryRecCde(entity.getCtryCde());
        prodKey.setGrpMembrRecCde(entity.getOrgnCde());
        prodKey.setProdTypeCde(StringUtils.stripToNull(entity.getProdTypeCde()));
        prodKey.setProdCde(StringUtils.stripToNull(entity.getProdCde()));
        
        RecDtTmSeg recDtTm = new RecDtTmSeg();
        recDtTm.setRecCreatDtTm(dateTime);
        recDtTm.setRecUpdtDtTm(dateTime);
        recDtTm.setTimeZone(GMTStr);
        
        String supportProdTypeCodeList = ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST);
        String supportPriceTypeCodeList = ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.SUPPORT_PRICE_TYPE_CODE_LIST);
        if (StringUtils.isBlank(supportProdTypeCodeList)) {
            String configKey = getJobCode() + "." + ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST;
//            logger.error(configKey + " can't be null, need include export interface type code.");
            throw new IllegalArgumentException(configKey + " can't be null, need include export interface type code.");
        }
        String[] typeCodes = supportProdTypeCodeList.split(",");
        List<String> supportTypeList = Arrays.asList(typeCodes);
        
        List<String> supportPriceTypeList = null;
        if (StringUtils.isNotBlank(supportPriceTypeCodeList)) {
            String[] priceTypes = supportPriceTypeCodeList.split(",");
            supportPriceTypeList = Arrays.asList(priceTypes);
        }
        
        String secTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_TYPE_CDE_SEC);
        String wrtsTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_TYPE_CDE_WRTS);
        String eliTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_TYPE_CDE_ELI);
        String snTypeCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_TYPE_CDE_SN);
        
        StockInstm sec = null;
        Warr wrts = null;
        Eli eli = null;
        GnrcProd sn = null;
        ProdPrc prodSECPrc = null;
        ProdPrc prodWRTSPrc = null;
        ProdPrc prodSNPrc = null;
        
        // if the record NOT in the supported type list, then directly skip
        if (!supportTypeList.contains(internalProdTypeCode)) {
            return multiWriterObj;
        }
        if (StringUtils.equals(internalProdTypeCode, secTypeCde)) {
            sec = convertSec(entity, prodKey, recDtTm);
            // ProdPrc
            if (supportPriceTypeList != null && supportPriceTypeList.contains(secTypeCde)) {
                prodSECPrc = convertSecProdPrc(entity, prodKey, recDtTm);
            }
            
            multiWriterObj.addObj(sec);
            multiWriterObj.addObj(prodSECPrc);
        } else if (StringUtils.equals(internalProdTypeCode, wrtsTypeCde)) {
            // Notice: not tested as the service not in use
            wrts = convertWrts(entity, prodKey, recDtTm);
            // ProdPrc
            if (supportPriceTypeList != null && supportPriceTypeList.contains(wrtsTypeCde)) {
                prodWRTSPrc = convertWrtsProdPrc(entity, prodKey, recDtTm);
            }
            
            multiWriterObj.addObj(wrts);
            multiWriterObj.addObj(prodWRTSPrc);
        } else if ((StringUtils.equals(internalProdTypeCode, eliTypeCde)) || (StringUtils.equals(internalProdTypeCode, snTypeCde))) {
            // both eli and sn using this object, logic from GHFI
            eli = convertEli(entity, prodKey, recDtTm);
            multiWriterObj.addObj(eli);
            // ProdPrc
            if (supportPriceTypeList != null && supportPriceTypeList.contains(snTypeCde)) {
                prodSNPrc = convertSnProdPrc(entity, prodKey, recDtTm);
                multiWriterObj.addObj(prodSNPrc);
            }
        } else {
            // this is not the real sn, it could be bond or others
//            sn = initGnrcProd();
            sn = convetSnProd(entity, prodKey, recDtTm);
            multiWriterObj.addObj(sn);
        }
        
        return multiWriterObj;
    }
    
    private Warr convertWrts(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
        Warr wrts = new Warr();
        // ProdKeySeg
        wrts.setProdKeySeg(prodKey);
        
        // ProdAltNumSeg
        ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
        prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
        prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
        wrts.addProdAltNumSeg(prodAltNumSeg_P);
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
        prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
        wrts.addProdAltNumSeg(prodAltNumSeg_M);
        
        // ProdInfoSeg
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        // ProdAsetUndlSeg
        ProdAsetUndlSeg undlSeg = new ProdAsetUndlSeg();
        undlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ASET_UNDL_CDE_SEQ_NUM_1));
        undlSeg.setAsetUndlCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ASET_UNDL_CDE));
        prodInfoSeg.addProdAsetUndlSeg(undlSeg);
        
        prodInfoSeg.setMktInvstCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MKT_INVST_CDE));
        prodInfoSeg.setAllowBuyUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_PROD_IND));
        prodInfoSeg.setAllowBuyAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_AMT_PROD_IND));
        prodInfoSeg.setAllowSellUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_UT_PROD_IND));
        prodInfoSeg.setAllowSellAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_AMT_PROD_IND));
        prodInfoSeg.setAllowSellMipUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_UT_PROD_IND));
        prodInfoSeg.setAllowSellMipAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwInUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwInAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwOutUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_UT_PROD_IND));
        prodInfoSeg.setAllowSwOutAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SW_IN_AMT_PROD_IND));
        prodInfoSeg.setIncmCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INCM_CHAR_PROD_IND));
        prodInfoSeg.setCptlGurntProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CPTL_GURNT_PROD_IND));
        prodInfoSeg.setGrwthCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRWTH_CHAR_PROD_IND));
        prodInfoSeg.setPrtyProdSrchRsultNum(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.PRTY_PROD_SRCH_RSULT_NUM));
        prodInfoSeg.setAvailMktInfoInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.AVAIL_MKT_INFO_IND));
        prodInfoSeg.setDmyProdSubtpRecInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DMY_PROD_SUBTP_REC_IND));
        prodInfoSeg.setDispComProdSrchInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DISP_COM_PROD_SRCH_IND));
        prodInfoSeg.setMrkToMktInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MRK_TO_MKT_IND));
        prodInfoSeg.setProdSubtpCde(StringUtils.stripToNull(entity.getProdSubtpCde()));
        prodInfoSeg.setProdName(StringUtils.stripToNull(entity.getProdName()));
        prodInfoSeg.setProdPllName(StringUtils.stripToNull(entity.getProdPllName()));
        prodInfoSeg.setProdShrtName(StringUtils.stripToNull(entity.getShrtName()));
        prodInfoSeg.setProdShrtPllName(StringUtils.stripToNull(entity.getShrtPllName()));
        prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(entity.getStatCde()));
        prodInfoSeg.setCcyProdCde(StringUtils.stripToNull(entity.getCrncyCde()));
        prodInfoSeg.setRiskLvlCde(StringUtils.stripToNull(entity.getRiskLvlCde()));
        prodInfoSeg.setPrdProdCde(StringUtils.stripToNull(entity.getPrdProdCde()));
        prodInfoSeg.setPrdProdNum(DecimalHelper.bigDecimal2String(entity.getPrdProdNum()));
        prodInfoSeg.setProdLnchDt(entity.getLaunchDt());
        prodInfoSeg.setProdMturDt(entity.getExprDt());
        prodInfoSeg.setDivrNum(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()));
        prodInfoSeg.setProdIssueCrosReferDt(entity.getIssDt());
        wrts.setProdInfoSeg(prodInfoSeg);
        
        WarrSeg wrtsSeg = new WarrSeg();
        wrtsSeg.setProdPrcChngPct(DecimalHelper.bigDecimal2String(entity.getProdPrcChngPct()));
        wrtsSeg.setProdBodLotQtyCnt(DecimalHelper.bigDecimal2String(entity.getLotSizeNum()));
        wrtsSeg.setProdTdyHighPrcAmt(DecimalHelper.bigDecimal2String(entity.getProdPrcMaxAmt()));
        wrtsSeg.setProdTdyLowPrcAmt(DecimalHelper.bigDecimal2String(entity.getProdPrcMinAmt()));
        wrtsSeg.setShareTrdCnt(DecimalHelper.bigDecimal2String(entity.getShareTradeCnt()));
        wrtsSeg.setProdTrnvrAmt(DecimalHelper.bigDecimal2String(entity.getShareTradeAmt()));
        wrtsSeg.setProdClosePrcAmt(DecimalHelper.bigDecimal2String(entity.getProdPrcCloseAmt()));
        wrts.setWarrSeg(wrtsSeg);
        
        // RecDtTmSeg
        wrts.setRecDtTmSeg(recDtTm);
        
        return wrts;
    }
    
    private StockInstm convertSec(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
        StockInstm sec = new StockInstm();
        // ProdKeySeg
        sec.setProdKeySeg(prodKey);
        
        // ProdAltNumSeg
        ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
        prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
        prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
        sec.addProdAltNumSeg(prodAltNumSeg_P);
        
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
        prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
        sec.addProdAltNumSeg(prodAltNumSeg_M);
        
        String isinCode = entity.getISINCde();
        if (StringUtils.isNotBlank(isinCode)) {
            ProdAltNumSeg prodAltNumSeg_I = new ProdAltNumSeg();
            prodAltNumSeg_I.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_I));
            prodAltNumSeg_I.setProdAltNum(isinCode);
            sec.addProdAltNumSeg(prodAltNumSeg_I);
        }
        
        // ProdInfoSeg
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        
        prodInfoSeg.setMktInvstCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MKT_INVST_CDE));
        prodInfoSeg.setAllowBuyUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_PROD_IND));
        prodInfoSeg.setAllowBuyAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_AMT_PROD_IND));
        
        prodInfoSeg.setAllowSellUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_UT_PROD_IND));
        prodInfoSeg
            .setAllowSellAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_AMT_PROD_IND));
        
        prodInfoSeg.setAllowSellMipUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_UT_PROD_IND));
        prodInfoSeg.setAllowSellMipAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        
        prodInfoSeg.setAllowSwInUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwInAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        
        prodInfoSeg
            .setAllowSwOutUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_UT_PROD_IND));
        prodInfoSeg.setAllowSwOutAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SW_IN_AMT_PROD_IND));
        
        prodInfoSeg.setIncmCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INCM_CHAR_PROD_IND));
        prodInfoSeg.setCptlGurntProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CPTL_GURNT_PROD_IND));
        prodInfoSeg.setGrwthCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRWTH_CHAR_PROD_IND));
        prodInfoSeg.setPrtyProdSrchRsultNum(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.PRTY_PROD_SRCH_RSULT_NUM));
        
        prodInfoSeg.setAvailMktInfoInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.AVAIL_MKT_INFO_IND));
        
        prodInfoSeg.setDmyProdSubtpRecInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DMY_PROD_SUBTP_REC_IND));
        prodInfoSeg.setDispComProdSrchInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DISP_COM_PROD_SRCH_IND));
        
        prodInfoSeg.setMrkToMktInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MRK_TO_MKT_IND));
        prodInfoSeg.setProdSubtpCde(StringUtils.stripToNull(entity.getProdSubtpCde()));
        prodInfoSeg.setProdName(StringUtils.stripToNull(entity.getProdName()));
        prodInfoSeg.setProdPllName(StringUtils.stripToNull(entity.getProdPllName()));
        prodInfoSeg.setProdShrtName(StringUtils.stripToNull(entity.getShrtName()));
        prodInfoSeg.setProdShrtPllName(StringUtils.stripToNull(entity.getShrtPllName()));
        prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(convertProductStatus(entity.getStatCde())));
        prodInfoSeg.setCcyProdCde(StringUtils.stripToNull(entity.getCrncyCde()));
        prodInfoSeg.setRiskLvlCde(StringUtils.stripToNull(entity.getRiskLvlCde()));
        
        prodInfoSeg.setProdLnchDt(StringUtils.stripToNull(entity.getLaunchDt()));
        prodInfoSeg.setProdMturDt(StringUtils.stripToNull(entity.getExprDt()));
        prodInfoSeg.setDivrNum(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()));
        prodInfoSeg.setProdIssueCrosReferDt(StringUtils.stripToNull(entity.getIssDt()));
        
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
        prodInfoSeg.setCcyInvstCde(emptyStr);
        prodInfoSeg.setQtyTypeCde(emptyStr);
        prodInfoSeg.setProdLocCde(emptyStr);
        prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
        prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
        prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
        prodInfoSeg.setAumChrgProdInd(emptyStr);
        
        // ProdAsetUndlSeg
        ProdAsetUndlSeg undlSeg = new ProdAsetUndlSeg();
        undlSeg.setAsetUndlCdeSeqNum(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ASET_UNDL_CDE_SEQ_NUM_1));
        undlSeg.setAsetUndlCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ASET_UNDL_CDE));
        prodInfoSeg.addProdAsetUndlSeg(undlSeg);
        
        sec.setProdInfoSeg(prodInfoSeg);
        
        // StockInstmSeg
        StockInstmSeg secSeg = new StockInstmSeg();
        secSeg.setInvstMipMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INVST_MIP_MIN_AMT));
        secSeg.setInvstMipIncrmMinAmt(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INVST_MIP_INCRM_MIN_AMT));
        secSeg.setSuptAuctnTrdInd(emptyStr);
        secSeg.setMktProdTrdCde(emptyStr);
        String mrgnTrdInd = StringUtils.stripToNull(entity.getMrgnTrdInd());
        String mrgn_trd_ind = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MRGN_TRD_IND);
        secSeg.setMrgnTrdInd(mrgnTrdInd == null ? mrgn_trd_ind : mrgnTrdInd);
        secSeg.setMrgnSecODPct(DecimalHelper.bigDecimal2String(entity.getMrgnSecOvdftPct()));
        String suptAuctnTrdInd = StringUtils.stripToNull(entity.getAuctnTrdInd());
        String supt_auctn_trd_ind = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.SUPT_AUCTN_TRD_IND);
        secSeg.setSuptAuctnTrdInd(suptAuctnTrdInd == null ? supt_auctn_trd_ind : suptAuctnTrdInd);
        secSeg.setStopLossMinPct(DecimalHelper.bigDecimal2String(entity.getStopLossMinPct()));
        secSeg.setStopLossMaxPct(DecimalHelper.bigDecimal2String(entity.getStopLossMaxPct()));
        secSeg.setSprdStopLossMinCnt(DecimalHelper.bigDecimal2String(entity.getSprdSplsMinCnt()));
        secSeg.setSprdStopLossMaxCnt(DecimalHelper.bigDecimal2String(entity.getSprdSplsMaxCnt()));
        String prod_bod_lot_qty_cnt = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_BOD_LOT_QTY_CNT);
        String lotSizeNum = DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getLotSizeNum()));
        secSeg.setProdBodLotQtyCnt(lotSizeNum == null ? prod_bod_lot_qty_cnt : lotSizeNum);
        String mktProdTrdCde = StringUtils.stripToNull(entity.getExchgCde());
        String mkt_prod_trd_cde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MKT_PROD_TRD_CDE);
        secSeg.setMktProdTrdCde(mktProdTrdCde == null ? mkt_prod_trd_cde : mktProdTrdCde);
        
        secSeg.setBodLotProdInd(emptyStr);
        secSeg.setTrdLimitInd(emptyStr);
        secSeg.setPsblProdBorwInd(emptyStr);
        secSeg.setAllowProdLendInd(emptyStr);
        secSeg.setPoolProdInd(emptyStr);
        secSeg.setScripOnlyProdInd(emptyStr);
        secSeg.setSuptAtmcTrdInd(emptyStr);
        secSeg.setSuptNetSetlInd(emptyStr);
        secSeg.setSuptStopLossOrdInd(emptyStr);
        secSeg.setSuptWinWinOrdInd(emptyStr);
        secSeg.setSuptAllIn1OrdInd(emptyStr);
        secSeg.setSuptProdSpltInd(emptyStr);
        secSeg.setExclLimitCalcInd(emptyStr);
        secSeg.setProdMktStatChngLaDt(emptyStr);
        secSeg.setPrcVarCde(emptyStr);
        
        sec.setStockInstmSeg(secSeg);
        
        // RecDtTmSeg
        sec.setRecDtTmSeg(recDtTm);
        return sec;
    }
    
    private Eli convertEli(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {

        Eli eli = new Eli();
        // ProdKeySeg
        eli.setProdKeySeg(prodKey);
        
        // ProdAltNumSeg
        ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
        prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
        prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
        eli.addProdAltNumSeg(prodAltNumSeg_P);
        
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
        prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
        eli.addProdAltNumSeg(prodAltNumSeg_M);
        
        // ProdInfoSeg
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        
        prodInfoSeg.setMktInvstCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MKT_INVST_CDE));
        prodInfoSeg.setAllowBuyUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_PROD_IND));
        prodInfoSeg.setAllowBuyAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_BUY_AMT_PROD_IND));
        prodInfoSeg.setAllowSellUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_UT_PROD_IND));
        prodInfoSeg.setAllowSellAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SELL_AMT_PROD_IND));
        prodInfoSeg.setAllowSellMipUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_UT_PROD_IND));
        prodInfoSeg.setAllowSellMipAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwInUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg.setAllowSwInAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SELL_MIP_AMT_PROD_IND));
        prodInfoSeg
            .setAllowSwOutUtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.ALLOW_SW_IN_UT_PROD_IND));
        prodInfoSeg.setAllowSwOutAmtProdInd(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.ALLOW_SW_IN_AMT_PROD_IND));
        prodInfoSeg.setIncmCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.INCM_CHAR_PROD_IND));
        prodInfoSeg.setCptlGurntProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CPTL_GURNT_PROD_IND));
        prodInfoSeg.setGrwthCharProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.GRWTH_CHAR_PROD_IND));
        prodInfoSeg.setPrtyProdSrchRsultNum(ConstantsPropertiesHelper.getValue(getJobCode(),
            ConfigConstant.PRTY_PROD_SRCH_RSULT_NUM));
        prodInfoSeg.setAvailMktInfoInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.AVAIL_MKT_INFO_IND));
        prodInfoSeg.setDmyProdSubtpRecInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DMY_PROD_SUBTP_REC_IND));
        prodInfoSeg.setDispComProdSrchInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.DISP_COM_PROD_SRCH_IND));
        prodInfoSeg.setMrkToMktInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.MRK_TO_MKT_IND));
        
        prodInfoSeg.setProdSubtpCde(StringUtils.stripToNull(entity.getProdSubtpCde()));
        prodInfoSeg.setProdName(StringUtils.stripToNull(entity.getProdName()));
        prodInfoSeg.setProdPllName(StringUtils.stripToNull(entity.getProdPllName()));
        prodInfoSeg.setProdShrtName(StringUtils.stripToNull(entity.getShrtName()));
        prodInfoSeg.setProdShrtPllName(StringUtils.stripToNull(entity.getShrtPllName()));
        prodInfoSeg.setProdStatCde(StringUtils.trimToEmpty(entity.getStatCde()));
        prodInfoSeg.setCcyProdCde(StringUtils.stripToNull(entity.getCrncyCde()));
        prodInfoSeg.setRiskLvlCde(StringUtils.stripToNull(entity.getRiskLvlCde()));
        prodInfoSeg.setProdLnchDt(entity.getLaunchDt());
        prodInfoSeg.setProdMturDt(entity.getMatDt());
        prodInfoSeg.setDivrNum(DecimalHelper.bigDecimal2String(entity.getPrcDivrNum()));
        
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
        prodInfoSeg.setCcyInvstCde(emptyStr);
        prodInfoSeg.setQtyTypeCde(emptyStr);
        prodInfoSeg.setProdLocCde(emptyStr);
        prodInfoSeg.setSuptRcblCashProdInd(emptyStr);
        prodInfoSeg.setSuptRcblScripProdInd(emptyStr);
        prodInfoSeg.setDcmlPlaceTradeUnitNum(emptyStr);
        prodInfoSeg.setAumChrgProdInd(emptyStr);
        
        List advcRmk = new ArrayList();
        String getter = null;
        Method method = null;
        for (int i = 1; i < 16; i++) {
            getter = "getAdvcRmk" + i;
            try {
                method = entity.getClass().getMethod(getter, null);
                if (method.invoke(entity, null) != null)
                    advcRmk.add(method.invoke(entity, null));
                else
                    break;
            } catch (Exception e) {
                
            }
        }
        prodInfoSeg.setAddInfo(StringUtils.join(advcRmk.toArray(), '\n'));
        
        /* SN handling, For User Defined Fields */
        if (StringUtils.equals(entity.getProdTypeCde(), Const.STRUCTURE_NOTE)) {
            convertSnSpecifiedField(entity, prodInfoSeg);
        }
        
        eli.setProdInfoSeg(prodInfoSeg);
        
        // EliSeg
        EliSeg eliSeg = new EliSeg();
        eliSeg.setLnchProdInd(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.LNCH_PROD_IND));
        eliSeg.setProdExtnlCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_EXTNL_CDE));
        
        eliSeg.setDscntSellPct(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt()));
        eliSeg.setValnDt(StringUtils.stripToNull(entity.getPriceDt()));
        
        /* SN handling, won't set ELI segment */
        if (StringUtils.equals(entity.getProdTypeCde(), Const.STRUCTURE_NOTE)) {
            eliSeg = new EliSeg();
        }
        
        eli.setEliSeg(eliSeg);
        
        // RecDtTmSeg
        eli.setRecDtTmSeg(recDtTm);
        
        return eli;
        
    }
    
    private GnrcProd convetSnProd(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {

        GnrcProd prod = new GnrcProd();
        // ProdKeySeg
        prod.setProdKeySeg(prodKey);
        // ProdAltNumSeg
        ProdAltNumSeg prodAltNumSeg_P = new ProdAltNumSeg();
        prodAltNumSeg_P.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(),ConfigConstant.PROD_CDE_ALT_CLASS_CDE_P));
        prodAltNumSeg_P.setProdAltNum(entity.getProdCde());
        prod.addProdAltNumSeg(prodAltNumSeg_P);
        ProdAltNumSeg prodAltNumSeg_M = new ProdAltNumSeg();
        prodAltNumSeg_M.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PROD_CDE_ALT_CLASS_CDE_M));
        prodAltNumSeg_M.setProdAltNum(entity.getProdCde());
        prod.addProdAltNumSeg(prodAltNumSeg_M);
        
        // ProdInfoSeg
        ProdInfoSeg prodInfoSeg = new ProdInfoSeg();
        prodInfoSeg.setProdSubtpCde(StringUtils.stripToNull(entity.getProdSubtpCde()));
        prodInfoSeg.setProdShrtName(StringUtils.stripToNull(entity.getShrtName()));
        prodInfoSeg.setProdShrtPllName(StringUtils.stripToNull(entity.getShrtPllName()));
        prod.setProdInfoSeg(prodInfoSeg);
        
        // RecDtTmSeg
        prod.setRecDtTmSeg(recDtTm);
        
        return prod;
    }
    
    private void convertSnSpecifiedField(ProductEntity entity, ProdInfoSeg prodInfoSeg) {
        
        ProdUserDefSeg prodUserDef_12 = new ProdUserDefSeg();
        prodUserDef_12.setFieldTypeCde(ConstantsPropertiesHelper.getValue(getJobCode(), "field_type_cde"));
        prodUserDef_12.setFieldCde(ConstantsPropertiesHelper.getValue(getJobCode(), "field_cde_prefix") + "12");
        
        BigDecimal invstInitMinAmt = entity.getInvstInitMinAmt();
        BigDecimal invstIncrmMinAmt = entity.getInvstIncrmMinAmt();
        if (StringUtils.equals(entity.getCrncyCde(), "JPY")) {
            prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstInitMinAmt)));
            
            prodUserDef_12.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstIncrmMinAmt)));
            prodInfoSeg.addProdUserDefSeg(prodUserDef_12);
        } else {
            if (invstInitMinAmt != null) {
                prodInfoSeg.setInvstInitMinAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstInitMinAmt
                    .movePointLeft(2))));
            }
            if (invstIncrmMinAmt != null) {
                prodUserDef_12.setFieldValueText(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(invstIncrmMinAmt
                    .movePointLeft(2))));
                prodInfoSeg.addProdUserDefSeg(prodUserDef_12);
            }
        }
    }
    
    private ProdAltNumSeg convertProdAltNumSeg(final ProductEntity entity, String altTypeKey) {
        ProdAltNumSeg seg = new ProdAltNumSeg();
        seg.setProdCdeAltClassCde(ConstantsPropertiesHelper.getValue(getJobCode(), altTypeKey));
        seg.setProdAltNum(entity.getProdCde());
        return seg;
    }
    
    private ProdPrc convertSecProdPrc(final ProductEntity entity, final ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
        // ProdPrc
        ProdPrc secPrc = new ProdPrc();
        secPrc.setProdKeySeg(prodKey);
        
        // ProdPrcSeg
        ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
        prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PDCY_PRC_CDE));
        prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        prodPrcSeg.setCcyProdMktPrcCde(StringUtils.stripToNull(entity.getCrncyCde()));
        
        prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getBidPrcAmt())));
        prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
        prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getNomlPrcAmt())));
        
        // StockPrcSeg
        StockPrcSeg stockPrcSeg = new StockPrcSeg();
        stockPrcSeg.setProdPrcChngPct(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcChngPct())));
        stockPrcSeg.setProdPrcChngAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcChngAmt())));
        stockPrcSeg.setProdTdyHighPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcMaxAmt())));
        stockPrcSeg.setProdTdyLowPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getProdPrcMinAmt())));
        stockPrcSeg.setShareTrdCnt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getShareTradeCnt())));
        stockPrcSeg.setProdTrnvrAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getShareTradeAmt())));
        prodPrcSeg.setStockPrcSeg(stockPrcSeg);
        
        // RecDtTmSeg
        prodPrcSeg.setRecDtTmSeg(recDtTm);     
        
        secPrc.addProdPrcSeg(prodPrcSeg);
        return secPrc;
    }
    
    private ProdPrc convertWrtsProdPrc(final ProductEntity entity, final ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
        
        ProdPrc wrtsPrc = new ProdPrc();
        // ProdKeySeg
        wrtsPrc.setProdKeySeg(prodKey);
        
        // ProdPrcSeg
        ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
        
        prodPrcSeg.setPrcEffDt(StringUtils.trimToEmpty(entity.getMktPrcDt()));
        prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PDCY_PRC_CDE));
        prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        prodPrcSeg.setCcyProdMktPrcCde(StringUtils.trimToEmpty(entity.getMktPrcCcy()));
        
        prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getNomlPrcAmt())));
        prodPrcSeg.setProdBidPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getBidPrcAmt())));
        prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
        
        // RecDtTmSeg
        prodPrcSeg.setRecDtTmSeg(recDtTm);
        
        wrtsPrc.addProdPrcSeg(prodPrcSeg);
        
        return wrtsPrc;
    }
    
    private ProdPrc convertSnProdPrc(final ProductEntity entity, final ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
        // ProdPrc
        ProdPrc snPrc = new ProdPrc();
        snPrc.setProdKeySeg(prodKey);
        
        // ProdPrcSeg
        ProdPrcSeg prodPrcSeg = new ProdPrcSeg();
        prodPrcSeg.setPrcEffDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        prodPrcSeg.setPdcyPrcCde(ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.PDCY_PRC_CDE));
        prodPrcSeg.setPrcInpDt(DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATE_FORMAT));
        prodPrcSeg.setCcyProdMktPrcCde(StringUtils.stripToNull(entity.getCrncyCde()));
        
        prodPrcSeg.setProdOffrPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
        prodPrcSeg.setProdMktPrcAmt(DecimalHelper.trimZero(DecimalHelper.bigDecimal2String(entity.getOfferPrcAmt())));
        
        // RecDtTmSeg
        prodPrcSeg.setRecDtTmSeg(recDtTm);     
        
        snPrc.addProdPrcSeg(prodPrcSeg);
        return snPrc;
    }
    
    // will change status A to N, special logic for SEC
    private String convertProductStatus(final String opeProductStatus) {
        String wpcProductStatus = null;
        if ("A".equals(opeProductStatus)) {
            wpcProductStatus = "N";
        } else {
            wpcProductStatus = opeProductStatus;
        }
        return wpcProductStatus;
    }
}
