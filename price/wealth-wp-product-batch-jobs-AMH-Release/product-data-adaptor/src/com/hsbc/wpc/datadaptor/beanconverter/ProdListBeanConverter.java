package com.dummy.wpc.datadaptor.beanconverter;

import static com.dummy.wpc.datadaptor.constant.ConfigConstant.CTRY_REC_CDE;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.GRP_MEMBR_REC_CDE;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_BOND;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_ELI;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_AU;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_CA;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_CN;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_GB;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_HK;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_JP;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_OT;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SEC_US;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_SN;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_UT;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_AU;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_CA;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_CN;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_GB;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_HK;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_JP;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_OT;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.PROD_TYPE_CDE_WRTS_US;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.SUPPORT_PRICE_TYPE_CODE_LIST;
import static com.dummy.wpc.datadaptor.constant.ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dummy.wpc.batch.object.castor.ProductEntity;
import com.dummy.wpc.batch.xml.DebtInstm;
import com.dummy.wpc.batch.xml.Eli;
import com.dummy.wpc.batch.xml.GnrcProd;
import com.dummy.wpc.batch.xml.ProdKeySeg;
import com.dummy.wpc.batch.xml.ProdPrc;
import com.dummy.wpc.batch.xml.RecDtTmSeg;
import com.dummy.wpc.batch.xml.StockInstm;
import com.dummy.wpc.datadaptor.constant.ConfigConstant;
import com.dummy.wpc.datadaptor.mapper.MultiWriterObj;
import com.dummy.wpc.datadaptor.processor.BeanProcessorFactory;
import com.dummy.wpc.datadaptor.util.ConstantsPropertiesHelper;
import com.dummy.wpc.datadaptor.util.DateHelper;
import com.dummy.wpc.datadaptor.util.ProdTypCdeMappingHelper;
import com.dummy.wpc.datadaptor.util.TimeZoneHelper;

/*
 * Convert OPE object to WPC object.
 */
public class ProdListBeanConverter extends AbstractBeanConverter {

	private static Logger logger = Logger.getLogger(ProdListBeanConverter.class);

	public Object convert(Object source) {
		ProductEntity entity = (ProductEntity) source;
		MultiWriterObj multiWriterObj = new MultiWriterObj();
		String ctryCde = getContConfig(CTRY_REC_CDE);
		String orgnCde = getContConfig(GRP_MEMBR_REC_CDE);
		String externalProdTypeCode = entity.getProdTypeCde();
		String internalProdTypeCode = ProdTypCdeMappingHelper.getInternalProdTypCde(ctryCde, orgnCde, externalProdTypeCode);
		String jobCode = getJobCode();
		// prodKey & recDtTm
		ProdKeySeg prodKey = buildProdKey(entity);
		RecDtTmSeg recDtTm = buildRecDtTm(ctryCde, orgnCde);
		// supported product type & price list
		List<String> supportTypeList=splitCodeList(SUPPORT_PROD_TYPE_CODE_LIST);
		List<String> supportPriceTypeList =splitCodeList(SUPPORT_PRICE_TYPE_CODE_LIST);
		if ( CollectionUtils.isEmpty(supportTypeList)) {
			String configKey = jobCode + "." + ConfigConstant.SUPPORT_PROD_TYPE_CODE_LIST;
			logger.error(configKey + " can't be null, need include export interface type code.");
			throw new IllegalArgumentException(configKey + " can't be null, need include export interface type code.");
		}
		// product type code
		String eliTypeCde = getContConfig(PROD_TYPE_CDE_ELI);
		String snTypeCde = getContConfig(PROD_TYPE_CDE_SN);
		String bondTypeCde = getContConfig(PROD_TYPE_CDE_BOND);
		String utTypeCde = getContConfig(PROD_TYPE_CDE_UT);
		// SEC type
		String secTypeCdeHK = getContConfig(PROD_TYPE_CDE_SEC_HK);
		String secTypeCdeUS = getContConfig(PROD_TYPE_CDE_SEC_US);
		String secTypeCdeCN = getContConfig(PROD_TYPE_CDE_SEC_CN);
		String secTypeCdeAU = getContConfig(PROD_TYPE_CDE_SEC_AU);
		String secTypeCdeCA = getContConfig(PROD_TYPE_CDE_SEC_CA);
		String secTypeCdeJP = getContConfig(PROD_TYPE_CDE_SEC_JP);
		String secTypeCdeGB = getContConfig(PROD_TYPE_CDE_SEC_GB);
		String secTypeCdeOT = getContConfig(PROD_TYPE_CDE_SEC_OT);
		// WRTS type
		String wrtsTypeCdeHK = getContConfig(PROD_TYPE_CDE_WRTS_HK);
		String wrtsTypeCdeUS = getContConfig(PROD_TYPE_CDE_WRTS_US);
		String wrtsTypeCdeCN = getContConfig(PROD_TYPE_CDE_WRTS_CN);
		String wrtsTypeCdeAU = getContConfig(PROD_TYPE_CDE_WRTS_AU);
		String wrtsTypeCdeCA = getContConfig(PROD_TYPE_CDE_WRTS_CA);
		String wrtsTypeCdeJP = getContConfig(PROD_TYPE_CDE_WRTS_JP);
		String wrtsTypeCdeGB = getContConfig(PROD_TYPE_CDE_WRTS_GB);
		String wrtsTypeCdeOT = getContConfig(PROD_TYPE_CDE_WRTS_OT);

		/*
		 * handle product by bean processor factory
		 */
		String ctryTradeCde = getCtryTradeCdeByExchange(entity.getExchgCde());
		BeanProcessorFactory factory = new BeanProcessorFactory(jobCode, internalProdTypeCode, ctryTradeCde);	
		// SEC Product
		StockInstm secHK = factory.getStockInstmProcessor(supportTypeList, secTypeCdeHK).process(entity, prodKey, recDtTm);
		StockInstm secUS = factory.getStockInstmProcessor(supportTypeList, secTypeCdeUS).process(entity, prodKey, recDtTm);
		StockInstm secCN = factory.getStockInstmProcessor(supportTypeList, secTypeCdeCN).process(entity, prodKey, recDtTm);
		StockInstm secAU = factory.getStockInstmProcessor(supportTypeList, secTypeCdeAU).process(entity, prodKey, recDtTm);
		StockInstm secCA = factory.getStockInstmProcessor(supportTypeList, secTypeCdeCA).process(entity, prodKey, recDtTm);
		StockInstm secGB = factory.getStockInstmProcessor(supportTypeList, secTypeCdeGB).process(entity, prodKey, recDtTm);
		StockInstm secJP = factory.getStockInstmProcessor(supportTypeList, secTypeCdeJP).process(entity, prodKey, recDtTm);
		StockInstm secOT = factory.getStockInstmProcessor(supportTypeList, secTypeCdeOT).process(entity, prodKey, recDtTm);
		// Special Process For SEC~CN
		setProdLocCde(secCN, entity.getExchgCde());
		// SEC product Price
		ProdPrc secPriceHK = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeHK).process(entity, prodKey, recDtTm);
		ProdPrc secPriceUS = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeUS).process(entity, prodKey, recDtTm);
		ProdPrc secPriceCN = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeCN).process(entity, prodKey, recDtTm);
		ProdPrc secPriceAU = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeAU).process(entity, prodKey, recDtTm);
		ProdPrc secPriceCA = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeCA).process(entity, prodKey, recDtTm);
		ProdPrc secPriceGB = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeGB).process(entity, prodKey, recDtTm);
		ProdPrc secPriceJP = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeJP).process(entity, prodKey, recDtTm);
		ProdPrc secPriceOT = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, secTypeCdeOT).process(entity, prodKey, recDtTm);
		// WRTS Product
		StockInstm wrtsHK = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeHK).process(entity, prodKey, recDtTm);
		StockInstm wrtsUS = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeUS).process(entity, prodKey, recDtTm);
		StockInstm wrtsCN = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeCN).process(entity, prodKey, recDtTm);
		StockInstm wrtsAU = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeAU).process(entity, prodKey, recDtTm);
		StockInstm wrtsCA = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeCA).process(entity, prodKey, recDtTm);
		StockInstm wrtsGB = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeGB).process(entity, prodKey, recDtTm);
		StockInstm wrtsJP = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeJP).process(entity, prodKey, recDtTm);
		StockInstm wrtsOT = factory.getStockInstmProcessor(supportTypeList, wrtsTypeCdeOT).process(entity, prodKey, recDtTm);
		// WRTS product price
		ProdPrc wrtsPriceHK = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeHK).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceUS = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeUS).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceCN = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeCN).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceAU = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeAU).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceCA = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeCA).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceGB = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeGB).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceJP = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeJP).process(entity, prodKey, recDtTm);
		ProdPrc wrtsPriceOT = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, wrtsTypeCdeOT).process(entity, prodKey, recDtTm);
		// ELI / SN Product
		Eli eli = factory.getEliProcessor(supportTypeList, eliTypeCde).process(entity, prodKey, recDtTm);
		if (eli == null) {
			eli = factory.getEliProcessor(supportTypeList, snTypeCde).process(entity, prodKey, recDtTm);
		}

		// BOND Product & Price
		DebtInstm bond = factory.getDebtInstmProcessor(supportTypeList, bondTypeCde).process(entity, prodKey, recDtTm);
		ProdPrc bondPrice = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, bondTypeCde).process(entity, prodKey, recDtTm);
		// UT Product
		GnrcProd ut = factory.getGnrcProdProcessor(supportTypeList, utTypeCde).process(entity, prodKey, recDtTm);

		// Store result data
		// HK market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeHK, secHK);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeHK, secPriceHK);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeHK, wrtsHK);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeHK, wrtsPriceHK);
		// US market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeUS, secUS);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeUS, secPriceUS);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeUS, wrtsUS);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeUS, wrtsPriceUS);
		// CN market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeCN, secCN);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeCN, secPriceCN);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeCN, wrtsCN);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeCN, wrtsPriceCN);
		// AU market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeAU, secAU);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeAU, secPriceAU);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeAU, wrtsAU);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeAU, wrtsPriceAU);
		// CA market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeCA, secCA);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeCA, secPriceCA);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeCA, wrtsCA);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeCA, wrtsPriceCA);
		// GB market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeGB, secGB);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeGB, secPriceGB);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeGB, wrtsGB);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeGB, wrtsPriceGB);
		// JP market
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeJP, secJP);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeJP, secPriceJP);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeJP, wrtsJP);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeJP, wrtsPriceJP);
		// For other market which is not in (HK,US,CN,AU,CA,UK,JP) product, generate SEC~OTHERS file
		addProductBean(multiWriterObj, supportTypeList, secTypeCdeOT, secOT);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, secTypeCdeOT, secPriceOT);
		addProductBean(multiWriterObj, supportTypeList, wrtsTypeCdeOT, wrtsOT);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, wrtsTypeCdeOT, wrtsPriceOT);
		// others
		addProductBeanWithMultipleProdTypeCde(multiWriterObj, supportTypeList, eli, eliTypeCde, snTypeCde);
		addProductBean(multiWriterObj, supportTypeList, bondTypeCde, bond);
		addProductBean(multiWriterObj, supportTypeList, utTypeCde, ut);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, bondTypeCde, bondPrice);
		
		// ELI Price 
		ProdPrc eliPrice = factory.getProdPrcProcessor(supportTypeList, supportPriceTypeList, eliTypeCde).process(entity, prodKey, recDtTm);
		addProductPriceBean(multiWriterObj, supportTypeList, supportPriceTypeList, eliTypeCde, eliPrice);

		return multiWriterObj;
	}

	private String getContConfig(String configKey) {
		return ConstantsPropertiesHelper.getValue(getJobCode(), configKey);
	}

	private ProdKeySeg buildProdKey(ProductEntity entity) {
		ProdKeySeg prodKey = new ProdKeySeg();
		prodKey.setCtryRecCde(entity.getCtryCde());
		prodKey.setGrpMembrRecCde(entity.getOrgnCde());
		prodKey.setProdTypeCde(entity.getProdTypeCde());
		prodKey.setProdCde(entity.getProdCde());
		prodKey.setProdCdeAltClassCde("P");
		return prodKey;
	}

	private RecDtTmSeg buildRecDtTm(String ctryCde, String orgnCde) {
		String GMTStr = DateHelper.convertTimeZoneToGMTString(TimeZoneHelper.getTimeZone(ctryCde, orgnCde));
		String dateTime = DateHelper.formatDate2String(DateHelper.getCurrentDate(), DateHelper.DEFAULT_DATETIME_FORMAT);
		RecDtTmSeg recDtTm = new RecDtTmSeg();
		recDtTm.setRecCreatDtTm(dateTime);
		recDtTm.setRecUpdtDtTm(dateTime);
		recDtTm.setTimeZone(GMTStr);
		return recDtTm;
	}

	// Special process set PROD_LOC_CDE for CN market
	private void setProdLocCde(StockInstm stock, String exchangeCde) {
		if (null == stock) {
			return;
		}
		String locationCode = exchangeCde.substring(0, (exchangeCde.length() - 1));
		stock.getProdInfoSeg().setProdLocCde(locationCode);
	}

	private void addProductBean(MultiWriterObj multiWriterObj, List<String> supportTypeList, String prodTypeCde, Object prodct) {
		if (supportTypeList.contains(prodTypeCde)) {
			multiWriterObj.addObj(prodct);
		}
	}

	private void addProductBeanWithMultipleProdTypeCde(MultiWriterObj multiWriterObj, List<String> supportTypeList, Object prodct, String... prodTypeCdes) {
		for (String prodTypeCde : prodTypeCdes) {
			if (supportTypeList.contains(prodTypeCde)) {
				multiWriterObj.addObj(prodct);
				break;
			}
		}
	}

	private void addProductPriceBean(MultiWriterObj multiWriterObj, List<String> supportTypeList, List<String> supportPriceTypeList, String prodTypeCde, Object prodct) {
		if (supportTypeList.contains(prodTypeCde) && supportPriceTypeList.contains(prodTypeCde)) {
			multiWriterObj.addObj(prodct);
		}
	}

	private String getCtryTradeCdeByExchange(String exchange) {
		String emptyStr = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.EMPTY_STR);
		String mapStr = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CTRY_PROD_TRADE_CDE_MAP);
		if (mapStr == null) {
			return emptyStr;
		} else {
			Map<String, String> map = new HashMap<String, String>();
			for (final String entry : mapStr.split(",")) {
				String[] parts = entry.split(":");
				if (parts.length != 2) {
					logger.debug("invalid entry mapping:" + entry);
					continue;
				}
				map.put(parts[0], parts[1]);
			}
			String ctryProdTradeCde = map.get(exchange);
			if (ctryProdTradeCde != null) {
				return ctryProdTradeCde;
			} else {
				String defaultCde = ConstantsPropertiesHelper.getValue(getJobCode(), ConfigConstant.CTRY_PROD_TRADE_OTHERS);
				if (defaultCde != null) {
					return defaultCde;
				}

				return emptyStr;
			}
		}
	}

	private List<String> splitCodeList(String key) {
		String CodeList = getContConfig(key);
		List<String> codeList = new ArrayList<String>();
		if (StringUtils.isNotBlank(CodeList)) {
			String[] priceTypes = CodeList.split(",");
			codeList = new ArrayList<String>(Arrays.asList(priceTypes));
		}
		return codeList;
	}
}
