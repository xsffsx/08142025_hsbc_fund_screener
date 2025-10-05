package com.dummy.wmd.wpc.graphql.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeUtils {
    private static final Pattern arrayPattern = Pattern.compile("\\[\\d+]");

    /*
     * Invalid cases:
     * not character(0-9,a-z,A-Z,"_"),
     * not operator("+","/","(",")"),
     * not white space (\ \t\r\n\f )
     * not allow "()","(+","(/",   "++","+/","+)",   "//","/+","/)",   "X(",   ")X",   "[BEGIN]+","[BEGIN]/",   "+[END]","/[END]"
     */
    private static final String[] valSyntPatterns = {
            "[^(\\w\\s+/)]",
            "(\\(\\s*[\\)\\+\\/])",
            "(\\+\\s*[\\+\\/\\)])",
            "(\\/\\s*[\\/\\+\\)])",
            "(\\w\\s*\\()|(\\)\\s*\\w)",
            "(^\\s*[\\+\\/])",
            "([\\+\\/]\\s*$)"
    };

     public static String normalizedJsonPath(String jsonPath){
        String path = jsonPath;
        if(jsonPath.startsWith("[ROOT].")){
            path = jsonPath.substring(7);
        }

        Matcher m = arrayPattern.matcher(path);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "[*]");
        }
        m.appendTail(sb);

        return sb.toString();
    }

    public static boolean validateSyntax(String rule) {
        if (StringUtils.isBlank(rule)) {
            return true;
        }

        for (String regex : valSyntPatterns) {
            if (Pattern.compile(regex).matcher(rule).find()) {
                return false;
            }
        }

        /*
         * Initialize a counter to be 0. +1 when the character is "(" and -1 when ")"
         * 1. Onec the counter < 0, it shows that the brackets are unmatched.
         * 2. If the counter's final value != 0, it shows that the brackets are unmatched.
         */
        int matchBracket = 0;
        for (char c : rule.toCharArray()) {
            if (c == '(') {
                matchBracket++;
            } else if (c == ')') {
                matchBracket--;
            }

            if (matchBracket < 0) {
                return false;
            }
        }

        return matchBracket == 0;
    }
}
