package com.hhhh.group.secwealth.mktdata.quote.beans.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class QuoteDetailRequestTest {

    @Test
    void testToString() {
        Assertions.assertNotNull(new QuoteDetailRequest().toString());
    }

    @Test
    void testHashCode() {
        Assertions.assertNotEquals(new Object().hashCode(), new QuoteDetailRequest().hashCode());
    }

    @Test
    void testNotEquals() {
        Assertions.assertNotEquals(new Object(), new QuoteDetailRequest());
    }

    @Test
    void testRequest_1() {
        QuoteDetailRequest test = new QuoteDetailRequest();
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
        QuoteDetailRequest test = new QuoteDetailRequest();
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
        QuoteDetailRequest test = new QuoteDetailRequest();
        test.setMarket("test");
        test.setProductType("test");
        test.setProdCdeAltClassCde("test");
        test.setProdAltNum("test");
        test.setDelay(false);
        test.setTradeDay(false);
        test.setTradeHours("test");
        test.setFreeQuote(1);
        test.setRequestType("test");
        test.setSkipAgreementCheck("test");
        test.setEntityTimezone("test");
        test.setAdjustedCusSegment("test");
        test.setSide("test");
        test.setQuantity(1);
        test.setChannelRestrictCode("test");
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

    private void testGetter(final QuoteDetailRequest test) {
        String result = test.getMarket() + test.getProductType() + test.getProdCdeAltClassCde() + test.getProdAltNum()
                + test.getDelay() + test.getTradeDay() + test.getTradeHours() + test.getFreeQuote() + test.getRequestType()
                + test.getSkipAgreementCheck() + test.getEntityTimezone() + test.getAdjustedCusSegment() + test.getSide()
                + test.getQuantity() + test.getChannelRestrictCode() + test.getSiteKey() + test.getLocale() + test.getCountryCode()
                + test.getGroupMember() + test.getChannelId() + test.getAppCode() + test.getLineOfBusiness()
                + test.getE2ETrustWealthSaml() + test.getE2ETrustSaml()  + test.getHeaders();
        Assertions.assertNotNull(result);

    }
}
