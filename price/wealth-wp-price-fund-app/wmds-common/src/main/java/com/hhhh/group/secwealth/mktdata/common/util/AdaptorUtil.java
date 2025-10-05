package com.hhhh.group.secwealth.mktdata.common.util;

import org.apache.http.NameValuePair;

import java.util.List;

public class AdaptorUtil {

	private AdaptorUtil () {}

    public static String convertNameValuePairListToString(List<NameValuePair> nameValuePairs) {
        StringBuilder result = new StringBuilder();

        for (NameValuePair pair : nameValuePairs) {
            result.append(pair.getName())
                    .append("=")
                    .append(pair.getValue())
                    .append("&");
        }

        // Remove the last separator if needed
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }
}