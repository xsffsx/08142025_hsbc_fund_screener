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
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.common.SearchableObject;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.generic.GnrcProd;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.generic.GnrcProdLst;
import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefDataLst;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;

public class GnrcProdDataConverter implements DataConverter {

    private Unmarshaller unmarshaller;

    private long delayConversion;

    public void setUnmarshaller(final Unmarshaller shaller) throws Exception {
        this.unmarshaller = shaller;
    }

    public void setDelayConversion(final long deplay) throws Exception {
        this.delayConversion = deplay;
    }

    /**
     * Convert Bond data file to Bond java object list
     */
    public void handleData(final File file, final RefDataLst refDataList, final DataSiteEntity entity, final boolean skipErrRecord)
        throws Exception {
        GnrcProdLst lst = (GnrcProdLst) this.unmarshaller
            .unmarshal(new InputStreamReader(new FileInputStream(file), CommonConstants.CODING_UTF8));
        if (null != lst && null != lst.getGnrcProd()) {
            LogUtil.debug(GnrcProdDataConverter.class, "Start to handle data for the file=" + file.getName());
            List<GnrcProd> instmList = lst.getGnrcProd();
            List<SearchableObject> objList = entity.getObjList();
            int size = instmList.size();
            int symbolErr = 0;
            int codeErr = 0;
            for (int i = 0; i < size; i++) {
                // errors[0] check for symbol
                // errors[1] check for T/R code
                boolean[] errors = {false, false};
                GnrcProd obj = instmList.get(i);
                SearchableObject so =
                    CommonConverter.handleCommonData(obj.getProdInfoSeg(), obj.getProdKeySeg(), obj.getProdAltNumSeg(), errors);

                if (errors[0]) {
                    symbolErr++;
                } else if (errors[1]) {
                    codeErr++;
                }

                if (errors[0] && skipErrRecord) {
                    LogUtil.error(GnrcProdDataConverter.class,
                        file.getName() + "|Skip to save the record without symbol|prodName=" + so.getProductName());
                } else {
                    // from refData
                    so.setParentAssetClass(
                        CommonConverter.getParentAssetClass(refDataList, obj.getProdInfoSeg().getProdAsetVoltlClassSeg()));
                    objList.add(so);
                }
                Thread.sleep(this.delayConversion);
            }
            CommonConverter.handleCommonLog(size, symbolErr, codeErr, entity, file.getName());

            LogUtil.debug(GnrcProdDataConverter.class, "End to handle data for the file=" + file.getName());
        }
    }

}
