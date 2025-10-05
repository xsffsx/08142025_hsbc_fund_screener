package com.hhhh.group.secwealth.mktdata.test.service;

import com.hhhh.group.secwealth.mktdata.test.utils.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class DistributedCacheService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${mds.distributed-cache-uri:}")
    private String distributedCacheUri;

    public HttpStatus login(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-hhhh-Saml3", "<saml:Assertion xmlns:saml='http://www.hhhh.com/saas/assertion' xmlns:ds='http://www.w3.org/2000/09/xmldsig#' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' ID='id_53146260-b347-45bd-bae9-8a9b40f0f812' IssueInstant='2020-07-13T03:59:44.447Z' Version='3.0'><saml:Issuer>https://www.hhhh.com/rbwm/dtp</saml:Issuer><ds:Signature><ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'/><ds:SignatureMethod Algorithm='http://www.w3.org/2001/04/xmldsig-more#rsa-sha256'/><ds:Reference URI='#id_53146260-b347-45bd-bae9-8a9b40f0f812'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#enveloped-signature'/><ds:Transform Algorithm='http://www.w3.org/2001/10/xml-exc-c14n#'><ds:InclusiveNamespaces xmlns:ds='http://www.w3.org/2001/10/xml-exc-c14n#' PrefixList='#default saml ds xs xsi'/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2001/04/xmlenc#sha256'/><ds:DigestValue>2fM36POlu8b+dPY/16Odo2ih0dgqrjR0AyI6WE/CIXs=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue>eAOyPtmHKzKzKy2XhccJ3gFsb41VT7cfI+bYG0UD6TCg+ix+9p9iUE+wko4jQJ3AnAGZEhUD0OljAMZn0b4mIXq6Td7zhJb/ayw9PiJnV2B5NSsJTlhdgbc7J5tIUINsL4CRB2LcuufrKfOr/bcFgpxWiHrAxj+z7BpKO2Xwd9GZsLROMomdsB+fgpXIsR3pFuHginQsu0ZqFV8Jv6NJe7TohQwjEew5lOk+3ff7MLkGlfslXwOygqIbbESOTcSw6ce9hCMrOjbljfCXeDB5zOwkx9GBl5r1Ya0fvcxayJrQtUoV8ne7NihwUMWL349JlTTKar1DlDbZcfs+HhkX9Q==</ds:SignatureValue></ds:Signature><saml:Subject><saml:NameID>HK26719562688801F</saml:NameID></saml:Subject><saml:Conditions NotBefore='2020-07-13T03:59:43.447Z' NotOnOrAfter='2020-07-13T04:00:44.447Z'/><saml:AttributeStatement><saml:Attribute Name='GUID'><saml:AttributeValue>c14e0090-b3ea-11e2-b7aa-000006010303</saml:AttributeValue></saml:Attribute><saml:Attribute Name='CAM'><saml:AttributeValue>30</saml:AttributeValue></saml:Attribute><saml:Attribute Name='KeyAlias'><saml:AttributeValue>E2E_TRUST_SAAS_EU01_PPROD_ALIAS</saml:AttributeValue></saml:Attribute></saml:AttributeStatement></saml:Assertion>");

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("value", "{\"lineOfBusiness\":\"\",\"eID\":\"DKQ089QQS089FI29F\",\"cusSegment\":\"N\",\"freeQuote\":\"50\",\"chargeAcStatusUS\":\"01\",\"lastLogonTime\":\"0\",\"bonus\":\"100\",\"permID\":\"HK26713763688801A\",\"packageID\":\"0\",\"chargeAcStatusHK\":\"01\",\"packageStatus\":\"0\",\"staffId\":\"\"}");
        bodyMap.put("key", "cc8691fa23da45ae71ef9819cb47a0b803125721a4f487fdb57b19ecfda356c5~~~S4F5PS5JysXYbvcgOptir7j~~~marketData");
        bodyMap.put("expiryTimeFlag", "L");

        String body = JSONUtil.toString(bodyMap);
        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(distributedCacheUri, HttpMethod.POST, entity, String.class);
        log.info("Distributed cache login with {}", response.getStatusCode());
        return response.getStatusCode();
    }

}
