package com.dummy.wpc.datadaptor.reader;

import org.apache.log4j.Logger;

public class DefaultTrailerLineProcessor implements TrailerLineProcessor {

	private Logger log = Logger.getLogger(DefaultTrailerLineProcessor.class);

	public Object process(Object data) throws Exception {
		log.info("data:" + data);
		return null;
	}

}
