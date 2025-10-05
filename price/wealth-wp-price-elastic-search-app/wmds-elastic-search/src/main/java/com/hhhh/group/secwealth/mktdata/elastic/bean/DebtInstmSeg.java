/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean;

import lombok.Data;

@Data
public class DebtInstmSeg {
	private String isrBndNme;
	private String prodIssDt;
	private String pdcyCoupnPymtCd;
	private String coupnAnnlRt;
	private String pymtCoupnNextDt;
	private String flexMatOptInd;
	private String intIndAccrAmt;
	private String invstIncMinAmt;
	private String subDebtInd;
	private String prodBodLotQtyCnt;
	private String mturExtDt;
	private String bondStatCde;
	private String ctryBondIssueCde;
	private String bondCallNextDt;
	private String intBassiCalcText;
	private String shrBidCnt;
	private String shrOffrCnt;
	private String bondCloseDt;
	private String bondSetlDt;
	private String prcBondRecvDtTm;
	private CreditRtingSeg creditRtingSeg;
}
