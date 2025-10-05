package com.dummy.wpb.product.model.xml;

import lombok.Data;
import org.springframework.batch.item.ResourceAware;
import org.springframework.core.io.Resource;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "stockInstm")
@Data
public class StockInstrument implements ResourceAware {
	private ProductKeySegment prodKeySeg;

	private List<ProductAlternativeNumberSegment> prodAltNumSeg;

	private ProductInformationSegment prodInfoSeg;

	private StockInstrumentSegment stockInstmSeg;

	private EsgSegment esgSeg;

	private RecordDateTimeSegment recDtTmSeg;

	@XmlTransient
	private Resource resource;

	@Override
	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
