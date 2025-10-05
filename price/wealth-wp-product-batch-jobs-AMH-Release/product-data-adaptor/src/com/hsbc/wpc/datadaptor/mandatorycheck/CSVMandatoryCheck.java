package com.dummy.wpc.datadaptor.mandatorycheck;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.util.Constants;

public class CSVMandatoryCheck extends SingleCommonMandatoryCheck{

	
	@Override
	public boolean check() {
		boolean success = super.check();
		if(success == false){
			return false;
		}
		String field_set_mapper = configMap.get(Constants.FIELD_SET_MAPPER);
		if (StringUtils.isEmpty(field_set_mapper)) {
			msg = "The property \"" + Constants.FIELD_SET_MAPPER + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		String fieldListConfig = configMap.get(Constants.FIELD_LIST_CONFIG);
		if (StringUtils.isEmpty(fieldListConfig)) {
			msg = "The property \"" + Constants.FIELD_LIST_CONFIG + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		initLinksToSkip();
		return true;
	}

}
