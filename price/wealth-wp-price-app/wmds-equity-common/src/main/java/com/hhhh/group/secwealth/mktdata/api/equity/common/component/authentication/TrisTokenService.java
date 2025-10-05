/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.Keystore;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.TrisToken;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.DateUtil;
import com.hhhh.group.secwealth.mktdata.api.equity.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.Constant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

@Component
public class TrisTokenService {

    private static final Logger logger = LoggerFactory.getLogger(TrisTokenService.class);

    private static final String DEFAULT_SITE = "DEFAULT";

    @Autowired
    private TrisProperties trisProps;

    private Map<String, AuthenticationService> trisAuthenticationServiceMapper = new HashMap<>();

    /**
     *
     * <p>
     * <b> site => Tris Authentication. </b>
     * </p>
     *
     */
    @PostConstruct
    public void initTrisAuthenticationServiceMapper() {
        Map<String, Keystore> trisKeystoreMappper = this.trisProps.getTokenKeystore();
        if (trisKeystoreMappper.isEmpty()) {
            TrisTokenService.logger.error("Please check your configuration: \"tris.token-keystore\", it's necessary");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        for (Map.Entry<String, Keystore> mapper : trisKeystoreMappper.entrySet()) {
            MICipherAuthenticationService trisAuthenticationService = new MICipherAuthenticationService();
            Keystore keystore = mapper.getValue();
            if (!IsValidKeystore(keystore)) {
                TrisTokenService.logger.error("Please check your configuration: \"tris.token-keystore\", properties is invalid");
                throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
            }
            trisAuthenticationService.setKeystoreFilePath(keystore.getKeystoreFilePath());
            trisAuthenticationService.setKeystorePasswordFilePath(keystore.getKeystorePasswordFilePath());
            trisAuthenticationService.setInitialisationVector(keystore.getInitialisationVector());
            try {
                trisAuthenticationService.build();
            } catch (Exception e) {
                TrisTokenService.logger
                    .error("Please check your configuration: \"tris.token-keystore\", init MICipher encounter error", e);
                throw e;
            }
            this.trisAuthenticationServiceMapper.put(mapper.getKey(), trisAuthenticationService);
        }
    }

    private boolean IsValidKeystore(final Keystore keystore) {
        return StringUtil.isValid(keystore.getKeystoreFilePath()) && StringUtil.isValid(keystore.getKeystorePasswordFilePath())
            && StringUtil.isValid(keystore.getInitialisationVector());
    }

    /**
     * <p>
     * <b> Generate an encrypted tris token string. </b>
     * </p>
     *
     * @param site
     * @param header
     * @param appCode
     * @param closure
     * @param timezone
     * @return
     */
    public String getEncryptedTrisToken(final String site, final CommonRequestHeader header, final String appCode,
        final String closure, final String timezone) {

        TrisToken token = generateTrisToken(header, appCode, closure, timezone);
        return encryptTrisToken(site, token);
    }

    /**
     * <p>
     * <b>Generate tris token object </b>
     * </p>
     *
     * @param header
     * @param appCode
     * @param closure
     * @param timezone
     * @return
     */
    private TrisToken generateTrisToken(final CommonRequestHeader header, final String appCode, final String closure,
        final String timezone) {
        TrisToken token = new TrisToken(header);
        token.setAppCode(appCode);
        token.setCustomerId(String.valueOf(ArgsHolder.getArgs(Constant.THREAD_INVISIBLE_ENCRYPTED_CUSTOMER_ID)));
        token.setClosure(closure);
        token.setTimezone(timezone);
        String timeStamp = DateUtil.afterMinutesOfCurrent(Constant.DATE_FORMAT_TRIS_TOKEN, TimeZone.getTimeZone(timezone),
            Constant.TRIS_TOKEN_DURATION_MINUTES);
        token.setTimestamp(timeStamp);
        return token;
    }

    /**
     * <p>
     * <b> Encrypted Tris token. </b>
     * </p>
     *
     * @param site
     * @param trisToken
     * @return
     */
    public String encryptTrisToken(final String site, final TrisToken trisToken) {
        AuthenticationService authenticationService;
        if (this.trisAuthenticationServiceMapper.containsKey(site)) {
            authenticationService = this.trisAuthenticationServiceMapper.get(site);
        } else if (this.trisAuthenticationServiceMapper.containsKey(TrisTokenService.DEFAULT_SITE)) {
            authenticationService = this.trisAuthenticationServiceMapper.get(TrisTokenService.DEFAULT_SITE);
        } else {
            TrisTokenService.logger
                .error("Please check your configuration: \"tris.token-keystore\", default site should be \"DEFAULT\"");
            throw new IllegalConfigurationException(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION);
        }
        String token = trisToken.toString();
        TrisTokenService.logger.info("Start encrypt Tris token: " + token);
        String encryptedToken;
        try {
            encryptedToken = authenticationService.encrypt(token);
        } catch (Exception e) {
            TrisTokenService.logger.error("Encrypt Tris token encounter error", e);
            throw e;
        }
        TrisTokenService.logger.info("Encrypted Tris token is: " + encryptedToken);
        return encryptedToken;
    }


}
