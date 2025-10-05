/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.SearchProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.helper.InternalSearchRequest;
import com.hhhh.group.secwealth.mktdata.elastic.service.PredsrchHelperService;

@Component
public class InternalProductKeyUtil {

	private static Logger logger = LoggerFactory.getLogger(InternalProductKeyUtil.class);

	@Autowired
	private PredsrchHelperService predictiveSearchService;
	@Autowired
	private ServiceKeyToProdCdeAltClassCdeConvertor prodCdeAltClassCdeConvertor;

	public static final String MSTAR = "MSTAR";

	/**
	 * Gets the product info
	 * 
	 * @param ipk
	 * @return
	 * @throws Exception
	 */
	public CustomizedEsIndexForProduct getProductInfo(final InternalProductKey ipk) {
		return getProductInfoWithLocale(ipk, null);
	}

	private CustomizedEsIndexForProduct getProductInfoWithLocale(InternalProductKey ipk, final String locale) {
		return this.predictiveSearchService.getBySymbolMarket(ipk, locale);
	}
	
	public SearchProduct getProductBySearchWithAltClassCde(InternalSearchRequest request) {
	    return getProductBySearchWithAltClassCde(
	        request.getAltClassCde(),
	        request.getCountryCode(),
	        request.getGroupMember(),
	        request.getProdAltNum(),
	        request.getCountryTradableCode(),
	        request.getProductType(),
	        request.getLocale()
	        );
	}

	public SearchProduct getProductBySearchWithAltClassCde(final String altClassCde, final String countryCode,
			final String groupMember, final String prodAltNum, final String countryTradableCode,
			final String productType, final String locale) {

		SearchProduct object = new SearchProduct();
		CustomizedEsIndexForProduct obj;
		// Search the product by product code type
		if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equals(altClassCde)) {
			obj = this.predictiveSearchService.getBySymbolMarket(countryCode, groupMember, prodAltNum,
					countryTradableCode, productType, locale);
			object.setSearchObject(obj);
		} else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(altClassCde)
				|| PredictiveSearchConstant.PROD_ALT_CLASS_CDE_R.equals(altClassCde)) {
			obj = this.predictiveSearchService.getByRicCode(countryCode, groupMember, prodAltNum, locale);
			object.setSearchObject(obj);
		} else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_W.equals(altClassCde)) {
			obj = this.predictiveSearchService.getByWCode(countryCode, groupMember, prodAltNum, locale);
			object.setSearchObject(obj);
		} else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_P.equals(altClassCde)) {
			obj = this.predictiveSearchService.getByPCode(countryCode, groupMember, prodAltNum, locale);
			object.setSearchObject(obj);
		} else {
			logger.error("Can't support to predictive search the prodCdeAltClassCde: {}", altClassCde);
			return null;
		}
		// Get the external code by prodCdeAltClassCde
		if (null != obj) {
			String prodCdeAltClassCde = this.prodCdeAltClassCdeConvertor.doConvert(InternalProductKeyUtil.MSTAR);
			if (null != prodCdeAltClassCde) {
				object.setProdCdeAltClassCde(prodCdeAltClassCde);
			} else {
				logger.error("Can't get the external code prodCdeAltClassCde: {}", altClassCde);
				return null;
			}
			this.updateObjExternalKey(object, obj, prodCdeAltClassCde);
		} else {
			return null;
		}
		return object;

	}

	private void updateObjExternalKey(SearchProduct object, CustomizedEsIndexForProduct obj, String prodCdeAltClassCde) {
		if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_M.equals(prodCdeAltClassCde)) {
			object.setExternalKey(obj.getSymbol());
		} else if (PredictiveSearchConstant.PROD_ALT_CLASS_CDE_T.equals(prodCdeAltClassCde)) {
			object.setExternalKey(obj.getKey());
		} else {
			List<ProdAltNumSeg> prodAltNumSegs = obj.getProdAltNumSegs();
			for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
				if (prodCdeAltClassCde.equals(prodAltNumSeg.getProdCdeAltClassCde())) {
					object.setExternalKey(prodAltNumSeg.getProdAltNum());
					break;
				}
			}
		}
	}

	/**
	 * <p>
	 * <b> Get the product code value by passed code type. </b>
	 * </p>
	 * 
	 * @param codeType
	 * @param searchObject
	 * @return
	 */
	public String getProductCodeValueByCodeType(final ProdCdeAltClassCdeEnum codeType,
			final CustomizedEsIndexForProduct searchObject) {
		if (null != searchObject) {
			List<ProdAltNumSeg> prodAltNumSegs = searchObject.getProdAltNumSegs();
			for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegs) {
				if (codeType.toString().equals(prodAltNumSeg.getProdCdeAltClassCde())) {
					return prodAltNumSeg.getProdAltNum();
				}
			}
		}
		return "";
	}

}
