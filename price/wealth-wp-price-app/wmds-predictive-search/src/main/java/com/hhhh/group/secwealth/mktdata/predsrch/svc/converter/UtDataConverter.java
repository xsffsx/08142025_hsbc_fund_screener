/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.xml.bind.Unmarshaller;

import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.ProdInfoSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefDataLst;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut.UtTrstInstm;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut.UtTrstInstmLst;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.ut.UtTrstInstmSeg;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;

public class UtDataConverter implements DataConverter {

    private Unmarshaller unmarshaller;

    private long delayConversion;

    public void setUnmarshaller(final Unmarshaller shaller) throws Exception {
        this.unmarshaller = shaller;
    }

    public void setDelayConversion(final long deplay) throws Exception {
        this.delayConversion = deplay;
    }

    /**
     * Convert UT data file to UT java object list
     */
    public void handleData(final File file, final RefDataLst refDataList, final DataSiteEntity entity, final boolean skipErrRecord)
        throws Exception {
        UtTrstInstmLst lst = (UtTrstInstmLst) this.unmarshaller
            .unmarshal(new InputStreamReader(new FileInputStream(file), CommonConstants.CODING_UTF8));
        if (null != lst && null != lst.getUtTrstInstm()) {
            LogUtil.debug(UtDataConverter.class, "Start to handle data for the file=" + file.getName());
            List<UtTrstInstm> instmList = lst.getUtTrstInstm();
            List<SearchableObject> objList = entity.getObjList();
            int size = instmList.size();
            int symbolErr = 0;
            int codeErr = 0;
            for (int i = 0; i < size; i++) {
                // errors[0] check for symbol
                // errors[1] check for T/R code
                boolean[] errors = {false, false};
                UtTrstInstm obj = instmList.get(i);
                SearchableObject so =
                    CommonConverter.handleCommonData(obj.getProdInfoSeg(), obj.getProdKeySeg(), obj.getProdAltNumSeg(), errors);

                if (errors[0]) {
                    symbolErr++;
                } else if (errors[1]) {
                    codeErr++;
                }

                if (errors[0] && skipErrRecord) {
                    LogUtil.error(UtDataConverter.class,
                        file.getName() + "|Skip to save the record without symbol|prodName=" + so.getProductName());
                } else {
                    UtTrstInstmSeg seg = obj.getUtTrstInstmSeg();
                    ProdInfoSeg prodInfoSeg = obj.getProdInfoSeg();
                    so.setProdShoreLocCde(seg.getProdShoreLocCde());
                    so.setFundHouseCde(seg.getFundHouseCde());
                    so.setFundHouseCde_analyzed(seg.getFundHouseCde());
                    // from refData
                    so.setParentAssetClass(
                        CommonConverter.getParentAssetClass(refDataList, prodInfoSeg.getProdAsetVoltlClassSeg()));
                    // set switch fund group
                    so.setSwitchableGroup_analyzed(CommonConverter.getKeyList(prodInfoSeg, "switchFundGrpList"));
                    // set channel Restrict Group
                    so.setResChannelCde(CommonConverter.getKeyList(prodInfoSeg, "resChannelCde"));
                    so.setResChannelCde_analyzed(CommonConverter.getKeyList(prodInfoSeg, "resChannelCde"));
                    // set chanl Code
                    so.setChanlCde(CommonConverter.getKeyList(prodInfoSeg, "chanlCde"));
                    so.setChanlCde_analyzed(CommonConverter.getKeyList(prodInfoSeg, "chanlCde"));

                    // set chanl Code allowBuy/allowSell indicate
                    so.setChanlAllowBuy(CommonConverter.getChanlAttrSegInd(prodInfoSeg, "allowBuyProdInd"));
                    so.setChanlAllowSell(CommonConverter.getChanlAttrSegInd(prodInfoSeg, "allowSellProdInd"));

                    so.setAllowTradeProdInd(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "allowTradeProdInd"));
                    so.setAllowTradeProdInd_analyzed(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "allowTradeProdInd"));
                    so.setRestrOnlScribInd(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "restrOnlScribInd"));
                    so.setRestrOnlScribInd_analyzed(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "restrOnlScribInd"));
                    so.setPiFundInd(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "piFundInd"));
                    so.setPiFundInd_analyzed(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "piFundInd"));
                    so.setDeAuthFundInd(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "deAuthFundInd"));
                    so.setDeAuthFundInd_analyzed(CommonConverter.getProdUserDefExtSegInd(prodInfoSeg, "deAuthFundInd"));

                    // set Fund Unswitchable Code
                    so.setFundUnswithSeg(CommonConverter.getFundUnswithSeg(seg));
                    objList.add(so);
                }
                Thread.sleep(this.delayConversion);
            }
            CommonConverter.handleCommonLog(size, symbolErr, codeErr, entity, file.getName());

            LogUtil.debug(UtDataConverter.class, "End to handle data for the file=" + file.getName());
        }
    }
}
