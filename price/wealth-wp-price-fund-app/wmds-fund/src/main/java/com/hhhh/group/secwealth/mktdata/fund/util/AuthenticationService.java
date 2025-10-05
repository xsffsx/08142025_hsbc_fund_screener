
package com.hhhh.group.secwealth.mktdata.fund.util;

import java.security.Security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.htsa.mipc.micipher.MICipher;


@Component("authenticationService")
public class AuthenticationService {
	
	{
        if (null == Security.getProperty("IBMJCE")) {
            Security.addProvider(new com.ibm.crypto.provider.IBMJCE());
        }
    }

    private MICipher cipher;

    @Value("#{systemConfig['dashboardkey.keystoreFile']}")
    private String keystoreFile;

    @Value("#{systemConfig['dashboardkey.passwordKeystoreFile']}")
    private String passwordKeystoreFile;

    @Value("#{systemConfig['dashboardKey.ivParam']}")
    private String ivParam;

    @PostConstruct
    public void init() throws Exception {
        try {
//            URL keystoreFileURL = this.getClass().getClassLoader().getResource(this.keystoreFile);
//            URL pwdKeystoreFileURL = this.getClass().getClassLoader().getResource(this.passwordKeystoreFile);
//            this.cipher = new MICipher(keystoreFileURL.getPath(), pwdKeystoreFileURL.getPath(), this.ivParam);
            
            this.cipher = new MICipher(this.keystoreFile, this.passwordKeystoreFile, this.ivParam);
            
            if (this.cipher == null) {
                LogUtil.error(AuthenticationService.class,
                    "init Initialize the MICipher is null,keystoreFileURL: {}, passwordKeystoreFile: {}, ivParam: {}",
                    this.keystoreFile, this.passwordKeystoreFile, this.ivParam);
                throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR);
            }
        } catch (Exception e) {
            LogUtil.error(AuthenticationService.class,
                "Initialize the MICipher is fail!, keystoreFileURL: {}, passwordKeystoreFile: {}, ivParam: {}, message: {}",
                this.keystoreFile, this.passwordKeystoreFile, this.ivParam, e.getMessage(), e);
            throw new SystemException(ErrTypeConstants.SYSTEM_INIT_ERROR, e);
        }
    }


    public MICipher getCipher() {
        return this.cipher;
    }


    public void setCipher(final MICipher cipher) {
        this.cipher = cipher;
    }

    public String encrypt(final String token) throws Exception {
        // This implementation is not supported appId
        return this.encyptToken(token);
    }

    public String decrypt(final String token) throws Exception {
        // This implementation is not supported appId
        return this.decyptToken(token);
    }

    private synchronized String decyptToken(final String tokenStr) throws Exception {
        return this.cipher.decrypt(tokenStr);
    }

    private synchronized String encyptToken(final String tokenStr) throws Exception {
        return this.cipher.encrypt(tokenStr);
    }

}
