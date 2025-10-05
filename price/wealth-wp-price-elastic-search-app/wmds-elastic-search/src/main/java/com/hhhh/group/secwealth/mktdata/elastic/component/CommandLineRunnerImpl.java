/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.elastic.service.ScheduleDataLoadService;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(CommandLineRunnerImpl.class);
	
	@Autowired
	private ScheduleDataLoadService scheduleDataLoadService;
    
	//do we need to catch exception here? seems not necessary.
	@Override
    public void run(String... args) throws Exception {
		int errorCount = 0;
		while(true) {
			try {
				this.scheduleDataLoadService.loadData();
				return;
			} catch (Exception e) {
				if(errorCount < 5) {
					errorCount++;
					Thread.sleep(300);
				} else {
					logger.debug("load WPC files failed for 5 times");
					throw new ApplicationException(PredictiveSearchConstant.ERRORMSG_LOADWPCFILEERR);
				}
			}
		}
    }
}