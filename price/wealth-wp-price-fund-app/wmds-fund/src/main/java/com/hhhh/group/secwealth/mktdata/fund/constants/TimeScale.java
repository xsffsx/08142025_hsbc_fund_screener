
package com.hhhh.group.secwealth.mktdata.fund.constants;

public enum TimeScale {

    ONE_MONTH("1M"), THREE_MONTH("3M"), SIX_MONTH("6M"), ONE_YEAR("1Y"), THREE_YEAR("3Y"), FIVE_YEAR("5Y"), TEN_YEAR("10Y"), YTD(
        "ytd"), QTD("qtd"), MTD("mtd"), ALL("ALL");

    private String timeScale;

    TimeScale(final String timeScale) {
        this.timeScale = timeScale;
    }



    public String getTimeScale() {
        return this.timeScale;
    }



    public void setTimeScale(final String timeScale) {
        this.timeScale = timeScale;
    }


    public static TimeScale fromString(final String timeScaleString) {

        if (timeScaleString == null) {
            return null;
        }

        for (TimeScale timeScale : TimeScale.values()) {
            if (timeScaleString.equals(timeScale.getTimeScale())) {
                return timeScale;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.timeScale;
    }

}
