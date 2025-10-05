package com.hhhh.group.secwealth.mktdata.api.equity.topmover.entity;

import lombok.Data;

import java.util.List;

@Data
public class LabciBody {

    protected String exchange;

    protected String movertype;

    protected List<LabciStockList> stocklist;

    protected String lastupdateddate;

    protected String lastupdatedtime;

    protected String timezone;

}
