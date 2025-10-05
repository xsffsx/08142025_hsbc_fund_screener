
package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


@XmlRegistry
public class ObjectFactory {

    private final static QName _Enquire_QNAME = new QName("http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", "enquire");
    private final static QName _EnquireResponse_QNAME = new QName("http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", "enquireResponse");


    public ObjectFactory() {
    }


    public Enquire createEnquire() {
        return new Enquire();
    }


    public EnquireResponse createEnquireResponse() {
        return new EnquireResponse();
    }


    public WpcProductDetailByKeysEnquiryRequest createWpcProductDetailByKeysEnquiryRequest() {
        return new WpcProductDetailByKeysEnquiryRequest();
    }


    public ProductDetailByKeyResponseInfo createProductDetailByKeyResponseInfo() {
        return new ProductDetailByKeyResponseInfo();
    }


    public SortingCriteria createSortingCriteria() {
        return new SortingCriteria();
    }


    public WpcProductDetailByKeysEnquiryResponse createWpcProductDetailByKeysEnquiryResponse() {
        return new WpcProductDetailByKeysEnquiryResponse();
    }


    public ReasonCode createReasonCode() {
        return new ReasonCode();
    }


    public ProductKey createProductKey() {
        return new ProductKey();
    }


    public RequestAttribute createRequestAttribute() {
        return new RequestAttribute();
    }


    public ProductTypeIndicator createProductTypeIndicator() {
        return new ProductTypeIndicator();
    }


    public ProductAttribute createProductAttribute() {
        return new ProductAttribute();
    }


    @XmlElementDecl(namespace = "http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", name = "enquire")
    public JAXBElement<Enquire> createEnquire(Enquire value) {
        return new JAXBElement<Enquire>(_Enquire_QNAME, Enquire.class, null, value);
    }


    @XmlElementDecl(namespace = "http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", name = "enquireResponse")
    public JAXBElement<EnquireResponse> createEnquireResponse(EnquireResponse value) {
        return new JAXBElement<EnquireResponse>(_EnquireResponse_QNAME, EnquireResponse.class, null, value);
    }

}
