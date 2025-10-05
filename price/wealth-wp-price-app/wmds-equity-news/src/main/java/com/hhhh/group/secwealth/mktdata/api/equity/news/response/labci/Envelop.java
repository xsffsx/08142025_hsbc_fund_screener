package com.hhhh.group.secwealth.mktdata.api.equity.news.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"header", "body"})
@XmlRootElement(name = "envelop")
public class Envelop {

	@XmlElement(required = true)
	protected Header header;
	
	protected Body body;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
}
