package com.hhhh.group.secwealth.mktdata.elastic.bean.labci;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
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

}
