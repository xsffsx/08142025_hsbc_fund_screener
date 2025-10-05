/*
 */
package com.hhhh.group.secwealth.mktdata.common.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.MultiPredSrchRequest;
import com.hhhh.group.secwealth.mktdata.predsrch.pres.beans.PredSrchRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hhhh.group.secwealth.mktdata.common.controller.RestfulController;
import com.hhhh.group.secwealth.mktdata.common.convertor.RequestConverter;
import com.hhhh.group.secwealth.mktdata.common.dao.impl.CustomerContextHolder;
import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.exception.ValidatorException;
import com.hhhh.group.secwealth.mktdata.common.service.RestfulService;
import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.common.svc.response.Response;
import com.hhhh.group.secwealth.mktdata.common.svc.response.ResponseUtils;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.ListUtil;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.SiteFeature;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;
import com.hhhh.group.secwealth.mktdata.common.validator.RegionServiceAdapter;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.Validator;
import com.hhhh.group.secwealth.mktdata.common.validator.vo.ValidatorError;

import net.sf.json.JSONObject;

/**
 * <p>
 * <b> RestfulService. </b>
 * </p>
 */
@Service("restfulService")
public class RestfulServiceImpl implements RestfulService {

    private final String REQUEST_HEADER_CHANNEL_ID = ".requestHeader.x-hhhh-channel-id";
    private final String REQUEST_HEADER_APP_CODE = ".requestHeader.x-hhhh-app-code";

    @Value("#{systemConfig['app.requestHeader.names']}")
    private String headerNames;

    /** The site converter rule. */
    @Value("#{systemConfig['app.siteConverterRule']}")
    private String[] siteConverterRule;

    @Value("#{systemConfig['app.requestHeader.mandatories']}")
    private String headerMandatories;

    @Autowired
    @Qualifier("regionServiceAdapter")
    private RegionServiceAdapter regionServiceAdapter;

    @Autowired
    @Qualifier("requestConverter")
    private RequestConverter requestConverter;

    @Autowired
    @Qualifier("siteFeature")
    private SiteFeature siteFeature;

    @Autowired
    @Qualifier("labciProtalSearchService")
    private LabciProtalSearchService labciProtalSearchService;

    @Override
    public ResponseEntity<?> all(final String method, final JSONObject json, final HttpServletRequest request) throws Exception {
        // Get Request HeaderMap And Validate Request Header
        Map<String, String> headerMap = this.getHeaderMapAndValidate(request);
        // Support Sites
        String countryCode = headerMap.get(CommonConstants.REQUEST_HEADER_COUNTRYCODE);
        String groupMember = headerMap.get(CommonConstants.REQUEST_HEADER_GROUPMEMBER);
        String siteKey = countryCode + CommonConstants.SYMBOL_UNDERLINE + groupMember;
        // Valid ChannelId And AppCode
        this.validChannelIdAndAppCode(siteKey, headerMap);

        // Set HeaderMap to ThreadLocal
        CustomerContextHolder.setHeaderMap(headerMap);

        // Converter site
        boolean isConverter = false;
        for (int i = 0; null != this.siteConverterRule && i < this.siteConverterRule.length; i++) {
            String[] sites = this.siteConverterRule[i].split(CommonConstants.SYMBOL_SEPARATOR);
            if (sites != null && sites.length > 1) {
                String targetSite = sites[0];
                for (int j = 1; j < sites.length; j++) {
                    if (siteKey.equals(sites[j])) {
                        siteKey = targetSite;
                        String[] siteInfo = targetSite.split(CommonConstants.SYMBOL_UNDERLINE);
                        headerMap.put(CommonConstants.REQUEST_HEADER_COUNTRYCODE, siteInfo[0]);
                        headerMap.put(CommonConstants.REQUEST_HEADER_GROUPMEMBER, siteInfo[1]);
                        isConverter = true;
                        break;
                    }
                }
            }
            if (isConverter) {
                break;
            }
        }

        String serviceId = method.toUpperCase();
        LogUtil.debug(RestfulController.class, "Method: {}, RequestJson: {}", method, json.toString());

        // Validator RequestBody
        this.validator(siteKey, serviceId, headerMap, json);
        // doService
        return this.doService(siteKey, serviceId, headerMap, json);
    }

    /**
     * Get HeaderMap from request
     *
     * @param request
     * @return
     */
    private Map<String, String> getHeaderMapAndValidate(final HttpServletRequest request) {
        List<String> headerNamesList = Arrays.asList(this.headerNames.split(CommonConstants.SEPARATOR));
        List<ValidatorError> errors = new ArrayList<ValidatorError>();
        List<String> headerMandatoryFields = Arrays.asList(this.headerMandatories.split(CommonConstants.SEPARATOR));

        Map<String, String> headerMap = new HashMap<String, String>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            if (headerNamesList.contains(key.toLowerCase())) {
                String value = request.getHeader(key);
                headerMap.put(key.toLowerCase(), value);
            }
        }
        LogUtil.infoBeanToJson(RestfulController.class, "Request headers: " + CommonConstants.INBOUND_MSG_PREFIX, headerMap);
        for (String mandatoryField : headerMandatoryFields) {
            String value = headerMap.get(mandatoryField);
            if (StringUtil.isInvalid(value)) {
                errors.add(new ValidatorError(ErrTypeConstants.REQUEST_HEADER_INVALID, mandatoryField,
                        "The parameter " + mandatoryField + " is invalid", this.getClass().getSimpleName()));
            }
        }
        if (ListUtil.isValid(errors)) {
            LogUtil.errorBeanToJson(RestfulServiceImpl.class,
                    "ValidatorException: RestfulController, getHeaderMapAndValidate request header is Invalid: {}", headerMap);
            throw new ValidatorException(ErrTypeConstants.INPUT_PARAMETER_INVALID, errors);
        }
        return headerMap;
    }

    /**
     *
     * <p>
     * <b> Valid ChannelId And AppCode </b>
     * </p>
     *
     * @param site
     * @param headerMap
     */
    private void validChannelIdAndAppCode(final String site, final Map<String, String> headerMap) {
        String channelId = this.siteFeature.getStringDefaultFeature(site, this.REQUEST_HEADER_CHANNEL_ID);
        String appCode = this.siteFeature.getStringDefaultFeature(site, this.REQUEST_HEADER_APP_CODE);
        List<ValidatorError> errors = new ArrayList<ValidatorError>();
        if (StringUtil.isValid(channelId) && !CommonConstants.ALL.equals(channelId)) {
            String channelIdReq = headerMap.get(CommonConstants.REQUEST_HEADER_CHANNELID);
            String[] channelIdArray = channelId.split(CommonConstants.SEPARATOR);
            if (!ArrayUtils.contains(channelIdArray, channelIdReq)) {
                LogUtil.error(RestfulServiceImpl.class,
                        "ValidatorException: RestfulController, x-hhhh-channel-id is not allowed: {}", channelIdReq);
                errors.add(new ValidatorError(ErrTypeConstants.REQUEST_HEADER_INVALID, CommonConstants.REQUEST_HEADER_CHANNELID,
                        "x-hhhh-channel-id is not allowed, x-hhhh-channel-id is " + channelIdReq, this.getClass().getSimpleName()));
            }
        }

        if (StringUtil.isValid(appCode) && !CommonConstants.ALL.equals(appCode)) {
            String appCodeReq = headerMap.get(CommonConstants.REQUEST_HEADER_APPCODE);
            String[] appCodeArray = appCode.split(CommonConstants.SEPARATOR);
            if (!ArrayUtils.contains(appCodeArray, appCodeReq)) {
                LogUtil.error(RestfulServiceImpl.class, "ValidatorException: RestfulController, x-hhhh-app-code is not allowed: {}",
                        appCodeReq);
                errors.add(new ValidatorError(ErrTypeConstants.REQUEST_HEADER_INVALID, CommonConstants.REQUEST_HEADER_APPCODE,
                        "x-hhhh-channel-id is not allowed, x-hhhh-app-code is " + appCodeReq, this.getClass().getSimpleName()));
            }
        }

        if (ListUtil.isValid(errors)) {
            LogUtil.errorBeanToJson(RestfulServiceImpl.class, "ValidatorException: RestfulController, "
                            + CommonConstants.REQUEST_HEADER_CHANNELID + " and " + CommonConstants.REQUEST_HEADER_APPCODE + "is Invalid: {}",
                    headerMap);
            throw new ValidatorException(ErrTypeConstants.INPUT_PARAMETER_INVALID, errors);
        }
    }

    private void validator(final String siteKey, final String serviceId, final Map<String, String> headerMap, final JSONObject json)
            throws Exception {
        // Validator by RuleSet
        List<Validator> validators = this.regionServiceAdapter.getValidators(siteKey, serviceId);
        List<ValidatorError> errors = new ArrayList<ValidatorError>();
        for (Validator validator : validators) {
            errors.addAll(validator.validate(headerMap, json));
        }
        if (ListUtil.isValid(errors)) {
            LogUtil.error(RestfulServiceImpl.class,
                    "ValidatorException: RestfulController, function name: {}, validator request is Invalid: {}", serviceId,
                    json.toString());
            throw new ValidatorException(ErrTypeConstants.INPUT_PARAMETER_INVALID, errors);
        }
    }

    private ResponseEntity<?> doService(final String siteKey, final String serviceId, final Map<String, String> headerMap,
                                        final JSONObject json) throws Exception {
        ResponseEntity<?> response = null;
        String serviceName = this.regionServiceAdapter.getServiceName(siteKey, serviceId);
        String requestClassName = this.regionServiceAdapter.getRequestClassName(siteKey, serviceId);
        LogUtil.debug(RestfulController.class, "doService ServiceName: {}, requestClassName: {}", serviceName, requestClassName);
        com.hhhh.group.secwealth.mktdata.common.service.Service service = this.regionServiceAdapter.getServices().get(serviceName);

        if (service == null) {
            LogUtil.error(RestfulServiceImpl.class,
                    "ValidatorException: RestfulController, doService, Service is not support, No such Service: {}", serviceName);
            throw new CommonException(ErrTypeConstants.SERVICE_NO_AVAILABLE);
        }
        // converter request
        Request req = this.requestConverter.converterToRequest(headerMap, json.toString(), requestClassName);

        // todo maybach ======================
        Object obj = null;
        if (req instanceof MultiPredSrchRequest) {
            MultiPredSrchRequest request = (MultiPredSrchRequest) req;
            String market = request.getMarket();
            if ("US".equals(market)) {
                obj = labciProtalSearchService.multiPredsrchWithCache(request);
            } else {
                obj = service.doService(req);
            }
        } else if (req instanceof PredSrchRequest) {
            PredSrchRequest request = (PredSrchRequest) req;
            String market = request.getMarket();
            if ("US".equals(market)) {
                obj = labciProtalSearchService.predsrch(request);
            } else {
                obj = service.doService(req);
            }
        }
        // todo maybach =========================


        if (obj == null) {
            LogUtil.warn(RestfulController.class, ErrTypeConstants.WARNMSG_NORECORDFOUND + " -> Founction: {}, RequestJson: {}",
                    serviceId, json.toString());
            response = ResponseUtils.successWithEmptyValue();
        } else {
            if (obj instanceof Response) {
                response = ResponseUtils.successWithValue((Response) obj);
            } else if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<Response> list = (List<Response>) obj;
                if (list.size() == 0) {
                    LogUtil.warn(RestfulController.class,
                            ErrTypeConstants.WARNMSG_NORECORDFOUND + " -> Founction: {}, RequestJson: {}", serviceId, json.toString());
                    response = ResponseUtils.successWithEmptyValues();
                }
                response = ResponseUtils.successWithValues(list);
            } else {
                response = ResponseUtils.successWithObject(obj);
            }
        }
        // Remove HeaderMap from ThreadLocal
        CustomerContextHolder.remove();
        return response;
    }
}
