
package com.hhhh.group.secwealth.mktdata.fund.constants;

public enum CriteriaKey {
    TABLE_NAME("tableName"), TIME_SCALE("timeScale"), SUPPORT_MARKET("supportMarket"), TYPE_OF_ETF("typeOfETF"), CATEGORY(
        "category"), PRODUCT_SUB_TYPE("productSubType"),CURRENCY("currency") , PROD_STAT_CDE("prodStatCde"),LIMIT_RESULT("limitResult");

    private String text;

    CriteriaKey(final String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
