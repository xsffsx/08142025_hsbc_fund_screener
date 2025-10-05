
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hhhh.group.secwealth.mktdata.fund.beans.mstar.ResponseData;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class PerformanceReturnData extends ResponseData {

    @XmlElement(required = true)
    protected List<Data> data;


    public List<Data> getData() {
        if (this.data == null) {
            this.data = new ArrayList<Data>();
        }
        return this.data;
    }

}
