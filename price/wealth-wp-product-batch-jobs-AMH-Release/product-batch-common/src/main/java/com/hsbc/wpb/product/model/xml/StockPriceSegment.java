/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 1.1.2.1</a>, using an XML
 * Schema.
 * $Id$
 */

package com.dummy.wpb.product.model.xml;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import com.dummy.wpb.product.annotation.DocumentObject;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "stockPrcSeg")
@Data
@DocumentObject("priceHistory.stockPrice")
public class StockPriceSegment {

    private BigDecimal prodPrcChngPct;

    private BigDecimal prodPrcChngAmt;

    private BigDecimal prodOpenPrcAmt;

    private BigDecimal prodTdyHighPrcAmt;

    private BigDecimal prodTdyLowPrcAmt;

    private Long shareTrdCnt;

    private BigDecimal prodTrnvrAmt;

    private BigDecimal prodPrc52WeekMaxAmt;

    private BigDecimal prodPrc52WeekMinAmt;

    private BigDecimal prcEarnRate;

    private BigDecimal divPct;
}
