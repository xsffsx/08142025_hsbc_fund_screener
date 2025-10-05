package com.hhhh.group.secwealth.mktdata.common.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.RestfulService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

@RestController
public class RestfulController extends AbstractController {

    @Autowired
    @Qualifier("restfulService")
    private RestfulService restfulService;

    @RequestMapping(value = "wmds/{method}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable(value = "method", required = true) final String method,
        @RequestParam(name = "body", required = true) final String body, final HttpServletRequest request) throws Exception {
        LogUtil.infoInboundMsg(RestfulController.class, "RequestMethod: GET", request.getRequestURI()
            + CommonConstants.SYMBOL_INTERROGATION + request.getQueryString());
        JSONObject json = null;
        try {
            json = StringUtil.isValid(body) ? JSONObject.fromObject(body) : new JSONObject();
        } catch (Exception e) {
            LogUtil.error(RestfulController.class,
                "CommonException: RestfulController, get method, JSONObject.fromObject error json: " + json, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return this.restfulService.all(method, json, request);
    }
}
