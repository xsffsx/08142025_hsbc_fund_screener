package com.hhhh.group.secwealth.mktdata.elastic.properties;


import com.hhhh.group.secwealth.mktdata.elastic.bean.Keystore;
import com.hhhh.htsa.mipc.micipher.MICipher;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "labci")
@Getter
@Setter
@Component
public class LabciProtalProperties {
    private static Logger logger = LoggerFactory.getLogger(LabciProtalProperties.class);

    static {
        if (null == Security.getProperty("IBMJCE")) {
            Security.addProvider(new com.ibm.crypto.provider.IBMJCE());
        }
    }

    private static final String DEFAULT_SITE = "DEFAULT";

    private Map<String, Keystore> tokenKeystore = new HashMap<>();

    private String proxyName;

    private String queryUrl;

    private MICipher miCipher;

    @PostConstruct
    public void init() {
        try {
            Keystore keystore = tokenKeystore.get(DEFAULT_SITE);
            if (keystore != null) {
                miCipher = new MICipher(keystore.getKeystoreFilePath(), keystore.getKeystorePasswordFilePath());
            }
        } catch (Exception e) {
            logger.error(
                    "MICipher init error, Please check your configuration ! ");
            e.printStackTrace();
        }
    }
}
