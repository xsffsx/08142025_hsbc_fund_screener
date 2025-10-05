package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;

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

	public String getStocksymbol() {
		return stocksymbol;
	}

	public void setStocksymbol(String stocksymbol) {
		this.stocksymbol = stocksymbol;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getRiccode() {
		return riccode;
	}

	public void setRiccode(String riccode) {
		this.riccode = riccode;
	}

	public String getStockname() {
		return stockname;
	}

	public void setStockname(String stockname) {
		this.stockname = stockname;
	}

}
