
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

@Component("wpcApiCfg")
public class WpcApiCfg {

    @Autowired
    @Qualifier("wpcCfgProperty")
    private Properties properties;

    public String getValue(final String identifier, final String key) throws Exception {
        if (this.properties != null) {
            LogUtil.debug(WpcApiCfg.class, "identifier: {}, key: {}", identifier, key);
            String entitykey = identifier + "." + key;
            String defaultkey = "default." + key;
            if (this.properties.containsKey(entitykey.trim())) {
                return this.properties.getProperty(entitykey.trim());
            } else if (this.properties.containsKey(defaultkey.trim())) {
                return this.properties.getProperty(defaultkey.trim());
            } else {
                LogUtil.error(WpcApiCfg.class, "WpcApiClientconfig Property file does not contain required value, identifier: "
                    + identifier + ", key: " + key);
                throw new CommonException(ErrTypeConstants.SYSTEM_ERROR,
                    "WpcApiClientconfig Property file does not contain required value, identifier: " + identifier + ", key: " + key);
            }
        } else {
            LogUtil.error(WpcApiCfg.class, "WpcApiClientconfig Property Confifuration is not correct");
            throw new CommonException(ErrTypeConstants.SYSTEM_ERROR, "WpcApiClientconfig Property Confifuration is not correct");
        }
    }
}
