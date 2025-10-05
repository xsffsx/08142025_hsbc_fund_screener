/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.hhhh.group.secwealth.mktdata.elastic.util.CommonConstants;


/**
 * <p>
 * <b> Insert description of the class's responsibility/role. </b>
 * </p>
 */
public class Version {

    private String timeStamp;

    private String sequence;

    private boolean checkedExpired = false;

    public Version(final String timestamp, final String seq) {
        this.timeStamp = timestamp;
        this.sequence = seq;
    }

    public Version(final String timestamp, final String seq, final boolean checked) {
        this.timeStamp = timestamp;
        this.sequence = seq;
        this.checkedExpired = checked;
    }

    public String genVersion() {
        return new StringBuffer(this.timeStamp).append(CommonConstants.SYMBOL_UNDERLINE).append(this.sequence).toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("timeStamp", this.timeStamp).append("sequence", this.sequence).toString();
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(final String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSequence() {
        return this.sequence;
    }

    public void setSequence(final String sequence) {
        this.sequence = sequence;
    }

    public boolean isCheckedExpired() {
        return this.checkedExpired;
    }

    public void setCheckedExpired(final boolean checkedExpired) {
        this.checkedExpired = checkedExpired;
    }

}
