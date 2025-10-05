package com.dummy.wpc.datadaptor.processor;

import static com.dummy.wpc.datadaptor.constant.ConfigConstant.CTRY_REC_CDE;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.GRP_MEMBR_REC_CDE;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.batch.xml.ProdInfoSeg;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.ProdTypCdeMappingHelper;
import com.dummy.wpc.datadaptor.util.SCConvertUtil;

public abstract class AbstractBeanProcessor<T> implements BeanProcessor<T> {
	private static String NORMAL = "N";
	private static String ACTIVE = "A";
	private String TRADE_CODE_HK = "HK";
	protected static final String EMPTY_STR = "";
	protected static final String INCOMING_SUB_TYPE_DEFAULT_VALUE = "*";
	protected static Logger logger = Logger.getLogger(AbstractBeanProcessor.class);
	protected String jobCode;

	@Override
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	protected String getCtryTradeCdeByExchange(String exchange) {
		String mapStr = ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.CTRY_PROD_TRADE_CDE_MAP);
		if (mapStr == null) {
			return EMPTY_STR;
		} else {
			Map<String, String> map = new HashMap<String, String>();
			for (final String entry : mapStr.split(",")) {
				String[] parts = entry.split(":");
				if (parts.length != 2) {
					continue;
				}
				map.put(parts[0], parts[1]);
			}
			String ctryProdTradeCde = map.get(exchange);
			if (ctryProdTradeCde != null) {
				return ctryProdTradeCde;
			} else {
				String defaultCde = ConstantsPropertiesHelper.getValue(jobCode, ConfigConstant.CTRY_PROD_TRADE_OTHERS);
				if (defaultCde != null) {
					return defaultCde;
				}

				return EMPTY_STR;
			}
		}
	}
	
	protected String getInternalProdTypeCde(String externalProdTypeCde) {
		String ctryCde = ConstantsPropertiesHelper.getValue(jobCode, CTRY_REC_CDE);
		String orgnCde = ConstantsPropertiesHelper.getValue(jobCode,GRP_MEMBR_REC_CDE);
		return ProdTypCdeMappingHelper.getInternalProdTypCde(ctryCde, orgnCde, externalProdTypeCde);	
	}
	

	protected void setSllNameForHK(ProdInfoSeg prodInfoSeg) {
		if (!TRADE_CODE_HK.equals(prodInfoSeg.getCtryProdTrade1Cde())) {
			return;
		}
		if (StringUtils.isNotBlank(prodInfoSeg.getProdPllName())) {
			prodInfoSeg.setProdSllName(SCConvertUtil.convertSpecTC2SC(SCConvertUtil.convertFromTC2SC(prodInfoSeg.getProdPllName())));
		}

		if (StringUtils.isNotBlank(prodInfoSeg.getProdShrtPllName())) {
			prodInfoSeg.setProdShrtSllName(SCConvertUtil.convertSpecTC2SC(SCConvertUtil.convertFromTC2SC(prodInfoSeg.getProdShrtPllName())));
		}
	}

	protected void setProdLocCde(StockInstm stock, String exchangeCde) {
		String locationCode = exchangeCde.substring(0, (exchangeCde.length() - 1));
		stock.getProdInfoSeg().setProdLocCde(locationCode);
	}

	protected String convertProductStatus(String opeProductStatus) {
		return StringUtils.equals(NORMAL, opeProductStatus) ? ACTIVE : opeProductStatus;
	}

}
