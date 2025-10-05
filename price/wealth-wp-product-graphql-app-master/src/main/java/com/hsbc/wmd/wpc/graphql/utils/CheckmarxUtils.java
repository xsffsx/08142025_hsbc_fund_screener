package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.util.HtmlUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckmarxUtils {
    public static String preventHTTPResponseSplitting(String text) {
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        return text;
    }
    public static String preventCGIReflectedXSSAllClients(String text) {
        return HtmlUtils.htmlEscape(text);
    }
}
