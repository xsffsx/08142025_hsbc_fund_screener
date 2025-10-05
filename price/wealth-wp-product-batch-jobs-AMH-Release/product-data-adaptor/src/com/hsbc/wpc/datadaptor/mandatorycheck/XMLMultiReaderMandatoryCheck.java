package com.dummy.wpc.datadaptor.mandatorycheck;


public class XMLMultiReaderMandatoryCheck extends MultiCommonMandatoryCheck  {

	

	@Override
	public boolean check() {
		boolean success = super.check();
		if(success == false){
			return false;
		}
		return true;
	}

}
