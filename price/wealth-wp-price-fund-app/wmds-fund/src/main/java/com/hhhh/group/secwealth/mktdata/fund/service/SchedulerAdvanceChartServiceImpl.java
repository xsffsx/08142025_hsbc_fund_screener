
package com.hhhh.group.secwealth.mktdata.fund.service;

import java.util.Timer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;


@Service("scheduleradvaneChartService")
public class SchedulerAdvanceChartServiceImpl  {

    
    @Autowired
    @Qualifier("advanceChartJobCacheService")
    private AdvanceChartJobCacheServiceImpl advanceChartJobCacheService;

    @Value("#{systemConfig['advanceChart.scheduler.job']}")
    private String schedulerJob;

    

    public void refreshData() {
        boolean jobEnable = false;
        if (StringUtil.isValid(schedulerJob)) {
            jobEnable = Boolean.valueOf(schedulerJob);
        }
        if (jobEnable) {
            try {
                LogUtil.info(SchedulerAdvanceChartServiceImpl.class, "Start to refresh data");
                this.advanceChartJobCacheService.cacheData();
            } catch (Exception e) {
                if (e instanceof CommonException) {
                    CommonException commonException = (CommonException) e;
                    LogUtil.error(SchedulerAdvanceChartServiceImpl.class, "Exception with refresh data="
                        + commonException.getMessage() + ", error message: " + commonException.getErrMessage(), e);
                } else {
                    LogUtil.error(SchedulerAdvanceChartServiceImpl.class, "Exception with refresh data=" + e.getMessage(), e);
                }
            }
        }
    }

    @PostConstruct
    public void init() throws Exception {
        try {
            LogUtil.debug(SchedulerAdvanceChartServiceImpl.class, "Start to init advanceChart data...");
            Timer timer = new Timer();
            InitLoadData task = new InitLoadData();
            timer.schedule(task, 3000);
        } catch (Exception e) {
            LogUtil.error(SchedulerAdvanceChartServiceImpl.class, "init data error" + e.getMessage(), e);
        }
    }
    public class InitLoadData extends java.util.TimerTask {

        
        public void run() {
            refreshData();
        }

    }

}