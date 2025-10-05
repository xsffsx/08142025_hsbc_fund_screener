/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.Keystore;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.tris.ResponseStatus;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 *
 * <p>
 * <b> Provides a series of methods to get Labci related attribute values. </b>
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "labci-protal")
@ConditionalOnProperty(value = "service.quotes.Labci.injectEnabled")
@Getter
@Setter
public class LabciProtalProperties {

	private static final Logger logger = LoggerFactory.getLogger(LabciProtalProperties.class);

	private static final String DEFAULT_SITE = "DEFAULT";

	private static final String DEFAULT_PRODUCT = "DEFAULT";

	private static final String DEFAULT_EXCHANGE = "DEFAULT";

	private static final String DELAY_OPTION = "delay";

	private static final String REALTIME_OPTION = "realtime";

	private Map<String, Map<String, String>> service = new HashMap<>();

	private Map<String, String> closure = new HashMap<>();

	private Map<String, Keystore> tokenKeystore = new HashMap<>();

	private Map<String, String> tokenAppCode = new HashMap<>();

	private Map<String, String> tokenTimezone = new HashMap<>();

	private Map<String, String> tokenPattern;

	private String proxyName;

	private String queryUrl;

	private String chartDataUrl;

	public String tradingDayUrl;

	public String tradingHourUrl;

	public String tradingParam;

	public String tradingParamPrefix;

	private Map<String, Map<String, Map<String, Map<String, String>>>> fieldMapper = new HashMap<>();

	private Map<String, Map<String, String>> labciFieldMapper = new HashMap<>();

	private List<String> labciFieldMapperKeys = new ArrayList<>();

	private ResponseStatus responseStatus;

	private Map<String, String> exchangeMapping = new HashMap<>();


	@PostConstruct
	public void init() {
//		initLabciFieldMapper();
		initLabciFieldMapperKeys();
	}

	/**
	 *
	 * <p>
	 * <b> {site}_{product}_{exchange} => Labci field mapper. Please note, each
	 * level must have "DEFAULT" key. </b>
	 * </p>
	 *
	 */
//	private void initLabciFieldMapper() {
//		if (!this.fieldMapper.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
//			LabciProtalProperties.logger.error(
//					"Please check your configuration \"labci.field-mapper.{site}\", default site should be \"DEFAULT\"");
//			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
//		}
//		for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> siteEntry : this.fieldMapper.entrySet()) {
//			String site = siteEntry.getKey();
//			Map<String, Map<String, Map<String, String>>> siteMapper = siteEntry.getValue();
//			if (!siteMapper.containsKey(LabciProtalProperties.DEFAULT_PRODUCT)) {
//				LabciProtalProperties.logger.error(
//						"Please check your configuration \"labci.field-mapper.{site}.{product}\", default product should be \"DEFAULT\"");
//				throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
//			}
//			for (Map.Entry<String, Map<String, Map<String, String>>> productEntry : siteMapper.entrySet()) {
//				String product = productEntry.getKey();
//				Map<String, Map<String, String>> productMapper = productEntry.getValue();
//				if (!productMapper.containsKey(LabciProtalProperties.DEFAULT_EXCHANGE)) {
//					LabciProtalProperties.logger.error(
//							"Please check your configuration \"labci.field-mapper.{site}.{product}.{exchange}\", default exchange should be \"DEFAULT\"");
//					throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
//				}
//				for (Map.Entry<String, Map<String, String>> exchangeEntry : productMapper.entrySet()) {
//					String exchange = exchangeEntry.getKey();
//					Map<String, String> exchangeMapper = exchangeEntry.getValue();
//					if (exchangeMapper.isEmpty()) {
//						LabciProtalProperties.logger.error(
//								"Please check your configuration \"labci.field-mapper.{site}.{product}.{exchange}.Map\", Map should not be empty");
//						throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
//					}
//					String key = new StringBuilder().append(site).append(SymbolConstant.SYMBOL_UNDERLINE)
//							.append(product).append(SymbolConstant.SYMBOL_UNDERLINE).append(exchange).toString();
//					this.labciFieldMapper.put(key, exchangeMapper);
//				}
//			}
//		}
//	}

	public String getVendorTimezone(final String site) {
		String result;
		if (this.tokenTimezone.containsKey(site)) {
			result = this.tokenTimezone.get(site);
		} else if (this.tokenTimezone.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
			result = this.tokenTimezone.get(LabciProtalProperties.DEFAULT_SITE);
		} else {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"timezone\", default site should be \"DEFAULT\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
		}
		if (StringUtil.isInValid(result)) {
			LabciProtalProperties.logger.error("Please check your configuration: \"timezone\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> List<{site}_{product}_{exchange}> </b>
	 * </p>
	 *
	 */
	private void initLabciFieldMapperKeys() {
		for (String labciFieldMapperKey : this.labciFieldMapper.keySet()) {
			this.labciFieldMapperKeys.add(labciFieldMapperKey);
		}
	}

	/**
	 *
	 * <p>
	 * <b> Return a string identifying the desired data source. Service is required
	 * in Tirs request. </b>
	 * </p>
	 *
	 * @param site
	 * @param delay
	 * @return
	 */
	public String getLabciService(final String site, final boolean delay) {
		Map<String, String> serviceOptions;
		if (this.service.containsKey(site)) {
			serviceOptions = this.service.get(site);
		} else if (this.service.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
			serviceOptions = this.service.get(LabciProtalProperties.DEFAULT_SITE);
		} else {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.service\", default site should be \"DEFAULT\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		String key = delay ? LabciProtalProperties.DELAY_OPTION : LabciProtalProperties.REALTIME_OPTION;
		if (serviceOptions.containsKey(key)) {
			String result = serviceOptions.get(key);
			if (StringUtil.isValid(result)) {
				return result;
			} else {
				LabciProtalProperties.logger
						.error("Please check your configuration: \"labci.service\", the returned value is invalid");
				throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
			}
		} else {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.service\", options are \"realtime\" or \"delay\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
	}

	/**
	 *
	 * <p>
	 * <b> Return a string identifying the client. Closure is required in Labci
	 * request. </b>
	 * </p>
	 *
	 * @param site
	 * @return
	 */
	public String getLabciClosure(final String site) {
		String result;
		if (this.closure.containsKey(site)) {
			result = this.closure.get(site);
		} else if (this.closure.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
			result = this.closure.get(LabciProtalProperties.DEFAULT_SITE);
		} else {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.closure\", default site should be \"DEFAULT\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		if (StringUtil.isInValid(result)) {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.closure\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> AppCode is required in Labci token. </b>
	 * </p>
	 *
	 * @param site
	 * @return
	 */
	public String getLabciTokenAppCode(final String site) {
		String result;
		if (this.tokenAppCode.containsKey(site)) {
			result = this.tokenAppCode.get(site);
		} else if (this.tokenAppCode.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
			result = this.tokenAppCode.get(LabciProtalProperties.DEFAULT_SITE);
		} else {
			LabciProtalProperties.logger.error(
					"Please check your configuration: \"labci.token-app-code\", default site should be \"DEFAULT\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		if (StringUtil.isInValid(result)) {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.token-app-code\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> Timezone is required in Labci token. </b>
	 * </p>
	 *
	 * @param site
	 * @return
	 */
	public String getLabciTokenTimezone(final String site) {
		String result;
		if (this.tokenTimezone.containsKey(site)) {
			result = this.tokenTimezone.get(site);
		} else if (this.tokenTimezone.containsKey(LabciProtalProperties.DEFAULT_SITE)) {
			result = this.tokenTimezone.get(LabciProtalProperties.DEFAULT_SITE);
		} else {
			LabciProtalProperties.logger.error(
					"Please check your configuration: \"labci.token-timezone\", default site should be \"DEFAULT\"");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		if (StringUtil.isInValid(result)) {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.token-timezone\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		return result;
	}


	/**
	 *
	 * <p>
	 * <b> Get the corresponding Labci fields with the given fields and keys. </b>
	 * </p>
	 *
	 * @param fields
	 * @param keys
	 * @return
	 */
	public List<String> getLabciFields(final List<String> fields, final String[] keys) {
		return getLabciFields(fields, Constant.DEFAULT_OPTION, keys);
	}

	/**
	 *
	 * <p>
	 * <b> Get the corresponding Labci fields with the given fields, defaultKey and
	 * keys. </b>
	 * </p>
	 *
	 * @param fields
	 * @param defaultKey
	 * @param keys
	 * @return
	 */
	private List<String> getLabciFields(final List<String> fields, final String defaultKey, final String[] keys) {
		List<String> result = new ArrayList<>();
		List<Map<String, String>> labciFieldMappers = getLabciFieldMappers(defaultKey, keys);
		Map<String, String[]> responseFieldMapper = new HashMap<>();
		for (int i = 0; i < fields.size(); i++) {
			String field = fields.get(i);
			String[] labciField = getLabciField(labciFieldMappers, field);
			Collections.addAll(result, labciField);
			responseFieldMapper.put(field, labciField);
		}

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String[]>> fieldMappers = (Map<String, Map<String, String[]>>) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_LABCI_FIELD_MAPPER);
		String key = String.join(SymbolConstant.SYMBOL_VERTICAL_LINE, keys);
		if (fieldMappers == null || fieldMappers.isEmpty()) {
			fieldMappers = new HashMap<>();
		}
		fieldMappers.put(key, responseFieldMapper);
		ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_LABCI_FIELD_MAPPER, fieldMappers);
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> Get all fields mapper with given keys. </b>
	 * </p>
	 *
	 * @param defaultKey
	 * @param keys
	 * @return
	 */
	private List<Map<String, String>> getLabciFieldMappers(final String defaultKey, final String[] keys) {
		List<String> labciFieldMapperKeys = getLabciFieldMapperKeys(defaultKey, keys);
		List<Map<String, String>> result = new ArrayList<>();
		for (int i = 0; i < labciFieldMapperKeys.size(); i++) {
			result.add(this.labciFieldMapper.get(labciFieldMapperKeys.get(i)));
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> If defaultKey is DEFAULT, keys are A, B, C, possible values are (A_B_C,
	 * A_B_DEFAULT, A_DEFAULT_C, A_DEFAULT_DEFAULT, DEFAULT_B_C, DEFAULT_B_DEFAULT,
	 * DEFAULT_DEFAULT_C, DEFAULT_DEFAULT_DEFAULT), then intersect with
	 * labciFieldMapperKeys. </b>
	 * </p>
	 *
	 * @param defaultKey
	 * @param keys
	 * @return
	 */
	private List<String> getLabciFieldMapperKeys(final String defaultKey, final String[] keys) {
		List<List<String>> columns = new ArrayList<>(keys.length);
		for (int i = 0; i < keys.length; i++) {
			List<String> column = new ArrayList<>();
			column.add(keys[i]);
			column.add(defaultKey);
			columns.add(column);
		}
		// if keys is empty, column only contains DEFAULT
		if (columns.size() <= 0) {
			List<String> column = new ArrayList<>();
			column.add(defaultKey);
			columns.add(column);
		}
		List<String> possibleKeys = getKeys(columns);
		possibleKeys.retainAll(this.labciFieldMapperKeys);
		return possibleKeys;
	}

	/**
	 *
	 * <p>
	 * <b> Arrange and combine each columns. ((A,B),(a),(1,2,3)) => (A_a_1, A_a_2,
	 * A_a_3, B_a_1, B_a_2, B_a_3) </b>
	 * </p>
	 *
	 * @param columns
	 * @return
	 */
	private List<String> getKeys(final List<List<String>> columns) {
		if (columns.size() >= 2) {
			List<String> firstColumn = columns.get(0);
			int firstColumnSize = firstColumn.size();
			List<String> secondColumn = columns.get(1);
			int secondColumnSize = secondColumn.size();
			int totalSize = firstColumnSize * secondColumnSize;
			List<String> mergedColumn = new ArrayList<>(totalSize);
			for (int i = 0; i < firstColumnSize; i++) {
				for (int j = 0; j < secondColumnSize; j++) {
					mergedColumn.add(firstColumn.get(i) + SymbolConstant.SYMBOL_UNDERLINE + secondColumn.get(j));
				}
			}
			List<List<String>> mergedColumns = new ArrayList<>();
			for (int i = 2; i < columns.size(); i++) {
				mergedColumns.add(columns.get(i));
			}
			mergedColumns.add(0, mergedColumn);
			return getKeys(mergedColumns);
		} else {
			return columns.get(0);
		}
	}

	/**
	 *
	 * <p>
	 * <b> Query Labci field with given field in Labci field mappers. </b>
	 * </p>
	 *
	 * @param labciFieldMappers
	 * @param field
	 * @return
	 */
	private String[] getLabciField(final List<Map<String, String>> labciFieldMappers, final String field) {
		String result = "";
		for (int i = 0; i < labciFieldMappers.size(); i++) {
			Map<String, String> labciFieldMapper = labciFieldMappers.get(i);
			if (labciFieldMapper.containsKey(field)) {
				result = labciFieldMapper.get(field);
				break;
			}
		}
		if (StringUtil.isInValid(result)) {
			LabciProtalProperties.logger
					.error("Please check your configuration: \"labci.field-mapper\", can't find field: " + field);
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		if (result.contains(SymbolConstant.SYMBOL_COMMA)) {
			return result.split(SymbolConstant.SYMBOL_COMMA);
		} else {
			return new String[] { result };
		}
	}

	public String getResponseStatusCodeKey() {
		if (StringUtil.isInValid(this.responseStatus.getCodeKey())) {
			LabciProtalProperties.logger.error(
					"Please check your configuration: \"labci.response-status.code-key\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		return this.responseStatus.getCodeKey();
	}

	public boolean isCorrectResponseStatus(final String responseCode) {
		return this.responseStatus.getCorrectStatus().equals(responseCode);
	}

	public String getResponseStatusTextKey() {
		if (StringUtil.isInValid(this.responseStatus.getTextKey())) {
			LabciProtalProperties.logger.error(
					"Please check your configuration: \"labci.response-status.text-key\", the returned value is invalid");
			throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
		}
		return this.responseStatus.getTextKey();
	}

	public String getExchangeMapping(String exchange) {
		if (StringUtil.isValid(exchange)) {
			if(StringUtil.isValid(this.exchangeMapping.get(exchange))) {
				return this.exchangeMapping.get(exchange);
			}
		}
		return this.exchangeMapping.get(LabciProtalProperties.DEFAULT_EXCHANGE);
	}

}
