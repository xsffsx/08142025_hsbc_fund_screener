/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.news.util;

import com.google.gson.JsonObject;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.BigDecimalUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.JsonUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Map;
import java.util.TimeZone;

public class TrisPropsUtil {

    private static final Logger logger = LoggerFactory.getLogger(TrisPropsUtil.class);

    public static String inOrderStrProps(final String field, final String key, final JsonObject jsonObj) {
        String[] trisFields = getTrisFields(field, key);
        String result = "";
        for (String trisField : trisFields) {
            result = JsonUtil.getAsString(jsonObj, trisField);
            if (StringUtil.isInValidResp(result)) {
                break;
            }
        }
        return result;
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
