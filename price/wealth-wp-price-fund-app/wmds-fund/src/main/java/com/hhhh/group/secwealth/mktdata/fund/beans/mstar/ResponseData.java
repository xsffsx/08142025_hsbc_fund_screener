package com.hhhh.group.secwealth.mktdata.fund.beans.mstar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class ResponseData {

    @XmlElement(required = true)
    protected Status status;

    
    public Status getStatus() {
        return this.status;
    }

    
    public void setStatus(final Status value) {
        this.status = value;
    }

}
