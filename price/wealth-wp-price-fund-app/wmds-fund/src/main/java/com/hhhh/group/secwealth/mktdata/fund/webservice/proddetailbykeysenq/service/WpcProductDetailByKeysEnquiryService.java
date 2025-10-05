
package com.hhhh.group.secwealth.mktdata.fund.webservice.proddetailbykeysenq.service;

import java.util.List;

import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.ProductKey;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryResponse;



public interface WpcProductDetailByKeysEnquiryService {

    public final static String DEFAULT_PROD_CODE_TYPE = "M";


    public WpcProductDetailByKeysEnquiryResponse getWpcProductDetailByKeysEnquiry(List<ProductKey> productKeylist,
        String channelCode, String countryCode, String groupMemberCode, String localeCode, String hhhhSamlToken,
        String wealthSamlToken) throws Exception;

}
