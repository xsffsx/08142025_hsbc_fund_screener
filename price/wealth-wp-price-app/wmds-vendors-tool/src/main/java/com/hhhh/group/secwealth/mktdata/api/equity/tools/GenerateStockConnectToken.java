package com.hhhh.group.secwealth.mktdata.api.equity.tools;

import com.hhhh.group.secwealth.mktdata.api.equity.model.LabciToken;
import com.hhhh.group.secwealth.mktdata.api.equity.utlis.DateUtil;
import com.hhhh.htsa.mipc.micipher.MICipher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class GenerateStockConnectToken {

    private static final String YYYY_M_MDD_H_HMMSS = "yyyyMMddHHmmss";
	private static final int MINUTES = 15;

    /**
     * Token params
     */
	private static final String CHANNEL_ID = "OHI";
//	private static final String APP_CODE = "MDS_HASE";
	private static final String APP_CODE = "stock_connect";
	private static final String TIMEZONE = "GMT";

    /**
     * Token keystore config
     */
	private static final String KEYSTORE_LABCI_SIT_SGH_UTB_LABCI_KEYSTORE_DAT = "keystore/stockconnect/mktdata_keystore.dat";
	private static final String KEYSTORE_LABCI_SIT_SGH_UTB_LABCI_PASSWORD_DAT = "keystore/stockconnect/mktdata_cipher_keystore_password.dat";
	private static final String IV_PARAM = "55,05,09,72,73,18,86,93";
    public static final String CUSTOMER_ID = "u0001";
    public static final String DURATION = "43200";
    public static final String MARKET_ID = "CN";
    public static final String MARKET_FLAG = "Y";
    public static final String OBO = "N";

    private static MICipher miCipher;

    static {
        try {
            String path = GenerateStockConnectToken.class.getClassLoader().getResource("").getPath();
            miCipher = new MICipher(path+KEYSTORE_LABCI_SIT_SGH_UTB_LABCI_KEYSTORE_DAT, path+KEYSTORE_LABCI_SIT_SGH_UTB_LABCI_PASSWORD_DAT,IV_PARAM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static LabciToken getTrisTokenModel() {
        LabciToken labciToken = new LabciToken();
        labciToken.setChannelId(CHANNEL_ID);
        labciToken.setAppCode(APP_CODE);
        labciToken.setCustomerId(CUSTOMER_ID);
        labciToken.setDuration(DURATION);
        labciToken.setMarketId(MARKET_ID);
        labciToken.setMarketFlag(MARKET_FLAG);
        labciToken.setObo(OBO);
        String timeStamp = DateUtil.afterMinutesOfCurrent(YYYY_M_MDD_H_HMMSS, TimeZone.getTimeZone(TIMEZONE),
                MINUTES);
        labciToken.setTimeStamp(timeStamp);
        return labciToken;
    }

    public static void main(String[] args) throws Exception {
//        LabciToken tokenModel = getTrisTokenModel();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateText = dateFormat.format(new Date());
//        String token = "stockconnect~~OHI~~HK~~HASE~~"+dateText+"~~null~~IE954811~~null~~21280543~~";
        String token = "HASE_DDF~~OHI~~HK~~HASE~~"+dateText+"~~CN~~F0HPPTT7OO59CKRUEMMBEDCR";
//        String token = "HASE_DDF~~OHI~~HK~~HASE~~"+dateText+"~~CN~~F0HPPTT7OO59CKRSDFSDF";
        Map<String, Object> o = convertToMap(token);
        System.out.println(o);

        System.out.println("Unencrypted Token:  " + token);

        String tokenString = miCipher.encrypt(token);
        System.out.println("Encrypted Token:    " + tokenString);
    }

    public static final String SYMBOL_DELIMITER = "~~";
    public static final String SYMBOL_TOKEN_END = "~~null";

    private static String[] tokens = new String[]{"appCode","channelId","countryCode","groupMember","timeStamp","market","customerId","cusSegment"};



    public static Map<String, Object> convertToMap(final String decryptedToken) throws Exception {
        StringBuffer tokenBuff = new StringBuffer(decryptedToken);
        String[] filedValue = tokenBuff.append(SYMBOL_TOKEN_END).toString().split(SYMBOL_DELIMITER);
        // Support different token length by site or project
        int length = filedValue.length > tokens.length ? tokens.length : filedValue.length;
        Map<String, Object> tokenMap = new HashMap<String, Object>();
        for (int i = 0; i < length; i++) {
            tokenMap.put(tokens[i], filedValue[i]);
        }
        return tokenMap;
    }
}
