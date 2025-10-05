/*
 */
package com.hhhh.group.secwealth.mktdata.predsrch.svc.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

@Component("volumeServiceConfig")
public class VolumeServiceConfig {

    public static final String DEFAULT = "DEFAULT";
    public static final String REMOTE_FILE_PATH_KEY = "volumeService.remoteFilePath";
    public static final String PRIVATEKEY_PATH_KEY = "volumeService.privateKeyPath";

    public static final String VOLUME_SERVICE_ENABLED = "volumeService.enabled";
    public static final String VOLUME_SERVICE_DATATYPE = "volumeService.dataType";
    public static final String PREDSRCH_IS_VERIFY = "predsrch.isVerify";

    @Autowired
    @Qualifier("volumeServiceProperty")
    private Properties properties;

    public String getValue(final String identifier, final String key) throws Exception {
        if (this.properties != null) {
            LogUtil.debug(VolumeServiceConfig.class, "identifier: {}, key: {}", identifier, key);
            String entitykey = identifier + CommonConstants.SYMBOL_DOT + key;
            String defaultkey = VolumeServiceConfig.DEFAULT + CommonConstants.SYMBOL_DOT + key;
            if (this.properties.containsKey(entitykey.trim()) && StringUtil.isValid(this.properties.getProperty(entitykey))) {
                return this.properties.getProperty(entitykey);
            } else if (this.properties.containsKey(defaultkey) && StringUtil.isValid(this.properties.getProperty(defaultkey))) {
                return this.properties.getProperty(defaultkey);
            } else {
                return null;
            }
        } else {
            throw new SystemException("VolumeServiceConfig Property Confifuration is not correct");
        }
    }
}
