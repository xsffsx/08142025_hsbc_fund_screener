package com.hhhh.group.secwealth.mktdata.fund.criteria.util;


public enum SortOrder {

    ASC, DESC;

    public static SortOrder fromString(final String sortOrderString) {

        if (sortOrderString == null) {
            return null;
        }
        for (SortOrder sortOrder : SortOrder.values()) {
            if (sortOrder.name().equalsIgnoreCase(sortOrderString)) {
                return sortOrder;
            }
        }
        return null;
    }
}
