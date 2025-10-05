package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"header", "body"})
@XmlRootElement(name = "envelop")
public class NewsHeadlineEnvelop {

	@XmlElement(required = true)
	protected Header header;
	
	protected NewsHeadlineBody body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public NewsHeadlineBody getBody() {
		return body;
	}

	public void setBody(NewsHeadlineBody body) {
		this.body = body;
	}

}
