
package com.hhhh.group.secwealth.mktdata.fund.constants;

public enum SupportMarket {

    CANADA("CA"), UNITED_STATES("US");

    private String supportMarket;

    SupportMarket(final String supportMarket) {
        this.supportMarket = supportMarket;
    }


    public String getSupportMarket() {
        return this.supportMarket;
    }

    public void setSupportMarket(final String supportMarket) {
        this.supportMarket = supportMarket;
    }

    public static SupportMarket fromString(final String supportMarketString) {

        if (supportMarketString == null) {
            return null;
        }

        for (SupportMarket supportMarket : SupportMarket.values()) {
            if (supportMarketString.equals(supportMarket.getSupportMarket())) {
                return supportMarket;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.supportMarket;
    }

}
