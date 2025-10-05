/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.manager.PredictiveSearchManager;
import com.hhhh.group.secwealth.mktdata.predsrch.svc.service.SchedulerService;

/**
 * <p>
 * <b> Load data scheduler. </b>
 * </p>
 */
@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

    /** The manager. */
    @Autowired
    @Qualifier("predictiveSearchManager")
    private PredictiveSearchManager predictiveSearchManager;

    /**
     * Load predictive search data as schedule
     */
    public void refreshData() throws Exception {
        try {
            LogUtil.info(SchedulerServiceImpl.class, "Start to refresh data");
            this.predictiveSearchManager.loadData();
        } catch (Exception e) {
            if (e instanceof CommonException) {
                CommonException commonException = (CommonException) e;
                LogUtil.error(SchedulerServiceImpl.class, "Exception with refresh data=" + commonException.getMessage()
                    + ", error message: " + commonException.getErrMessage(), e);
            } else {
                LogUtil.error(SchedulerServiceImpl.class, "Exception with refresh data=" + e.getMessage(), e);
            }
        }
    }
}