package com.dummy.wmd.wpc.graphql.utils;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

import com.dummy.wmd.wpc.graphql.constant.Crypto;

public class CryptoUtilTest {
    @Test
    public void testWithSpecifiedKey(){
        String key = "hello";
        String text = "world";
        String secret = CryptoUtil.encrypt(text, key);
        assertEquals(text, CryptoUtil.decrypt(secret, key));
    }

    @Test
    public void testWithEnvironmentKey(){
        String key = "hello";
        System.setProperty(Crypto.key, key);

        String text = "world";
        String secret = CryptoUtil.encrypt(text);
        assertEquals(text, CryptoUtil.decrypt(secret));
    }

    @Test
    public void testProperties(){
        Properties props = new Properties();
        String key = "hello";
        String text = "world encrypted";
        String secret = CryptoUtil.encrypt(text, key);

        props.put(Crypto.key, key);
        props.put("test1", "world");
        props.put("test2", CryptoUtil.CIPHER_PREFIX + secret);
        CryptoUtil.decrypt(props);
        assertEquals("world", props.getProperty("test1"));
        assertEquals(text, props.getProperty("test2"));
    }
}
