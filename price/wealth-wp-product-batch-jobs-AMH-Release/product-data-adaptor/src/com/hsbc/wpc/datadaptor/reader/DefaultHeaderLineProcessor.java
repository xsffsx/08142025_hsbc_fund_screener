package com.dummy.wpc.datadaptor.reader;

import org.apache.log4j.Logger;

public class DefaultHeaderLineProcessor implements HeaderLineProcessor{

	private Logger log = Logger.getLogger(DefaultHeaderLineProcessor.class);
	
	public Object process(Object data) throws Exception {
		log.info("data:" + data);
		return null;
	}

}
