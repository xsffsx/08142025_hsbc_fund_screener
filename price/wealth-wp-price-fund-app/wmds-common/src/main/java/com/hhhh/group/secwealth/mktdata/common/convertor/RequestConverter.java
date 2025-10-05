/*
 */
package com.hhhh.group.secwealth.mktdata.common.convertor;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.hhhh.group.secwealth.mktdata.common.svc.request.Request;
import com.hhhh.group.secwealth.mktdata.common.util.JacksonUtil;

/**
 * <p>
 * <b>  Insert description of the class's responsibility/role. </b>
 * </p>
 */
@Component("requestConverter")
public class RequestConverter {

    @SuppressWarnings("unchecked")
    public Request converterToRequest(final Map<String, String> headerMap, final String json, final String requestClassName)
        throws Exception {
        Class<Request> clz = (Class<Request>) Class.forName(requestClassName);
        Request request = JacksonUtil.jsonToBean(json.toString(), clz);
        request.putAllHeaders(headerMap);
        return request;
    }
}
