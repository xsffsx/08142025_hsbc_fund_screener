package com.dummy.wpb.wpc.utils;

import com.dummy.wpb.wpc.utils.task.HealthCheckTask;
import com.dummy.wpb.wpc.utils.task.HousekeepingTask;
import com.dummy.wpb.wpc.utils.task.WatchTask;
import com.dummy.wpb.wpc.utils.task.WorkerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
public class Application {
	@Autowired
	private ThreadPoolTaskScheduler taskScheduler;

	@Value("${product.sync.interval}")
	private int dataSyncInterval;	// in seconds

	@Value("${product.sync.auto-watch}")
	private boolean dataSyncAutoWatch;

	@Autowired
	private WatchTask watchTask;

	@Autowired
	private WorkerTask workerTask;

	@Autowired
	private HousekeepingTask housekeepingTask;

	@Autowired
	private HealthCheckTask healthCheckTask;

	@Value("${health-check.cron:0 0/15 * ? * *}")
	private String[] healthCrons;

	@Value("${health-check.timezone:UTC}")
	private String healthTimezone;

	public static void main(String[] args) {
		// decrypt encrypted properties in the system properties, like -Dmy.password="{cipher}98fe61d81c3c595928e79eebc8ef4d7339d4a5ee4125ae927ef2e72c03fb2536"
		System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
        CryptoUtil.decrypt(System.getProperties());
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	private void scheduleTasks(){
		watchTask.setStopWatch(!dataSyncAutoWatch);
		taskScheduler.scheduleWithFixedDelay(watchTask, dataSyncInterval * 1000L);
		log.info("Watch Task is scheduled at {} seconds interval", dataSyncInterval);

		taskScheduler.execute(workerTask);
		log.info("Worker Task is executing");

		taskScheduler.scheduleAtFixedRate(housekeepingTask, 3600000L * 24);		// housekeeping every 24 hours
		log.info("Housekeeping Task is scheduled at 24 hours interval");
		// schedule health check tasks
		TimeZone timeZone = TimeZone.getTimeZone(healthTimezone);
		for (String healthCron : healthCrons) {
			taskScheduler.schedule(healthCheckTask, new CronTrigger(healthCron, timeZone));
			log.info("Health Check Task is scheduled with cron expression {} ({})", healthCron, healthTimezone);
		}
	}
}
