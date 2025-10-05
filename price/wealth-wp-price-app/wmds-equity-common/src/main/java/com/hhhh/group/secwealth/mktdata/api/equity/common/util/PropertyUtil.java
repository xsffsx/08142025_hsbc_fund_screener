/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyUtil {

    private final static Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private Properties props;

    public PropertyUtil(final String file) throws Exception {
        this.props = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
            this.props.load(in);
        } catch (Exception e) {
            PropertyUtil.logger.error("init PropertyUtil error, Properties file=" + file);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    PropertyUtil.logger.error("InputStream close error", e);
                }
            }
        }
    }

    public String getProperty(final String key) {
        return this.props.getProperty(key);
    }

    public String getProperty(final String text, final String target, final String replacement) {
        String key = StringUtils.replace(text, target, replacement);
        return this.props.getProperty(key);
    }

    public String getProperty(final String key, final String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }
}
