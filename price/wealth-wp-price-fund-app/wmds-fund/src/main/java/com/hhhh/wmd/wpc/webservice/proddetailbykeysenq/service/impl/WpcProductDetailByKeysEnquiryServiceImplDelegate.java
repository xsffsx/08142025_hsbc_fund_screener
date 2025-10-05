package com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(targetNamespace = "http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", name = "WpcProductDetailByKeysEnquiryServiceImplDelegate")
@XmlSeeAlso({ObjectFactory.class})
public interface WpcProductDetailByKeysEnquiryServiceImplDelegate {

    @WebMethod
    @RequestWrapper(localName = "enquire", targetNamespace = "http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", className = "com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.Enquire")
    @ResponseWrapper(localName = "enquireResponse", targetNamespace = "http://impl.service.proddetailbykeysenq.webservice.wpc.wmd.hhhh.com/", className = "com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.EnquireResponse")
    @WebResult(name = "return", targetNamespace = "")
    public com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryResponse enquire(
        @WebParam(name = "request", targetNamespace = "")
        com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryRequest request
    );
}
