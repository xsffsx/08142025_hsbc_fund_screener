package com.hhhh.group.secwealth.mktdata.api.equity.news.constants;

import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;

import java.util.HashMap;
import java.util.Map;

public class NewsConstant {

    /**
     * new headline category:
     * CN: TN/LN
     */
    public static final String MARKET_CN_CATEGORY_TN = "TN";
    public static final String MARKET_CN_CATEGORY_LN = "LN";
    public static final String MARKET_CN = "CN";
    public static final String MARKET_HK = "HK";

    public static final String Locale_ZH_CN = "zh_CN";
    public static final String Locale_ZH = "zh";

    public static final String SORT_BY = "revisiontime";
    public static final String CATEGORY_RELATEDNEWS = "relatednews";
    public static final String KEYS = "keys";

    public static final String NEWS_HEADLINES_SERVICE_FLAG = "NEWS_HEADLINES";
    public static final String NEWS_HEADLINES_CMB_SERVICE_FLAG = "NEWS_HEADLINES_CMB";
    public static final String NEWS_DETAIL_SERVICE_FLAG = "NEWS_DETAIL";
    public static final String NEWS_DETAIL_CMB_SERVICE_FLAG = "NEWS_DETAIL_CMB";

    public static final String TOP_NEWS_QUERY_PATTERN = "(topics:CN) AND (topics:STX OR topics:INV OR topics:GVD OR topics:CDM OR topics:USC OR topics:COM OR topics:HEDGE OR topics:REAL OR topics:MMT)";

    public static final String LATEST_NEWS_QUERY_PATTERN = "(topics:CN)";

    public static final String HEADLINE_FILTER = "HEADLINE";

    public static final String DETAIL_FILTER = "DETAIL";

    public static final String CODING_UTF8 = "UTF-8";

    public static final String HTTP_PARAM_TOKEN = "token";

    public static final String HTTP_PARAM_MESSAGE = "message";


    public static final Map<String, String> TRIS_ERROR_CODE_EXCEPTION_MAPPING = new HashMap<String, String>() {
        {
            put("301", ExCodeConstant.EX_CODE_TRIS_INVALID_TOKEN);
            put("302", ExCodeConstant.EX_CODE_TRIS_INVALID_TOKEN);
            put("303", ExCodeConstant.EX_CODE_TRIS_PERMISS_DENIED);
            put("471", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("450", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("472", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("440", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("451", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("473", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("441", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("452", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("474", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("420", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("442", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("453", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("475", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("443", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("476", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("444", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("477", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("445", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("478", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("446", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("447", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("448", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
            put("449", ExCodeConstant.EX_CODE_TRIS_INVAILD_REQUEST);
        }
    };


}



