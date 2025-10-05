package com.hhhh.group.secwealth.mktdata.elastic.properties;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "predsrch-priority")
@Getter
@Setter
public class PredsrchSortingOrderProperties {

	private Map<String, Map<String, String>> predsrchSortingOrdersBySite;
	
	private Map<String,Map<String,Map<String,String>>> sortingField;

	private static final String PROD_TYPE_SORT = "productTypeSortingOrder";

	private static final String PROD_SUB_TYPE_SORT = "productSubTypeSortingOrder";

	private static final String COUNTRY_CODE_SORT = "countryTradableCodeSortingOrder";

	private static final String MARKET_SORT = "marketSortingOrder";

	private Map<String, Map<String, String>> siteProductTypePriorityMap;

	private Map<String, Map<String, Map<String, String>>> siteProductTypeProductSubTypePriorityMap;

	private Map<String, Map<String, String>> siteCountryTradableCodePriorityMap;

	private Map<String, Map<String, String>> siteMarketPriorityMap;

	@PostConstruct
	public void init() {
		Builder<String, Map<String, String>> siteProductTypePriorityBuilder = new ImmutableMap.Builder<>();

		Builder<String, Map<String, Map<String, String>>> siteProductTypeProductSubTypePriorityBuilder = new ImmutableMap.Builder<>();

		Builder<String, Map<String, String>> siteCountryTradableCodePriorityBuilder = new ImmutableMap.Builder<>();

		Builder<String, Map<String, String>> siteMarketPriorityBuilder = new ImmutableMap.Builder<>();

		for (Map.Entry<String, Map<String, String>> entry : predsrchSortingOrdersBySite.entrySet()) {
			Builder<String, String> typeBuilder = this.getTypeBuilder(entry);
			siteProductTypePriorityBuilder.put(entry.getKey(), typeBuilder.build());

			String productSubTypeSortingOrderAllStr = entry.getValue().get(PROD_SUB_TYPE_SORT);
			String[] productSubTypeSortingOrderAllArr = productSubTypeSortingOrderAllStr.split(CommonConstants.SYMBOL_SEMICOLON);

			for(String productSubTypeSortingOrderSubStr : productSubTypeSortingOrderAllArr){
				String[] productSubTypeSortingOrder = productSubTypeSortingOrderSubStr.split(CommonConstants.SYMBOL_COMMA);
				String productType = productSubTypeSortingOrder[0];
				String[] productSubType = productSubTypeSortingOrder[1].split(CommonConstants.SYMBOL_SEPARATOR);
				Builder<String, Map<String, String>> typeSubTypeBuilder = this.getTypeSubTypeBuilder(productType, productSubType);
				siteProductTypeProductSubTypePriorityBuilder.put(entry.getKey()+productType, typeSubTypeBuilder.build());
			}

			Builder<String, String> countryCodeBuilder = this.getCountryCodeBuilder(entry);
			siteCountryTradableCodePriorityBuilder.put(entry.getKey(), countryCodeBuilder.build());

			Builder<String, String> marketBuilder = this.getMarketBuilder(entry);
			siteMarketPriorityBuilder.put(entry.getKey(), marketBuilder.build());
		}

		siteProductTypePriorityMap = siteProductTypePriorityBuilder.build();

		siteProductTypeProductSubTypePriorityMap = siteProductTypeProductSubTypePriorityBuilder.build();

		siteCountryTradableCodePriorityMap = siteCountryTradableCodePriorityBuilder.build();

		siteMarketPriorityMap = siteMarketPriorityBuilder.build();
	}

	private Builder<String, Map<String, String>> getTypeSubTypeBuilder(String productType, String[] productSubType) {
		// subType
		Builder<String, Map<String, String>> typeSubTypeBuilder = new Builder<>();
		Builder<String, String> subTypeBuilder = new Builder<>();
		for (int i = 0; i < productSubType.length; i++) {
			String[] subTypes = productSubType[i].split(CommonConstants.SYMBOL_COLON);
			String subTypeOrder = "";
			if (i < 9) {
				subTypeOrder = subTypeOrder + "0" + (i + 1);
			} else {
				subTypeOrder = subTypeOrder + (i + 1);
			}
			for (String subType : subTypes) {
				subTypeBuilder.put(subType, subTypeOrder);
			}
		}
		typeSubTypeBuilder.put(productType, subTypeBuilder.build());
		return typeSubTypeBuilder;
	}

	private Builder<String, String> getMarketBuilder(Map.Entry<String, Map<String, String>> entry) {
		// Market
		Builder<String, String> marketBuilder = new Builder<>();
		String marketSortingOrderStr = entry.getValue().get(MARKET_SORT);
		String[] marketSortingOrder = marketSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
		for (int i = 0; i < marketSortingOrder.length; i++) {
			String marketOrder = "";
			if (i < 9) {
				marketOrder = marketOrder + "0" + (i + 1);
			} else {
				marketOrder = marketOrder + (i + 1);
			}
			marketBuilder.put(marketSortingOrder[i], marketOrder);
		}
		return marketBuilder;
	}

	private Builder<String, String> getCountryCodeBuilder(Map.Entry<String, Map<String, String>> entry) {
		// CountryTradableCode
		Builder<String, String> countryCodeBuilder = new Builder<>();
		String countryTradableCodeSortingOrderStr = entry.getValue().get(COUNTRY_CODE_SORT);
		String[] countryTradableCodeSortingOrder = countryTradableCodeSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
		for (int i = 0; i < countryTradableCodeSortingOrder.length; i++) {
			String countryCodeOrder = "";
			if (i < 9) {
				countryCodeOrder = countryCodeOrder + "0" + (i + 1);
			} else {
				countryCodeOrder = countryCodeOrder + (i + 1);
			}
			countryCodeBuilder.put(countryTradableCodeSortingOrder[i], countryCodeOrder);
		}
		return countryCodeBuilder;
	}

	private Builder<String, String> getTypeBuilder(Map.Entry<String, Map<String, String>> entry) {
		// Type
		Builder<String, String> typeBuilder = new Builder<>();
		String productTypeSortingOrderStr = entry.getValue().get(PROD_TYPE_SORT);
		String[] productTypeSortingOrder = productTypeSortingOrderStr.split(CommonConstants.SYMBOL_COMMA);
		for (int i = 0; i < productTypeSortingOrder.length; i++) {// give each type a sequence number
			String order = "";
			if (i < 9) {
				order = order + "0" + (i + 1);
			} else {
				order = order + (i + 1);
			}
			typeBuilder.put(productTypeSortingOrder[i], order);
		}
		return typeBuilder;
	}

}
