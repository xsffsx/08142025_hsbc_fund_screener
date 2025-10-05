package com.dummy.wpb.wpc.utils.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Service {

    public static final String REST_ADAPTER_SERVICE_ID = "product-rest-adapter";
    public static final String SOAP_ADAPTER_SERVICE_ID = "product-soap-adapter";
    public static final String EGRESS_SERVICE_ID = "product-dp-file-egress";
    public static final String INGRESS_SERVICE_ID = "product-dp-file-ingress";
    public static final String GRAPHQL_SERVICE_ID = "product-graphql";
}
