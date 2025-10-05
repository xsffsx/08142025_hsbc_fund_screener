package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"msgid", "marketid", "responsecode", "errormsg"})
@XmlRootElement(name = "header")
public class Header {

	@XmlElement(required = true)
	protected String msgid;
	
	@XmlElement(required = true)
	protected String marketid;
	
	@XmlElement(required = true)
	protected String responsecode;
	
	@XmlElement(required = true)
	protected String errormsg;

	public String getMsgid() {
		return msgid;
	}

	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}

	public String getMarketid() {
		return marketid;
	}

	public void setMarketid(String marketid) {
		this.marketid = marketid;
	}

	public String getResponsecode() {
		return responsecode;
	}

	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
}
