/*
 */
package com.hhhh.group.secwealth.mktdata.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.IOUtils;
import org.castor.util.Base64Decoder;

import com.sun.crypto.provider.SunJCE;

public class CryptoUtil {

    /** The Constant ALGORITHM. */
    private static final String ALGORITHM = "DESede";
    private static final String PROPERTY_CIPHER_PREFIX = "{crypto}";

    /**
     * decrypt the password with key bytes The keyByte and pwd have a higher
     * priority against the keyPath and pwdPath
     */
    public synchronized static String decryptPwd(final String keyPath, String encryptedPwd, final String DESedePath)
        throws Exception {

        InputStream keyIputStream = null;
        InputStream DESedePathIputStream = null;

        if (StringUtil.isInvalid(encryptedPwd) || StringUtil.isInvalid(keyPath) || StringUtil.isInvalid(DESedePath)) {
            return encryptedPwd;
        }

        if (encryptedPwd.indexOf(CryptoUtil.PROPERTY_CIPHER_PREFIX) != 0) {
            return encryptedPwd;
        }

        try {
            encryptedPwd = encryptedPwd.replace(CryptoUtil.PROPERTY_CIPHER_PREFIX, "");
            byte[] pwdByte = Base64Decoder.decode(encryptedPwd);

            byte[] keyByte = new byte[128];
            keyIputStream = CryptoUtil.class.getClassLoader().getResourceAsStream(keyPath);
            keyIputStream.read(keyByte);

            DESedePathIputStream = CryptoUtil.class.getClassLoader().getResourceAsStream(DESedePath);
            byte[] iv = IOUtils.toByteArray(DESedePathIputStream);

            Security.addProvider(new SunJCE());
            SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance(CryptoUtil.ALGORITHM);
            DESedeKeySpec desedekeyspec = new DESedeKeySpec(keyByte);
            javax.crypto.SecretKey secretkey = secretkeyfactory.generateSecret(desedekeyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretkey, ivspec);
            return new String(cipher.doFinal(pwdByte));
        } catch (Exception e) {
            LogUtil.error(CryptoUtil.class,
                "decrypt error, keyPath=" + keyPath + ", encryptedPwd=" + encryptedPwd + ", DESedePath=" + DESedePath, e);
            throw e;
        } finally {
            if (null != keyIputStream) {
                try {
                    keyIputStream.close();
                } catch (IOException e) {
                    LogUtil.error(CryptoUtil.class, "close InputStream error", e);
                }
            }
            if (null != DESedePathIputStream) {
                try {
                    DESedePathIputStream.close();
                } catch (IOException e) {
                    LogUtil.error(CryptoUtil.class, "close InputStream error", e);
                }
            }
        }
    }

    public synchronized static String decryptFilePwd(final String keyPath, final String encryptedFilePwd, final String DESedePath)
        throws Exception {
        String encryptedPwd = readFile(encryptedFilePwd);
        return decryptPwd(keyPath, encryptedPwd, DESedePath);
    }

    private static String readFile(final String file) throws Exception {
        String result = "";
        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            in = CryptoUtil.class.getClassLoader().getResourceAsStream(file);
            reader = new InputStreamReader(in);
            br = new BufferedReader(reader);

            String line = null;
            while ((line = br.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            LogUtil.error(CryptoUtil.class, "readFile error, file name=" + file + ", error=" + e.getMessage(), e);
            throw e;
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    LogUtil.error(CryptoUtil.class, "close BufferedReader error", e);
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LogUtil.error(CryptoUtil.class, "close FileReader error", e);
                }
            }
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtil.error(CryptoUtil.class, "close InputStream error", e);
                }
            }
        }

        return result;
    }
}
