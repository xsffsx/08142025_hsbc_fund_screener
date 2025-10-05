package com.dummy.wpc.datadaptor.processor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.Eli;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.datadaptor.constant.ProductTypeEnum;

public class BeanProcessorFactory {

	private static Pattern prodTypePattern = Pattern.compile("^(\\w+)(.(\\w+))?$");
	private String jobCode;
	private String entityProdTypeCode;
	private String entityCtryTradeCode;
	
	public BeanProcessorFactory() {
	}
	
	public BeanProcessorFactory(String jobCode, String entityProdTypeCode, String entityCtryTradeCode) {
		this.jobCode = jobCode;
		this.entityProdTypeCode = entityProdTypeCode;
		this.entityCtryTradeCode = entityCtryTradeCode;
	}

	public BeanProcessor<StockInstm> getStockInstmProcessor(List<String> supportedList, String prodTypeCde) {
		String inputProdType = getProdTypeAndCtryTradeCde(prodTypeCde)[0];
		String inputTradeCde = getProdTypeAndCtryTradeCde(prodTypeCde)[1];
		BeanProcessor<StockInstm> processor = new DefaultErrorBeanProccessor<StockInstm>(inputProdType, inputTradeCde, "StockInstmProcessor");
		processor.setJobCode(jobCode);
		if (!supportedList.contains(prodTypeCde) || !StringUtils.equals(entityProdTypeCode, inputProdType) || !StringUtils.equals(entityCtryTradeCode, inputTradeCde)) {
			return processor;
		}
		switch (getProdTypeEnum(prodTypeCde)) {
			case SEC:
			case WRTS:
				processor = new StockInstmBeanProcessor();
				processor.setJobCode(jobCode);
				break;
			default:
				break;
		}
		return processor;
	}

	public BeanProcessor<DebtInstm> getDebtInstmProcessor(List<String> supportedList, String prodTypeCde) {
		String inputProdType = getProdTypeAndCtryTradeCde(prodTypeCde)[0];
		BeanProcessor<DebtInstm> processor = new DefaultErrorBeanProccessor<DebtInstm>(inputProdType, "DebtInstmProcessor");
		processor.setJobCode(jobCode);
		if (!supportedList.contains(prodTypeCde) || !StringUtils.equals(entityProdTypeCode, inputProdType)) {
			return processor;
		}
		switch (getProdTypeEnum(prodTypeCde)) {
			case BOND:
				processor = new DebtInstmBeanProcessor();
				processor.setJobCode(jobCode);
				break;
			default:
				break;
		}
		return processor;
	}

	public BeanProcessor<GnrcProd> getGnrcProdProcessor(List<String> supportedList, String prodTypeCde) {
		String inputProdType = getProdTypeAndCtryTradeCde(prodTypeCde)[0];
		BeanProcessor<GnrcProd> processor = new DefaultErrorBeanProccessor<GnrcProd>(inputProdType, "GnrcProdProcessor");
		processor.setJobCode(jobCode);
		if (!supportedList.contains(prodTypeCde) || !StringUtils.equals(entityProdTypeCode, inputProdType)) {
			return processor;
		}
		switch (getProdTypeEnum(prodTypeCde)) {
			case UT:
				processor = new GnrcProdBeanProcessor();
				processor.setJobCode(jobCode);
				break;
			default:
				break;
		}
		return processor;
	}

	public BeanProcessor<Eli> getEliProcessor(List<String> supportedList, String prodTypeCde) {
		String inputProdType = getProdTypeAndCtryTradeCde(prodTypeCde)[0];
		BeanProcessor<Eli> processor = new DefaultErrorBeanProccessor<Eli>(inputProdType, "EliProcessor");
		processor.setJobCode(jobCode);
		if (!supportedList.contains(prodTypeCde) || !StringUtils.equals(entityProdTypeCode, inputProdType)) {
			return processor;
		}
		switch (getProdTypeEnum(prodTypeCde)) {
			case ELI:
			case SN:
				processor = new EliBeanProcessor();
				processor.setJobCode(jobCode);
				break;
			default:
				break;
		}
		return processor;
	}

	public BeanProcessor<ProdPrc> getProdPrcProcessor(List<String> supportedProdList, List<String> supportedProdPrcList, String prodTypeCde) {
		String inputProdType = getProdTypeAndCtryTradeCde(prodTypeCde)[0];
		String inputTradeCde = getProdTypeAndCtryTradeCde(prodTypeCde)[1];
		BeanProcessor<ProdPrc> processor = new DefaultErrorBeanProccessor<ProdPrc>(inputProdType, "ProdPrcProcessor");
		processor.setJobCode(jobCode);
		if (!supportedProdList.contains(prodTypeCde) || !supportedProdPrcList.contains(prodTypeCde) || !StringUtils.equals(entityProdTypeCode, inputProdType)) {
			return processor;
		}
		if (StringUtils.isNotBlank(inputTradeCde) && !StringUtils.equals(entityCtryTradeCode, inputTradeCde)) {
			return processor;
		}
		switch (getProdTypeEnum(prodTypeCde)) {
			case SEC:
			case WRTS:
			case BOND:
			case ELI:
				processor = new ProdPrcBeanProcessor();
				processor.setJobCode(jobCode);
				break;
			default:
				break;
		}
		return processor;
	}

	private ProductTypeEnum getProdTypeEnum(String prodTypeCde) {
		Matcher matcher = prodTypePattern.matcher(prodTypeCde);
		if (matcher.find()) {
			return ProductTypeEnum.retrieveProdType(matcher.group(1));
		}
		return ProductTypeEnum.DFLT;
	}

	/**
	 * This method is used to extract product type code and country trade code from input product type code. 
	 * Currently only SEC and WRTS product type supports this pattern.
	 * 
	 * @param prodTypeCde
	 *            input product type code which pattern is SEC~CN or WRTS~HK
	 * @return An array which contains product type code and country trade code
	 */
	private String[] getProdTypeAndCtryTradeCde(String prodTypeCde) {
		String[] prodTypeAndCtryTradeCde = new String[2];
		if(StringUtils.isBlank(prodTypeCde)){
			return prodTypeAndCtryTradeCde;
		}
		Matcher matcher = prodTypePattern.matcher(prodTypeCde);
		if (matcher.find()) {
			prodTypeAndCtryTradeCde[0] = matcher.group(1);
			prodTypeAndCtryTradeCde[1] = matcher.group(3);
		}
		return prodTypeAndCtryTradeCde;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public void setEntityProdTypeCode(String entityProdTypeCode) {
		this.entityProdTypeCode = entityProdTypeCode;
	}

	public void setEntityCtryTradeCode(String entityCtryTradeCode) {
		this.entityCtryTradeCode = entityCtryTradeCode;
	}
	
}
