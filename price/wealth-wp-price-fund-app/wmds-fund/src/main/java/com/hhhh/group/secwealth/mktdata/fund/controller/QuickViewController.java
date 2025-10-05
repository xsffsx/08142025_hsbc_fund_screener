
package com.hhhh.group.secwealth.mktdata.fund.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
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

import net.sf.json.JSONObject;

@RestController
@RequestMapping(value = "${app.route}")
public class QuickViewController {

    private final String METHOD = "FUNDQUICKVIEW";

    @Autowired
    @Qualifier("restfulService")
    private RestfulService restfulService;

    @RequestMapping(value = "/fund/search/category", method = RequestMethod.GET)
    public ResponseEntity<?> get(@RequestParam(name = "param", required = true) final String param,
        final HttpServletRequest request) throws Exception {
        LogUtil.infoInboundMsg(QuickViewController.class, "RequestMethod: GET",
            request.getRequestURI() + CommonConstants.SYMBOL_INTERROGATION + request.getQueryString());
        JSONObject json = null;
        try {
            json = StringUtil.isValid(param) ? JSONObject.fromObject(param) : new JSONObject();
        } catch (Exception e) {
            LogUtil.error(QuickViewController.class,
                "CommonException: QuickViewController, get method, JSONObject.fromObject error json: " + json, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return this.restfulService.all(this.METHOD, json, request);
    }
}