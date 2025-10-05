package com.hhhh.group.secwealth.mktdata.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@Slf4j
public class BaseService {

    protected HttpEntity<String> getBaseHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-hhhh-Function-Id", "29");
        headers.add("X-hhhh-User-Id", "HK26713763688801A");
        headers.add("X-hhhh-Session-Correlation-Id", "S4F5PS5JysXYbvcgOptir7j");
        headers.add("X-hhhh-Chnl-CountryCode", "HK");
        headers.add("X-hhhh-Chnl-Group-Member", "HASE");
        headers.add("X-hhhh-Channel-Id", "OHI");
        headers.add("X-hhhh-Locale", "en");
        headers.add("X-hhhh-App-Code", "STMA");
        headers.add("X-hhhh-Saml3", "<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_8617f5fb-e443-4d8f-aa37-f4f9965da66f' IssueInstant='2019-07-15T09:01:59.661Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_8617f5fb-e443-4d8f-aa37-f4f9965da66f'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>FtIDuv4fU+UosU38WjaFyx69tPKUkbhvOxgY3Shjric=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>tQ4Wwmy9XGCmPm4J7o6NhFGn9nUzHONJdU70cht+vzEDhRRGqrvFtzTBe1TxEcoGcfPk4WgRLfBxJd6bK8TFY1Sa+m/kIzCTMAdtMK2P9vzb4efYcgFP60rygu2lh896VNpjqOZgWGkc+903w8eGy8IvTVKyr4RhdPVu+oVvt2k62QwOG6UKJwLgW1/dQwck9C34RJdfgKLNdF6wTdbvuQIyhPQ1nUmFZy/8edDTIUm9Yd69yabPfD2gK57FqspmtNHlHhiKxxLf3hugz8jAzAIAKd4TdeMPAsRMnqj1GncBri0E6dv3F69JwsCu7TQXCTejUxc/HECSzJbreohGXQ==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>HK26713763688801A</saml:NameID></saml:Subject><saml:Conditions NotBefore='2019-07-15T09:01:58.661Z' NotOnOrAfter='2019-07-15T09:02:59.661Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>f20fe6e0-b3e4-11e2-b7aa-000006010303</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        return new HttpEntity<>(headers);
    }

}
