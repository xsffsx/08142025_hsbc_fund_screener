/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.quotes.response.entity.BidAskQueue;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;

public class TrisPropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(TrisPropsUtil.class);

    public static String inOrderStrProps(final String field, final String key, final JsonObject jsonObj) {
        String[] trisFields = getTrisFields(field, key);
        String result = "";
        for (String trisField : trisFields) {
            result = JsonUtil.getAsString(jsonObj, trisField);
            if (StringUtil.isValidResp(result)) {
                break;
            }
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> Read tris fields orderly. </b>
     * </p>
     *
     * @param field
     * @param key
     * @param jsonObj
     * @return
     */
    public static BigDecimal inOrderNumberProps(final String field, final String key, final JsonObject jsonObj) {
        String[] trisFields = getTrisFields(field, key);
        BigDecimal result = null;
        for (String trisField : trisFields) {
            result = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisField));
            if (isValidNumber(result)) {
                break;
            }
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> If the first (arrays.length - 1) elements in the array are valid
     * Number, result is call method subtractProps, numbers is
     * arrays[arrays.length - 1], otherwise result is the last element. Please
     * note, if arrays.length is 2, result is call method subtractProps,
     * numbers is these two elements. </b>
     * </p>
     *
     * @param field
     * @param key
     * @param jsonObj
     * @return
     */
    public static BigDecimal subtractProps(final String field, final String key, final JsonObject jsonObj) {
        String[] trisFields = getTrisFields(field, key);
        int size = trisFields.length;
        if (size > 2) {
            BigDecimal[] numbers = new BigDecimal[size - 1];
            for (int i = 0; i < size - 1; i++) {
                BigDecimal number = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[i]));
                if (isInvalidNumber(number)) {
                    return BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[size - 1]));
                } else {
                    numbers[i] = number;
                }
            }
            return subtractProps(numbers);
        } else if (size > 1) {
            BigDecimal[] numbers = new BigDecimal[size];
            for (int i = 0; i < size; i++) {
                BigDecimal number = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[i]));
                if (isInvalidNumber(number)) {
                    return null;
                } else {
                    numbers[i] = number;
                }
            }
            return subtractProps(numbers);
        } else if (size == 1) {
            return BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[0]));
        } else {
            TrisPropsUtil.logger
                .error("Tris fields size should not be zero. Please check your configuration tris.field-mapper, field is " + field
                    + ", key is " + key);
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

    public static List<BidAskQueue> bidAskQuote(final String key, final JsonObject jsonObj) {
        String[] bidAskQueues = getTrisFields("bidAskQueues", key);
        List<BidAskQueue> result = new ArrayList<>();
        for (int i = 0; i < bidAskQueues.length;) {
            BidAskQueue queue = new BidAskQueue();
            result.add(queue);
            queue.setBidPrice(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setBidSize(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setBidOrder(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskPrice(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskSize(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskOrder(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
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
     * @param jsonObj
     * @param context
     * @return
     */
    public static BigDecimal growthRateProps(final String field, final String key, final JsonObject jsonObj,
        final MathContext context) {
        String[] trisFields = getTrisFields(field, key);
        BigDecimal result = null;
        int size = trisFields.length;
        if (size == 3) {
            BigDecimal current = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[0]));
            BigDecimal last = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[1]));
            if (isValidNumber(current) && isValidNumber(last)) {
                result = current.subtract(last).divide(last, context).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
            } else {
                result = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[2]));
            }
        } else if (size == 2) {
            BigDecimal current = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[0]));
            BigDecimal last = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[1]));
            if (isValidNumber(current) && isValidNumber(last)) {
                result = current.subtract(last).divide(last, context).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN);
            }
        } else if (size == 1) {
            result = BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, trisFields[0]));
        } else {
            TrisPropsUtil.logger.error(
                "Tris fields size does not meet the requirements. Please check your configuration tris.field-mapper, field is "
                    + field + ", key is " + key);
            throw new ApplicationException();
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> Take the date and time from Tris Json Object, then convert to
     * ISO9601 format. </b>
     * </p>
     *
     * @param field
     * @param key
     * @param jsonObj
     * @param timezone
     * @return
     */
    public static String dateProps(final String field, final String key, final JsonObject jsonObj, final TimeZone timezone) {
        String[] trisFields = getTrisFields(field, key);
        if (trisFields.length != 2) {
            TrisPropsUtil.logger
                .error("Tris fields size should be two. Please check your configuration tris.field-mapper, field is " + field
                    + ", key is " + key);
            throw new ApplicationException();
        }
        String result = "";
        if (StringUtil.isValid(trisFields[0]) && StringUtil.isValid(trisFields[1])) {
            String iso8601Str = JsonUtil.getAsString(jsonObj, trisFields[0]) + "T" + JsonUtil.getAsString(jsonObj, trisFields[1]);
            try {
                result = DateUtil.convertToISO8601Format(iso8601Str, Constant.DATE_FORMAT_TRIS_ISO8601,
                    TimeZone.getTimeZone(Constant.TIMEZONE_TRIS_ISO8601), timezone);
            } catch (ParseException e) {
                TrisPropsUtil.logger.error("Parse date encounter exception, date is " + iso8601Str, e);
            }
        }
        return result;
    }

    public static List<BidAskQueue> bidAskQueues(final String key, final JsonObject jsonObj) {
        String[] bidAskQueues = getTrisFields("bidAskQueues", key);
        List<BidAskQueue> result = new ArrayList<>();
        for (int i = 0; i < bidAskQueues.length;) {
            BidAskQueue queue = new BidAskQueue();
            result.add(queue);
            queue.setBidPrice(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setBidSize(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setBidOrder(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskPrice(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskSize(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
            queue.setAskOrder(BigDecimalUtil.fromString(JsonUtil.getAsString(jsonObj, bidAskQueues[i++])));
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
     * <b> Get the Tris fields from ThreadLocal. </b>
     * </p>
     *
     * @param field
     * @param key
     * @return
     */
    public static String[] getTrisFields(final String field, final String key) {
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String[]>> fieldMappers =
            (Map<String, Map<String, String[]>>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER);
        if (fieldMappers == null || fieldMappers.isEmpty()) {
            TrisPropsUtil.logger.error("Can't get RESPONSE_TRIS_FIELD_MAPPER from ThreadLocal");
            throw new ApplicationException();
        }
        Map<String, String[]> fieldMapper = fieldMappers.get(key);
        if (fieldMapper == null || fieldMapper.isEmpty()) {
            TrisPropsUtil.logger.error("Can't get fieldMapper from RESPONSE_TRIS_FIELD_MAPPER, key is " + key);
            throw new ApplicationException();
        }
        if (!fieldMapper.containsKey(field)) {
            TrisPropsUtil.logger.error("Can't get Tris fields from field mapper, field is " + field);
            throw new ApplicationException();
        }
        return fieldMapper.get(field);
    }

}
