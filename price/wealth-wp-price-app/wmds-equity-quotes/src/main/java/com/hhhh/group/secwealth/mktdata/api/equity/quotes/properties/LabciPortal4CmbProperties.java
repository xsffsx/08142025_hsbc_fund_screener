package com.hhhh.group.secwealth.mktdata.api.equity.quotes.properties;


import com.hhhh.group.secwealth.mktdata.api.equity.quotes.request.entity.Keystore;
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

@Component
@ConfigurationProperties(prefix = "labci-protal-cmb")
@Getter
@Setter
public class LabciPortal4CmbProperties extends LabciPortalProperties{
    private static Logger logger = LoggerFactory.getLogger(LabciPortal4CmbProperties.class);

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

    private String pastPerformanceUrl;

    private String stockInfoUrl;

    private String chartDataUrl;

    @PostConstruct
    public void init() {
        super.init();
    }
}
