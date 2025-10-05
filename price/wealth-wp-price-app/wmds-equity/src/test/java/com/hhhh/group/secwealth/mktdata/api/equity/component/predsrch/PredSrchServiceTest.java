/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.component.predsrch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class PredSrchServiceTest {

    @Test
    public void testURLEncoder() {
        String str = "abc";
        String charset = StandardCharsets.UTF_8.name();
        try {
            URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Assert.fail("Unsupported Encoding Exception");
        }
    }

}
