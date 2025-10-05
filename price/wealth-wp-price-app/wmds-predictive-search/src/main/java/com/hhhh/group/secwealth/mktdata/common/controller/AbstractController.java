package com.hhhh.group.secwealth.mktdata.common.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;

import com.hhhh.group.secwealth.mktdata.common.exception.SystemException;
import com.hhhh.group.secwealth.mktdata.common.util.LogUtil;

/**
 * BaseController
 */
public abstract class AbstractController {

    /**
     * print request parameters
     * 
     * @param request
     */
    protected void printRequestParameters(final HttpServletRequest request) {
        LogUtil.debug(AbstractController.class, "------------------ request ------------------");
        Enumeration<String> params = request.getParameterNames();
        String name;
        while (params.hasMoreElements()) {
            name = params.nextElement();
            LogUtil.debug(AbstractController.class, "{}-->{}", name, request.getParameter(name));
        }
        LogUtil.debug(AbstractController.class, "------------------ request ------------------");
    }

    protected IForm getFormWrapper(final HttpServletRequest req) {
        return new FormImpl(req.getParameterMap());
    }

    public <T> T mapping(final HttpServletRequest req, final Class<T> clazz) throws Exception {
        try {
            T obj = clazz.newInstance();
            BeanUtils.populate(obj, req.getParameterMap());
            return obj;
        } catch (Exception e) {
            LogUtil.error(AbstractController.class, "SystemException: AbstractController, mapping, Param: " + clazz.toString(), e);
            throw new SystemException(e);
        }
    }
}