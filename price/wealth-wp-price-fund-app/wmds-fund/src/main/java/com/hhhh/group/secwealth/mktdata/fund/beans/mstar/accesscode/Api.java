

package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"accessCode", "accountCode", "createTime", "expireTime", "expired"})
@XmlRootElement(name = "api")
public class Api {


    @XmlElement(name = "AccessCode", required = true)
    protected String accessCode;


    @XmlElement(name = "AccountCode", required = true)
    protected String accountCode;

    @XmlElement(name = "CreateTime", required = true)
    protected String createTime;

    @XmlElement(name = "ExpireTime", required = true)
    protected String expireTime;

    @XmlElement(name = "Expired", required = true)
    protected boolean expired;

    public String getAccessCode() {
        return this.accessCode;
    }


    public void setAccessCode(final String accessCode) {
        this.accessCode = accessCode;
    }


    public String getAccountCode() {
        return this.accountCode;
    }


    public void setAccountCode(final String accountCode) {
        this.accountCode = accountCode;
    }


    public String getCreateTime() {
        return this.createTime;
    }


    public void setCreateTime(final String createTime) {
        this.createTime = createTime;
    }


    public String getExpireTime() {
        return this.expireTime;
    }


    public void setExpireTime(final String expireTime) {
        this.expireTime = expireTime;
    }


    public boolean isExpired() {
        return this.expired;
    }


    public void setExpired(final boolean expired) {
        this.expired = expired;
    }


}
