/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication;

import java.security.Security;

import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalInitializationException;
import com.hhhh.htsa.mipc.micipher.MICipher;

import lombok.Setter;

public class MICipherAuthenticationService implements AuthenticationService {

    {
        if (null == Security.getProperty("IBMJCE")) {
            Security.addProvider(new com.ibm.crypto.provider.IBMJCE());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(MICipherAuthenticationService.class);

    private MICipher miCipher;

    @Setter
    private String keystoreFilePath;

    @Setter
    private String keystorePasswordFilePath;

    @Setter
    private String initialisationVector;

    public void build() {

        try {
            if (this.keystoreFilePath != null && this.keystorePasswordFilePath != null && StringUtil.isValid(this.initialisationVector)) {
                this.miCipher = new MICipher(this.keystoreFilePath, this.keystorePasswordFilePath, this.initialisationVector);
            } else if (this.keystoreFilePath != null && this.keystorePasswordFilePath != null) {
                this.miCipher = new MICipher(this.keystoreFilePath, this.keystorePasswordFilePath);
            } else {
                MICipherAuthenticationService.logger.info(
                        "keystoreFilePath:{}, keystorePasswordFilePath:{}, initialisationVector:{}", this.keystoreFilePath,
                        this.keystorePasswordFilePath, this.initialisationVector);
            }
        } catch (Exception e) {
            MICipherAuthenticationService.logger.error("Init MICipher encounter error", e);
            throw new IllegalInitializationException(ExCodeConstant.EX_CODE_AUTHENTICATION_ILLEGAL_INITIALIZATION, e);
        }
    }

    @Override
    public String encrypt(final String token) {
        try {
            return this.miCipher.encrypt(token);
        } catch (Exception e) {
            MICipherAuthenticationService.logger.error("Encrypt token encounter error, token is " + token, e);
            throw new ApplicationException(ExCodeConstant.EX_CODE_AUTHENTICATION_ENCRYPTION_FAILED, e);
        }
    }

    @Override
    public String decrypt(final String token) {
        try {
            return this.miCipher.decrypt(token);
        } catch (Exception e) {
            MICipherAuthenticationService.logger.error("Decrypt token encounter error, token is " + token, e);
            throw new ApplicationException(ExCodeConstant.EX_CODE_AUTHENTICATION_DECRYPTION_FAIELD, e);
        }
    }

}
