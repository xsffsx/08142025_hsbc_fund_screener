
package com.hhhh.group.secwealth.mktdata.fund.constants;

public enum FundSearchListCriteriaKeys {

    FAM("FAM", true), CAT("CAT", true), RISK("RISK", false), CCY("CCY", false), DF("DF", false), Y1QTL("Y1QTL", false), Y3QTL(
        "Y3QTL", false), Y5QTL("Y5QTL", false), Y10QTL("Y10QTL",false), ACQN("ACQN", false), CATLV1("CATLV1", true), INVSTRG(
            "INVSTRG", true), AMCM("AMCM", true), PSC("PSC", true), PCDI("PCDI", true), GBAA("GBAA",true),GNRA("GNRA",true),SIFC(
                    "SIFC",true),SRLN("SRLN",true),ESG("ESG",true);

    private String key;
    private boolean isLocale;

    FundSearchListCriteriaKeys(final String key, final boolean isLocale) {
        this.key = key;
        this.isLocale = isLocale;
    }

    
    public String getKey() {
        return this.key;
    }

    
    public void setKey(final String key) {
        this.key = key;
    }

    
    public boolean isLocale() {
        return this.isLocale;
    }

    
    public void setLocale(final boolean isLocale) {
        this.isLocale = isLocale;
    }

    public static boolean isLocale(final String value) {
        return FundSearchListCriteriaKeys.valueOf(value).isLocale;
    }
}
