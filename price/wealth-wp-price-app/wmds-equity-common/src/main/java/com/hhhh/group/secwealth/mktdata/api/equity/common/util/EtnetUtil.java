package com.hhhh.group.secwealth.mktdata.api.equity.common.util;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

@Slf4j
public class EtnetUtil {

    public static String encode(String encodeString) {
        if (StringUtil.isInValid(encodeString)) {
            log.error("encode the string should't be empty!");
            throw new ApplicationException();
        }
        return "Basic " + Base64.encodeBase64String(encodeString.getBytes());
    }



    public static void main(String[] args) {
//        String usernameColonPassword = "user:passwd";
        String usernameColonPassword = "hs11user:hset1234";
        String basicAuthPayload = encode(usernameColonPassword);
        System.out.println("The Authorization:  " + basicAuthPayload);
    }
}
