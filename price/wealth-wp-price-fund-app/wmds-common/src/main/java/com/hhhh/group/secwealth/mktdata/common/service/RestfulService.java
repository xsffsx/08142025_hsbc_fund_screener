/*
 */
package com.hhhh.group.secwealth.mktdata.common.service;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.http.ResponseEntity;


public interface RestfulService {

    /**
     * @param method
     * @param json
     * @param request
     * @return
     * @throws Exception
     */
    public ResponseEntity<?> all(String method, JSONObject json, HttpServletRequest request) throws Exception;

}
