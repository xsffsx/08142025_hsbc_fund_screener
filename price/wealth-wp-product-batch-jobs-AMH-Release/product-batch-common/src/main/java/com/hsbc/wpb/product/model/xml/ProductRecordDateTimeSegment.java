package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentObject;
import com.dummy.wpb.product.xmladapter.LocalDateTimeAdapter;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recDtTmSeg")
@DocumentObject("$")
@Data
public class ProductRecordDateTimeSegment extends RecordDateTimeSegment {

	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	private LocalDateTime prodPrcUpdtDtTm;

	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	private LocalDateTime recOnlnUpdtDtTm;

	@XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
	private LocalDateTime prodStatUpdtDtTm;
}
