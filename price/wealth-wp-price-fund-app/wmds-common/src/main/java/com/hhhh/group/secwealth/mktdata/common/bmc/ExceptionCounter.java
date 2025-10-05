/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExceptionCounter {

    private String exceptionName;

    private String key;

    private int timeDuration;

    private int maxCount;

    private List<DurationException> exceptionList;

    private String bmcErrCde;

    private String bmcErrMsg;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("exceptionName", this.exceptionName).append("key", this.key).append("timeDuration", this.timeDuration)
            .append("maxCount", this.maxCount).append("exceptionList", this.exceptionList).append("bmcErrCde", this.bmcErrCde)
            .append("bmcErrMsg", this.bmcErrMsg).toString();
    }

    /**
     * 
     * <p>
     * <b> when maxCount==-1 will ignore the exception. </b>
     * </p>
     * 
     * @return
     */
    public boolean ignoreException() {
        if (this.maxCount == -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the exceptionList
     */
    public List<DurationException> getExceptionList() {
        if (null == this.exceptionList) {
            this.exceptionList = new ArrayList<DurationException>();
        }
        return this.exceptionList;
    }

    /**
     * @param exceptionList
     *            the exceptionList to set
     */
    public void setExceptionList(final List<DurationException> exceptionList) {
        this.exceptionList = exceptionList;
    }

    /**
     * @return the maxCount
     */
    public int getMaxCount() {
        return this.maxCount;
    }

    /**
     * @param maxCount
     *            the maxCount to set
     */
    public void setMaxCount(final int maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * @return the exceptionName
     */
    public String getExceptionName() {
        return this.exceptionName;
    }

    /**
     * @param exceptionName
     *            the exceptionName to set
     */
    public void setExceptionName(final String exceptionName) {
        this.exceptionName = exceptionName;
    }

    /**
     * @return the bmcErrCde
     */
    public String getBmcErrCde() {
        return this.bmcErrCde;
    }

    /**
     * @param bmcErrCde
     *            the bmcErrCde to set
     */
    public void setBmcErrCde(final String bmcErrCde) {
        this.bmcErrCde = bmcErrCde;
    }

    /**
     * @return the bmcErrMsg
     */
    public String getBmcErrMsg() {
        return this.bmcErrMsg;
    }

    /**
     * @param bmcErrMsg
     *            the bmcErrMsg to set
     */
    public void setBmcErrMsg(final String bmcErrMsg) {
        this.bmcErrMsg = bmcErrMsg;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * @return the timeDuration
     */
    public int getTimeDuration() {
        return this.timeDuration;
    }

    /**
     * @param timeDuration
     *            the timeDuration to set
     */
    public void setTimeDuration(final int timeDuration) {
        this.timeDuration = timeDuration;
    }
}
