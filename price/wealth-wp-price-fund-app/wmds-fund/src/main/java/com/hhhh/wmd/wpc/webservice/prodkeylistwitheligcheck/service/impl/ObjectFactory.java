
package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;



@XmlRegistry
public class ObjectFactory {

    private final static QName _Enquire_QNAME = new QName("http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", "enquire");
    private final static QName _EnquireResponse_QNAME = new QName("http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", "enquireResponse");


    public ObjectFactory() {
    }


    public Enquire createEnquire() {
        return new Enquire();
    }


    public EnquireResponse createEnquireResponse() {
        return new EnquireResponse();
    }


    public KeyValueWithIndex createKeyValueWithIndex() {
        return new KeyValueWithIndex();
    }


    public KeyEligCritValue createKeyEligCritValue() {
        return new KeyEligCritValue();
    }


    public RequestProdCdeAltClsCde createRequestProdCdeAltClsCde() {
        return new RequestProdCdeAltClsCde();
    }


    public SortingCriteria createSortingCriteria() {
        return new SortingCriteria();
    }


    public ProductKeyListWithEligibilityCheckRequest createProductKeyListWithEligibilityCheckRequest() {
        return new ProductKeyListWithEligibilityCheckRequest();
    }


    public ResponseDetails createResponseDetails() {
        return new ResponseDetails();
    }


    public RequestAttribute createRequestAttribute() {
        return new RequestAttribute();
    }


    public MarketCriteria createMarketCriteria() {
        return new MarketCriteria();
    }


    public ProductKeyObjectListJsonString createProductKeyObjectListJsonString() {
        return new ProductKeyObjectListJsonString();
    }


    public FilterCriteriaFormula createFilterCriteriaFormula() {
        return new FilterCriteriaFormula();
    }


    public KeyValueCriteriaWithIndex createKeyValueCriteriaWithIndex() {
        return new KeyValueCriteriaWithIndex();
    }


    public BusinessProcessCode createBusinessProcessCode() {
        return new BusinessProcessCode();
    }


    public LocaleCode createLocaleCode() {
        return new LocaleCode();
    }


    public ProductKeyListWithEligibilityCheckResponse createProductKeyListWithEligibilityCheckResponse() {
        return new ProductKeyListWithEligibilityCheckResponse();
    }


    public KeyEligibilityCriteriaValue createKeyEligibilityCriteriaValue() {
        return new KeyEligibilityCriteriaValue();
    }


    public ReasonCode createReasonCode() {
        return new ReasonCode();
    }


    public Value createValue() {
        return new Value();
    }


    public EligCrtaValue createEligCrtaValue() {
        return new EligCrtaValue();
    }


    @XmlElementDecl(namespace = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", name = "enquire")
    public JAXBElement<Enquire> createEnquire(Enquire value) {
        return new JAXBElement<Enquire>(_Enquire_QNAME, Enquire.class, null, value);
    }


    @XmlElementDecl(namespace = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", name = "enquireResponse")
    public JAXBElement<EnquireResponse> createEnquireResponse(EnquireResponse value) {
        return new JAXBElement<EnquireResponse>(_EnquireResponse_QNAME, EnquireResponse.class, null, value);
    }

}
