
package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ProductInfo {


    private String prdTpCd;

    private String prdAltNum;

    private String prdAltClsCd;

    private String prdTrdCde;

    private String ccyPrdCd;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).appendSuper(super.toString()).append("prdTpCd", this.prdTpCd)
            .append("prdAltNum", this.prdAltNum).append("prdAltClsCd", this.prdAltClsCd).append("prdTrdCde", this.prdTrdCde)
            .append("ccyPrdCd", this.ccyPrdCd).toString();
    }


    public String getPrdTpCd() {
        return this.prdTpCd;
    }


    public void setPrdTpCd(final String prdTpCd) {
        this.prdTpCd = prdTpCd;
    }


    public String getPrdAltNum() {
        return this.prdAltNum;
    }


    public void setPrdAltNum(final String prdAltNum) {
        this.prdAltNum = prdAltNum;
    }


    public String getPrdAltClsCd() {
        return this.prdAltClsCd;
    }


    public void setPrdAltClsCd(final String prdAltClsCd) {
        this.prdAltClsCd = prdAltClsCd;
    }


    public String getPrdTrdCde() {
        return this.prdTrdCde;
    }


    public void setPrdTrdCde(final String prdTrdCde) {
        this.prdTrdCde = prdTrdCde;
    }


    public String getCcyPrdCd() {
        return this.ccyPrdCd;
    }


    public void setCcyPrdCd(final String ccyPrdCd) {
        this.ccyPrdCd = ccyPrdCd;
    }


}
