package com.hhhh.group.secwealth.mktdata.api.equity.tools;

import com.hhhh.group.secwealth.mktdata.api.equity.model.TrisToken;
import com.hhhh.group.secwealth.mktdata.api.equity.utlis.DateUtil;
import com.hhhh.htsa.mipc.micipher.MICipher;

import java.util.TimeZone;

public class GenerateTrisToken {

    private static final String YYYY_M_MDD_H_HMMSS = "yyyyMMddHHmmss";
    private static final int MINUTES = 15;

    /**
     * Token params
     */
    private static final String COUNTRY_CODE = "HK";
    private static final String GROUP_MEMBER = "HASE";
    private static final String CHANNEL_ID = "OHI";
    private static final String APP_CODE = "HASE_STMA";
    private static final String CLOSURE = "app_tris_rbwm_stockconnect_sat";
    private static final String TIMEZONE = "GMT";

    /**
     * Token keystore config
     */
    private static final String KEYSTORE_TRIS_SIT_SGH_UTB_TRIS_KEYSTORE_DAT = "keystore/tris/sit_sgh_utb/tris_keystore.dat";
    private static final String KEYSTORE_TRIS_SIT_SGH_UTB_TRIS_PASSWORD_DAT = "keystore/tris/sit_sgh_utb/tris_password.dat";
    private static final String IV_PARAM = "12,34,56,78,90,87,65,43";

    private static MICipher miCipher;

    static {
        try {
            String path = GenerateTrisToken.class.getClassLoader().getResource("").getPath();
            miCipher = new MICipher(path+KEYSTORE_TRIS_SIT_SGH_UTB_TRIS_KEYSTORE_DAT, path+KEYSTORE_TRIS_SIT_SGH_UTB_TRIS_PASSWORD_DAT, IV_PARAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TrisToken getTrisTokenModel() {
        TrisToken trisToken = new TrisToken();
        trisToken.setCountryCode(COUNTRY_CODE);
        trisToken.setGroupMember(GROUP_MEMBER);
        trisToken.setChannelId(CHANNEL_ID);
        trisToken.setAppCode(APP_CODE);
        trisToken.setClosure(CLOSURE);
        trisToken.setTimezone(TIMEZONE);
        String timeStamp = DateUtil.afterMinutesOfCurrent(YYYY_M_MDD_H_HMMSS, TimeZone.getTimeZone(TIMEZONE),
                MINUTES);
        trisToken.setTimestamp(timeStamp);
        return trisToken;
    }

    public static void main(String[] args) throws Exception {
        TrisToken tokenModel = getTrisTokenModel();
        System.out.println("Unencrypted Token:  " + tokenModel.toString());
        String tokenString = miCipher.encrypt(tokenModel.toString());
        System.out.println("Encrypted Token:    " + tokenString);
    }

}
