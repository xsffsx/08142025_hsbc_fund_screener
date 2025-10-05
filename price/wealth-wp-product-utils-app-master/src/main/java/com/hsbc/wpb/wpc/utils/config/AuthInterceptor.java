package com.dummy.wpb.wpc.utils.config;

import lombok.NonNull;
import com.dummy.wpb.wpc.utils.AdminControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");

        log.info("pre authenticate {}...", request.getRequestURI());
        String secretKey = request.getHeader("x-dummy-secret-key");
        if ("/api/health".equals(request.getRequestURI())) {
            return true;
        } else if (null != secretKey && AdminControl.validateSecretKey(secretKey)) {
            return true;
        } else {
            log.warn("fail to check auth header");
            PrintWriter out = response.getWriter();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print("Secret key is invalid");
            out.flush();
            return false;
        }
    }
}
