
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class RedemptionFeesData extends ResponseData {

    @XmlElement(required = true)
    protected Data data;

    public Data getData() {
        return this.data;
    }


    public void setData(final Data value) {
        this.data = value;
    }

}
