package com.hhhh.group.secwealth.mktdata.elastic.bean.labci;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"stocksymbol", "exchange", "riccode", "stockname"})
@XmlRootElement(name = "stockslist")
public class StocksList {
	
	@XmlElement(required = true)
	protected String stocksymbol;
	
	@XmlElement(required = true)
	protected String exchange;
	
	@XmlElement(required = true)
	protected String riccode;
	
	@XmlElement(required = true)
	protected String stockname;


}
