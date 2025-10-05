package com.dummy.wpc.datadaptor.mapper;

import java.text.ParseException;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.dummy.wpc.batch.xml.UtTrstInstm;
import com.dummy.wpc.batch.xml.UtTrstInstmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.constant.Const;
import com.dummy.wpc.datadaptor.reader.Sheet;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;


public class UtProdRowMapper extends GnrcProdRowMapper {
    
    @Override
    public MultiWriterObj mapRow(Sheet sheet, Map<String, String> mappings) throws Exception {
        
        MultiWriterObj multiWriterObj = new MultiWriterObj();
        
        UtTrstInstm ut = new UtTrstInstm();

        ut.setProdAltNumSeg(parseProdAltNumSeg(sheet, mappings));
        ut.setProdKeySeg(parseProdKeySeg(sheet, mappings));
        ut.setRecDtTmSeg(parseRecDtTmSeg(sheet, mappings));
        ut.setProdInfoSeg(parseProdInfo(sheet, mappings));

        ut.setUtTrstInstmSeg(convertUtProd(sheet, mappings));
        
        String[] ctryList = loadReplicaCtryList(getJobCode());
        for (String ctry : ctryList) {
            String grbMebCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.REPLICATION_GROUP_MEMBER_CDE
                + Const.PERIOD + ctry);
            UtTrstInstm instance = new UtTrstInstm();
            BeanUtils.copyProperties(ut, instance);
            instance.setProdKeySeg(replicaProdKeySeg(instance.getProdKeySeg(), ctry, grbMebCde));
            instance.setProdInfoSeg(assignConfigurableValue(instance.getProdInfoSeg(), ctry, grbMebCde));
            multiWriterObj.addObj(instance);
        }
        return multiWriterObj;
    }
    
    private UtTrstInstmSeg convertUtProd(Sheet sheet, Map<String, String> mappings) throws ParseException {
        UtTrstInstmSeg seg = new UtTrstInstmSeg();
        
        seg.setFundHouseCde(mappings.get(Const.FUND_HOUSE_CDE));
        seg.setCloseEndFundInd(mappings.get(Const.CLOSE_END_FUND_IND));
        seg.setUtPrcProdLnchAmt(mappings.get(Const.UT_PRC_PROD_LNCH_AMT));
        seg.setFundAmt(mappings.get(Const.FUND_AMT));
        seg.setInvstIncrmMinAmt(mappings.get(Const.INVST_INCRM_MIN_AMT));
        seg.setUtRdmMinNum(mappings.get(Const.UT_RDM_MIN_NUM));
        seg.setRdmMinAmt(mappings.get(Const.RDM_MIN_AMT));
        seg.setUtRtainMinNum(mappings.get(Const.UT_RTAIN_MIN_NUM));
        seg.setFundRtainMinAmt(mappings.get(Const.FUND_RTAIN_MIN_AMT));
        seg.setSuptMipInd(mappings.get(Const.SUPT_MIP_IND));
        
        seg.setInvstMipMinAmt(mappings.get(Const.INVST_MIP_MIN_AMT));
        seg.setInvstMipIncrmMinAmt(mappings.get(Const.INVST_MIP_INCRM_MIN_AMT));
        seg.setUtMipRdmMinNum(mappings.get(Const.UT_MIP_RDM_MIN_NUM));
        seg.setRdmMipMinAmt(mappings.get(Const.RDM_MIP_MIN_AMT));
        seg.setUtMipRtainMinNum(mappings.get(Const.UT_MIP_RTAIN_MIN_NUM));
        seg.setFundMipRtainMinAmt(mappings.get(Const.FUND_MIP_RTAIN_MIN_AMT));
        seg.setRtrnGurntPct(mappings.get(Const.RTRN_GURNT_PCT));
        seg.setSchemChrgCde(mappings.get(Const.SCHEM_CHRG_CDE));
        seg.setChrgInitSalesPct(mappings.get(Const.CHRG_INIT_SALES_PCT));
        seg.setChrgFundSwPct(mappings.get(Const.CHRG_FUND_SW_PCT));
        
        seg.setDscntMaxPct(mappings.get(Const.DSCNT_MAX_PCT));
        
        seg.setOfferStartDtTm(DateHelper.convertExcelDateTime2XmlFormat(mappings.get(Const.OFFER_START_DT_TM)));
        seg.setOfferEndDtTm(DateHelper.convertExcelDateTime2XmlFormat(mappings.get(Const.OFFER_END_DT_TM)));
        seg.setPayCashDivInd(mappings.get(Const.PAY_CASH_DIV_IND));
        seg.setHldayFundNextDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.HLDAY_FUND_NEXT_DT)));
        seg.setScribCtoffNextDtTm(DateHelper.convertExcelDateTime2XmlFormat(mappings.get(Const.SCRIB_CTOFF_NEXT_DT_TM)));
        seg.setRdmCtoffNextDtTm(DateHelper.convertExcelDateTime2XmlFormat(mappings.get(Const.RDM_CTOFF_NEXT_DT_TM)));
        seg.setDealNextDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.DEAL_NEXT_DT)));
        seg.setFundClassCde(mappings.get(Const.FUND_CLASS_CDE));
        seg.setAmcmInd(mappings.get(Const.AMCM_IND));
        
        seg.setDivDclrDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.DIV_DCLR_DT)));
        seg.setDivPymtDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.DIV_PYMT_DT)));
        seg.setAutoSweepFundInd(mappings.get(Const.AUTO_SWEEP_FUND_IND));
        seg.setSpclFundInd(mappings.get(Const.SPCL_FUND_IND));
        seg.setInsuLinkUtTrstInd(mappings.get(Const.INSU_LINK_UT_TRST_IND));
        seg.setProdTrmtDt(DateHelper.convertExcelDate2XmlFormat(mappings.get(Const.PROD_TRMT_DT)));
        seg.setFundSwInMinAmt(mappings.get(Const.FUND_SW_IN_MIN_AMT));
        seg.setFundSwOutMinAmt(mappings.get(Const.FUND_SW_OUT_MIN_AMT));
        seg.setFundSwOutRtainMinAmt(mappings.get(Const.FUND_SW_OUT_RTAIN_MIN_AMT));
        seg.setUtSwOutRtainMinNum(mappings.get(Const.UT_SW_OUT_RTAIN_MIN_NUM));
        
        seg.setFundSwOutMaxAmt(mappings.get(Const.FUND_SW_OUT_MAX_AMT));
        seg.setTranMaxAmt(mappings.get(Const.TRAN_MAX_AMT));
        seg.setIncmHandlOptCde(mappings.get(Const.INCM_HANDL_OPT_CDE));
        seg.setAutoRenewFundInd(mappings.get(Const.AUTO_RENEW_FUND_IND));
        seg.setFundValnTm(mappings.get(Const.FUND_VALN_TM));
        seg.setProdShoreLocCde(mappings.get(Const.PROD_SHORE_LOC_CDE));
        seg.setDivYieldPct(mappings.get(Const.DIV_YIELD_PCT));
        
        return seg;
    }
}
