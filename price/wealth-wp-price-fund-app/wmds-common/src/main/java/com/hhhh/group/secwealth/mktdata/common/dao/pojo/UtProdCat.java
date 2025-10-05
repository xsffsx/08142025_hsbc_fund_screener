
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "V_UT_PROD_CAT")
public class UtProdCat implements Serializable {

    private static final long serialVersionUID = -3711661587756893456L;

    @EmbeddedId
    private UtProdCatId utProdCatId;

    @Column(nullable = false, name = "PROD_SUBTP_CDE")
    private String prodSubtypeCode;

    @Column(nullable = true, name = "PROD_TYPE_CDE")
    private String productType;

    @Column(name = "RTRN_STD_DVIAT_NUM", columnDefinition = "number")
    private BigDecimal stddev;

    @Column(nullable = true, name = "CTRY_NAME", columnDefinition = "char")
    private String country;

    @Column(nullable = true, name = "PROD_NLS_NAME1")
    protected String productNlsName1;

    @Column(nullable = true, name = "PROD_NLS_NAME2")
    protected String productNlsName2;

    @Column(nullable = true, name = "PROD_NLS_NAME3")
    protected String productNlsName3;

    @Column(nullable = true, name = "ASET_CLASS_CDE")
    private String assetClassCode;

    @Column(nullable = false, name = "REC_UPDT_DT_TM")
    private Date updatedOn;

    @Column(nullable = false, name = "USER_UPDT_NUM")
    private String updatedBy;

    @Transient
    private List<UtProdCatTtlRtrnIndex> categoryTotalReturnIndex = new ArrayList<>();

    @Transient
    private List<UtProdCatPerfmRtrn> utProdCatPerfmRtrn = new ArrayList<>();

    @Transient
    private Integer numberOfProducts;

    
    public UtProdCatId getUtProdCatId() {
        return this.utProdCatId;
    }

    
    public void setUtProdCatId(final UtProdCatId utProdCatId) {
        this.utProdCatId = utProdCatId;
    }

    
    public String getProdSubtypeCode() {
        return this.prodSubtypeCode;
    }

    
    public void setProdSubtypeCode(final String prodSubtypeCode) {
        this.prodSubtypeCode = prodSubtypeCode;
    }

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public Integer getNumberOfProducts() {
        return this.numberOfProducts;
    }

    
    public void setNumberOfProducts(final Integer numberOfProducts) {
        this.numberOfProducts = numberOfProducts;
    }

    
    public BigDecimal getStddev() {
        return this.stddev;
    }

    
    public void setStddev(final BigDecimal stddev) {
        this.stddev = stddev;
    }

    
    public String getCountry() {
        return this.country;
    }

    
    public void setCountry(final String country) {
        this.country = country;
    }

    
    public String getProductNlsName1() {
        return this.productNlsName1;
    }

    
    public void setProductNlsName1(final String productNlsName1) {
        this.productNlsName1 = productNlsName1;
    }

    
    public String getProductNlsName2() {
        return this.productNlsName2;
    }

    
    public void setProductNlsName2(final String productNlsName2) {
        this.productNlsName2 = productNlsName2;
    }

    
    public String getProductNlsName3() {
        return this.productNlsName3;
    }

    
    public void setProductNlsName3(final String productNlsName3) {
        this.productNlsName3 = productNlsName3;
    }

    
    public String getAssetClassCode() {
        return this.assetClassCode;
    }

    
    public void setAssetClassCode(final String assetClassCode) {
        this.assetClassCode = assetClassCode;
    }

    
    public Date getUpdatedOn() {
        return this.updatedOn;
    }

    
    public void setUpdatedOn(final Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public List<UtProdCatTtlRtrnIndex> getCategoryTotalReturnIndex() {
        return this.categoryTotalReturnIndex;
    }

    
    public void setCategoryTotalReturnIndex(final List<UtProdCatTtlRtrnIndex> categoryTotalReturnIndex) {
        this.categoryTotalReturnIndex = categoryTotalReturnIndex;
    }

    
    public List<UtProdCatPerfmRtrn> getUtProdCatPerfmRtrn() {
        return this.utProdCatPerfmRtrn;
    }

    
    public void setUtProdCatPerfmRtrn(final List<UtProdCatPerfmRtrn> utProdCatPerfmRtrn) {
        this.utProdCatPerfmRtrn = utProdCatPerfmRtrn;
    }

}
