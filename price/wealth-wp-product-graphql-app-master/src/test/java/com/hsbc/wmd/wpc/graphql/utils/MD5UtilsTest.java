package com.dummy.wmd.wpc.graphql.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MD5UtilsTest {
    @Test
    public void testMd5(){
        String expected = "63594bbffb3190f61ac75b3cd33e1f81";
        String text1 = "<request><channelCode>WDS</channelCode><countryCode>GB</countryCode>";
        String text2 = "  <request>   <channelCode>WDS</channelCode>   <countryCode>GB</countryCode>   ";
        String md51 = MD5Utils.md5(text1);
        String md52 = MD5Utils.md5(text1.getBytes(StandardCharsets.UTF_8));
        String md53 = MD5Utils.md5(text2, true);
        String md54 = MD5Utils.md5(text2, false);
        assertEquals(expected, md51);
        assertEquals(expected, md52);
        assertEquals(expected, md53);
        assertNotEquals(expected, md54);
    }

    @Test
    public void testLeadingZero() throws DecoderException {
        String md5 = "0c48622de6637c0604eff5948375706c";
        byte[] bytes = Hex.decodeHex(md5);
        String md51 = Hex.encodeHexString(bytes);
        assertEquals(md5, md51);
    }
}
