
package com.hhhh.group.secwealth.mktdata.fund.criteria;

import java.util.ArrayList;
import java.util.List;

import com.hhhh.group.secwealth.mktdata.common.criteria.SearchCriteria;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.beans.request.QuickViewRequest;
import com.hhhh.group.secwealth.mktdata.fund.constants.CriteriaKey;
import com.hhhh.group.secwealth.mktdata.fund.constants.QuickViewTableName;
import com.hhhh.group.secwealth.mktdata.fund.constants.SupportMarket;
import com.hhhh.group.secwealth.mktdata.fund.constants.TimeScale;
import com.hhhh.group.secwealth.mktdata.fund.constants.TypeofETF;
import com.hhhh.group.secwealth.mktdata.fund.criteria.util.Operator;

public class QuickViewCriteria {

    private static List<String> utCriteriaKeyList = new ArrayList<>();
    private static List<String> etfCriteriaKeyList = new ArrayList<>();

    static {
        // UT CriteriaKey List
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.TABLE_NAME.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.TIME_SCALE.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.CATEGORY.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.PRODUCT_SUB_TYPE.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.CURRENCY.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.PROD_STAT_CDE.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.LIMIT_RESULT.getText());
        // ETF CriteriaKey List
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.TABLE_NAME.getText());
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.TIME_SCALE.getText());
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.CATEGORY.getText());
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.SUPPORT_MARKET.getText());
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.TYPE_OF_ETF.getText());
        QuickViewCriteria.etfCriteriaKeyList.add(CriteriaKey.PRODUCT_SUB_TYPE.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.CURRENCY.getText());
        QuickViewCriteria.utCriteriaKeyList.add(CriteriaKey.PROD_STAT_CDE.getText());
    }

    private QuickViewTableName tableName;
    private TimeScale timeScale;
    private SupportMarket supportMarket;
    private TypeofETF typeofETF;
    private String category;
    private String productSubType;
    private String currency;
    private String prodStatCde;
    private Integer limitResult;


    public Integer getlimitResult() {
        return limitResult;
    }

    public void setlimitResult(Integer limitResult) {
        this.limitResult = limitResult;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProdStatCde() {
        return prodStatCde;
    }

    public void setProdStatCde(String prodStatCde) {
        this.prodStatCde = prodStatCde;
    }
    
    public QuickViewTableName getTableName() {
        return this.tableName;
    }

    
    public void setTableName(final QuickViewTableName tableName) {
        this.tableName = tableName;
    }

    
    public TimeScale getTimeScale() {
        return this.timeScale;
    }

    
    public void setTimeScale(final TimeScale timeScale) {
        this.timeScale = timeScale;
    }

    
    public SupportMarket getSupportMarket() {
        return this.supportMarket;
    }

    
    public void setSupportMarket(final SupportMarket supportMarket) {
        this.supportMarket = supportMarket;
    }

    
    public TypeofETF getTypeofETF() {
        return this.typeofETF;
    }

    
    public void setTypeofETF(final TypeofETF typeofETF) {
        this.typeofETF = typeofETF;
    }

    
    public String getCategory() {
        return this.category;
    }

    
    public void setCategory(final String category) {
        this.category = category;
    }

    
    public String getProductSubType() {
        return this.productSubType;
    }

    
    public void setProductSubType(final String productSubType) {
        this.productSubType = productSubType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.tableName == null) ? 0 : this.tableName.hashCode());
        result = prime * result + ((this.timeScale == null) ? 0 : this.timeScale.hashCode());
        result = prime * result + ((this.category == null) ? 0 : this.category.hashCode());
        result = prime * result + ((this.productSubType == null) ? 0 : this.productSubType.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        QuickViewCriteria other = (QuickViewCriteria) obj;
        if (this.tableName != other.tableName) {
            return false;
        }
        if (this.timeScale != other.timeScale) {
            return false;
        }
        if (!this.category.equals(other.category)) {
            return false;
        }
        if (!this.productSubType.equals(other.productSubType)) {
            return false;
        }
        return true;
    }

    public static QuickViewCriteria getQuickViewCriteria(final QuickViewRequest request, final String defaultTimescale)
        throws Exception {
        String productType = request.getProductType();
        List<SearchCriteria> criterias = request.getCriterias();
        validateQuickViewRequest(request);
        QuickViewCriteria quickViewCriteria = new QuickViewCriteria();

        boolean hasTableName = false;
        boolean hasTimeScale = false;
        boolean hasSupportMarket = false;
        boolean hasTypeOfETF = false;

        for (SearchCriteria sc : criterias) {
            String criteriaKey = sc.getCriteriaKey();
            String criteriaValue = sc.getCriteriaValue();
            String operator = sc.getOperator();
            if (!operator.equalsIgnoreCase(Operator.EQ.name())) {
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria parsing error.");
            }
            if (criteriaKey.equals(CriteriaKey.TABLE_NAME.getText())) {
                hasTableName = true;
                quickViewCriteria.setTableName(QuickViewTableName.valueOf(criteriaValue));
            } else if (criteriaKey.equals(CriteriaKey.TIME_SCALE.getText())) {
                hasTimeScale = true;
                quickViewCriteria.setTimeScale(TimeScale.fromString(criteriaValue));
            } else if (criteriaKey.equals(CriteriaKey.SUPPORT_MARKET.getText())) {
                hasSupportMarket = true;
                quickViewCriteria.setSupportMarket(SupportMarket.fromString(criteriaValue));
            } else if (criteriaKey.equals(CriteriaKey.TYPE_OF_ETF.getText())) {
                hasTypeOfETF = true;
                quickViewCriteria.setTypeofETF(TypeofETF.fromString(criteriaValue));
            } else if (criteriaKey.equals(CriteriaKey.CATEGORY.getText())) {
                quickViewCriteria.setCategory(criteriaValue);
            } else if (criteriaKey.equals(CriteriaKey.PRODUCT_SUB_TYPE.getText())) {
                quickViewCriteria.setProductSubType(criteriaValue);
            }  else if (criteriaKey.equals(CriteriaKey.CURRENCY.getText())) {
                quickViewCriteria.setCurrency(criteriaValue);
            }else if (criteriaKey.equals(CriteriaKey.PROD_STAT_CDE.getText())) {
                quickViewCriteria.setProdStatCde(criteriaValue);
            }else if (criteriaKey.equals(CriteriaKey.LIMIT_RESULT.getText())) {
                quickViewCriteria.setlimitResult(Integer.parseInt(criteriaValue));
            }else {
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria parsing error.");
            }
        }
        if (!hasTimeScale && StringUtil.isValid(defaultTimescale)) {
            hasTimeScale = true;
            quickViewCriteria.setTimeScale(TimeScale.fromString(defaultTimescale));
        }
        // validate
        if (!hasTableName) {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria parsing error.");
        }
        if (productType != null && "UT".equals(productType) && !hasTimeScale) {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria parsing error.");
        }
        if (productType != null && "ETF".equals(productType) && !hasTimeScale && !hasSupportMarket && !hasTypeOfETF) {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria parsing error.");
        }
        return quickViewCriteria;
    }

    private static void validateQuickViewRequest(final QuickViewRequest request) throws Exception {
        boolean returnOnlyNumberOfMatches = request.getReturnOnlyNumberOfMatches() == null ? false : request
            .getReturnOnlyNumberOfMatches().booleanValue();
        String productType = request.getProductType();
        List<SearchCriteria> criteris = request.getCriterias();
        if (ListUtil.isInvalid(criteris)) {
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "DetailedCriteria should not be empty");
        }

        if (productType != null && "UT".equals(productType)) {
//            if (!returnOnlyNumberOfMatches && (criteris.size() < 2 || criteris.size() > 4)) {
//                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria Parameter is Invalid");
//            }
            for (SearchCriteria sc : criteris) {
                if (!QuickViewCriteria.utCriteriaKeyList.contains(sc.getCriteriaKey()) || null == sc.getCriteriaValue()// !sc.getCriteriaValue().matches("(\\w)*")
                    || !sc.getOperator().equalsIgnoreCase(Operator.EQ.name().toLowerCase())) {
                    throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID,
                        "Criteria Parameter is Invalid, CriteriaKey: " + sc.getCriteriaKey() + ", CriteriaValue: "
                            + sc.getCriteriaValue() + ", CriteriaOperator: " + sc.getOperator());
                }
            }
        }

        if (productType != null && "ETF".equals(productType)) {
            if (!returnOnlyNumberOfMatches && (criteris.size() < 4 || criteris.size() > 6)) {
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID, "Criteria Parameter is Invalid");
            }

            for (SearchCriteria sc : criteris) {
                if (!QuickViewCriteria.etfCriteriaKeyList.contains(sc.getCriteriaKey()) || null == sc.getCriteriaValue()// !sc.getCriteriaValue().matches("(\\w)*")
                    || !sc.getOperator().equalsIgnoreCase(Operator.EQ.name().toLowerCase())) {
                    throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID,
                        "Criteria Parameter is Invalid, CriteriaKey: " + sc.getCriteriaKey() + ", CriteriaValue: "
                            + sc.getCriteriaValue() + ", CriteriaOperator: " + sc.getOperator());
                }
            }
        }
    }

}
