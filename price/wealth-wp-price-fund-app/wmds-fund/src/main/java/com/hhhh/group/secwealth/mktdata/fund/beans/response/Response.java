
package com.hhhh.group.secwealth.mktdata.fund.beans.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class Response {

    // Denote warning or error
    private String responseCode;
    // Denote the detail
    private String reasonCode;
    // The unique id for per F/E request
    private String flag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString())
            .append("responseCode", this.responseCode).append("reasonCode", this.reasonCode).append("flag", this.flag).toString();
    }


    public String getResponseCode() {
        return this.responseCode;
    }


    public void setResponseCode(final String responseCode) {
        this.responseCode = responseCode;
    }


    public String getReasonCode() {
        return this.reasonCode;
    }


    public void setReasonCode(final String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(final String flag) {
        this.flag = flag;
    }
}
