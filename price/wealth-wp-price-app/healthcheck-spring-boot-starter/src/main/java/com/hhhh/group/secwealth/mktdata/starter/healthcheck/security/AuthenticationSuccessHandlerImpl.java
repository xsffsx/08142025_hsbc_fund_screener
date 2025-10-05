/*
 */
package com.hhhh.group.secwealth.mktdata.starter.healthcheck.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.hhhh.group.secwealth.mktdata.starter.healthcheck.constant.Constant;

public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
        final Authentication authentication) throws IOException, ServletException {
        request.getRequestDispatcher(Constant.URL_DASHBOARD).forward(request, response);
    }

}
