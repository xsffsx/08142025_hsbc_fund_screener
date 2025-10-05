package com.dummy.wmd.wpc.graphql.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CheckmarxUtilsTest {
    @Test
    public void testPreventHTTPResponseSplitting() {
        String str = "\n\r";
        assertEquals("", CheckmarxUtils.preventHTTPResponseSplitting(str));
    }

    @Test
    public void testPreventCGIReflectedXSSAllClients() {
        String str = "><script>alert(document.cookie)</script>";
        assertEquals("&gt;&lt;script&gt;alert(document.cookie)&lt;/script&gt;",
                CheckmarxUtils.preventCGIReflectedXSSAllClients(str));
    }
}
