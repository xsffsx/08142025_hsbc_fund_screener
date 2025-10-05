package com.hhhh.group.secwealth.mktdata.api.equity.topmover.entity;

import lombok.Data;

// @XmlRootElement(name = "stockslist")
@Data
public class LabciStockList {

    protected String stocksymbol;

    protected String riccode;

    protected String stockname;

    protected String last;

    protected String change;

    protected String changepct;

    protected String open;

    protected String previousclose;

    protected String turnover;

    protected String volume;

    protected String score;

}
