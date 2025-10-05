
package com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.hhhh.group.secwealth.mktdata.common.util.JacksonUtil;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.fund.util.WpcApiCfg;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductInfo;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.json.ProductKeyObject;
import com.hhhh.group.secwealth.mktdata.fund.webservice.prodkeylistwitheligcheck.service.ProductKeyListWithEligibilityCheckService;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.BusinessProcessCode;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.FilterCriteriaFormula;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.KeyValueCriteriaWithIndex;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.KeyValueWithIndex;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.LocaleCode;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.MarketCriteria;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckImplDelegate;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckRequest;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyListWithEligibilityCheckResponse;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ProductKeyObjectListJsonString;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.ReasonCode;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.RequestAttribute;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.RequestProdCdeAltClsCde;
import com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.SortingCriteria;

@Service("productKeyListWithEligibilityCheckService")
public class ProductKeyListWithEligibilityCheckServiceImpl implements ProductKeyListWithEligibilityCheckService {

    private final static int SUCCESS_STATS_CODE = 0;

    @Autowired
    @Qualifier("wpcApiCfg")
    private WpcApiCfg wpcApiCfg;

    @Value("#{systemConfig['wpcWebService.conn.ProductKeyListWithEligibilityCheck.urlPath']}")
    private String wsUrl;

    @Value("#{systemConfig['newfunds.return.time']}")
    private String period;

    @Resource(name = "productKeyListWithEligibilityCheckImplDelegate")
    private ProductKeyListWithEligibilityCheckImplDelegate productKeyListWithEligibilityCheckImplDelegate;

    @Override
    public List<ProductInfo> getProductInfoList(final String locale, final String countryCode, final String groupMember,
        final String productType, final String productSubType, final String propertyKey, final String[] switchFundGroup)
        throws Exception {
        List<ProductInfo> list = new ArrayList<ProductInfo>();
        ProductKeyListWithEligibilityCheckResponse response = getProductKeyListWithEligibilityCheck(locale, countryCode,
            groupMember, productType, productSubType, propertyKey, switchFundGroup);
        if (response != null) {
            ProductKeyObjectListJsonString productKeyObjectListJsonString = response.getProductKeyObjectListJsonString();
            if (productKeyObjectListJsonString != null) {
                String prodKeyObjListJsonStr = productKeyObjectListJsonString.getProdKeyObjListJsonStr();
                if (StringUtil.isValid(prodKeyObjListJsonStr)) {
                    try {
                        List<ProductKeyObject> productKeyObjectList =
                            JacksonUtil.jsonToList(prodKeyObjListJsonStr, ProductKeyObject.class);
                        if (ListUtil.isValid(productKeyObjectList)) {
                            list = this.getProductInfoListByProductKeyObjectList(productKeyObjectList);
                        }
                    } catch (Exception e) {
                        LogUtil.error(ProductKeyListWithEligibilityCheckServiceImpl.class,
                            "ProductKeyListWithEligibilityCheck response can not jsonToList, response is: " + prodKeyObjListJsonStr,
                            e);
                        throw new CommonException(WpcWebServiceExceptionConstants.INVALID_RESPONSE);
                    }
                }
            } else {
                LogUtil.error(ProductKeyListWithEligibilityCheckServiceImpl.class,
                    "ProductKeyListWithEligibilityCheck invalid response, locale: " + locale + " ,countryCode: " + countryCode
                        + " ,groupMember: " + groupMember + " ,productType: " + productType + " ,productSubType: " + productSubType
                        + " ,propertyKey: " + propertyKey);
                throw new CommonException(WpcWebServiceExceptionConstants.INVALID_RESPONSE);
            }
        }
        return list;
    }

    private List<ProductInfo> getProductInfoListByProductKeyObjectList(final List<ProductKeyObject> productKeyObjectList) {
        List<ProductInfo> productKeyList = new ArrayList<ProductInfo>();
        for (ProductKeyObject productKeyObject : productKeyObjectList) {
            List<ProductInfo> productInfoList = productKeyObject.getProductSimpleInformation().getProductKey();
            for (ProductInfo info : productInfoList) {
                if (null != info && "M".equals(info.getPrdAltClsCd())) {
                    productKeyList.add(info);
                }
            }
        }
        return productKeyList;
    }

    @Override
    public ProductKeyListWithEligibilityCheckResponse getProductKeyListWithEligibilityCheck(final String locale,
        final String countryCode, final String groupMember, final String productType, final String productSubType,
        final String propertyKey, final String[] switchFundGroup) throws Exception {

        ProductKeyListWithEligibilityCheckRequest request = this.getProductKeyListWithEligibilityCheckRequest(locale, countryCode,
            groupMember, productType, productSubType, propertyKey, switchFundGroup);
        LogUtil.infoBeanToJson(ProductKeyListWithEligibilityCheckServiceImpl.class,
            "WPC Webservice ProductKeyListWithEligibilityCheck request " + CommonConstants.OUTBOUND_MSG_PREFIX, request);
        ProductKeyListWithEligibilityCheckResponse response = this.getProductKeyListWithEligibilityCheck(request);
        return response;
    }

    private ProductKeyListWithEligibilityCheckResponse getProductKeyListWithEligibilityCheck(
        final ProductKeyListWithEligibilityCheckRequest request) throws Exception {
        try {
            ProductKeyListWithEligibilityCheckResponse response =
                this.productKeyListWithEligibilityCheckImplDelegate.enquire(request);
            LogUtil.infoBeanToJson(ProductKeyListWithEligibilityCheckServiceImpl.class,
                "WPC Webservice ProductKeyListWithEligibilityCheck response" + CommonConstants.INBOUND_MSG_PREFIX, response);
            Integer responseCode = response.getResponseDetails().getResponseCode();
            if (responseCode == null
                || ProductKeyListWithEligibilityCheckServiceImpl.SUCCESS_STATS_CODE != responseCode.intValue()) {
                if (responseCode == null) {
                    responseCode = Integer.parseInt(ErrTypeConstants.VENDOREXCEPTION_DEFAULT_CODE);
                }
                LogUtil.errorBeanToJson(ProductKeyListWithEligibilityCheckServiceImpl.class,
                    "ProductKeyListWithEligibilityCheck Fail to send request, responseCode: " + responseCode + ", request is",
                    request);
                throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.WPCWS + responseCode,
                    "ProductKeyListWithEligibilityCheck Fail to send request");
            }
            List<ReasonCode> reasonCodeList = response.getReasonCode();
            if (ListUtil.isValid(reasonCodeList) && reasonCodeList.size() > 0) {
                String reasonCode = reasonCodeList.get(0).getReasonCode();
                if (WpcWebServiceExceptionConstants.WPC_NO_RECORD_CODE.equals(reasonCode)) {
                    LogUtil.errorBeanToJson(ProductKeyListWithEligibilityCheckServiceImpl.class,
                        "ProductKeyListWithEligibilityCheck is no such record in WPC DB, reasonCode: " + reasonCode
                            + ", request is",
                        request);
                    return null;
                }
            }
            return response;
        } catch (Exception e) {
            if (e instanceof BaseException) {
                throw e;
            }
            LogUtil.errorBeanToJson(ProductKeyListWithEligibilityCheckServiceImpl.class,
                "ProductKeyListWithEligibilityCheck send request error, request is", request, e);
            throw new CommonException(WpcWebServiceExceptionConstants.UNDEFINED, e.getMessage());
        }
    }

    private ProductKeyListWithEligibilityCheckRequest getProductKeyListWithEligibilityCheckRequest(final String locale,
        final String countryCode, final String groupMember, final String productType, final String productSubType,
        final String propertyKey, final String[] switchFundGroup) throws Exception {
        try {
            ProductKeyListWithEligibilityCheckRequest request = null;
            if ("switchablefunds".equals(propertyKey)) {
                request =
                    this.fillWpcCriteriaList(countryCode, groupMember, productType, productSubType, propertyKey, switchFundGroup);
            } else {
                request = this.fillWpcCriteriaList(countryCode, groupMember, productType, productSubType, propertyKey);
            }
            LocaleCode localeCode = new LocaleCode();
            localeCode.setLocaleNationalLanguageSupportCode(locale);

            MarketCriteria marketCriteria = new MarketCriteria();
            marketCriteria.setCountryRecordCode(countryCode);
            marketCriteria.setGroupMemberRecordCode(groupMember);
            BusinessProcessCode businessProcessCode = new BusinessProcessCode();
            businessProcessCode.setBusinessProcessCode("SFPB");

            String formula = null;
            String identifier = countryCode + CommonConstants.SYMBOL_LINE_CONNECTIVE + groupMember;
            if (StringUtil.isValid(productSubType)) {
                formula = this.wpcApiCfg.getValue(identifier, propertyKey + ".formula.10");
            } else {
                formula = this.wpcApiCfg.getValue(identifier, propertyKey + ".formula.9");
            }
            FilterCriteriaFormula filterCriteriaFormula = new FilterCriteriaFormula();
            filterCriteriaFormula.setFilterFormula(formula);

            request.setLocaleCode(localeCode);
            request.setBusinessProcessCode(businessProcessCode);
            request.setMarketCriteria(marketCriteria);
            request.setFilterCriteriaFormula(filterCriteriaFormula);
            return request;
        } catch (Exception e) {
            LogUtil.error(ProductKeyListWithEligibilityCheckServiceImpl.class,
                "ProductKeyListWithEligibilityCheck invalid request, locale: " + locale + " ,countryCode: " + countryCode
                    + " ,groupMember: " + groupMember + " ,productType: " + productType + " ,productSubType: " + productSubType
                    + " ,propertyKey: " + propertyKey,
                e);
            throw new CommonException(WpcWebServiceExceptionConstants.INVALID_REQUEST, e.getMessage());
        }
    }

    private ProductKeyListWithEligibilityCheckRequest fillWpcCriteriaList(final String countryCode, final String groupMember,
        final String productType, final String productSubType, final String propertyKey) throws Exception {
        ProductKeyListWithEligibilityCheckRequest request = new ProductKeyListWithEligibilityCheckRequest();
        List<KeyValueCriteriaWithIndex> keyValueCriteriaWithIndexList = request.getKeyValueCriteriaWithIndex();
        String identifier = countryCode + CommonConstants.SYMBOL_LINE_CONNECTIVE + groupMember;
        for (int parameterIndex = 1; parameterIndex < 9; parameterIndex++) {

            String parameterStr = this.wpcApiCfg.getValue(identifier, propertyKey + CommonConstants.SYMBOL_DOT + parameterIndex);
            String[] parameterArray = parameterStr.split(CommonConstants.SYMBOL_COMMA);
            if (parameterArray.length != 5) {
                LogUtil.error(ProductKeyListWithEligibilityCheckServiceImpl.class, "Confifuration is not correct");
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }

            String keyType = parameterArray[0].trim();
            String key = parameterArray[1].trim();
            String operator = parameterArray[2].trim();
            String valueType = parameterArray[3].trim();
            String value = parameterArray[4].trim();

            // ProductType
            if (value.equals("[ProductType]")) {
                value = productType;
            }
            // ProductSubType
            if (value.equals("[ProductSubType]")) {
                if (StringUtil.isValid(productSubType)) {
                    value = productSubType;
                } else {
                    continue;
                }
            }

            KeyValueCriteriaWithIndex keyValueCriteriaWithIndex = new KeyValueCriteriaWithIndex();
            KeyValueWithIndex keyValueWithIndex = new KeyValueWithIndex();
            keyValueWithIndex.setIndex(String.valueOf(parameterIndex - 1));
            keyValueWithIndex.setKeyType(keyType);
            keyValueWithIndex.setKey(key);
            keyValueWithIndex.setOperator(operator);
            keyValueWithIndex.setValueType(valueType);
            keyValueCriteriaWithIndex.setKeyValueWithIndex(keyValueWithIndex);

            com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value valueBean =
                new com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value();
            valueBean.setValue(value);

            List<com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value> valueList =
                keyValueCriteriaWithIndex.getValue();
            valueList.add(valueBean);

            keyValueCriteriaWithIndexList.add(keyValueCriteriaWithIndex);
        }
        List<RequestProdCdeAltClsCde> requestProdCdeAltClsCdeList = request.getRequestProdCdeAltClsCde();
        RequestProdCdeAltClsCde requestProdCdeAltClsCdeP = new RequestProdCdeAltClsCde();
        requestProdCdeAltClsCdeP.setProductCodeAlternativeClassCode("P");
        requestProdCdeAltClsCdeList.add(requestProdCdeAltClsCdeP);

        RequestProdCdeAltClsCde requestProdCdeAltClsCdeM = new RequestProdCdeAltClsCde();
        requestProdCdeAltClsCdeM.setProductCodeAlternativeClassCode("M");
        requestProdCdeAltClsCdeList.add(requestProdCdeAltClsCdeM);

        List<RequestAttribute> requestAttributeList = request.getRequestAttribute();
        RequestAttribute requestAttribute = new RequestAttribute();
        requestAttribute.setAttributeName("prodName");
        requestAttributeList.add(requestAttribute);
        List<SortingCriteria> sortingCriteriaList = request.getSortingCriteriaList();
        SortingCriteria sortingCriteria = new SortingCriteria();
        sortingCriteria.setSortField("A.PROD_NAME");
        sortingCriteria.setSortOrder("+");
        sortingCriteriaList.add(sortingCriteria);
        return request;
    }

    private ProductKeyListWithEligibilityCheckRequest fillWpcCriteriaList(final String countryCode, final String groupMember,
        final String productType, final String productSubType, final String propertyKey, final String[] switchFundGroup)
        throws Exception {
        ProductKeyListWithEligibilityCheckRequest request = new ProductKeyListWithEligibilityCheckRequest();
        List<KeyValueCriteriaWithIndex> keyValueCriteriaWithIndexList = request.getKeyValueCriteriaWithIndex();
        String identifier = countryCode + CommonConstants.SYMBOL_LINE_CONNECTIVE + groupMember;
        for (int parameterIndex = 1; parameterIndex < 11; parameterIndex++) {

            String parameterStr = this.wpcApiCfg.getValue(identifier, propertyKey + CommonConstants.SYMBOL_DOT + parameterIndex);
            String[] parameterArray = parameterStr.split(CommonConstants.SYMBOL_COMMA);
            if (parameterArray.length != 5) {
                LogUtil.error(ProductKeyListWithEligibilityCheckServiceImpl.class, "Confifuration is not correct");
                throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
            }

            String keyType = parameterArray[0].trim();
            String key = parameterArray[1].trim();
            String operator = parameterArray[2].trim();
            String valueType = parameterArray[3].trim();
            String value = parameterArray[4].trim();

            KeyValueCriteriaWithIndex keyValueCriteriaWithIndex = new KeyValueCriteriaWithIndex();
            List<com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value> valueList =
                keyValueCriteriaWithIndex.getValue();

            // SwitchFundGroup
            if (value.equals("[SwitchFundGroup]")) {
                for (String switchFund : switchFundGroup) {
                    com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value valueBean =
                        new com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value();
                    valueBean.setValue(switchFund);
                    valueList.add(valueBean);
                }
            }

            // ProductType
            if (value.equals("[ProductType]")) {
                com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value valueBean =
                    new com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value();
                valueBean.setValue(productType);
                valueList.add(valueBean);
            }
            // ProductSubType
            if (value.equals("[ProductSubType]")) {
                if (StringUtil.isValid(productSubType)) {
                    com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value valueBean =
                        new com.hhhh.wmd.wpc.webservice.prodkeylistwitheligcheck.service.impl.Value();
                    valueBean.setValue(productSubType);
                    valueList.add(valueBean);
                } else {
                    continue;
                }
            }


            KeyValueWithIndex keyValueWithIndex = new KeyValueWithIndex();
            keyValueWithIndex.setIndex(String.valueOf(parameterIndex - 1));
            keyValueWithIndex.setKeyType(keyType);
            keyValueWithIndex.setKey(key);
            keyValueWithIndex.setOperator(operator);
            keyValueWithIndex.setValueType(valueType);
            keyValueCriteriaWithIndex.setKeyValueWithIndex(keyValueWithIndex);

            keyValueCriteriaWithIndexList.add(keyValueCriteriaWithIndex);
        }
        List<RequestProdCdeAltClsCde> requestProdCdeAltClsCdeList = request.getRequestProdCdeAltClsCde();
        RequestProdCdeAltClsCde requestProdCdeAltClsCdeP = new RequestProdCdeAltClsCde();
        requestProdCdeAltClsCdeP.setProductCodeAlternativeClassCode("P");
        requestProdCdeAltClsCdeList.add(requestProdCdeAltClsCdeP);

        RequestProdCdeAltClsCde requestProdCdeAltClsCdeM = new RequestProdCdeAltClsCde();
        requestProdCdeAltClsCdeM.setProductCodeAlternativeClassCode("M");
        requestProdCdeAltClsCdeList.add(requestProdCdeAltClsCdeM);

        List<RequestAttribute> requestAttributeList = request.getRequestAttribute();
        RequestAttribute requestAttribute = new RequestAttribute();
        requestAttribute.setAttributeName("prodName");
        requestAttributeList.add(requestAttribute);
        List<SortingCriteria> sortingCriteriaList = request.getSortingCriteriaList();
        SortingCriteria sortingCriteria = new SortingCriteria();
        sortingCriteria.setSortField("A.PROD_NAME");
        sortingCriteria.setSortOrder("+");
        sortingCriteriaList.add(sortingCriteria);
        return request;
    }
}
