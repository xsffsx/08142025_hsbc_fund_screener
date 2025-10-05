package com.dummy.wpb.product.model.xml;

import com.dummy.wpb.product.annotation.DocumentField;
import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "prodTraEligSeg")
@DocumentObject("tradeElig")
@Data
public class ProductTradeEligibilitySegment {

	@DocumentField("ovridCuspCatEligInd")
	private String ovridEligCuspCatInd;

	private String reqCmplRpqInd;

	private String ovridReqCmplRpqInd;

	private String prdVlidRpqCde;

	private Long prdVlidRpqNum;

	private String ovridRpqVlidPrdInd;

	private String actionLowRpqScrText;

	private String ovridReqFormInd;

	private Long ageAllowTrdMinNum;

	private Long ageAllowTrdMaxNum;

	private String ovridMinAgeRestrInd;

	private String ovridMaxAgeRestrInd;

	private String operTypeCde;

	private String ovridCorsCtryChkInd;

	private String ovridResCtryChkInd;

	@DocumentField("ovridRestrFisclCtryChkInd")
	private String ovridRestrCtryChkInd;

	private String ovridNatlChkInd;
	private String reqInvstHldgChkInd;

	private String ovridInvstHldgChkInd;
}
