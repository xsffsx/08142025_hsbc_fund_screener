package com.hhhh.group.secwealth.mktdata.api.equity.index.response.labci;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"indiceslist"})
@XmlRootElement(name = "body")
public class Body_US {
	
	@XmlElement(required = true)
	private List<IndicesList_US> indiceslist;

	public List<IndicesList_US> getIndiceslist() {
		return indiceslist;
	}

	public void setIndiceslist(List<IndicesList_US> indiceslist) {
		this.indiceslist = indiceslist;
	}
	
}
