package com.dummy.wpc.datadaptor.mapper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.dummy.wpc.batch.xml.CreditRtingSeg;
import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.DebtInstmSeg;
import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.YieldHistSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.reader.Sheet;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;


public class BondProdRowMapper extends GnrcProdRowMapper {
    
    @Override
    public MultiWriterObj mapRow(Sheet sheet, Map<String, String> mappings) throws Exception {
        
        MultiWriterObj multiWriterObj = new MultiWriterObj();
        
        DebtInstm bond = new DebtInstm();
        
        bond.setProdAltNumSeg(parseProdAltNumSeg(sheet, mappings));
        bond.setProdKeySeg(parseProdKeySeg(sheet, mappings));
        bond.setRecDtTmSeg(parseRecDtTmSeg(sheet, mappings));
        bond.setProdInfoSeg(parseProdInfo(sheet, mappings));
        
        updateProdInfo(sheet, mappings, bond);
        
        bond.setDebtInstmSeg(convertBondProd(sheet, mappings));
        
        String[] ctryList = loadReplicaCtryList(getJobCode());
        for (String ctry : ctryList) {
            String grbMebCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.REPLICATION_GROUP_MEMBER_CDE
                + Const.PERIOD + ctry);
            DebtInstm instance = new DebtInstm();
            BeanUtils.copyProperties(bond, instance);
            instance.setProdKeySeg(replicaProdKeySeg(instance.getProdKeySeg(), ctry, grbMebCde));
            instance.setProdInfoSeg(assignConfigurableValue(instance.getProdInfoSeg(), ctry, grbMebCde));
            multiWriterObj.addObj(instance);
        }
        return multiWriterObj;
    }
    
    private ProdInfoSeg updateProdInfo(Sheet sheet, Map<String, String> mappings, DebtInstm bond) {
        ProdInfoSeg prodInfoSeg = bond.getProdInfoSeg();
        
        if(prodInfoSeg != null) {
            prodInfoSeg.setAddInfo(mappings.get(Const.PROD_NARR_TEXT));
            prodInfoSeg.setAddPLLInfo(mappings.get(Const.PROD_NARR_PLL_TEXT));
            prodInfoSeg.setAddSLLInfo(mappings.get(Const.PROD_NARR_SLL_TEXT));
        }
        
        return prodInfoSeg;
    }
    
    private DebtInstmSeg convertBondProd(Sheet sheet, Map<String, String> mappings) throws ParseException {
        DebtInstmSeg seg = new DebtInstmSeg();
        seg.setIsrBndNme(mappings.get(Const.ISR_BOND_NAME));
        seg.setIssueNum(mappings.get(Const.ISSUE_NUM));
        seg.setProdIssDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.PROD_ISS_DT)));
        seg.setPdcyCoupnPymtCd(mappings.get(Const.PDCY_COUPN_PYMT_CDE));
        seg.setCoupnAnnlRt(mappings.get(Const.COUPN_ANNL_RATE));
        seg.setCoupnExtInstmRt(mappings.get(Const.COUPN_EXT_INSTM_RATE));
        seg.setPymtCoupnNextDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.PYMT_COUPN_NEXT_DT)));
        seg.setFlexMatOptInd(mappings.get(Const.FLEX_MAT_OPT_IND));
        seg.setIntIndAccrAmt(mappings.get(Const.INT_IND_ACCR_AMT));
        seg.setInvstIncMinAmt(mappings.get(Const.INVST_INCRM_MIN_AMT));
        seg.setProdBodLotQtyCnt(mappings.get(Const.PROD_BOD_LOT_QTY_CNT));
        seg.setMturExtDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.MTUR_EXT_DT)));
        seg.setSubDebtInd(mappings.get(Const.SUB_DEBT_IND));
        seg.setBondStatCde(mappings.get(Const.BOND_STAT_CDE));
        seg.setCtryBondIssueCde(mappings.get(Const.CTRY_BOND_ISSUE_CDE));
        seg.setGrntrName(mappings.get(Const.GRNTR_NAME));
        seg.setCptlTierText(mappings.get(Const.CPTL_TIER_TEXT));
        seg.setCoupnType(mappings.get(Const.COUPN_TYPE));
        seg.setCoupnPrevRt(mappings.get(Const.COUPN_PREV_RATE));
        seg.setIndexFltRtNme(mappings.get(Const.INDEX_FLT_RATE_NAME));
        seg.setBondFltSprdRt(mappings.get(Const.BOND_FLT_SPRD_RATE));
        seg.setCoupnCurrStartDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.COUPN_CURR_START_DT)));
        seg.setCoupnPrevStartDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.COUPN_PREV_START_DT)));
        seg.setBondCallNextDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.BOND_CALL_NEXT_DT)));
        seg.setIntBassiCalcText(mappings.get(Const.INT_BASIS_CALC_TEXT));
        seg.setIntAccrDayCnt(mappings.get(Const.INT_ACCR_DAY_CNT));
        seg.setInvstSoldLestAmt(mappings.get(Const.INVST_SOLD_LEST_AMT));
        seg.setInvstIncrmSoldAmt(mappings.get(Const.INVST_INCRM_SOLD_AMT));
        seg.setShrBidCnt(mappings.get(Const.SHR_BID_CNT));
        seg.setShrOffrCnt(mappings.get(Const.SHR_OFFR_CNT));
        seg.setProdClsBidPrcAmt(mappings.get(Const.PROD_CLOSE_BID_PRC_AMT));
        seg.setProdClsOffrPrcAmt(mappings.get(Const.PROD_CLOSE_OFFR_PRC_AMT));
        seg.setBondCloseDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.BOND_CLOSE_DT)));
        seg.setBondSetlDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.BOND_SETL_DT)));
        seg.setDscntMrgnBselPct(mappings.get(Const.DSCNT_MRGN_BSEL_PCT));
        seg.setDscntMrgnBbuyPct(mappings.get(Const.DSCNT_MRGN_BBUY_PCT));
        
        seg.setPrcBondRecvDtTm(DateHelper.convertExcelDateTime2XmlFormat(mappings.get(Const.PRC_BOND_RECV_DT_TM)));
        seg.setYieldOfferText(mappings.get(Const.YIELD_OFFER_TEXT));
        seg.setCoupnAnnlText(mappings.get(Const.COUPN_ANNL_TEXT));
        
        seg.setYieldHistSeg(parseYieldHist(sheet, mappings));
        
        seg.setCreditRtingSeg(parseCreditRting(sheet, mappings));
        
        seg.setIsrDesc(mappings.get(Const.ISR_DESC));
        seg.setSrTypeCde(mappings.get(Const.SR_TYPE_CDE));
        seg.setIsrPllDesc(mappings.get(Const.ISR_PLL_DESC));
        seg.setIsrSllDesc(mappings.get(Const.ISR_SLL_DESC));
        
        return seg;
    }
    
    private YieldHistSeg[] parseYieldHist(Sheet sheet, Map<String, String> mappings) {
        String yieldDate = DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.YIELD_DT));
        
        List<YieldHistSeg> segs = new ArrayList<YieldHistSeg>();
        if(StringUtils.isNotBlank(yieldDate)) {
            YieldHistSeg seg = new YieldHistSeg();
            
            seg.setYieldTypeCde(Const.YIELD_TYPE_DAILY);
            seg.setYieldDt(yieldDate);
            seg.setYieldEffDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.YIELD_EFF_DT)));
            seg.setYieldBidPct(mappings.get(Const.YIELD_BID_PCT));
            seg.setYieldToCallBidPct(mappings.get(Const.YIELD_TO_CALL_BID_PCT));
            seg.setYieldToMturBidPct(mappings.get(Const.YIELD_TO_MTUR_BID_PCT));
            seg.setYieldBidClosePct(mappings.get(Const.YIELD_BID_CLOSE_PCT));
            seg.setYieldOfferPct(mappings.get(Const.YIELD_OFFER_PCT));
            seg.setYieldToCallOfferPct(mappings.get(Const.YIELD_TO_CALL_OFFER_PCT));
            seg.setYieldToMturOfferText(mappings.get(Const.YIELD_TO_MTUR_OFFER_TEXT));
            seg.setYieldOfferClosePct(mappings.get(Const.YIELD_OFFER_CLOSE_PCT));
            segs.add(seg);
        }
        
        return segs.toArray(new YieldHistSeg[segs.size()]);
    }
    
    private CreditRtingSeg[] parseCreditRting(Sheet sheet, Map<String, String> mappings) {
        
        List<CreditRtingSeg> segs = new ArrayList<CreditRtingSeg>();
        
        for (int index = 1; index <= 3; index++) {
            String rtngAgcyCde = mappings.get(Const.CREDIT_RTNG_AGCY_CDE + Const.UNDERLINE + index);
            String rtngCde = mappings.get(Const.CREDIT_RTNG_CDE + Const.UNDERLINE + index);
            if (StringUtils.isNotBlank(rtngAgcyCde) && StringUtils.isNotBlank(rtngCde)) {
                CreditRtingSeg seg = new CreditRtingSeg();
                seg.setCreditRtingAgcyCde(rtngAgcyCde);
                seg.setCreditRtingCde(rtngCde);
                segs.add(seg);
            }
        }
        
        return segs.toArray(new CreditRtingSeg[segs.size()]);
        
    }
}
