package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.util.Properties;
import java.util.Set;

import com.dummy.wmd.wpc.graphql.constant.Crypto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CryptoUtil {
    // Encryptor salt from:
    // https://github.com/spring-cloud/spring-cloud-commons/blob/master/spring-cloud-context/src/main/java/org/springframework/cloud/context/encrypt/EncryptorFactory.java
    private static final String ENCRYPTOR_SALT = "deadbeef";
    public static final String CIPHER_PREFIX = "{cipher}";
    // public static final String ENCRYPT_KEY
    private static TextEncryptor encryptor = null;
    static {
        String encryptKey = getEncryptKey();
        if(null != encryptKey){
            encryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
        }
    }

    /**
     * Decrypt property value starts with {cipher}, with the ENCRYPT_KEY from
     * 1 - The user properties
     * 2 - System properties
     * 3 - System env
     *
     * @param props
     */
    public static void decrypt(final Properties props) {
        TextEncryptor thisEncryptor = encryptor;
        String encryptKey = props.getProperty(Crypto.key);
        if(null != encryptKey) {
            log.info("Got ENCRYPT_KEY from user properties");
            thisEncryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
        }
        if(null == thisEncryptor) return;
        final Set<String> keys = props.stringPropertyNames();
        for (final String key : keys) {
            final String value = props.getProperty(key);
            if (value.startsWith(CIPHER_PREFIX)) {
                String encryptedText = value.substring(CIPHER_PREFIX.length());
                String decryptedValue = thisEncryptor.decrypt(encryptedText);
                props.setProperty(key, decryptedValue);
                log.info(String.format("decrypted %s=%s", key, value));
            }
        }
    }

    /**
     * What's fantastic is, even with the same secret, it will generate different encrypted results, and those results
     * can be decrypted back to the same secret
     *
     * @param secret
     * @return
     */
    public static String decrypt(String secret){
        if(null == encryptor) return secret;
        if(secret.startsWith(CIPHER_PREFIX)) {
            secret = secret.substring(CIPHER_PREFIX.length());
        }
        return encryptor.decrypt(secret);
    }

    public static String decrypt(String secret, String encryptKey){
        TextEncryptor thisEncryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
        return thisEncryptor.decrypt(secret);
    }

    public static String encrypt(String secret){
        if(null == encryptor) return secret;
        return encryptor.encrypt(secret);
    }

    public static String encrypt(String secret, String encryptKey){
        TextEncryptor thisEncryptor = Encryptors.text(encryptKey, ENCRYPTOR_SALT);
        return thisEncryptor.encrypt(secret);
    }

    private static String getEncryptKey() {
        String encryptKey = System.getProperty(Crypto.key);
        if(null != encryptKey) {
            log.info("Got ENCRYPT_KEY from system properties");
            return encryptKey;
        }
        encryptKey = System.getenv(Crypto.key);
        if(null != encryptKey) {
            log.info("Got ENCRYPT_KEY from system env");
            return encryptKey;
        }
        return null;
    }
}