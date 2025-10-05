package com.hhhh.group.secwealth.mktdata.predsrch.svc.properties;


import com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.Keystore;
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

    {
        if (null == Security.getProperty("IBMJCE")) {
            Security.addProvider(new com.ibm.crypto.provider.IBMJCE());
        }
    }

    private static final String DEFAULT_SITE = "DEFAULT";

    private Map<String, Keystore> tokenKeystore = new HashMap<>();

    private String proxyName;

    private String queryUrl;

    public MICipher miCipher;

    @PostConstruct
    public void init() {
        try {
            Keystore keystore = tokenKeystore.get(DEFAULT_SITE);
            if (keystore != null) {
                if (keystore.getInitialisationVector() == null) {
                    miCipher = new MICipher(keystore.getKeystoreFilePath(), keystore.getKeystorePasswordFilePath());
                }else{
                    miCipher = new MICipher(keystore.getKeystoreFilePath(), keystore.getKeystorePasswordFilePath(),keystore.getInitialisationVector());
                }
            }
        } catch (Exception e) {
            logger.error(
                    "MICipher init error, Please check your configuration ! ");
        }
    }
}
