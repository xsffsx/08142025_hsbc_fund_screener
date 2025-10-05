/**
 * @Title ConvertorsUtile.java
 * @description TODO
 * @author OJim
 * @time May 31, 2019 10:08:15 AM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ProductKey;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.ServiceProductKey;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;

/**
 * 
 */
/**
 * @Title ConvertorsUtile.java
 * @description TODO
 * @author OJim
 * @time May 31, 2019 10:08:15 AM
 */
public class ConvertorsUtil {

	public static List<ServiceProductKey> constructServiceProductKeys(final List<String> symbols,
			final List<PredSrchResponse> responses, final String convertors) {
		List<ServiceProductKey> serviceProductKeys = new ArrayList<>();
		if (symbols == null || responses == null || StringUtil.isInValid(convertors)) {
			return null;
		}
		for (String symbol : symbols) {
			ServiceProductKey serviceProductKey = new ServiceProductKey();
			for (PredSrchResponse response : responses) {
				// Should be ignore upper case or lower case
				if (symbol.equalsIgnoreCase(response.getSymbol())) {
					List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
					for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
						if (convertors.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
							serviceProductKey.setProdCdeAltClassCde(convertors);
							serviceProductKey.setProdAltNum(prodAltNumSeg.getProdAltNum());
						}
					}
					if (StringUtil.isInValid(serviceProductKey.getProdAltNum())) {
						serviceProductKey.setProdAltNum(response.getSymbol());
					}
					serviceProductKey.setProductType(response.getProductType());
					serviceProductKey.setMarket(response.getMarket());
					serviceProductKey.setExchange(response.getExchange());
					serviceProductKeys.add(serviceProductKey);
				}
			}
		}
		return serviceProductKeys;
	}

	/**
	 * @Title constructServiceProductKeys
	 * @Description
	 * @param symbolsMap
	 * @param responses
	 * @param prodCdeAltClassCodeM
	 * @return
	 * @return List<ServiceProductKey>
	 * @Author OJim
	 */
	public static List<ServiceProductKey> constructServiceProductKeys(final List<ProductKey> productKeys,
			final Map<String, List<String>> symbolsMap, final List<PredSrchResponse> responses,
			final String convertors) {
		List<ServiceProductKey> serviceProductKeys = new ArrayList<>();
		if (symbolsMap == null || responses == null || StringUtil.isInValid(convertors)) {
			return null;
		}
		for (ProductKey productKey : productKeys) {// productKeys in request
			for (Map.Entry<String, List<String>> symbolMap : symbolsMap.entrySet()) {// makeup symbolMap
				if (productKey.getProductType().equalsIgnoreCase(symbolMap.getKey())) {
					symbolMap: for (String s : symbolMap.getValue()) {// fetch correct symbols
						if (productKey.getProdAltNum().equalsIgnoreCase(s)) {
							ServiceProductKey serviceProductKey = new ServiceProductKey();
							for (PredSrchResponse response : responses) {// predsrch
								// Should be ignore upper case or lower case
								if (productKey.getProdAltNum().equalsIgnoreCase(response.getSymbol())
										&& ("ALL".equals(symbolMap.getKey())
												|| symbolMap.getKey().equalsIgnoreCase(response.getProductType()))) {
									List<ProdAltNumSeg> prodAltNumSegs = response.getProdAltNumSegs();
									for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
										if (convertors.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
											serviceProductKey.setProdCdeAltClassCde(convertors);
											serviceProductKey.setProdAltNum(prodAltNumSeg.getProdAltNum());
										}
									}
									if (StringUtil.isInValid(serviceProductKey.getProdAltNum())) {
										serviceProductKey.setProdAltNum(response.getSymbol());
									}
									serviceProductKey.setProductType(response.getProductType());
									serviceProductKey.setMarket(response.getMarket());
									serviceProductKey.setExchange(response.getExchange());
									serviceProductKeys.add(serviceProductKey);// add market&exchange
									break symbolMap;
								}
							}
						}
					}
				}
			}
		}
		return serviceProductKeys;
	}

	public static Map<String, Object> getPredsrchSymbol(final List<ProductKey> productKeys,
			final Map<String, List<String>> symbolsMap, final List<PredSrchResponse> responses,
			final String convertors) {
		List<ServiceProductKey> serviceProductKeys = new ArrayList<>();
		if (symbolsMap == null || responses == null || StringUtil.isInValid(convertors)) {
			return null;
		}
		List<ProductKey> resultList = new ArrayList<>();
		Map<String, Object> predsrchMap = new HashMap<String, Object>();
		for (PredSrchResponse response : responses) {
			for (ProductKey productKey : productKeys) {
				if (productKey.getProdAltNum().equalsIgnoreCase(response.getSymbol())
						&& productKey.getProductType().equalsIgnoreCase(response.getProductType())) {
					ServiceProductKey serviceProductKey = new ServiceProductKey();
					for (ProdAltNumSeg prodAltNumSeg : response.getProdAltNumSegs()) {
						if (convertors.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
							serviceProductKey.setProdCdeAltClassCde(convertors);
							serviceProductKey.setProdAltNum(prodAltNumSeg.getProdAltNum());
							// change productKey for later validate
							productKey.setProdCdeAltClassCde(convertors);
							productKey.setProdAltNum(prodAltNumSeg.getProdAltNum());
						}
					} // TODO: not setProdCdeAltClassCde here?
					if (StringUtil.isInValid(serviceProductKey.getProdAltNum())) {
						serviceProductKey.setProdAltNum(response.getSymbol());
					}
					serviceProductKey.setProductType(response.getProductType());
					serviceProductKey.setMarket(response.getMarket());
					serviceProductKey.setExchange(response.getExchange());
					serviceProductKeys.add(serviceProductKey);
					resultList.add(productKey);
				}
			}
		}
		ArgsHolder.putArgs(Constant.CONTAINED_PROD_KEYS, resultList);
		productKeys.removeAll(resultList);
		predsrchMap.put(Constant.SERVICE_PROD_KEYS, serviceProductKeys);
		predsrchMap.put(Constant.MISSING_PROD_KEYS, productKeys);
		return predsrchMap;
	}
}
