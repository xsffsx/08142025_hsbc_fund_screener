package com.dummy.wpb.product.model.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodTradElg")
@Data
public class TradingEligibility {

	private ProductKeySegment prodKeySeg;

	private ProductTradeEligibilitySegment prodTraEligSeg;

	private List<CustomerCateEligListSegment> custCateEligSeg;

	private List<CustomerFormEligListSegment> custFormEligSeg;

	@XmlElement(name = "InvstHldChkCriterSeg")
	private List<InvestmentHoldingCheckCriteriaSegment> invstHldChkCriterSeg;

	private List<ProductRestrictCustCountrySegment> prodRestCustCtrySeg;

	private RecordDateTimeSegment recDtTmSeg;
}
