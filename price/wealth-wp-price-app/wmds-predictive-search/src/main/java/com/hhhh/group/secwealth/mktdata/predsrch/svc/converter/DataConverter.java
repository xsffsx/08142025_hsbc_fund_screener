/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.converter;

import java.io.File;

import javax.xml.bind.Unmarshaller;

import com.hhhh.group.secwealth.mktdata.predsrch.dal.model.refData.RefDataLst;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.DataSiteEntity;

public interface DataConverter {

    public void setUnmarshaller(Unmarshaller shaller) throws Exception;

    public void setDelayConversion(long deplay) throws Exception;

    public void handleData(File file, RefDataLst refDataList, DataSiteEntity entity, boolean ifCheckParingErr) throws Exception;

}
