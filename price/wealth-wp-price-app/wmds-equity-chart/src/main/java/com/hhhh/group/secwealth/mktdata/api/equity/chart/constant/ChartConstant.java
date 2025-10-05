package com.hhhh.group.secwealth.mktdata.api.equity.chart.constant;


import java.util.HashMap;
import java.util.Map;

public class ChartConstant {
    public static final String CHART_DATA_SERVICE_FLAG = "EQUITY_CHART";
    public static final String CHART_DATA_CMB_SERVICE_FLAG = "EQUITY_CHART_CMB";
    public static final String INDICATORS = "SMA10,SMA20,SMA50";
    public static final String Locale_ZH_CN = "zh_CN";
    public static final String Locale_ZH = "zh";
    public static final String MINUTE = "MINUTE";
    public static final String DAILY = "DAILY";
    public static final String SYMBOL_M = "m";
    public static final String FLAG_Y = "Y";

    public static final Map<String, String> LABCI_PORTAL_ERROR_CODE_INVALID_REQUEST_MAPPING = new HashMap<String, String>() {
        {
            put("441", "Missing required item parameter(s)");
            put("442", "Missing required interval unit parameter");
            put("445", "Invalid input item");
            put("446", "Invalid input filters");
            put("447", "Invalid input start time");
            put("448", "Invalid input end time");
            put("449", "Invalid input interval unit");
            put("450", "Invalid input interval length");
            put("452", "Mismatching between interval unit and interval length");
            put("460", "Invalid input period");
            put("461", "Missing required item ric");
            put("462", "Invalid input - ric");
            put("463", "Invalid input - last");
            put("464", "Invalid input - ric");
        }
    };

    public static final Map<String, String> LABCI_PORTAL_ERROR_CODE_INVALID_TOKEN_MAPPING = new HashMap<String, String>() {
        {
            put("100", "Invalid token or missing token");

        }
    };
}
