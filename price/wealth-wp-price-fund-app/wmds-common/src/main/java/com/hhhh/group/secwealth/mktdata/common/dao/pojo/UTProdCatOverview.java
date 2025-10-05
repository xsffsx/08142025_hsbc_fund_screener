
package com.hhhh.group.secwealth.mktdata.common.dao.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "V_UT_PROD_CAT_ORVW")
public class UTProdCatOverview implements Serializable {

    private static final long serialVersionUID = -7790652215730851116L;

    @EmbeddedId
    private UTProdCatOverviewId utProdCatOverviewId;

    @Column(name = "PROD_TYPE_CDE")
    private String productType;

    @Column(name = "FUND_CAT_CDE")
    private String categoryCode;

    @Column(name = "DISP_SEQ_NUM_CAT")
    private Integer categorySequencenNum;

    @Column(name = "PROD_NLS_NAME_CAT1")
    private String categoryName1;

    @Column(name = "PROD_NLS_NAME_CAT2")
    private String categoryName2;

    @Column(name = "PROD_NLS_NAME_CAT3")
    private String categoryName3;

    @Column(nullable = true, name = "FUND_CAT_LVL1_CDE")
    private String categoryLevel1Code;

    @Column(nullable = true, name = "DISP_SEQ_NUM_LVL1_CAT")
    private Integer categoryLevel1SequencenNum;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME1")
    private String categoryLevel1Name1;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME2")
    private String categoryLevel1Name2;

    @Column(nullable = true, name = "PROD_NLS_LVL1_NAME3")
    private String categoryLevel1Name3;

    @Column(name = "RTRN_1YR_AMT")
    private BigDecimal return1yr;

    @Column(name = "RTRN_3YR_AMT")
    private BigDecimal return3yr;

    @Column(name = "RTRN_5YR_AMT")
    private BigDecimal return5yr;

    @Column(name = "RTRN_10YR_AMT")
    private BigDecimal return10yr;

    @Column(nullable = true, name = "RTRN_STD_DVIAT_3YR_NUM")
    private BigDecimal stdDev3Yr;

    @Column(name = "RESTR_ONLN_SCRIB_IND", columnDefinition = "char")
    private String restrOnlScribInd;

    @Column(name = "USER_UPDT_NUM")
    private String updatedBy;

    @Column(name = "REC_UPDT_DT_TM")
    private Date updaetdOn;


    
    public UTProdCatOverviewId getUtProdCatOverviewId() {
        return this.utProdCatOverviewId;
    }

    
    public void setUtProdCatOverviewId(final UTProdCatOverviewId utProdCatOverviewId) {
        this.utProdCatOverviewId = utProdCatOverviewId;
    }

    
    public String getProductType() {
        return this.productType;
    }

    
    public void setProductType(final String productType) {
        this.productType = productType;
    }

    
    public String getCategoryCode() {
        return this.categoryCode;
    }

    
    public void setCategoryCode(final String categoryCode) {
        this.categoryCode = categoryCode;
    }

    
    public Integer getCategorySequencenNum() {
        return this.categorySequencenNum;
    }

    
    public void setCategorySequencenNum(final Integer categorySequencenNum) {
        this.categorySequencenNum = categorySequencenNum;
    }

    
    public String getCategoryName1() {
        return this.categoryName1;
    }

    
    public void setCategoryName1(final String categoryName1) {
        this.categoryName1 = categoryName1;
    }

    
    public String getCategoryName2() {
        return this.categoryName2;
    }

    
    public void setCategoryName2(final String categoryName2) {
        this.categoryName2 = categoryName2;
    }

    
    public String getCategoryName3() {
        return this.categoryName3;
    }

    
    public void setCategoryName3(final String categoryName3) {
        this.categoryName3 = categoryName3;
    }

    
    public String getCategoryLevel1Code() {
        return this.categoryLevel1Code;
    }

    
    public void setCategoryLevel1Code(final String categoryLevel1Code) {
        this.categoryLevel1Code = categoryLevel1Code;
    }

    
    public Integer getCategoryLevel1SequencenNum() {
        return this.categoryLevel1SequencenNum;
    }

    
    public void setCategoryLevel1SequencenNum(final Integer categoryLevel1SequencenNum) {
        this.categoryLevel1SequencenNum = categoryLevel1SequencenNum;
    }

    
    public String getCategoryLevel1Name1() {
        return this.categoryLevel1Name1;
    }

    
    public void setCategoryLevel1Name1(final String categoryLevel1Name1) {
        this.categoryLevel1Name1 = categoryLevel1Name1;
    }

    
    public String getCategoryLevel1Name2() {
        return this.categoryLevel1Name2;
    }

    
    public void setCategoryLevel1Name2(final String categoryLevel1Name2) {
        this.categoryLevel1Name2 = categoryLevel1Name2;
    }

    
    public String getCategoryLevel1Name3() {
        return this.categoryLevel1Name3;
    }

    
    public void setCategoryLevel1Name3(final String categoryLevel1Name3) {
        this.categoryLevel1Name3 = categoryLevel1Name3;
    }

    
    public BigDecimal getReturn1yr() {
        return this.return1yr;
    }

    
    public void setReturn1yr(final BigDecimal return1yr) {
        this.return1yr = return1yr;
    }

    
    public BigDecimal getReturn3yr() {
        return this.return3yr;
    }

    
    public void setReturn3yr(final BigDecimal return3yr) {
        this.return3yr = return3yr;
    }

    
    public BigDecimal getReturn5yr() {
        return this.return5yr;
    }

    
    public void setReturn5yr(final BigDecimal return5yr) {
        this.return5yr = return5yr;
    }

    
    public BigDecimal getReturn10yr() {
        return this.return10yr;
    }

    
    public void setReturn10yr(final BigDecimal return10yr) {
        this.return10yr = return10yr;
    }

    
    public BigDecimal getStdDev3Yr() {
        return this.stdDev3Yr;
    }

    
    public void setStdDev3Yr(final BigDecimal stdDev3Yr) {
        this.stdDev3Yr = stdDev3Yr;
    }

    
    public String getRestrOnlScribInd() {
        return this.restrOnlScribInd;
    }

    
    public void setRestrOnlScribInd(final String restrOnlScribInd) {
        this.restrOnlScribInd = restrOnlScribInd;
    }

    
    public String getUpdatedBy() {
        return this.updatedBy;
    }

    
    public void setUpdatedBy(final String updatedBy) {
        this.updatedBy = updatedBy;
    }

    
    public Date getUpdaetdOn() {
        return this.updaetdOn;
    }

    
    public void setUpdaetdOn(final Date updaetdOn) {
        this.updaetdOn = updaetdOn;
    }

}
