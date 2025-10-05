

package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.accesscode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"api", "name"})
@XmlRootElement(name = "data")
public class Data {

    @XmlElement(required = true)
    protected Api api;
    @XmlAttribute(name = "_name")
    protected String name;


    public Api getApi() {
        return this.api;
    }


    public void setApi(final Api value) {
        this.api = value;
    }


    public String getName() {
        return this.name;
    }


    public void setName(final String name) {
        this.name = name;
    }

}
