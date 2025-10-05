package com.hhhh.group.secwealth.mktdata.api.equity.index.response.labci;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"header", "body"})
@XmlRootElement(name = "envelop")
public class Envelop_US {
	
	@XmlElement(required = true)
	protected Header_US header;
	
	protected Body_US body;

	public Header_US getHeader() {
		return header;
	}

	public void setHeader(Header_US header) {
		this.header = header;
	}

	public Body_US getBody() {
		return body;
	}

	public void setBody(Body_US body) {
		this.body = body;
	}
	
}
