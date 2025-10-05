/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.properties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.agreenmentcheck.bean.RealTimeQuote;
import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.SameSectorParameter;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApplicationProperties {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationProperties.class);

    private Map<String, Map<String, Map<String, List<String>>>> quotesResponseTrisFields = new HashMap<>();
    private Map<String, Map<String, Map<String, List<String>>>> newsResponseTrisFields = new HashMap<>();

    
    private Map<String, Map<String, Map<String, List<String>>>> quotesResponseLabciFields = new HashMap<>();
    
    private Map<String, Map<String, Map<String, List<String>>>> quotesResponseTopMoverFields = new HashMap<>();

    private Map<String, Map<String, Map<String, List<String>>>> quotesResponseCachingServerFields = new HashMap<>();

    private Map<String, String> exchangeTimezoneMapper = new HashMap<>();

    private SameSectorParameter sameSectorParameter;

    private static final String URL = "url";

    private static final String PARAMETER = "parameter";

    private String timezone;
    
    private Map<String, String> siteTimezone = new HashMap<>();

    private Map<String, Map<String, Map<String, String>>> responseFieldMapper = new HashMap<>();

    private Map<String, Map<String, String>> healthcheck = new HashMap<>();
    
    private Map<String,Map<String,Map<String,String>>> quoteMeter = new HashMap<>();

    @Autowired
    private RealTimeQuote realTimeQuote;
    
    public String getSiteTimezone(String site) {
       return siteTimezone.get(site);
    }
    
    public String getQuoteMeter(final String exchange,final String type, final String key){
        return this.quoteMeter.get(exchange).get(type).get(key);
    }

    public Map<String, String> getResponseFieldMapper(final String serviceId, final String beanName) {
        return this.responseFieldMapper.get(serviceId).get(beanName);
    }

    public String getHealthcheckUrl(final String vendor) {
        return this.healthcheck.get(vendor).get(ApplicationProperties.URL);
    }

    public String getHealthcheckParameter(final String vendor) {
        return this.healthcheck.get(vendor).get(ApplicationProperties.PARAMETER);
    }

    public List<String> getQuotesResponseTrisFields(final String[] keys) {
        return getQuotesResponseTrisFields(Constant.DEFAULT_OPTION, keys);
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getQuotesResponseLabciFields(final String[] keys) {
    	 return (List<String>) getList(this.quotesResponseLabciFields, 0, Constant.DEFAULT_OPTION, keys);
    }

    public List<String> getNewsResponseTrisFields(String[] keys) {
        return (List)this.getList(this.newsResponseTrisFields, 0, "DEFAULT", keys);
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getQuotesResponseTopMoverFields(final String[] keys) {
    	 return (List<String>) getList(this.quotesResponseTopMoverFields, 0, Constant.DEFAULT_OPTION, keys);
    }

    /**
     *
     * <p>
     * <b> Get quotes response fields come from Tris with given keys. </b>
     * </p>
     *
     * @param defaultKey
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    private List<String> getQuotesResponseTrisFields(final String defaultKey, final String[] keys) {
        return (List<String>) getList(this.quotesResponseTrisFields, 0, defaultKey, keys);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getQuotesResponseCachingServerFields(final String[] keys) {
         return (List<String>) getList(this.quotesResponseCachingServerFields, 0, Constant.DEFAULT_OPTION, keys);
    }

    /**
     *
     * <p>
     * <b> Loop through the field mapper with the given keys. Finally take the
     * List from the Map. </b>
     * </p>
     *
     * @param fieldMapper
     * @param index
     * @param defaultKey
     * @param keys
     * @return
     */
    @SuppressWarnings("unchecked")
    private Object getList(final Map<String, ?> fieldMapper, int index, final String defaultKey, final String[] keys) {
        if (fieldMapper == null || fieldMapper.isEmpty()) {
            ApplicationProperties.logger.error(
                "Please check your configuration: \"app.quotes-response-tris-fields\", field mapper should not be empty, index: "
                    + index + ", keys: " + Arrays.toString(keys));
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
        // keys is empty
        if (keys == null || keys.length == 0) {
            ApplicationProperties.logger.error("Keys should not be empty");
            throw new ApplicationException();
        }
        if (fieldMapper.containsKey(keys[index])) {
            if (isEndLoop(index, keys)) {
                return (List<String>) fieldMapper.get(keys[index]);
            }
            return getList((Map<String, ?>) fieldMapper.get(keys[index]), ++index, defaultKey, keys);
        } else if (fieldMapper.containsKey(defaultKey)) {
            if (isEndLoop(index, keys)) {
                return (List<String>) fieldMapper.get(defaultKey);
            }
            return getList((Map<String, ?>) fieldMapper.get(defaultKey), ++index, defaultKey, keys);
        } else {
            ApplicationProperties.logger
                .error("Please check your configuration: \"app.quotes-response-tris-fields\", can't find mapper. index: " + index
                    + ", keys: " + Arrays.toString(keys));
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
    }

    private boolean isEndLoop(final int index, final String[] keys) {
        int nextIndex = index + 1;
        return nextIndex >= keys.length;
    }

    /**
     *
     * <p>
     * <b> Merge two arrays. </b>
     * </p>
     *
     * @param a
     * @param b
     * @return
     */
    @Deprecated
    @SuppressWarnings("unused")
    private String[] concat(final String[] a, final String[] b) {
        String[] result = new String[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     *
     * <p>
     * <b> Loop through the field mapper with the given keys. </b>
     * </p>
     *
     * @param fieldMapper
     * @param index
     * @param defaultKey
     * @param keys
     * @return
     */
    @Deprecated
    @SuppressWarnings({"unchecked", "unused"})
    private Map<String, ?> getMapper(final Map<String, ?> fieldMapper, int index, final String defaultKey, final String[] keys) {
        if (fieldMapper == null || fieldMapper.isEmpty()) {
            ApplicationProperties.logger
                .error("Please check your configuration: \"app.**\", field mapper should not be empty, index: " + index + ", keys: "
                    + Arrays.toString(keys));
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
        // keys is empty
        if (keys == null || keys.length == 0) {
            return fieldMapper;
        }
        if (index >= keys.length) {
            return fieldMapper;
        }
        if (fieldMapper.containsKey(keys[index])) {
            return getMapper((Map<String, ?>) fieldMapper.get(keys[index]), ++index, defaultKey, keys);
        } else if (fieldMapper.containsKey(defaultKey)) {
            return getMapper((Map<String, ?>) fieldMapper.get(defaultKey), ++index, defaultKey, keys);
        } else {
            ApplicationProperties.logger.error("Please check your configuration: \"app.**\", can't find mapper. index: " + index
                + ", keys: " + Arrays.toString(keys));
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
    }

    public TimeZone getTimezone(final String exchange) {
        TimeZone result;
        if (this.exchangeTimezoneMapper.isEmpty()) {
            ApplicationProperties.logger
                .error("Please check your configuration: \"app.exchange-timezone-mapper\", mapper should not be empty");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
        if (this.exchangeTimezoneMapper.containsKey(exchange)) {
            result = TimeZone.getTimeZone(this.exchangeTimezoneMapper.get(exchange));
        } else if (this.exchangeTimezoneMapper.containsKey(Constant.DEFAULT_OPTION)) {
            result = TimeZone.getTimeZone(this.exchangeTimezoneMapper.get(Constant.DEFAULT_OPTION));
        } else {
            ApplicationProperties.logger
                .error("Please check your configuration: \"app.exchange-timezone-mapper\", default exchange should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_APP_ILLEGAL_CONFIGURATION);
        }
        return result;
    }

}
