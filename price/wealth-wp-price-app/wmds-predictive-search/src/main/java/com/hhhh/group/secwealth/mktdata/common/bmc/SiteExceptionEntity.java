/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class SiteExceptionEntity extends CommonBMCRequest {

    private ConcurrentHashMap<String, ExceptionCounter> exceptionCounters = new ConcurrentHashMap<String, ExceptionCounter>();
    private int totalExceptionCount = 0;
    private List<DurationException> recentExceptionList = new ArrayList<DurationException>();;
    private int timeDuration;
    private int errNum;
    private int errNumStart;
    private String errCdeThrownPastSec;
    private String errCdeExceedLimit;
    private String errmgsThrownPastSec;
    private String errmgsExceedLimit;
    private String prefix;

    private List<ExceptionCounter> counterList;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("exceptionCounters", this.exceptionCounters).append("totalExceptionCount", this.totalExceptionCount)
            .append("recentExceptionList", this.recentExceptionList).append("timeDuration", this.timeDuration)
            .append("errNum", this.errNum).append("errNumStart", this.errNumStart)
            .append("errCdeThrownPastSec", this.errCdeThrownPastSec).append("errCdeExceedLimit", this.errCdeExceedLimit)
            .append("errmgsThrownPastSec", this.errmgsThrownPastSec).append("errmgsExceedLimit", this.errmgsExceedLimit)
            .append("prefix", this.prefix).append("counterList", this.counterList).toString();
    }

    public void initCounterMap() {
        if (null != this.counterList) {
            for (int i = 0; i < this.counterList.size(); i++) {
                ExceptionCounter counter = this.counterList.get(i);
                this.exceptionCounters.put(counter.getExceptionName() + counter.getKey(), counter);
            }
        }
    }

    public void totalCountIncrement() {
        this.totalExceptionCount++;
    }

    public void totalCountDecrease(final int count) {
        this.totalExceptionCount = this.totalExceptionCount - count;
    }

    public void clearTotalCount() {
        this.totalExceptionCount = 0;
    }

    /**
     * @return the totalExceptionCount
     */
    public int getTotalExceptionCount() {
        return this.totalExceptionCount;
    }

    /**
     * @param totalExceptionCount
     *            the totalExceptionCount to set
     */
    public void setTotalExceptionCount(final int totalExceptionCount) {
        this.totalExceptionCount = totalExceptionCount;
    }

    /**
     * @return the recentExceptionList
     */
    public List<DurationException> getRecentExceptionList() {
        return this.recentExceptionList;
    }

    /**
     * @param recentExceptionList
     *            the recentExceptionList to set
     */
    public void setRecentExceptionList(final List<DurationException> recentExceptionList) {
        this.recentExceptionList = recentExceptionList;
    }

    /**
     * @return the exceptionCounters
     */
    public ConcurrentHashMap<String, ExceptionCounter> getExceptionCounters() {
        return this.exceptionCounters;
    }

    /**
     * @param exceptionCounters
     *            the exceptionCounters to set
     */
    public void setExceptionCounters(final ConcurrentHashMap<String, ExceptionCounter> exceptionCounters) {
        this.exceptionCounters = exceptionCounters;
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

    /**
     * @return the errNum
     */
    public int getErrNum() {
        return this.errNum;
    }

    /**
     * @param errNum
     *            the errNum to set
     */
    public void setErrNum(final int errNum) {
        this.errNum = errNum;
    }

    /**
     * @return the errNumStart
     */
    public int getErrNumStart() {
        return this.errNumStart;
    }

    /**
     * @param errNumStart
     *            the errNumStart to set
     */
    public void setErrNumStart(final int errNumStart) {
        this.errNumStart = errNumStart;
    }

    /**
     * @return the errCdeThrownPastSec
     */
    public String getErrCdeThrownPastSec() {
        return this.errCdeThrownPastSec;
    }

    /**
     * @param errCdeThrownPastSec
     *            the errCdeThrownPastSec to set
     */
    public void setErrCdeThrownPastSec(final String errCdeThrownPastSec) {
        this.errCdeThrownPastSec = errCdeThrownPastSec;
    }

    /**
     * @return the errCdeExceedLimit
     */
    public String getErrCdeExceedLimit() {
        return this.errCdeExceedLimit;
    }

    /**
     * @param errCdeExceedLimit
     *            the errCdeExceedLimit to set
     */
    public void setErrCdeExceedLimit(final String errCdeExceedLimit) {
        this.errCdeExceedLimit = errCdeExceedLimit;
    }

    /**
     * @return the errmgsThrownPastSec
     */
    public String getErrmgsThrownPastSec() {
        return this.errmgsThrownPastSec;
    }

    /**
     * @param errmgsThrownPastSec
     *            the errmgsThrownPastSec to set
     */
    public void setErrmgsThrownPastSec(final String errmgsThrownPastSec) {
        this.errmgsThrownPastSec = errmgsThrownPastSec;
    }

    /**
     * @return the errmgsExceedLimit
     */
    public String getErrmgsExceedLimit() {
        return this.errmgsExceedLimit;
    }

    /**
     * @param errmgsExceedLimit
     *            the errmgsExceedLimit to set
     */
    public void setErrmgsExceedLimit(final String errmgsExceedLimit) {
        this.errmgsExceedLimit = errmgsExceedLimit;
    }

    /**
     * @return the counterList
     */
    public List<ExceptionCounter> getCounterList() {
        return this.counterList;
    }

    /**
     * @param counterList
     *            the counterList to set
     */
    public void setCounterList(final List<ExceptionCounter> counterList) {
        this.counterList = counterList;
    }

    /**
     * @return the prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * @param prefix
     *            the prefix to set
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }
}