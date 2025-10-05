package com.hhhh.group.secwealth.mktdata.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.beans.SearchProduct;
import com.hhhh.group.secwealth.mktdata.common.beans.SearchableObject;
import com.hhhh.group.secwealth.mktdata.common.beans.response.ProdAltNumSeg;
import com.hhhh.group.secwealth.mktdata.common.common.ProdCdeAltClassCdeEnum;
import com.hhhh.group.secwealth.mktdata.common.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.common.exception.VendorException;
import com.hhhh.group.secwealth.mktdata.common.http_client_manager.component.HttpClientHelper;
import com.hhhh.group.secwealth.mktdata.common.predsrch.request.PredSrchRequest;
import com.hhhh.group.secwealth.mktdata.common.predsrch.request.helper.InternalSearchRequest;
import com.hhhh.group.secwealth.mktdata.common.predsrch.response.PredSrchResponse;
import com.hhhh.group.secwealth.mktdata.common.properties.PredSrchProperties;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ResponseCode;
import com.hhhh.group.secwealth.mktdata.common.system.constants.VendorType;

import com.google.gson.reflect.TypeToken;

@Component("internalProductKeyUtil")
public class InternalProductKeyUtil {
	
	@Value("#{systemConfig['app.requestHeader.mandatories']}")
    private String headerMandatories;
	
    @Autowired
    private PredSrchProperties predSrchProps;

    @Autowired
    private HttpClientHelper httpClientHelper;

    public SearchProduct getProductBySearchWithAltClassCde(final String altClassCde, final String countryCode,
        final String groupMember, final String prodAltNum, final String countryTradableCode, 
        final String productType, final String locale) {
        
        InternalSearchRequest request = new InternalSearchRequest(altClassCde, countryCode, groupMember, prodAltNum, countryTradableCode, productType, locale);
        return internalSearch(request);
    }
    
    public SearchProduct internalSearch(final InternalSearchRequest request) {
        String params = "";
        String str = JsonUtil2.toJson(request);
        String charset = StandardCharsets.UTF_8.name();
        try {
            params = new StringBuilder().append(this.predSrchProps.getPredSrchBodyPrefix()).append(URLEncoder.encode(str, charset))
                .toString();
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(InternalProductKeyUtil.class,
                "Encode predsrch request encounter error, str is: " + str + ", charset is: " + charset, e);
            throw new ApplicationException(e.getMessage());
        }
        String response = "";
        try {
            response = this.httpClientHelper.doGet(this.predSrchProps.getInternalUrl(), params, null);
        } catch (Exception e) {
            LogUtil.error(InternalProductKeyUtil.class, "Access predsrch encounter error", e);
            throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.INTERNAL_PREDSRCH,
                "Fail to access predsrch");
        }
        return JsonUtil2.fromJson(response, new TypeToken<SearchProduct>() {}.getType());
    }

    // for health check
    public SearchProduct internalSearch(final String request) {
        String params = "";
        String str = request;
        String charset = StandardCharsets.UTF_8.name();
        try {
            params = new StringBuilder().append(this.predSrchProps.getPredSrchBodyPrefix()).append(URLEncoder.encode(str, charset))
                .toString();
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(InternalProductKeyUtil.class,
                "Encode predsrch request encounter error, str is: " + str + ", charset is: " + charset, e);
            throw new ApplicationException(e.getMessage());
        }
        String response = "";
        try {
            response = this.httpClientHelper.doGet(this.predSrchProps.getInternalUrl(), params, null);
        } catch (Exception e) {
            LogUtil.error(InternalProductKeyUtil.class, "Access predsrch encounter error", e);
            throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.INTERNAL_PREDSRCH,
                "Fail to access predsrch");
        }
        return JsonUtil2.fromJson(response, new TypeToken<SearchProduct>() {}.getType());
    }

    public String getProductCodeValueByCodeType(final ProdCdeAltClassCdeEnum codeType, final SearchableObject searchObject) {
        if (null != searchObject) {
            List<ProdAltNumSeg> prodAltNumSegList = searchObject.getProdAltNumSeg();
            if (ListUtil.isValid(prodAltNumSegList)) {
                for (ProdAltNumSeg prodAltNumSeg : prodAltNumSegList) {
                    if (codeType.toString().equals(prodAltNumSeg.getProdCdeAltClassCde())) {
                        return prodAltNumSeg.getProdAltNum();
                    }
                }
            }
        }
        return "";
    }

    // for fundSearchResult get PredSrchResponse list
    public List<PredSrchResponse> doService(final PredSrchRequest request) {
    	Map<String, String> headers = request.getHeaders();
    	/**
    	 * need to set empty for headers when call predsrch, 
    	 * because got error statusCode: 413, reason: Request Entity Too Large
    	 */
    	if(!headers.isEmpty()) {
    		request.getHeaders().clear();
    	}
    	
    	String params = "";
        String str = JsonUtil2.toJson(request);
        String charset = StandardCharsets.UTF_8.name();
        try {
            params = new StringBuilder().append(this.predSrchProps.getPredSrchBodyPrefix()).append(URLEncoder.encode(str, charset))
                .toString();
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(InternalProductKeyUtil.class,
                "Encode predsrch request encounter error, str is: " + str + ", charset is: " + charset, e);
            throw new ApplicationException(e.getMessage());
        }
        String response = "";
        try {
            response = this.httpClientHelper.doGet(this.predSrchProps.getUrl(), params, filterRequestHeaderParams(headers));
        } catch (Exception e) {
            LogUtil.error(InternalProductKeyUtil.class, "Access predsrch encounter error", e);
            throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.INTERNAL_PREDSRCH,
                "Fail to access predsrch");
        }

        return JsonUtil2.fromJson(response, new TypeToken<ArrayList<PredSrchResponse>>() {}.getType());
    }

    /**
     *
     * @author 43967870 Add predictive-search controller Mapping url
     *         "/wealth/api/v1/market-data/product/predictive-search" Dispatch
     *         to wmds-elastic-search project [which from @Fred prepared]
     */
    public List<PredSrchResponse> doService(final String param, final Map<String, String> headerMap) {
        String paramEncode = "";
        String charset = StandardCharsets.UTF_8.name();
        try {
            paramEncode = new StringBuilder().append(this.predSrchProps.getPredSrchBodyPrefix())
                .append(URLEncoder.encode(param, charset)).toString();
        } catch (UnsupportedEncodingException e) {
            LogUtil.error(InternalProductKeyUtil.class,
                "Encode predsrch request encounter error, str is: " + param + ", charset is: " + charset, e);
            throw new ApplicationException(e.getMessage());
        }
        String response = "";
        try {
            response = this.httpClientHelper.doGet(this.predSrchProps.getUrl(), paramEncode, filterRequestHeaderParams(headerMap));
        } catch (Exception e) {
            LogUtil.error(InternalProductKeyUtil.class, "Access predsrch encounter error", e);
            throw new VendorException(CommonConstants.EMPTY_STRING, ResponseCode.ERROR, VendorType.INTERNAL_PREDSRCH,
                "Fail to access predsrch");
        }

        return JsonUtil2.fromJson(response, new TypeToken<ArrayList<PredSrchResponse>>() {}.getType());
    }
    
    /**
     *  
     * @param headerMap
     * When Request forward to call predsrch, need filter some request header key
     * This issue Was found by @Fred
     */
    private Map<String, String> filterRequestHeaderParams(Map<String, String> headerMap) {
    	Map<String, String> filterHeaderMap = new HashMap<String, String>();
    	if(null != headerMap && headerMap.size() > 0) {
    		if(StringUtil.isValid(this.headerMandatories)) {
    			String[] headerMandatoryFields = this.headerMandatories.split(",");
    			if(null != headerMandatoryFields && headerMandatoryFields.length > 0) {
	    			for(String key : headerMap.keySet()) {
	    				for(String headerMandatoryField : headerMandatoryFields) {
	    					//Means this when header key is mandatory, then add
	    					if(key.equalsIgnoreCase(headerMandatoryField)) {
	    						filterHeaderMap.put(key, headerMap.get(key));
	    					}
	    				}
	    			}
    			}
    		}
    	}
    	LogUtil.debug(InternalProductKeyUtil.class, "filterHeaderMap is: " + filterHeaderMap.toString());
    	return filterHeaderMap;
    }

}
