/*
 */
package com.hhhh.group.secwealth.mktdata.api.equity.quotes.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Security;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.castor.util.Base64Decoder;

import com.hhhh.wmd.itt.security.util.FileUtils;

import com.sun.crypto.provider.SunJCE;

/**
 * The Class DecryptPassword.
 */
public class DecryptPassword {

    // DON'T use logger in this classs, will impact the third party and GSE
    // load shell script running.
    /** The Constant logger. */
    // private static final Logger logger =
    // LoggerFactory.getLogger(DecryptPassword.class);

    /** The Constant ALGORITHM. */
    public static final String ALGORITHM = "DESede";

    private static String PATHSTRING = "(\\S|\\s)*.(key|password|pwd)$";

    private static final String PUB_FILE = "/DESede.pub";

    private static final int MAX_STR_LEN = Integer.MAX_VALUE;

    public static String readLine(final BufferedReader br) throws IOException {
        String line = br.readLine();
        if (StringUtils.isNotBlank(line) && line.length() >= DecryptPassword.MAX_STR_LEN) {
            throw new RuntimeException("input too long");
        }
        return line;
    }

    /**
     * Instantiates a new decrypt password.
     */
    public DecryptPassword() {}

    /**
     * 
     * @param pwdPath
     * @param keyPath
     * @return
     * @throws Exception
     */
    public static String decrypt(final String pubFilePath, final String keyFilePath, final String pwdFilePath) throws Exception {
        if (!Pattern.compile(DecryptPassword.PATHSTRING).matcher(pwdFilePath).matches()
            || !Pattern.compile(DecryptPassword.PATHSTRING).matcher(keyFilePath).matches()) {
            throw new RuntimeException("File type incorrect, expect file suffix *.key, *.password, *.pwd");
        }
        byte keyByte[] = new byte[128];
        byte pwdByte[] = new byte[128];
        InputStream pwdIputStrem = FileUtils.getFileAsStream(pwdFilePath);
        InputStream keyIputStrem = FileUtils.getFileAsStream(keyFilePath);

        BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(pwdIputStrem));
        String line = readLine(bufferedreader);
        pwdByte = Base64Decoder.decode(line);
        try {
            InputStream pubInputStream = FileUtils.getFileAsStream(pubFilePath);
            Security.addProvider(new SunJCE());
            keyIputStrem.read(keyByte);
            SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance(DecryptPassword.ALGORITHM);
            DESedeKeySpec desedekeyspec = new DESedeKeySpec(keyByte);
            javax.crypto.SecretKey secretkey = secretkeyfactory.generateSecret(desedekeyspec);
            Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            // byte[] iv = {
            // 12, 34, 56, 78, 90, 87, 65, 43
            // };
            // SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            // byte[] iv = new byte[8];
            // random.nextBytes(iv);
            byte[] iv = IOUtils.toByteArray(pubInputStream);
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretkey, ivspec);
            return new String(cipher.doFinal(pwdByte));
        } catch (IOException ioexception) {
            // if (logger.isErrorEnabled()) {
            // logger.error("Error: cannot read from key file", ioexception);
            // }
            // if (logger.isErrorEnabled()) {
            // logger.error("Error: cannot read from key file", ioexception);
            // }
            throw new Exception(ioexception);
        } catch (Exception exception) {
            // if (logger.isErrorEnabled()) {
            // logger.error("Error: cannot retriving secret key", exception);
            // }
            // if (logger.isErrorEnabled()) {
            // logger.error("Error: cannot retriving secret key", exception);
            // }
            throw new Exception(exception);
        } finally {
            if (pwdIputStrem != null) {
                pwdIputStrem.close();
            }
            if (keyIputStrem != null) {
                keyIputStrem.close();
            }
        }
    }

    public static void main(final String args[]) throws Exception {
        String pubFilePath = "C:/sandbox/eclipseworkspace/wmds-workspace-FE/wmds-online-config/src/main/resources/SKM_SGH_UTB/resources/system/common/e2etrust/DESede.pub_wds";
        String keyFilePath = "C:/sandbox/eclipseworkspace/wmds-workspace-FE/wmds-online-config/src/main/resources/SKM_SGH_UTB/resources/system/common/e2etrust/wealth-validation-keystore-asp-prd.key";
        String pwdFilePath = "C:/sandbox/eclipseworkspace/wmds-workspace-FE/wmds-online-config/src/main/resources/SKM_SGH_UTB/resources/system/common/e2etrust/wealth-validation-keystore-asp-prd.pwd";
        String password = decrypt(pubFilePath, keyFilePath, pwdFilePath);
        System.out.println(password);
    }

}
