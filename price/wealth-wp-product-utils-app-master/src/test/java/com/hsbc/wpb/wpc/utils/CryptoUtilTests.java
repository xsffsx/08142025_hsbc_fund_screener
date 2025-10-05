package com.dummy.wpb.wpc.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class CryptoUtilTests {

    @Test
    public void testDecrypt_givenProperties_doesNotThrow() {
        try {
            final Properties pro = new Properties();
            pro.put("ENCRYPT_KEY","properEncryptKey");
            CryptoUtil.decrypt(pro);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testDecrypt_givenSecret_doesNotThrow() {
        try {
            CryptoUtil.decrypt("{cipher}secret");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testDecrypt_givenSecretAndEncryptKey_doesNotThrow() {
        try {
            CryptoUtil.decrypt("112b115b4ad66fa3109a6f25a9eba8ee6b613b2c725fad580965efeea793c63c","123");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testEncrypt_givenSecret_doesNotThrow() {
        try {
            CryptoUtil.encrypt("{cipher}secret");
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void testEncrypt_givenSecretAndEncryptKey_doesNotThrow() {
        try {
            CryptoUtil.encrypt("secret","123");
        } catch (Exception e) {
            Assert.fail();
        }
    }

}