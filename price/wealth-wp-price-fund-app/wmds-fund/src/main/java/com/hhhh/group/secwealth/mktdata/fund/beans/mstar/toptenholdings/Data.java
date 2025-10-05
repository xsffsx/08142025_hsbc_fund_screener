


package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.toptenholdings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"api"})
@XmlRootElement(name = "data")
public class Data {

    @XmlElement(required = true)
    protected Api api;
    @XmlAttribute(name = "_idtype")
    protected String idtype;
    @XmlAttribute(name = "_id")
    protected String id;


    public Api getApi() {
        return this.api;
    }


    public void setApi(final Api value) {
        this.api = value;
    }


    public String getIdtype() {
        return this.idtype;
    }


    public void setIdtype(final String value) {
        this.idtype = value;
    }


    public String getId() {
        return this.id;
    }


    public void setId(final String value) {
        this.id = value;
    }

}
