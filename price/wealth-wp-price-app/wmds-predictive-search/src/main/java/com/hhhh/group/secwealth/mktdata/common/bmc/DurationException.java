/*
 */
package com.hhhh.group.secwealth.mktdata.common.bmc;

import java.util.GregorianCalendar;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hhhh.group.secwealth.mktdata.common.util.DateUtil;

public class DurationException {

    private GregorianCalendar time;
    private Throwable error;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString()).append("time", this.time)
            .append("error", this.error).toString();
    }

    public DurationException() {
        super();
    }

    public DurationException(final Throwable error) {
        super();
        this.error = error;
        this.time = DateUtil.getMachineCalendar(null, null);
    }

    /**
     * @return the time
     */
    public GregorianCalendar getTime() {
        return this.time;
    }

    /**
     * @param time
     *            the time to set
     */
    public void setTime(final GregorianCalendar time) {
        this.time = time;
    }

    /**
     * @return the error
     */
    public Throwable getError() {
        return this.error;
    }

    /**
     * @param error
     *            the error to set
     */
    public void setError(final Throwable error) {
        this.error = error;
    }

}
