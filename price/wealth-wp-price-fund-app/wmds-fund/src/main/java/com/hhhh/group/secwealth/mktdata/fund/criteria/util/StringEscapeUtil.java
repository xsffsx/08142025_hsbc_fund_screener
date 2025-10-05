
package com.hhhh.group.secwealth.mktdata.fund.criteria.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;


public class StringEscapeUtil {

    final static Map<String, String> specialCharacterMap = new HashMap<String, String>();
    static {
        StringEscapeUtil.specialCharacterMap.put("\\|", "|");
        StringEscapeUtil.specialCharacterMap.put("\\:", ":");
        StringEscapeUtil.specialCharacterMap.put("\\,", ",");
        StringEscapeUtil.specialCharacterMap.put("\\[", "[");
        StringEscapeUtil.specialCharacterMap.put("\\]", "]");
        StringEscapeUtil.specialCharacterMap.put("\\(", "(");
        StringEscapeUtil.specialCharacterMap.put("\\)", ")");
        StringEscapeUtil.specialCharacterMap.put("\\*", "*");
        StringEscapeUtil.specialCharacterMap.put("\\'", "'");
        StringEscapeUtil.specialCharacterMap.put("\\n", "\n");
        StringEscapeUtil.specialCharacterMap.put("\\t", "\t");
        StringEscapeUtil.specialCharacterMap.put("\\<", "<");
        StringEscapeUtil.specialCharacterMap.put("\\>", ">");
        StringEscapeUtil.specialCharacterMap.put("\\{", "{");
        StringEscapeUtil.specialCharacterMap.put("\\}", "}");
        StringEscapeUtil.specialCharacterMap.put("\\\"", "\"");
        StringEscapeUtil.specialCharacterMap.put("\\=", "=");
        StringEscapeUtil.specialCharacterMap.put("\\\\", "\\");
        StringEscapeUtil.specialCharacterMap.put("\\^", "^");

    }

    private Map<String, String> internalCharacterMap = new HashMap<String, String>();

    private int count = 0;

    public String escapeString(final String oldString) {

        if (StringUtils.isBlank(oldString)) {
            return oldString;
        }

        while (StringUtils.contains(oldString, new StringBuilder("{").append(this.count).append("}").toString())) {
            this.count++;
        }

        String escapedString = oldString;
        for (Map.Entry<String, String> entry : StringEscapeUtil.specialCharacterMap.entrySet()) {
            while (StringUtils.contains(oldString, new StringBuilder("{").append(this.count).append("}").toString())) {
                this.count++;
            }
            String internalCharacter = new StringBuilder("{").append(this.count).append("}").toString();
            escapedString = StringUtils.replace(escapedString, entry.getKey(), internalCharacter);
            this.internalCharacterMap.put(internalCharacter, entry.getValue());
            this.count++;
        }
        return escapedString;
    }

    public String unescapeString(final String oldString) {

        if (StringUtils.isBlank(oldString)) {
            return oldString;
        }

        String unescapedString = oldString;
        for (Map.Entry<String, String> entry : this.internalCharacterMap.entrySet()) {
            unescapedString = StringUtils.replace(unescapedString, entry.getKey(), entry.getValue());
        }

        return unescapedString;
    }
}
