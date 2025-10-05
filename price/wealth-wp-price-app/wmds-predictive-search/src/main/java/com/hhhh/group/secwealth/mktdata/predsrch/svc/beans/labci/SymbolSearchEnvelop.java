package com.hhhh.group.secwealth.mktdata.predsrch.svc.beans.labci;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;


@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"header", "body"})
@XmlRootElement(name = "envelop")
public class SymbolSearchEnvelop {

    @XmlElement(required = true)
    protected Header header;

    protected SymbolSearchBoby body;

}