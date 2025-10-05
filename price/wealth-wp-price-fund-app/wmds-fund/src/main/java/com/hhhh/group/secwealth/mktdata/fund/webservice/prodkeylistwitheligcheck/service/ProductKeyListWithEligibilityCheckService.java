
package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service;

import java.util.List;

import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckResponse;

public interface ProductKeyListWithEligibilityCheckService {


    public List<ProductInfo> getProductInfoList(String locale, String countryCode, String groupMember, String productType,
        String productSubType, String propertyKey, String[] switchFundGroup) throws Exception;


    public ProductKeyListWithEligibilityCheckResponse getProductKeyListWithEligibilityCheck(String locale, String countryCode,
        String groupMember, String productType, String productSubType, String propertyKey, String[] switchFundGroup)
        throws Exception;


}
