
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.returnIndexChart;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"calendarReturn"})
@XmlRootElement(name = "data")
public class Data {

    @XmlElement(name = "CalendarReturn", required = true)
    protected CalendarReturn calendarReturn;
    @XmlAttribute(name = "idtype")
    protected String idtype;
    @XmlAttribute(name = "id")
    protected String id;


    public CalendarReturn getCalendarReturn() {
        return this.calendarReturn;
    }


    public void setCalendarReturn(final CalendarReturn calendarReturn) {
        this.calendarReturn = calendarReturn;
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
