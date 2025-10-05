


package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class AccessCodeData extends ResponseData {

    @XmlElement(required = true)
    protected List<Data> data;


    public List<Data> getData() {
        return this.data;
    }


    public void setData(final List<Data> data) {
        this.data = data;
    }


}
