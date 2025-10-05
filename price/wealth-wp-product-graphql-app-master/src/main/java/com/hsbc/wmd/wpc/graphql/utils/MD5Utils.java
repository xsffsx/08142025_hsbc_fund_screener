package com.dummy.wmd.wpc.graphql.utils;

import com.dummy.wmd.wpc.graphql.error.productErrorException;
import com.dummy.wmd.wpc.graphql.error.productErrors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("java:S4790")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MD5Utils {
    public static String md5(String text, boolean compact){
        if(compact) {   // Remove leading spaces between xml tags
            text = text.trim().replaceAll(">(\\s+)<", "><");
        }
        return md5(text);
    }

    public static String md5(String text){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return Hex.encodeHexString(md.digest());
        } catch (NoSuchAlgorithmException e){
            throw new productErrorException(productErrors.RuntimeException, "Error calc md5 for: " + text);
        }
    }

    public static String md5(byte[] bytes){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return Hex.encodeHexString(md.digest());
        } catch (NoSuchAlgorithmException e){
            throw new productErrorException(productErrors.RuntimeException, "Error calc md5 for bytes");
        }
    }
}
