
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productKeyListWithEligibilityCheckRequest", propOrder = {
    "localeCode",
    "marketCriteria",
    "businessProcessCode",
    "filterCriteriaFormula",
    "keyValueCriteriaWithIndex",
    "keyEligibilityCriteriaValue",
    "requestProdCdeAltClsCde",
    "requestAttribute",
    "sortingCriteriaList"
})
public class ProductKeyListWithEligibilityCheckRequest {

    protected LocaleCode localeCode;
    protected MarketCriteria marketCriteria;
    protected BusinessProcessCode businessProcessCode;
    protected FilterCriteriaFormula filterCriteriaFormula;
    @XmlElement(nillable = true)
    protected List<KeyValueCriteriaWithIndex> keyValueCriteriaWithIndex;
    @XmlElement(nillable = true)
    protected List<KeyEligibilityCriteriaValue> keyEligibilityCriteriaValue;
    @XmlElement(nillable = true)
    protected List<RequestProdCdeAltClsCde> requestProdCdeAltClsCde;
    @XmlElement(nillable = true)
    protected List<RequestAttribute> requestAttribute;
    @XmlElement(nillable = true)
    protected List<SortingCriteria> sortingCriteriaList;

    
    public LocaleCode getLocaleCode() {
        return localeCode;
    }

    
    public void setLocaleCode(LocaleCode value) {
        this.localeCode = value;
    }

    
    public MarketCriteria getMarketCriteria() {
        return marketCriteria;
    }

    
    public void setMarketCriteria(MarketCriteria value) {
        this.marketCriteria = value;
    }

    
    public BusinessProcessCode getBusinessProcessCode() {
        return businessProcessCode;
    }

    
    public void setBusinessProcessCode(BusinessProcessCode value) {
        this.businessProcessCode = value;
    }

    
    public FilterCriteriaFormula getFilterCriteriaFormula() {
        return filterCriteriaFormula;
    }

    
    public void setFilterCriteriaFormula(FilterCriteriaFormula value) {
        this.filterCriteriaFormula = value;
    }

    
    public List<KeyValueCriteriaWithIndex> getKeyValueCriteriaWithIndex() {
        if (keyValueCriteriaWithIndex == null) {
            keyValueCriteriaWithIndex = new ArrayList<KeyValueCriteriaWithIndex>();
        }
        return this.keyValueCriteriaWithIndex;
    }

    
    public List<KeyEligibilityCriteriaValue> getKeyEligibilityCriteriaValue() {
        if (keyEligibilityCriteriaValue == null) {
            keyEligibilityCriteriaValue = new ArrayList<KeyEligibilityCriteriaValue>();
        }
        return this.keyEligibilityCriteriaValue;
    }

    
    public List<RequestProdCdeAltClsCde> getRequestProdCdeAltClsCde() {
        if (requestProdCdeAltClsCde == null) {
            requestProdCdeAltClsCde = new ArrayList<RequestProdCdeAltClsCde>();
        }
        return this.requestProdCdeAltClsCde;
    }

    
    public List<RequestAttribute> getRequestAttribute() {
        if (requestAttribute == null) {
            requestAttribute = new ArrayList<RequestAttribute>();
        }
        return this.requestAttribute;
    }

    
    public List<SortingCriteria> getSortingCriteriaList() {
        if (sortingCriteriaList == null) {
            sortingCriteriaList = new ArrayList<SortingCriteria>();
        }
        return this.sortingCriteriaList;
    }

}
