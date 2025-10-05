/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.Keystore;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.tris.ResponseStatus;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.vendor.tris.SameSectorResponseStatus;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.SymbolConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * <p>
 * <b> Provides a series of methods to get Tris related attribute values. </b>
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "tris")
@Getter
@Setter
public class TrisProperties {

    private static final Logger logger = LoggerFactory.getLogger(TrisProperties.class);

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

    private String url;
    
    private String newsHeadlinesUrl;
    
    private String newsDetailUrl;

    private String sameSectorUrl;
    
    private String chartDataUrl;

    private Map<String, Map<String, Map<String, Map<String, String>>>> fieldMapper = new HashMap<>();

    private Map<String, Map<String, String>> trisFieldMapper = new HashMap<>();

    private List<String> trisFieldMapperKeys = new ArrayList<>();

    private ResponseStatus responseStatus;

    private SameSectorResponseStatus sameSectorResponseStatus;

    @PostConstruct
    public void init() {
        initTrisFieldMapper();
        initTrisFieldMapperKeys();
    }

    /**
     *
     * <p>
     * <b> {site}_{product}_{exchange} => Tris field mapper. Please note, each
     * level must have "DEFAULT" key. </b>
     * </p>
     *
     */
    private void initTrisFieldMapper() {
        if (!this.fieldMapper.containsKey(TrisProperties.DEFAULT_SITE)) {
            TrisProperties.logger
                .error("Please check your configuration \"tris.field-mapper.{site}\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        for (Map.Entry<String, Map<String, Map<String, Map<String, String>>>> siteEntry : this.fieldMapper.entrySet()) {
            String site = siteEntry.getKey();
            Map<String, Map<String, Map<String, String>>> siteMapper = siteEntry.getValue();
            if (!siteMapper.containsKey(TrisProperties.DEFAULT_PRODUCT)) {
                TrisProperties.logger.error(
                    "Please check your configuration \"tris.field-mapper.{site}.{product}\", default product should be \"DEFAULT\"");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
            }
            for (Map.Entry<String, Map<String, Map<String, String>>> productEntry : siteMapper.entrySet()) {
                String product = productEntry.getKey();
                Map<String, Map<String, String>> productMapper = productEntry.getValue();
                if (!productMapper.containsKey(TrisProperties.DEFAULT_EXCHANGE)) {
                    TrisProperties.logger.error(
                        "Please check your configuration \"tris.field-mapper.{site}.{product}.{exchange}\", default exchange should be \"DEFAULT\"");
                    throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
                }
                for (Map.Entry<String, Map<String, String>> exchangeEntry : productMapper.entrySet()) {
                    String exchange = exchangeEntry.getKey();
                    Map<String, String> exchangeMapper = exchangeEntry.getValue();
                    if (exchangeMapper.isEmpty()) {
                        TrisProperties.logger.error(
                            "Please check your configuration \"tris.field-mapper.{site}.{product}.{exchange}.Map\", Map should not be empty");
                        throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
                    }
                    String key = new StringBuilder().append(site).append(SymbolConstant.SYMBOL_UNDERLINE).append(product)
                        .append(SymbolConstant.SYMBOL_UNDERLINE).append(exchange).toString();
                    this.trisFieldMapper.put(key, exchangeMapper);
                }
            }
        }
    }

    /**
     *
     * <p>
     * <b> List<{site}_{product}_{exchange}> </b>
     * </p>
     *
     */
    private void initTrisFieldMapperKeys() {
        for (String trisFieldMapperKey : this.trisFieldMapper.keySet()) {
            this.trisFieldMapperKeys.add(trisFieldMapperKey);
        }
    }

    /**
     *
     * <p>
     * <b> Return a string identifying the desired data source. Service is
     * required in Tirs request. </b>
     * </p>
     *
     * @param site
     * @param delay
     * @return
     */
    public String getTrisService(final String site, final boolean delay) {
        Map<String, String> serviceOptions;
        if (this.service.containsKey(site)) {
            serviceOptions = this.service.get(site);
        } else if (this.service.containsKey(TrisProperties.DEFAULT_SITE)) {
            serviceOptions = this.service.get(TrisProperties.DEFAULT_SITE);
        } else {
            TrisProperties.logger.error("Please check your configuration: \"tris.service\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        String key = delay ? TrisProperties.DELAY_OPTION : TrisProperties.REALTIME_OPTION;
        if (serviceOptions.containsKey(key)) {
            String result = serviceOptions.get(key);
            if (StringUtil.isValid(result)) {
                return result;
            } else {
                TrisProperties.logger.error("Please check your configuration: \"tris.service\", the returned value is invalid");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
            }
        } else {
            TrisProperties.logger.error("Please check your configuration: \"tris.service\", options are \"realtime\" or \"delay\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
    }

    /**
     *
     * <p>
     * <b> Return a string identifying the client. Closure is required in Tris
     * request. </b>
     * </p>
     *
     * @param site
     * @return
     */
    public String getTrisClosure(final String site) {
        String result;
        if (this.closure.containsKey(site)) {
            result = this.closure.get(site);
        } else if (this.closure.containsKey(TrisProperties.DEFAULT_SITE)) {
            result = this.closure.get(TrisProperties.DEFAULT_SITE);
        } else {
            TrisProperties.logger.error("Please check your configuration: \"tris.closure\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        if (StringUtil.isInValid(result)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.closure\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> AppCode is required in Tris token. </b>
     * </p>
     *
     * @param site
     * @return
     */
    public String getTrisTokenAppCode(final String site) {
        String result;
        if (this.tokenAppCode.containsKey(site)) {
            result = this.tokenAppCode.get(site);
        } else if (this.tokenAppCode.containsKey(TrisProperties.DEFAULT_SITE)) {
            result = this.tokenAppCode.get(TrisProperties.DEFAULT_SITE);
        } else {
            TrisProperties.logger
                .error("Please check your configuration: \"tris.token-app-code\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        if (StringUtil.isInValid(result)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.token-app-code\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> Timezone is required in Tris token. </b>
     * </p>
     *
     * @param site
     * @return
     */
    public String getTrisTokenTimezone(final String site) {
        String result;
        if (this.tokenTimezone.containsKey(site)) {
            result = this.tokenTimezone.get(site);
        } else if (this.tokenTimezone.containsKey(TrisProperties.DEFAULT_SITE)) {
            result = this.tokenTimezone.get(TrisProperties.DEFAULT_SITE);
        } else {
            TrisProperties.logger
                .error("Please check your configuration: \"tris.token-timezone\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        if (StringUtil.isInValid(result)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.token-timezone\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> Access Tris URL. </b>
     * </p>
     *
     * @return
     */
    public String getTrisUrl() {
        if (StringUtil.isInValid(this.url)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.url\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return this.url;
    }

    /**
     *
     * <p>
     * <b> Access Tris SameSector URL. </b>
     * </p>
     *
     * @return
     */
    public String getTrisSameSectorUrl() {
        if (StringUtil.isInValid(this.sameSectorUrl)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.same-sector-url\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return this.sameSectorUrl;
    }
    
    /**
    *
    * <p>
    * <b> Access Tris cahrt data URL. </b>
    * </p>
    *
    * @return
    */
   public String getCharDataUrl() {
       if (StringUtil.isInValid(this.chartDataUrl)) {
           TrisProperties.logger.error("Please check your configuration: \"tris.char-data-url\", the returned value is invalid");
           throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
       }
       return this.chartDataUrl;
   }

    /**
     *
     * <p>
     * <b> Get the corresponding Tris fields with the given fields and keys.
     * </b>
     * </p>
     *
     * @param fields
     * @param keys
     * @return
     */
    public List<String> getTrisFields(final List<String> fields, final String[] keys) {
        return getTrisFields(fields, Constant.DEFAULT_OPTION, keys);
    }

    /**
     *
     * <p>
     * <b> Get the corresponding Tris fields with the given fields, defaultKey
     * and keys. </b>
     * </p>
     *
     * @param fields
     * @param defaultKey
     * @param keys
     * @return
     */
    private List<String> getTrisFields(final List<String> fields, final String defaultKey, final String[] keys) {
        List<String> result = new ArrayList<>();
        List<Map<String, String>> trisFieldMappers = getTrisFieldMappers(defaultKey, keys);
        Map<String, String[]> responseFieldMapper = new HashMap<>();
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String[] trisField = getTrisField(trisFieldMappers, field);
            Collections.addAll(result, trisField);
            responseFieldMapper.put(field, trisField);
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, String[]>> fieldMappers =
            (Map<String, Map<String, String[]>>) ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER);
        String key = String.join(SymbolConstant.SYMBOL_VERTICAL_LINE, keys);
        if (fieldMappers == null || fieldMappers.isEmpty()) {
            fieldMappers = new HashMap<>();
        }
        fieldMappers.put(key, responseFieldMapper);
        ArgsHolder.putArgs(Constant.THREAD_INVISIBLE_RESPONSE_TRIS_FIELD_MAPPER, fieldMappers);
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
    private List<Map<String, String>> getTrisFieldMappers(final String defaultKey, final String[] keys) {
        List<String> trisFieldMapperKeys = getTrisFieldMapperKeys(defaultKey, keys);
        List<Map<String, String>> result = new ArrayList<>();
        for (int i = 0; i < trisFieldMapperKeys.size(); i++) {
            result.add(this.trisFieldMapper.get(trisFieldMapperKeys.get(i)));
        }
        return result;
    }

    /**
     *
     * <p>
     * <b> If defaultKey is DEFAULT, keys are A, B, C, possible values are
     * (A_B_C, A_B_DEFAULT, A_DEFAULT_C, A_DEFAULT_DEFAULT, DEFAULT_B_C,
     * DEFAULT_B_DEFAULT, DEFAULT_DEFAULT_C, DEFAULT_DEFAULT_DEFAULT), then
     * intersect with trisFieldMapperKeys. </b>
     * </p>
     *
     * @param defaultKey
     * @param keys
     * @return
     */
    private List<String> getTrisFieldMapperKeys(final String defaultKey, final String[] keys) {
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
        possibleKeys.retainAll(this.trisFieldMapperKeys);
        return possibleKeys;
    }

    /**
     *
     * <p>
     * <b> Arrange and combine each columns. ((A,B),(a),(1,2,3)) => (A_a_1,
     * A_a_2, A_a_3, B_a_1, B_a_2, B_a_3) </b>
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
     * <b> Query Tris field with given field in Tris field mappers. </b>
     * </p>
     *
     * @param trisFieldMappers
     * @param field
     * @return
     */
    private String[] getTrisField(final List<Map<String, String>> trisFieldMappers, final String field) {
        String result = "";
        for (int i = 0; i < trisFieldMappers.size(); i++) {
            Map<String, String> trisFieldMapper = trisFieldMappers.get(i);
            if (trisFieldMapper.containsKey(field)) {
                result = trisFieldMapper.get(field);
                break;
            }
        }
        if (StringUtil.isInValid(result)) {
            TrisProperties.logger.error("Please check your configuration: \"tris.field-mapper\", can't find field: " + field);
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        if (result.contains(SymbolConstant.SYMBOL_COMMA)) {
            return result.split(SymbolConstant.SYMBOL_COMMA);
        } else {
            return new String[] {result};
        }
    }

    public String getResponseStatusCodeKey() {
        if (StringUtil.isInValid(this.responseStatus.getCodeKey())) {
            TrisProperties.logger
                .error("Please check your configuration: \"tris.response-status.code-key\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return this.responseStatus.getCodeKey();
    }

    public boolean isCorrectResponseStatus(final String responseCode) {
        return this.responseStatus.getCorrectStatus().equals(responseCode);
    }

    public String getResponseStatusTextKey() {
        if (StringUtil.isInValid(this.responseStatus.getTextKey())) {
            TrisProperties.logger
                .error("Please check your configuration: \"tris.response-status.text-key\", the returned value is invalid");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        return this.responseStatus.getTextKey();
    }

    public String[] getSameSectorStatusKey() {
        String[] keys = this.sameSectorResponseStatus.getKey();
        if (keys == null || keys.length <= 0) {
            TrisProperties.logger
                .error("Please check your configuration: \"tris.same-sector-response-status.key\", it's should not be empty");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        for (String key : keys) {
            if (StringUtil.isInValid(key)) {
                TrisProperties.logger.error(
                    "Please check your configuration: \"tris.same-sector-response-status.key\", the returned value is invalid");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
            }
        }
        return this.sameSectorResponseStatus.getKey();
    }

    public boolean isNormalSameSectorResponseStatus(final String[] status) {
        return isCorrectResponseStatus(status) || isWarnResponseStatus(status);
    }

    public boolean isCorrectResponseStatus(final String[] status) {
        return Arrays.equals(status, this.sameSectorResponseStatus.getCorrectStatus());
    }

    public boolean isWarnResponseStatus(final String[] status) {
        return Arrays.equals(status, this.sameSectorResponseStatus.getWarnStatus());
    }

}
