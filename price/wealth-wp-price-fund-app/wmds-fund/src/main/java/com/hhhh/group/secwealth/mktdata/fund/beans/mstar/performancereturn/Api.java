
package com.hhhh.group.secwealth.mktdata.fund.beans.mstar.performancereturn;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"cyrYear1", "cyrYear2", "cyrYear3", "cyrYear4", "cyrYear5", "cyrYear6", "cyrYear7", "cyrYear8",
    "cyrYear9", "cyrYear10", "dpReturnYTD", "dpDayEndDate", "dmpReturnYTD", "dmpDayEndDate"})
@XmlRootElement(name = "api")
public class Api {

    @XmlElement(name = "CYR-Year1", required = true)
    protected String cyrYear1;
    @XmlElement(name = "CYR-Year2", required = true)
    protected String cyrYear2;
    @XmlElement(name = "CYR-Year3", required = true)
    protected String cyrYear3;
    @XmlElement(name = "CYR-Year4", required = true)
    protected String cyrYear4;
    @XmlElement(name = "CYR-Year5", required = true)
    protected String cyrYear5;
    @XmlElement(name = "CYR-Year6", required = true)
    protected String cyrYear6;
    @XmlElement(name = "CYR-Year7", required = true)
    protected String cyrYear7;
    @XmlElement(name = "CYR-Year8", required = true)
    protected String cyrYear8;
    @XmlElement(name = "CYR-Year9", required = true)
    protected String cyrYear9;
    @XmlElement(name = "CYR-Year10", required = true)
    protected String cyrYear10;
    @XmlElement(name = "DP-ReturnYTD", required = true)
    protected String dpReturnYTD;
    @XmlElement(name = "DP-DayEndDate", required = true)
    protected String dpDayEndDate;
    @XmlElement(name = "DMP-ReturnYTD", required = true)
    protected String dmpReturnYTD;
    @XmlElement(name = "DMP-DayEndDate", required = true)
    protected String dmpDayEndDate;
    @XmlAttribute(name = "_id")
    protected String id;


    public String getCyrYear1() {
        return this.cyrYear1;
    }


    public void setCyrYear1(final String cyrYear1) {
        this.cyrYear1 = cyrYear1;
    }


    public String getCyrYear2() {
        return this.cyrYear2;
    }


    public void setCyrYear2(final String cyrYear2) {
        this.cyrYear2 = cyrYear2;
    }


    public String getCyrYear3() {
        return this.cyrYear3;
    }


    public void setCyrYear3(final String cyrYear3) {
        this.cyrYear3 = cyrYear3;
    }


    public String getCyrYear4() {
        return this.cyrYear4;
    }


    public void setCyrYear4(final String cyrYear4) {
        this.cyrYear4 = cyrYear4;
    }


    public String getCyrYear5() {
        return this.cyrYear5;
    }


    public void setCyrYear5(final String cyrYear5) {
        this.cyrYear5 = cyrYear5;
    }


    public String getCyrYear6() {
        return this.cyrYear6;
    }


    public void setCyrYear6(final String cyrYear6) {
        this.cyrYear6 = cyrYear6;
    }


    public String getCyrYear7() {
        return this.cyrYear7;
    }


    public void setCyrYear7(final String cyrYear7) {
        this.cyrYear7 = cyrYear7;
    }


    public String getCyrYear8() {
        return this.cyrYear8;
    }


    public void setCyrYear8(final String cyrYear8) {
        this.cyrYear8 = cyrYear8;
    }


    public String getCyrYear9() {
        return this.cyrYear9;
    }


    public void setCyrYear9(final String cyrYear9) {
        this.cyrYear9 = cyrYear9;
    }


    public String getCyrYear10() {
        return this.cyrYear10;
    }


    public void setCyrYear10(final String cyrYear10) {
        this.cyrYear10 = cyrYear10;
    }


    public String getDpReturnYTD() {
        return this.dpReturnYTD;
    }


    public void setDpReturnYTD(final String dpReturnYTD) {
        this.dpReturnYTD = dpReturnYTD;
    }


    public String getDpDayEndDate() {
        return this.dpDayEndDate;
    }


    public void setDpDayEndDate(final String dpDayEndDate) {
        this.dpDayEndDate = dpDayEndDate;
    }


    public String getDmpReturnYTD() {
        return this.dmpReturnYTD;
    }


    public void setDmpReturnYTD(final String dmpReturnYTD) {
        this.dmpReturnYTD = dmpReturnYTD;
    }


    public String getDmpDayEndDate() {
        return this.dmpDayEndDate;
    }


    public void setDmpDayEndDate(final String dmpDayEndDate) {
        this.dmpDayEndDate = dmpDayEndDate;
    }


    public String getId() {
        return this.id;
    }


    public void setId(final String id) {
        this.id = id;
    }

}
