package com.dummy.wpc.datadaptor.mandatorycheck;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.datadaptor.util.Constants;

public class XMLMandatoryCheck extends SingleCommonMandatoryCheck {


	@Override
	public boolean check() {
		boolean success = super.check();
		if(success == false){
			return false;
		}
		String rootElementName = configMap.get(Constants.FRAGMENT_ROOT_ELEMENT_NAME);
		if (StringUtils.isEmpty(rootElementName)) {
			msg = "The property \"" + Constants.FRAGMENT_ROOT_ELEMENT_NAME + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}

		String xmlItemBean = configMap.get(Constants.XML_ITEM_BEAN);
		if (StringUtils.isEmpty(xmlItemBean)) {
			msg = "The property \"" + Constants.XML_ITEM_BEAN + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}

		String beanConverter = configMap.get(Constants.BEAN_CONVERTER);
		if (StringUtils.isEmpty(beanConverter)) {
			msg = "The property \"" + Constants.BEAN_CONVERTER + "\"" + necessaryMsg;
			log.error(msg);
			reportBean.log("----Failed, Result: " + msg);
			reportBean.increaseFailed();
			return false;
		}
		
		return true;
	}

}
