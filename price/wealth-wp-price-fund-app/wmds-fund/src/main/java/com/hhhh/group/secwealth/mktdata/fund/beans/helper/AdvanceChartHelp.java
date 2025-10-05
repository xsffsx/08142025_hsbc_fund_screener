package com.hhhh.group.secwealth.mktdata.fund.beans.helper;

import java.util.List;

import javax.xml.bind.JAXBContext;

import org.apache.http.NameValuePair;

public class AdvanceChartHelp {
	
	private String currency;
	
	private String startDate;
	
	private String endDate;

    private String reqUrl;

    private String id;

    private String MstarUrl;

    private List<NameValuePair> params;

    private JAXBContext jAXBContext;

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMstarUrl() {
        return MstarUrl;
    }

    public void setMstarUrl(String mstarUrl) {
        MstarUrl = mstarUrl;
    }

    public List<NameValuePair> getParams() {
        return params;
    }

    public void setParams(List<NameValuePair> params) {
        this.params = params;
    }

    public JAXBContext getjAXBContext() {
        return jAXBContext;
    }

    public void setjAXBContext(JAXBContext jAXBContext) {
        this.jAXBContext = jAXBContext;
    }
    
    
    

    public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((MstarUrl == null) ? 0 : MstarUrl.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((jAXBContext == null) ? 0 : jAXBContext.hashCode());
        result = prime * result + ((params == null) ? 0 : params.hashCode());
        result = prime * result + ((reqUrl == null) ? 0 : reqUrl.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "AdvanceCharHelp [reqUrl=" + reqUrl + ", id=" + id + ", MstarUrl=" + MstarUrl + ", params=" + params
            + ", jAXBContext=" + jAXBContext + ",startDate=" + startDate + ",,endDate=" + endDate + "]";
    }


}
