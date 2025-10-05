package com.dummy.wpc.datadaptor.processor;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.ProdTypCdeMappingHelper;

public class DefaultErrorBeanProccessor<T> extends AbstractBeanProcessor<T> {

	private String inputProdTypeCde;
	private String inputTradeCde;
	private String error;

	public DefaultErrorBeanProccessor() {
	}

	public DefaultErrorBeanProccessor(String inputProdTypeCde, String error) {
		this.inputProdTypeCde = inputProdTypeCde;
		this.error = error;
	}

	public DefaultErrorBeanProccessor(String inputProdTypeCde, String inputTradeCde, String error) {
		super();
		this.inputProdTypeCde = inputProdTypeCde;
		this.inputTradeCde = inputTradeCde;
		this.error = error;
	}

	@Override
	public T process(ProductEntity entity, ProdKeySeg prodKey, RecDtTmSeg recDtTm) {
		String ctryCde = ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.CTRY_REC_CDE);
		String orgnCde = ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.GRP_MEMBR_REC_CDE);
		String externalProdTypeCode = entity.getProdTypeCde();
		String prodTypeCde = ProdTypCdeMappingHelper.getInternalProdTypCde(ctryCde, orgnCde, externalProdTypeCode);
		String exchangeCtryCde = getCtryTradeCdeByExchange(entity.getExchgCde());
		if (!StringUtils.equals(inputProdTypeCde, prodTypeCde) || !StringUtils.equals(inputTradeCde, exchangeCtryCde)) {
			return null;
		}
		String errMsg = "%s does not support product type code: %s, country tradable code: %s, exchange code: %s.";
		errMsg = StringUtils.isBlank(error) ? errMsg : (errMsg += " ---- " + error);
		logger.error(String.format(errMsg, jobCode, prodTypeCde, exchangeCtryCde, entity.getExchgCde()));
		return null;
	}

	public void setInputProdTypeCde(String inputProdTypeCde) {
		this.inputProdTypeCde = inputProdTypeCde;
	}

	public void setInputTradeCde(String inputTradeCde) {
		this.inputTradeCde = inputTradeCde;
	}

	public void setError(String error) {
		this.error = error;
	}

}
