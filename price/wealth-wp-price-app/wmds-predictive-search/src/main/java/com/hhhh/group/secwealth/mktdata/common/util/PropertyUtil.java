/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertyUtil {

    private Properties props;

    public PropertyUtil(final String file) throws Exception {
        this.props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(file);
            this.props.load(in);
        } catch (Exception e) {
            LogUtil.error(PropertyUtil.class, "init PropertyUtil error, Properties file=" + file, e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(PropertyUtil.class, "InputStream close error", e);
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
