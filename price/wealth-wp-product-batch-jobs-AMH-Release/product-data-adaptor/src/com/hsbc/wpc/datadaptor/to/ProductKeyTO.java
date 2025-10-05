/*
 * ***************************************************************
 * Copyright.  dummy Holdings plc 2014 ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it
 * has been provided.  No part of it is to be reproduced,
 * disassembled, transmitted, stored in a retrieval system or
 * translated in any human or computer language in any way or
 * for any other purposes whatsoever without the prior written
 * consent of dummy Holdings plc.
 * ***************************************************************
 *
 * Creation Date Dec 4, 2014 11:27:12 AM
 *
 */

package com.dummy.wpc.datadaptor.to;

import java.math.BigDecimal;

public class ProductKeyTO {
    
    private String prodTypeCde = null;
    
    private String prodAltPrimNum = null;

    private Double prodMktPrcAmt = null;

	public String getProdTypeCde() {
		return prodTypeCde;
	}

	public void setProdTypeCde(String pROD_TYPE_CDE) {
		prodTypeCde = pROD_TYPE_CDE;
	}

	public String getProdAltPrimNum() {
		return prodAltPrimNum;
	}

	public void setProdAltPrimNum(String pROD_ALT_PRIM_NUM) {
		prodAltPrimNum = pROD_ALT_PRIM_NUM;
	}

	public Double getProdMktPrcAmt() {
		return prodMktPrcAmt;
	}

	public void setProdMktPrcAmt(Double pROD_MKT_PRC_AMT) {
		prodMktPrcAmt = pROD_MKT_PRC_AMT;
	}
    
}
