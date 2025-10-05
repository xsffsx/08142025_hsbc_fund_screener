/*
 */

package com.hhhh.group.secwealth.mktdata.elastic.bean;

import java.util.List;

public class UtTrstInstm {
    private ProdKeySeg prodKeySeg;
    private List<ProdAltNumSeg> prodAltNumSeg;
    private ProdInfoSeg prodInfoSeg;
    private UtTrstInstmSeg utTrstInstmSeg;
    private RecDtTmSeg recDtTmSeg;

    public ProdKeySeg getProdKeySeg() {
        return this.prodKeySeg;
    }

    public void setProdKeySeg(final ProdKeySeg prodKeySeg) {
        this.prodKeySeg = prodKeySeg;
    }

    public List<ProdAltNumSeg> getProdAltNumSeg() {
        return this.prodAltNumSeg;
    }

    public void setProdAltNumSeg(final List<ProdAltNumSeg> prodAltNumSeg) {
        this.prodAltNumSeg = prodAltNumSeg;
    }

    public ProdInfoSeg getProdInfoSeg() {
        return this.prodInfoSeg;
    }

    public void setProdInfoSeg(final ProdInfoSeg prodInfoSeg) {
        this.prodInfoSeg = prodInfoSeg;
    }

    public UtTrstInstmSeg getUtTrstInstmSeg() {
        return this.utTrstInstmSeg;
    }

    public void setUtTrstInstmSeg(final UtTrstInstmSeg utTrstInstmSeg) {
        this.utTrstInstmSeg = utTrstInstmSeg;
    }

    public RecDtTmSeg getRecDtTmSeg() {
        return this.recDtTmSeg;
    }

    public void setRecDtTmSeg(final RecDtTmSeg recDtTmSeg) {
        this.recDtTmSeg = recDtTmSeg;
    }
}
