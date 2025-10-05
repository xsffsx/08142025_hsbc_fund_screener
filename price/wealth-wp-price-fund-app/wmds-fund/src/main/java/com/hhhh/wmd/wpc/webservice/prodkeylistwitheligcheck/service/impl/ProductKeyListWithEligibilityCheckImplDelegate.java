package com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


@WebService(targetNamespace = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", name = "ProductKeyListWithEligibilityCheckImplDelegate")
@XmlSeeAlso({ObjectFactory.class})
public interface ProductKeyListWithEligibilityCheckImplDelegate {

    @WebMethod
    @Action(input = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/ProductKeyListWithEligibilityCheckImplDelegate/enquireRequest", output = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/ProductKeyListWithEligibilityCheckImplDelegate/enquireResponse")
    @RequestWrapper(localName = "enquire", targetNamespace = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", className = "com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Enquire")
    @ResponseWrapper(localName = "enquireResponse", targetNamespace = "http://impl.service.prodkeylistwitheligcheck.webservice.wpc.wmd.hhhh.com/", className = "com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.EnquireResponse")
    @WebResult(name = "return", targetNamespace = "")
    public com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckResponse enquire(
        @WebParam(name = "request", targetNamespace = "")
        com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckRequest request
        );
}
