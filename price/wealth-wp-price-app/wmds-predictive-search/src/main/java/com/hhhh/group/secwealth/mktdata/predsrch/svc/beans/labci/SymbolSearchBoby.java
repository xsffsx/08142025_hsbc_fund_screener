package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"stockslist", "totalrecordno",
        "lastupdateddate", "lastupdatedtime", "timezone"})
@XmlRootElement(name = "body")
public class SymbolSearchBoby {

    @XmlElement(required = true)
    protected List<StocksList> stockslist;

    @XmlElement(required = true)
    protected String totalrecordno;

    @XmlElement(required = true)
    protected String lastupdateddate;

    @XmlElement(required = true)
    protected String lastupdatedtime;

    @XmlElement(required = true)
    protected String timezone;

}
