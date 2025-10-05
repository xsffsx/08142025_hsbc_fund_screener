package com.hhhh.group.secwealth.mktdata.common.util;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CountryCodeConvertorTest {

    @Test
    public void test() {
        CountryCodeConvertor conv = CountryCodeConvertor.getInstance();
        assertNotNull(conv);
    }
}
