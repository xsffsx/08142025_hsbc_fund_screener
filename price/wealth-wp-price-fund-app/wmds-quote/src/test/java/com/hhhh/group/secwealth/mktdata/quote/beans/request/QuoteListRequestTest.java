package com.hhhh.group.secwealth.mktdata.quote.beans.request;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class QuoteListRequestTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new QuoteListRequest().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new QuoteListRequest().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new QuoteListRequest());
    }

    @Test
    void testRequest_1() {
        QuoteListRequest test = new QuoteListRequest();
        test.addHeader("x-hhhh-chnl-countrycode", "CN");
        test.addHeader("x-hhhh-chnl-group-member", "hhhh");
        test.addHeader("x-hhhh-channel-id", "OHI");
        test.addHeader("x-hhhh-locale", "zh_CN");
        test.addHeader("x-hhhh-app-code", "Wmds");
        test.addHeader("x-hhhh-line-of-business", "PFS");
        test.addHeader("X-hhhh-Wealth-Saml", "Wealth-Saml");
        test.addHeader("X-hhhh-Saml", "Saml");

        Assertions.assertEquals("CN_hhhh", test.getSiteKey());
        Assertions.assertEquals("zh_CN", test.getLocale());
        Assertions.assertEquals("CN", test.getCountryCode());
        Assertions.assertEquals("hhhh", test.getGroupMember());
        Assertions.assertEquals("OHI", test.getChannelId());
        Assertions.assertEquals("Wmds", test.getAppCode());
        Assertions.assertEquals("PFS", test.getLineOfBusiness());
        Assertions.assertEquals("Wealth-Saml", test.getE2ETrustWealthSaml());
        Assertions.assertEquals("Saml", test.getE2ETrustSaml());
    }

    @Test
    void testRequest_2() {
        QuoteListRequest test = new QuoteListRequest();
        Map<String, String> headers = new HashMap<>();
        headers.put("x-hhhh-chnl-countrycode", "CN");
        headers.put("x-hhhh-chnl-group-member", "hhhh");
        headers.put("x-hhhh-channel-id", "OHI");
        headers.put("x-hhhh-locale", "zh_CN");
        headers.put("x-hhhh-app-code", "Wmds");
        headers.put("x-hhhh-line-of-business", "PFS");
        headers.put("X-hhhh-Wealth-Saml", "Wealth-Saml");
        headers.put("X-hhhh-Saml", "Saml");
        test.putAllHeaders(headers);

        Assertions.assertEquals("CN_hhhh", test.getSiteKey());
        Assertions.assertEquals("zh_CN", test.getLocale());
        Assertions.assertEquals("CN", test.getCountryCode());
        Assertions.assertEquals("hhhh", test.getGroupMember());
        Assertions.assertEquals("OHI", test.getChannelId());
        Assertions.assertEquals("Wmds", test.getAppCode());
        Assertions.assertEquals("PFS", test.getLineOfBusiness());
        Assertions.assertEquals("Wealth-Saml", test.getE2ETrustWealthSaml());
        Assertions.assertEquals("Saml", test.getE2ETrustSaml());
    }

    @Test
    void testGetterAndSetter() {
        QuoteListRequest test = new QuoteListRequest();
        test.setMarket("test");
        test.setDelay(false);
        test.setProductKeys(Lists.newArrayList());
        test.setTradeDay(false);
        test.setTradeHours("test");
        test.setFreeQuote(1);
        test.setRequestType("test");
        test.setSkipAgreementCheck("SkipAgreementCheck");
        test.setEntityTimezone("EntityTimezone");
        Map<String, String> headers = new HashMap<>();
        headers.put("x-hhhh-chnl-countrycode", "CN");
        headers.put("x-hhhh-chnl-group-member", "hhhh");
        headers.put("x-hhhh-channel-id", "OHI");
        headers.put("x-hhhh-locale", "zh_CN");
        headers.put("x-hhhh-app-code", "Wmds");
        headers.put("x-hhhh-line-of-business", "PFS");
        headers.put("X-hhhh-Wealth-Saml", "Wealth-Saml");
        headers.put("X-hhhh-Saml", "Saml");

        Assertions.assertNotNull(test.toString());
        this.testGetter(test);
    }

    private void testGetter(final QuoteListRequest test) {
        String result = test.getMarket() + test.getDelay() + test.getProductKeys() + test.getTradeDay()
                + test.getTradeHours() + test.getFreeQuote() + test.getRequestType() + test.getSkipAgreementCheck()
                + test.getEntityTimezone() + test.getSiteKey() + test.getLocale() + test.getCountryCode() + test.getGroupMember()
                + test.getChannelId() + test.getAppCode() + test.getLineOfBusiness() + test.getE2ETrustWealthSaml()
                + test.getE2ETrustSaml()  + test.getHeaders();
        Assertions.assertNotNull(result);
    }
}
