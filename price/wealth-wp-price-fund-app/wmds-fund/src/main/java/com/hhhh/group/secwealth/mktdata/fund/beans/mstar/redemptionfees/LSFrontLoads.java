
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.redemptionfees;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"frontLoad"})
@XmlRootElement(name = "LS-FrontLoads")
public class LSFrontLoads {

    @XmlElement(name = "FrontLoad", required = true)
    protected List<FrontLoad> frontLoad;


    public List<FrontLoad> getFrontLoad() {
        return this.frontLoad;
    }


    public void setFrontLoad(final List<FrontLoad> frontLoad) {
        this.frontLoad = frontLoad;
    }


}
