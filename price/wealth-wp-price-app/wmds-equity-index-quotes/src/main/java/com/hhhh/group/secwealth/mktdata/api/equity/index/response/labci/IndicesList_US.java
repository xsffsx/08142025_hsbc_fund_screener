package com.hhhh.group.secwealth.mktdata.api.equity.index.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"indexsymbol","indexname","last","change","changepct","open","previousclose","dayhigh","daylow","w52high","w52low","lastupdateddate","lastupdatedtime","timezone"})
@XmlRootElement(name = "indiceslist")
public class IndicesList_US {

	@XmlElement(required = true)
	private String indexsymbol;
	
	@XmlElement(required = true)
	private String indexname;

	@XmlElement(required = true)
	private String last;

	@XmlElement(required = true)
	private String change;

	@XmlElement(required = true)
	private String changepct;

	@XmlElement(required = true)
	private String open;

	@XmlElement(required = true)
	private String previousclose;

	@XmlElement(required = true)
	private String dayhigh;

	@XmlElement(required = true)
	private String daylow;
	
	@XmlElement(required = true)
	private String w52high;
	
	@XmlElement(required = true)
	private String w52low;

	@XmlElement(required = true)
	private String lastupdateddate;

	@XmlElement(required = true)
	private String lastupdatedtime;

	@XmlElement(required = true)
	private String timezone;

	public String getIndexsymbol() {
		return indexsymbol;
	}

	public void setIndexsymbol(String indexsymbol) {
		this.indexsymbol = indexsymbol;
	}

	public String getIndexname() {
		return indexname;
	}

	public void setIndexname(String indexname) {
		this.indexname = indexname;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getChangepct() {
		return changepct;
	}

	public void setChangepct(String changepct) {
		this.changepct = changepct;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getPreviousclose() {
		return previousclose;
	}

	public void setPreviousclose(String previousclose) {
		this.previousclose = previousclose;
	}

	public String getDayhigh() {
		return dayhigh;
	}

	public void setDayhigh(String dayhigh) {
		this.dayhigh = dayhigh;
	}

	public String getDaylow() {
		return daylow;
	}

	public void setDaylow(String daylow) {
		this.daylow = daylow;
	}
	
	public String getW52high() {
		return w52high;
	}

	public void setW52high(String w52high) {
		this.w52high = w52high;
	}

	public String getW52low() {
		return w52low;
	}

	public void setW52low(String w52low) {
		this.w52low = w52low;
	}

	public String getLastupdateddate() {
		return lastupdateddate;
	}

	public void setLastupdateddate(String lastupdateddate) {
		this.lastupdateddate = lastupdateddate;
	}

	public String getLastupdatedtime() {
		return lastupdatedtime;
	}

	public void setLastupdatedtime(String lastupdatedtime) {
		this.lastupdatedtime = lastupdatedtime;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	
}
