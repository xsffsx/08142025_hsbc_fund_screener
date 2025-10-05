/**
 * @Title LabciPropsUtil.java
 * @description TODO
 * @author OJim
 * @time Jun 10, 2019 5:10:40 PM
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.response.labci.BidAskQueue;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.*;

/**
 * 
 */
/**
 * @Title LabciPropsUtil.java
 * @description TODO
 * @author OJim
 * @time Jun 10, 2019 5:10:40 PM
 */
public class LabciPropsUtil {

	private static final Logger logger = LoggerFactory.getLogger(LabciPropsUtil.class);

	public static String inOrderStrProps(final String field, final String key, final Map<String, Object> mapObj) {
		String[] labciFields = getLabciFields(field, key);
		String result = "";
		for (String labciField : labciFields) {
			result = (String) mapObj.get(labciField);
			if (StringUtil.isValidResp(result)) {
				break;
			}
		}
		return result;
	}

	public static String inOrderStrProps(final String field, final JsonObject jsonObj) {
		String result = "";
		if (jsonObj.get(field) != null) {
			result = jsonObj.get(field).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, "");
		}
		return result;
	}

	public static Boolean inOrderBooProps(final String field, final JsonObject jsonObj) {
		boolean result = false;
		if (jsonObj.get(field) != null) {
			if ("Y".equals(jsonObj.get(field).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""))
					|| "true".equals(
							jsonObj.get(field).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""))
					|| "1".equals(
							jsonObj.get(field).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, "")))
				result = true;
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> Read labci fields orderly. </b>
	 * </p>
	 *
	 * @param field
	 * @param key
	 * @param mapObj
	 * @return
	 */
	public static BigDecimal inOrderNumberProps(final String field, final String key,
			final Map<String, Object> mapObj) {
		String[] labciFields = getLabciFields(field, key);
		BigDecimal result = null;
		for (String labciField : labciFields) {
			result = BigDecimalUtil.fromString((String) mapObj.get(labciField));
			if (isValidNumber(result)) {
				break;
			}
		}
		return result;
	}

	public static BigDecimal inOrderNumberProps(final String field, final JsonObject jsonObj) {
		BigDecimal result = null;
		if (jsonObj.get(field) != null) {
			result = BigDecimalUtil.fromString(
					jsonObj.get(field).toString().replaceAll(SymbolConstant.SYMBOL_DOUBLE_QUOTATION_MARKS, ""));
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> If the first (arrays.length - 1) elements in the array are valid Number,
	 * result is call method subtractProps, numbers is arrays[arrays.length - 1],
	 * otherwise result is the last element. Please note, if arrays.length is 2,
	 * result is call method subtractProps, numbers is these two elements. </b>
	 * </p>
	 *
	 * @param field
	 * @param key
	 * @param mapObj
	 * @return
	 */
	public static BigDecimal subtractProps(final String field, final String key, final Map<String, Object> mapObj) {
		String[] labciFields = getLabciFields(field, key);
		int size = labciFields.length;
		if (size > 2) {
			BigDecimal[] numbers = new BigDecimal[size - 1];
			for (int i = 0; i < size - 1; i++) {
				BigDecimal number = BigDecimalUtil.fromString((String) mapObj.get(labciFields[i]));
				if (isInvalidNumber(number)) {
					return BigDecimalUtil.fromString((String) mapObj.get(labciFields[size - 1]));
				} else {
					numbers[i] = number;
				}
			}
			return subtractProps(numbers);
		} else if (size > 1) {
			BigDecimal[] numbers = new BigDecimal[size];
			for (int i = 0; i < size; i++) {
				BigDecimal number = BigDecimalUtil.fromString((String) mapObj.get(labciFields[i]));
				if (isInvalidNumber(number)) {
					return null;
				} else {
					numbers[i] = number;
				}
			}
			return subtractProps(numbers);
		} else if (size == 1) {
			return BigDecimalUtil.fromString((String) mapObj.get(labciFields[0]));
		} else {
			LabciPropsUtil.logger.error(
					"Labci fields size should not be zero. Please check your configuration labci.field-mapper, field is "
							+ field + ", key is " + key);
			throw new ApplicationException();
		}
	}

	/**
	 *
	 * <p>
	 * <b> Take the fisrt element in the array subtract other elements. </b>
	 * </p>
	 *
	 * @param numbers
	 * @return
	 */
	private static BigDecimal subtractProps(final BigDecimal[] numbers) {
		BigDecimal result = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			result = result.subtract(numbers[i]);
		}
		return result;
	}

	public static List<BidAskQueue> bidAskQuote(final String key, final Map<String, Object> mapObj) {
		String[] bidAskQueues = getLabciFields("bidAskQueues", key);
		List<BidAskQueue> result = new ArrayList<>();
		for (int i = 0; i < bidAskQueues.length;) {
			BidAskQueue queue = new BidAskQueue();
			result.add(queue);
			queue.setBidPrice(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setBidSize(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setBidOrder(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskPrice(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskSize(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskOrder(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
		}
		return result;
	}

	public static List<BidAskQueue> bidAskQueues(final String field, final JsonObject jsonObj) {
		if(jsonObj.get(field) == null) {
			return null;
		}
		JsonArray bidAskQueues = jsonObj.get(field).getAsJsonArray();
		List<BidAskQueue> result = new ArrayList<>();
		Iterator<JsonElement> it = bidAskQueues.iterator();
		while (it.hasNext()) {
			JsonObject resultJsonObj = it.next().getAsJsonObject();
			BidAskQueue queue = new BidAskQueue();
			result.add(queue);
			queue.setBidPrice(inOrderNumberProps("bidPrice", resultJsonObj));
			queue.setBidSize(inOrderNumberProps("bidSize", resultJsonObj));
			queue.setBidOrder(inOrderNumberProps("bidOrder", resultJsonObj));
			queue.setAskPrice(inOrderNumberProps("askPrice", resultJsonObj));
			queue.setAskSize(inOrderNumberProps("askSize", resultJsonObj));
			queue.setAskOrder(inOrderNumberProps("askOrder", resultJsonObj));
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> Calculate growth rate. </b>
	 * </p>
	 *
	 * @param field
	 * @param key
	 * @param mapObj
	 * @param context
	 * @return
	 */
	public static BigDecimal growthRateProps(final String field, final String key, final Map<String, Object> mapObj,
			final MathContext context) {
		String[] labciFields = getLabciFields(field, key);
		BigDecimal result = null;
		int size = labciFields.length;
		if (size == 3) {
			BigDecimal current = BigDecimalUtil.fromString((String) mapObj.get(labciFields[0]));
			BigDecimal last = BigDecimalUtil.fromString((String) mapObj.get(labciFields[1]));
			if (isValidNumber(current) && isValidNumber(last)) {
				result = current.subtract(last).divide(last, context).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
			} else {
				result = BigDecimalUtil.fromString((String) mapObj.get(labciFields[2]));
			}
		} else if (size == 2) {
			BigDecimal current = BigDecimalUtil.fromString((String) mapObj.get(labciFields[0]));
			BigDecimal last = BigDecimalUtil.fromString((String) mapObj.get(labciFields[1]));
			if (isValidNumber(current) && isValidNumber(last)) {
				result = current.subtract(last).divide(last, context).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
			}
		} else if (size == 1) {
			result = BigDecimalUtil.fromString((String) mapObj.get(labciFields[0]));
		} else {
			LabciPropsUtil.logger.error(
					"Labci fields size does not meet the requirements. Please check your configuration labci.field-mapper, field is "
							+ field + ", key is " + key);
			throw new ApplicationException();
		}
		return result;
	}

	/**
	 *
	 * <p>
	 * <b> Take the date and time from Labci Json Object, then convert to ISO9601
	 * format. </b>
	 * </p>
	 *
	 * @param field
	 * @param key
	 * @param mapObj
	 * @param timezone
	 * @return
	 */
	public static String dateProps(final String field, final String key, final Map<String, Object> mapObj,
			final TimeZone timezone) {
		String[] labciFields = getLabciFields(field, key);
		if (labciFields.length != 2) {
			LabciPropsUtil.logger.error(
					"Labci fields size should be two. Please check your configuration labci.field-mapper, field is "
							+ field + ", key is " + key);
			throw new ApplicationException();
		}
		String result = "";
		if (StringUtil.isValid(labciFields[0]) && StringUtil.isValid(labciFields[1])) {
			String iso8601Str = (String) mapObj.get(labciFields[0]) + "T" + (String) mapObj.get(labciFields[1]);
			try {
				result = DateUtil.convertToISO8601Format(iso8601Str, Constant.DATEFORMAT_DDMMYYYHHMM,
						TimeZone.getTimeZone(Constant.TIMEZONE), timezone);
			} catch (ParseException e) {
				LabciPropsUtil.logger.warn("Parse date encounter exception, date is " + iso8601Str, e);
			}
		}
		return result;
	}

	public static List<BidAskQueue> bidAskQueues(final String key, final Map<String, Object> mapObj) {
		String[] bidAskQueues = getLabciFields("bidAskQueues", key);
		List<BidAskQueue> result = new ArrayList<>();
		for (int i = 0; i < bidAskQueues.length;) {
			BidAskQueue queue = new BidAskQueue();
			result.add(queue);
			queue.setBidPrice(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setBidSize(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setBidOrder(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskPrice(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskSize(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
			queue.setAskOrder(BigDecimalUtil.fromString((String) mapObj.get(bidAskQueues[i++])));
		}
		return result;
	}

	private static boolean isInvalidNumber(final BigDecimal b) {
		return !isValidNumber(b);
	}

	/**
	 *
	 * <p>
	 * <b> Valid number should not be null or zero. </b>
	 * </p>
	 *
	 * @param b
	 * @return
	 */
	private static boolean isValidNumber(final BigDecimal b) {
		return b != null && b.compareTo(BigDecimal.ZERO) != 0;
	}

	/**
	 *
	 * <p>
	 * <b> Get the Labci fields from ThreadLocal. </b>
	 * </p>
	 *
	 * @param field
	 * @param key
	 * @return
	 */
	public static String[] getLabciFields(final String field, final String key) {
		@SuppressWarnings("unchecked")
		Map<String, Map<String, String[]>> fieldMappers = (Map<String, Map<String, String[]>>) ArgsHolder
				.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_LABCI_FIELD_MAPPER);
		if (fieldMappers == null || fieldMappers.isEmpty()) {
			LabciPropsUtil.logger.error("Can't get RESPONSE_LABCI_FIELD_MAPPER from ThreadLocal");
			throw new ApplicationException();
		}
		Map<String, String[]> fieldMapper = fieldMappers.get(key);
		if (fieldMapper == null || fieldMapper.isEmpty()) {
			LabciPropsUtil.logger.error("Can't get fieldMapper from RESPONSE_LABCI_FIELD_MAPPER, key is " + key);
			throw new ApplicationException();
		}
		if (!fieldMapper.containsKey(field)) {
			LabciPropsUtil.logger.error("Can't get Labci fields from field mapper, field is " + field);
			throw new ApplicationException();
		}
		return fieldMapper.get(field);
	}

	/**
	 * @Title inOrderBooPropsForRiskAlert
	 * @Description
	 * @param string
	 * @return
	 * @return String
	 * @Author OJim
	 */
	public static Boolean inOrderBooPropsForRiskAlert(String riskAlert) {
		List<String> riskAlertChars = Arrays.asList(riskAlert.split("-"));
		if ("D".equals(riskAlert) || "P".equals(riskAlert) || "0".equals(riskAlert) || "PFD".equals(riskAlert)
				|| "343".equals(riskAlert) || riskAlertChars.contains("11")) {
			return false;
		} else if ("S".equals(riskAlert) || "EIR".equals(riskAlert) || "342".equals(riskAlert)
				|| riskAlertChars.contains("4") || riskAlertChars.contains("5")) {
			return true;
		}
		return false;
	}

	/**
	 * @Title inOrderBooPropsForIsSuspended
	 * @Description
	 * @param inOrderStrProps
	 * @param countryCode
	 * @param suspendFlagConfig
	 * @return
	 * @return Boolean
	 * @Author OJim
	 */
	public static Boolean inOrderBooPropsForIsSuspended(final String nominalPriceType, final String countryCode,
			final String suspendFlagConfig) {
		String[] suspendFlags = suspendFlagConfig.split(SymbolConstant.SYMBOL_COMMA);
		for (String suspendFlag : suspendFlags) {
			String[] flagStrs = suspendFlag.split(SymbolConstant.SYMBOL_VERTICAL_LINE_ESCAPE);
			if (countryCode.equals(flagStrs[0])) {
				String[] flags = flagStrs[1].split(SymbolConstant.SYMBOL_COLON);
				for (String flag : flags) {
					if (StringUtil.isValid(nominalPriceType) && nominalPriceType.equals(flag)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
