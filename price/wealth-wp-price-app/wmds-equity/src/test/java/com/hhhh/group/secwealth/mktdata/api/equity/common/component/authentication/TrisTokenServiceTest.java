/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.hhhh.group.secwealth.mktdata.api.equity.common.bean.Keystore;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.AuthenticationService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.MICipherAuthenticationService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.component.authentication.TrisTokenService;
import com.hhhh.group.secwealth.mktdata.api.equity.common.properties.TrisProperties;
import com.hhhh.group.secwealth.mktdata.api.equity.common.token.TrisToken;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.IllegalConfigurationException;


public class TrisTokenServiceTest {

    @Test
    public void testInitTrisAuthenticationServiceMapperIncorrectInitialization() {
        TrisProperties trisProps = new TrisProperties();
        Map<String, Keystore> tokenKeystore = new HashMap<>();
        trisProps.setTokenKeystore(tokenKeystore);
        TrisTokenService service = new TrisTokenService();
        Whitebox.setInternalState(service, TrisProperties.class, trisProps);
        try {
            service.initTrisAuthenticationServiceMapper();
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

        trisProps = new TrisProperties();
        tokenKeystore = new HashMap<>();
        trisProps.setTokenKeystore(tokenKeystore);
        tokenKeystore.put("VALID_SITE", new Keystore());
        service = new TrisTokenService();
        Whitebox.setInternalState(service, TrisProperties.class, trisProps);
        try {
            service.initTrisAuthenticationServiceMapper();
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }
    }

    @Test
    public void testGetTrisTokenWithInvalidSiteParameter() {
        TrisTokenService service = new TrisTokenService();
        Map<String, AuthenticationService> trisAuthenticationServiceMapper = new HashMap<>();
        trisAuthenticationServiceMapper.put("NON_DEFAULT_SITE", new MICipherAuthenticationService());
        Whitebox.setInternalState(service, Map.class, trisAuthenticationServiceMapper);

        String invalidSite = "INVALID_SITE";
        TrisToken token = new TrisToken();
        try {
            service.encryptTrisToken(invalidSite, token);
        } catch (IllegalConfigurationException e) {
            String exCode = e.getMessage();
            assertThat(exCode, equalTo(ExCodeConstant.EX_CODE_TRIS_ILLEGAL_CONFIGURATION));
        }

    }

}
