package com.dummy.wpc.datadaptor.processor;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;

public interface BeanProcessor<T> {
	T process(final ProductEntity entity, final ProdKeySeg prodKey, final RecDtTmSeg recDtTm);
	void setJobCode(String jobCode);
}
