/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean.predsrch;


/**
 * The Class ScreenerServiceUtil.
 */
public final class CriteriaOperator {

    private CriteriaOperator() {}

    /** The I n_ operator. */
    public static final String IN_OPERATOR = "in";

    /** The I n_ operator. */
    public static final String IN_OPERATOR_EXPRESSION = "IS IN SET";

    /** The COLON. */
    public static final char COLON = ':';

    /** The COMMA. */
    public static final char COMMA = ',';

    /** The INFINITE. */
    public static final String INFINITE = "*";

    public static final String GT_OPERATOR = "gt";
    public static final String GE_OPERATOR = "ge";
    public static final String LT_OPERATOR = "lt";
    public static final String LE_OPERATOR = "le";
    public static final String EQ_OPERATOR = "eq";
    public static final String NE_OPERATOR = "ne";


}
