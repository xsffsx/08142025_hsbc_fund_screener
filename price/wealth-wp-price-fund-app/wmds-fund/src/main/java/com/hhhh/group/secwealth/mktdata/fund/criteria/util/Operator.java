
package com.hhhh.group.secwealth.mktdata.fund.criteria.util;


public enum Operator {
    EQ("="), LT("<"), GT(">"), LE("<="), GE(">="), NE("<>"), NI(" not in "), IN("in");

    private String text;

    Operator(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
