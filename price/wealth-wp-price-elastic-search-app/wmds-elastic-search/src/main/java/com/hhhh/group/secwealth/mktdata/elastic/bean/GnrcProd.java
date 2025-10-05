/*
 */
package com.hhhh.group.secwealth.mktdata.elastic.bean;

import lombok.Data;
import java.util.List;

@Data
public class GnrcProd {

    private ProdKeySeg prodKeySeg;

    private List<ProdAltNumSeg> prodAltNumSeg;

    private ProdInfoSeg prodInfoSeg;

    private RecDtTmSeg recDtTmSeg;

}
