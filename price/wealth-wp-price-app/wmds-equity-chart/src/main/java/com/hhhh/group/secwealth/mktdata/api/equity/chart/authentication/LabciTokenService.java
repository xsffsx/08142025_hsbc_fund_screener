/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.chart.authentication;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.Keystore;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.AuthenticationService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.MICipherAuthenticationService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.LabciProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@ConditionalOnProperty(value = "service.labci.TokenService.injectEnabled")
@Component
public class LabciTokenService {

    private static final Logger logger = LoggerFactory.getLogger(LabciTokenService.class);

    private static final String DEFAULT_SITE = "DEFAULT";

    @Autowired
    private LabciProperties labciProperties;

    private Map<String, AuthenticationService> labciAuthenticationServiceMapper = new HashMap<>();

    /**
     *
     * <p>
     * <b> site => Labci Authentication. </b>
     * </p>
     *
     */
    @PostConstruct
    public void initLabciAuthenticationServiceMapper() {
        Map<String, Keystore> labciKeystoreMappper = this.labciProperties.getTokenKeystore();
        if (labciKeystoreMappper.isEmpty()) {
            LabciTokenService.logger.error("Please check your configuration: \"labci.token-keystore\", it's necessary");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
        }
        for (Map.Entry<String, Keystore> mapper : labciKeystoreMappper.entrySet()) {
            MICipherAuthenticationService labciAuthenticationService = new MICipherAuthenticationService();
            Keystore keystore = mapper.getValue();
            if (!IsValidKeystore(keystore)) {
                LabciTokenService.logger.error("Please check your configuration: \"labci.token-keystore\", properties is invalid");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
            }
            labciAuthenticationService.setKeystoreFilePath(keystore.getKeystoreFilePath());
            labciAuthenticationService.setKeystorePasswordFilePath(keystore.getKeystorePasswordFilePath());
            labciAuthenticationService.setInitialisationVector(keystore.getInitialisationVector());
            try {
                labciAuthenticationService.build();
            } catch (Exception e) {
                LabciTokenService.logger
                    .error("Please check your configuration: \"labci.token-keystore\", init MICipher encounter error", e);
                throw e;
            }
            this.labciAuthenticationServiceMapper.put(mapper.getKey(), labciAuthenticationService);
        }
    }

    private boolean IsValidKeystore(final Keystore keystore) {
        return StringUtil.isValid(keystore.getKeystoreFilePath()) && StringUtil.isValid(keystore.getKeystorePasswordFilePath());
    }

    /**
     * <p>
     * <b> Encrypted Labci token. </b>
     * </p>
     *
     * @param site
     * @param labciToken
     * @return
     */
    public String encryptLabciToken(final String site, final LabciToken labciToken) {
        AuthenticationService authenticationService;
        if (this.labciAuthenticationServiceMapper.containsKey(site)) {
            authenticationService = this.labciAuthenticationServiceMapper.get(site);
        } else if (this.labciAuthenticationServiceMapper.containsKey(LabciTokenService.DEFAULT_SITE)) {
            authenticationService = this.labciAuthenticationServiceMapper.get(LabciTokenService.DEFAULT_SITE);
        } else {
            LabciTokenService.logger
                .error("Please check your configuration: \"labci.token-keystore\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_LABCI_ILLEGAL_CONFIGURATION);
        }
        String token = labciToken.generateTokenStr();
        LabciTokenService.logger.info("Start encrypt Labci token: " + token);
        String encryptedToken;
        try {
            encryptedToken = authenticationService.encrypt(token);
        } catch (Exception e) {
            LabciTokenService.logger.error("Encrypt Labci token encounter error", e);
            throw e;
        }
        LabciTokenService.logger.info("Encrypted Labci token is: " + encryptedToken);
        return encryptedToken;
    }
}
