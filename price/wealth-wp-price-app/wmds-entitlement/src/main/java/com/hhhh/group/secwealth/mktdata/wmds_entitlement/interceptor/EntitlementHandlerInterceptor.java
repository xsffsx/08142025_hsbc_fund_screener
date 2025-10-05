/*
 */
package com.hhhh.group.secwealth.mktdata.wmds_entitlement.interceptor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import com.hhhh.group.secwealth.mktdata.starter.http_request_resolver.resolver.header.Property;
import com.hhhh.group.secwealth.mktdata.wmds_entitlement.properties.EntitlementProperties;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.constant.ExCodeConstant;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.exception.ApplicationException;
import com.hhhh.group.secwealth.mktdata.wmds_prototype.header.CommonRequestHeader;

import lombok.Setter;

@Component
public class EntitlementHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(EntitlementHandlerInterceptor.class);

    @Setter
    private EntitlementService service;

    @Setter
    private EntitlementProperties prop;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.
     * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.Object)
     */
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        throws Exception {

        String requestUrl = request.getRequestURL().toString();
        LinkedHashMap<String, String> serviceUrlMap = this.prop.getServiceUrlMapping();
        String serviceName = getServiceNameByRequestURL(requestUrl, serviceUrlMap);
        if (StringUtils.isEmpty(serviceName)) {
            return true;
        }

        List<String> checklist = this.prop.getChecklistByServiceName(serviceName);
        if (checklist == null || checklist.isEmpty()) {
            return true;
        }

        Map<String, String> headerMap = getHeaderValueMap(request, handler, checklist);
        if (headerMap == null || headerMap.isEmpty()) {
            return true;
        }

        LinkedHashMap<String, List<String>> rulesMap = this.prop.getWhiteListRulesByServiceName(serviceName);
        if (rulesMap == null || rulesMap.isEmpty()) {
            return true;
        }

        boolean hasPermission = this.service.hasEntitlement(serviceName, checklist, headerMap, rulesMap);
        if (!hasPermission) {
            EntitlementHandlerInterceptor.logger.error("No authorized access to this service. serviceName:{}", serviceName);
            throw new ApplicationException(ExCodeConstant.EX_CODE_NO_AUTHORIZED_ACCESS);
        }

        return hasPermission;
    }

    /**
     * <p>
     * <b> Get service name by request url. </b>
     * </p>
     *
     * @param requestUrl
     * @param serviceUrlMap
     * @return
     */
    private String getServiceNameByRequestURL(final String requestUrl, final LinkedHashMap<String, String> serviceUrlMap) {
        if (serviceUrlMap == null || serviceUrlMap.isEmpty()) {
            return null;
        }
        if (serviceUrlMap.keySet() != null) {
            for (String key : serviceUrlMap.keySet()) {
                if (requestUrl.endsWith(key)) {
                    return serviceUrlMap.get(key);
                }
            }
        }
        return null;
    }

    /**
     * <p>
     * <b> Get header realtime value and put into a hashmap. </b>
     * </p>
     *
     * @param request
     * @param handler
     * @param checklist
     * @return
     */
    private Map<String, String> getHeaderValueMap(final HttpServletRequest request, final Object handler,
        final List<String> checklist) {

        HandlerMethod handlerMethod = null;
        if (handler instanceof HandlerMethod) {
            handlerMethod = (HandlerMethod) handler;
        } else {
            return null;
        }
        MethodParameter[] parameterArray = handlerMethod.getMethodParameters();
        if (parameterArray == null || parameterArray.length == 0) {
            return null;
        }

        Map<String, String> headerMap = new HashMap<String, String>();
        for (MethodParameter mp : parameterArray) {
            Class<?> paraterTypeClass = mp.getParameterType();
            if (CommonRequestHeader.class.getSimpleName().equals(paraterTypeClass.getSimpleName())) {
                invokeHeaderValueToMap(request, paraterTypeClass, checklist, headerMap);
            }
        }
        return headerMap;
    }

    /**
     * <p>
     * <b> Invoke header value to a hashmap. </b>
     * </p>
     *
     * @param request
     * @param paraterTypeClass
     * @param checklist
     * @param headerMap
     */
    private void invokeHeaderValueToMap(final HttpServletRequest request, final Class<?> paraterTypeClass,
        final List<String> checklist, final Map<String, String> headerMap) {
        if (paraterTypeClass == null || paraterTypeClass.getDeclaredFields() == null) {
            return;
        }
        for (Field field : paraterTypeClass.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            if (checklist.contains(fieldName)) {
                if (field.isAnnotationPresent(Property.class)) {
                    Property property = field.getAnnotation(Property.class);
                    String headerPropertyName = property.value();
                    String headerValue = request.getHeader(headerPropertyName);
                    headerMap.put(fieldName, headerValue);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.
     * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.Object, org.springframework.web.servlet.ModelAndView)
     */
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final ModelAndView modelAndView) throws Exception {

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
     * .servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object,
     * java.lang.Exception)
     */
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final Exception ex) throws Exception {

    }
}
