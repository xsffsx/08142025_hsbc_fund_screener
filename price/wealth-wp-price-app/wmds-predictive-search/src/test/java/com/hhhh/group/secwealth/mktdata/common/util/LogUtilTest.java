package com.hhhh.group.secwealth.mktdata.common.util;


import ch.qos.logback.classic.Logger;
import com.hhhh.group.secwealth.mktdata.common.exception.BaseException;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class LogUtilTest {


	@Test
	public void  allTest(){
		Logger logger = LogUtil.getLogger(LogUtilTest.class);
		assertNotNull(logger);
		LogUtil.debug(LogUtilTest.class,"LogUtilTest");
		LogUtil.debug(LogUtilTest.class, "LogUtilTest = {}", System.currentTimeMillis());
		LogUtil.debug(LogUtilTest.class, "siteKey: {}, LogUtilTest: {}", "siteKey",1l);
		LogUtil.debug(LogUtilTest.class,
				"LogUtilTest {},LogUtilTest: {},  LogUtilTest: {}", "LogUtilTest", "LogUtilTest",
				"LogUtilTest");
		LogUtil.debug(LogUtilTest.class, "msg", new BaseException() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		});
		LogUtil.debug(LogUtilTest.class, "msg",new Throwable());
		LogUtil.debugBeanToJson(LogUtilTest.class, "msg",new Throwable());
		LogUtil.debugOutboundMsg(LogUtilTest.class, "msg","msg","msg");
		LogUtil.debugOutboundMsg(LogUtilTest.class, "msg","msg","msg");
		LogUtil.debugInboundMsg(LogUtilTest.class, "msg","msg");


		LogUtil.info(LogUtilTest.class, "msg");
		LogUtil.info(LogUtilTest.class, "msg","msg");
		LogUtil.info(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.info(LogUtilTest.class, "msg","msg","msg");
		LogUtil.info(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.info(LogUtilTest.class, "msg", new BaseException() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		});

		LogUtil.info(LogUtilTest.class, "msg",new Throwable());
		LogUtil.infoBeanToJson(LogUtilTest.class, "msg",new Throwable());
		LogUtil.infoOutboundMsg(LogUtilTest.class, "msg","msg","msg");
		LogUtil.infoOutboundMsg(LogUtilTest.class, "msg","msg","msg");
		LogUtil.infoInboundMsg(LogUtilTest.class, "msg","msg");




		LogUtil.warn(LogUtilTest.class, "msg");
		LogUtil.warn(LogUtilTest.class, "msg","msg");
		LogUtil.warn(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.warn(LogUtilTest.class, "msg","msg","msg");
		LogUtil.warn(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.warn(LogUtilTest.class, "msg", new BaseException() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		});

		LogUtil.warn(LogUtilTest.class, "msg",new Throwable());





		LogUtil.error(LogUtilTest.class, "msg");
		LogUtil.error(LogUtilTest.class, "msg","msg");
		LogUtil.error(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.error(LogUtilTest.class, "msg","msg","msg");
		LogUtil.error(LogUtilTest.class, "msg","msg","msg","msg");
		LogUtil.error(LogUtilTest.class, "msg", new BaseException() {
			@Override
			public int compareTo(Object o) {
				return 0;
			}
		});

		LogUtil.error(LogUtilTest.class, "msg",new Throwable());
		LogUtil.errorBeanToJson(LogUtilTest.class, "msg",new Throwable());
		LogUtil.errorBeanToJson(LogUtilTest.class, "msg","msg",new Throwable());


		try {
			LogUtil.genLogFile("/sda/das/1.log", "pwd");
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}