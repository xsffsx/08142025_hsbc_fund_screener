/*
 */
package com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hhhh.group.secwealth.mktdata.starter.base.args.ArgsHolder;
import com.hhhh.group.secwealth.mktdata.starter.global_arguments_handler.constant.Constant;

import lombok.Setter;

public class GlobalArgumentsInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(GlobalArgumentsInterceptor.class);

    @Setter
    private String argumentsPatterns;

    @Setter
    private String defaultExCode;

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final Exception ex) throws Exception {
        ArgsHolder.removeArgs();
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
        final ModelAndView modelAndView) throws Exception {
        // intentionally-blank override
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
        throws Exception {
        if (!StringUtils.isEmpty(this.argumentsPatterns)) {
            final String[] parts = this.argumentsPatterns.split(Constant.SYMBOL_COMMA);
            for (final String part : parts) {
                final String[] arguments = part.split(Constant.SYMBOL_DELIMITER);
                // if not simple argument
                if (arguments.length > 3) {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < arguments.length - 1; i++) {
                        final String argument = arguments[i];
                        if (argument.startsWith(Constant.SYMBOL_LEFT_ANGLE_BRACKET)
                            && argument.endsWith(Constant.SYMBOL_RIGHT_ANGLE_BRACKET)) {
                            sb.append(argument.replace(Constant.SYMBOL_LEFT_ANGLE_BRACKET, Constant.EMPTY)
                                .replace(Constant.SYMBOL_RIGHT_ANGLE_BRACKET, Constant.EMPTY));
                        } else {
                            sb.append(getValue(request, argument, arguments[arguments.length - 1]));
                        }
                    }
                    ArgsHolder.putArgs(arguments[0], sb.toString());
                } else {
                    ArgsHolder.putArgs(arguments[0], getValue(request, arguments[1], arguments[2]));
                }
            }
        } else {
            GlobalArgumentsInterceptor.logger
                .error("Please check your configuration, \"global.arguments.handler.patterns\" should not be empty");
            throw new Exception(this.defaultExCode);
        }
        return true;
    }

    private Object getValue(final HttpServletRequest request, final String argument, final String argumentType) {
        Object result;
        switch (argumentType) {
        case Constant.ARGUMENTS_TYPE_HEADER:
            result = request.getHeader(argument);
            break;
        case Constant.ARGUMENTS_TYPE_PARAMETER:
            result = request.getParameter(argument);
            break;
        case Constant.ARGUMENTS_TYPE_ATTRIBUTE:
            result = request.getAttribute(argument);
            break;
        default:
            result = Constant.EMPTY;
        }
        return result;
    }

}
