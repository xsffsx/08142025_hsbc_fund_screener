/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.service;

import java.util.List;
import com.hhhh.group.secwealth.mktdata.elastic.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParserBase;;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import com.hhhh.group.secwealth.mktdata.elastic.bean.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.CustomizedEsIndexForProduct;
import com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch.InternalProductKey;
import com.hhhh.group.secwealth.mktdata.elastic.properties.AppProperties;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;
import com.hhhh.group.secwealth.mktdata.elastic.util.PredictiveSearchConstant;
import com.hhhh.group.secwealth.mktdata.elastic.util.StringUtil;

@Service
public class PredsrchHelperService {

	@Autowired
	private ElasticsearchRestTemplate elasticsearchTemplate;

	private static final String WHITE_SPACE_PATTERN = "\\u0020";

	@Autowired
	private AppProperties appProperty;

	@Autowired
	private PredsrchCommonService predsrchCommonService;

	public CustomizedEsIndexForProduct getBySymbolMarket(InternalProductKey ipk, String locale) {
		return getBySymbolCountryTradableCode(ipk.getCountryCode(), ipk.getGroupMember(), ipk.getProdAltNum(),
				ipk.getCountryTradableCode(), ipk.getProductType(), locale);
	}

	private CustomizedEsIndexForProduct getBySymbolCountryTradableCode(String countryCode, String groupMember,
			String symbol, String countryTradableCode, String productType, String locale) {
		CustomizedEsIndexForProduct utObj = null;
		if (StringUtil.isValid(symbol)) {
			BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
			tempQuery.must(QueryBuilders.termQuery(PredictiveSearchConstant.SYMBOL, toQueryString(symbol)))
					.must(QueryBuilders.termQuery(PredictiveSearchConstant.PRODUCT_TYPE_ANALYZED,
							toQueryString(productType)))
					.must(QueryBuilders.termQuery(PredictiveSearchConstant.COUNTRY_TRADEABLE_CODE,
							toQueryString(countryTradableCode)));
			utObj = findProductListByKey(tempQuery, toQueryString(countryCode), toQueryString(groupMember),
					locale, 1);
		}
		return utObj;
	}

	// lower case and replace blank
	public String toQueryString(final String keyWord) {
		if (keyWord != null) {
			return StringUtils.replace(QueryParserBase.escape(keyWord), CommonConstants.SPACE, WHITE_SPACE_PATTERN)
					.toLowerCase();
		}
		return null;
	}

	public CustomizedEsIndexForProduct getBySymbolMarket(String countryCode, String groupMember, String prodAltNum,
			String countryTradableCode, String productType, String locale) {
		return getBySymbolCountryTradableCode(countryCode, groupMember, prodAltNum, countryTradableCode, productType,
				locale);
	}

	public CustomizedEsIndexForProduct getByRicCode(String countryCode, String groupMember, String ric, String locale) {
		CustomizedEsIndexForProduct utObj = null;
		if (StringUtil.isValid(ric)) {
			BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
			tempQuery.must(QueryBuilders.termQuery("key", toQueryString(ric)));
			utObj = findProductListByKey(tempQuery, countryCode, groupMember, locale, 1);
		}
		return utObj;
	}

	public CustomizedEsIndexForProduct getByWCode(String countryCode, String groupMember, String wCode, String locale) {
		CustomizedEsIndexForProduct utObj = null;
		if (StringUtil.isValid(wCode)) {
			BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
			tempQuery.must(QueryBuilders.termQuery("productCode", toQueryString(wCode)));
			utObj = findProductListByKey(tempQuery, countryCode, groupMember, locale, 1);
		}
		return utObj;
	}

	public CustomizedEsIndexForProduct getByPCode(String countryCode, String groupMember, String code, String locale) {
		CustomizedEsIndexForProduct utObj = null;
		if (StringUtil.isValid(code)) {
			BoolQueryBuilder tempQuery = QueryBuilders.boolQuery();
			tempQuery.must(QueryBuilders.termQuery("prodCode", toQueryString(code)));
			utObj = findProductListByKey(tempQuery, toQueryString(countryCode), toQueryString(groupMember), locale, 1);
		}
		return utObj;
	}

	public CustomizedEsIndexForProduct findProductListByKey(BoolQueryBuilder tempQuery, String countryCode, String groupMember, String locale, final int topNum) {
		CustomizedEsIndexForProduct utObj = null;
		PageRequest page = PageRequest.of(0, topNum);
		Query searchQuery = new NativeSearchQueryBuilder().withQuery(tempQuery)
				.withPageable(page).build();
		IndexCoordinates indexName = IndexCoordinates.of(this.predsrchCommonService.getCurrentIndexName(StringUtil.combineWithUnderline(countryCode, groupMember)));
		SearchHits<CustomizedEsIndexForProduct> sampleEntities=
				this.elasticsearchTemplate.search(searchQuery,CustomizedEsIndexForProduct.class,indexName);
		List<CustomizedEsIndexForProduct> resultList = (List) SearchHitSupport.unwrapSearchHits(sampleEntities);
		addWCode(resultList);
		if (ListUtil.isValid(resultList)) {
			utObj = resultList.get(0);
			int index = 0;
			if (null != locale) {
				String indexKey = StringUtil.combineWithDot(countryCode, locale);
				index = appProperty.getNameByIndex(indexKey);
			}
			String productName = utObj.getProductNameAnalyzed().get(index);
			String productShortName = utObj.getProductShrtNameAnalyzed().get(index);

			if (null != productName && !CommonConstants.EMPTY_STRING.equals(productName)) {
				utObj.setProductName(productName);
			}
			if (null != productShortName && !CommonConstants.EMPTY_STRING.equals(productShortName)) {
				utObj.setProductShortName(productShortName);
			}
		}

		return utObj;

	}

	/**
	 * add W Code to searchableObjectList
	 *
	 * @param searchableObjectList
	 */
	private void addWCode(final List<CustomizedEsIndexForProduct> searchableObjectList) {
		for (CustomizedEsIndexForProduct searchableObject : searchableObjectList) {
			List<ProdAltNumSeg> prodAltNumSegs = searchableObject.getProdAltNumSegs();
			ProdAltNumSeg seg = new ProdAltNumSeg();
			seg.setProdAltNum(searchableObject.getProductCode());
			seg.setProdCdeAltClassCde(PredictiveSearchConstant.PROD_ALT_CLASS_CDE_W);
			prodAltNumSegs.add(seg);
		}
	}

}
