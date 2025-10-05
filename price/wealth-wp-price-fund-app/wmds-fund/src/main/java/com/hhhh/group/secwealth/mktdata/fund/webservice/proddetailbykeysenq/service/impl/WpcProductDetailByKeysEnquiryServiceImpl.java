
package com.hhhh.group.secwealth.mktdata.fund.webservice.proddetailbykeysenq.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.exception.BaseException;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;
import com.hhhh.group.secwealth.mktdata.common.system.constants.WpcWebServiceExceptionConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.fund.webservice.proddetailbykeysenq.service.WpcProductDetailByKeysEnquiryService;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.ProductKey;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.ReasonCode;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.RequestAttribute;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryRequest;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryResponse;
import com.hhhh.wmd.wpc.webservice.proddetailbykeysenq.service.impl.WpcProductDetailByKeysEnquiryServiceImplDelegate;

@Service("wpcProductDetailByKeysEnquiryService")
public class WpcProductDetailByKeysEnquiryServiceImpl implements WpcProductDetailByKeysEnquiryService {

    private final static int SUCCESS_STATS_CODE = 0;

    @Value("#{systemConfig['wpcWebService.conn.wpcProductDetailByKeysEnquiry.attributeName']}")
    private String attributeName;

    @Value("#{systemConfig['wpcWebService.conn.wpcProductDetailByKeysEnquiry.urlPath']}")
    private String wsUrl;

    @Resource(name = "wpcProductDetailByKeysEnquiryServiceImplDelegate")
    private WpcProductDetailByKeysEnquiryServiceImplDelegate wpcProductDetailByKeysEnquiryServiceImplDelegate;

    public WpcProductDetailByKeysEnquiryResponse getWpcProductDetailByKeysEnquiry(final List<ProductKey> productKeylist,
        final String channelCode, final String countryCode, final String groupMemberCode, final String localeCode, final String hhhhSamlToken,
        final String wealthSamlToken) throws Exception {

        WpcProductDetailByKeysEnquiryRequest _enquire_request = new WpcProductDetailByKeysEnquiryRequest();

        _enquire_request.setChannelCode(channelCode);
        _enquire_request.setCountryCode(countryCode);
        _enquire_request.setGroupMemberCode(groupMemberCode);
        _enquire_request.setLocaleCode(localeCode);
        _enquire_request.getProductKey().addAll(productKeylist);
        java.util.List<RequestAttribute> _enquire_requestRequestAttribute = new java.util.ArrayList<RequestAttribute>();
        String[] attributeNames = this.attributeName.split(CommonConstants.SYMBOL_COMMA);
        for (int i = 0; i < attributeNames.length; i++) {
            String attributeName = attributeNames[i];
            RequestAttribute _enquire_requestRequestAttributeVal1 = new RequestAttribute();
            _enquire_requestRequestAttributeVal1.setAttributeName(attributeName);
            _enquire_requestRequestAttribute.add(_enquire_requestRequestAttributeVal1);
        }
        _enquire_request.getRequestAttribute().addAll(_enquire_requestRequestAttribute);
        try {
            LogUtil.infoBeanToJson(WpcProductDetailByKeysEnquiryServiceImpl.class,
                "WPC Webservice WpcProductDetailByKeysEnquiry request" + CommonConstants.OUTBOUND_MSG_PREFIX, _enquire_request);

            WpcProductDetailByKeysEnquiryResponse _enquire__return = this.wpcProductDetailByKeysEnquiryServiceImplDelegate.enquire(
                _enquire_request);
            LogUtil.infoBeanToJson(WpcProductDetailByKeysEnquiryServiceImpl.class,
                "WPC Webservice WpcProductDetailByKeysEnquiry response" + CommonConstants.INBOUND_MSG_PREFIX, _enquire__return);
            Integer responseCode = _enquire__return.getResponseCode();
            if (responseCode == null || WpcProductDetailByKeysEnquiryServiceImpl.SUCCESS_STATS_CODE != responseCode.intValue()) {
                if (responseCode == null) {
                    responseCode = Integer.parseInt(ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE);
                }
                LogUtil.errorBeanToJson(WpcProductDetailByKeysEnquiryServiceImpl.class,
                    "WpcProductDetailByKeysEnquiry Fail to send request, responseCode: " + responseCode + ", request is",
                    _enquire_request);
                throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.WPCWS + responseCode,
                    "WpcProductDetailByKeysEnquiry Fail to send request");
            }

            List<ReasonCode> reasonCodeList = _enquire__return.getReasonCode();
            if (ListUtil.isValid(reasonCodeList) && reasonCodeList.size() > 0) {
                String reasonCode = reasonCodeList.get(0).getReasonCode();
                if (WpcWebServiceExceptionConstants.WPC_NO_RECORD_CODE.equals(reasonCode)) {
                    LogUtil.errorBeanToJson(WpcProductDetailByKeysEnquiryServiceImpl.class,
                        "WpcProductDetailByKeysEnquiry is no such record in WPC DB, reasonCode: " + reasonCode + ", request is",
                        _enquire__return);
                    return null;
                }
            }
            return _enquire__return;
        } catch (Exception e) {
            if (e instanceof BaseException) {
                throw e;
            }
            LogUtil.errorBeanToJson(WpcProductDetailByKeysEnquiryServiceImpl.class,
                "WpcProductDetailByKeysEnquiry send request error, request is", _enquire_request, e);
            throw new CommonException(WpcWebServiceExceptionConstants.UNDEFINED, e.getMessage());
        }

    }
}
