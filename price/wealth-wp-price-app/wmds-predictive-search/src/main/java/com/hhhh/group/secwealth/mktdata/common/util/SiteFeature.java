/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;


/**
 * <p>
 * <b> SiteFeature. </b>
 * </p>
 */
@Component("siteFeature")
public class SiteFeature {

    private static final String DEFAULT_SITE = "DEFAULT";
    private static final String FILE_PATH = "system/appConfig.properties";
    private PropertyUtil prop;

    @PostConstruct
    public void init() throws Exception {
        this.prop = new PropertyUtil(SiteFeature.FILE_PATH);
    }

    private boolean getBooleanFeature(final String key, final boolean defaultValue) {
        boolean b = defaultValue;
        String value = this.prop.getProperty(key);
        if (StringUtil.isValid(value)) {
            b = Boolean.valueOf(value);
        }
        return b;
    }

    public boolean getBooleanFeature(final String site, final String key, final boolean defaultValue) {
        return this.getBooleanFeature(site + key, defaultValue);
    }

    public String getStringFeature(final String key) {
        return this.prop.getProperty(key);
    }

    public String getStringFeature(final String site, final String key) {
        return this.getStringFeature(site + key);
    }

    public String getStringDefaultFeature(final String site, final String key) {
        String value = this.getStringFeature(site + key);
        if (StringUtil.isInvalid(value)) {
            value = this.getStringFeature(SiteFeature.DEFAULT_SITE + key);
        }
        return value;
    }
}
