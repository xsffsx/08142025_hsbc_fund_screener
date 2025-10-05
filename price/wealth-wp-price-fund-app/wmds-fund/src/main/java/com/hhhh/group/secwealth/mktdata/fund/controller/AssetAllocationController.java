
package com.hhhh.group.secwealth.mktdata.fund.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import com.hhhh.group.secwealth.mktdata.common.exception.CommonException;
import com.hhhh.group.secwealth.mktdata.common.service.RestfulService;
import com.hhhh.group.secwealth.mktdata.common.system.constants.CommonConstants;
import com.hhhh.group.secwealth.mktdata.common.system.constants.ErrTypeConstants;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;
import com.hhhh.group.secwealth.mktdata.common.util.StringUtil;

import net.sf.json.JSONObject;

//  merge to HoldingAllocation
// @RestController
// @RequestMapping(value = "${app.route}")
public class AssetAllocationController {

    private final String METHOD = "ASSETALLOCATION";

    // @Autowired
    // @Qualifier("restfulService")
    private RestfulService restfulService;

    // @RequestMapping(value = "/fund/holding/groups", method =
    // RequestMethod.GET)
    public ResponseEntity<?> get(@RequestParam(name = "param", required = true) final String param,
        final HttpServletRequest request) throws Exception {
        LogUtil.infoInboundMsg(AssetAllocationController.class, "RequestMethod: GET",
            request.getRequestURI() + CommonConstants.SYMBOL_INTERROGATION + request.getQueryString());
        JSONObject json = null;
        try {
            json = StringUtil.isValid(param) ? JSONObject.fromObject(param) : new JSONObject();
        } catch (Exception e) {
            LogUtil.error(AssetAllocationController.class,
                "CommonException: AssetAllocationController, get method, JSONObject.fromObject error json: " + json, e);
            throw new CommonException(ErrTypeConstants.INPUT_PARAMETER_INVALID);
        }
        return this.restfulService.all(this.METHOD, json, request);
    }
}