/*
 */
package com.hhhh.group.secwealth.mktdata.starter.http_message_logger.interceptor;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhhh.group.secwealth.mktdata.starter.http_message_logger.constant.Constant;

import lombok.Setter;

@ControllerAdvice
public class HttpResponseMessageAdvice implements ResponseBodyAdvice<Object> {

	private static final Logger logger = LoggerFactory.getLogger(HttpResponseMessageAdvice.class);

	@Setter
	private ObjectMapper objectMapper;

	@Override
	public Object beforeBodyWrite(final Object body, final MethodParameter returnType,
			final MediaType selectedContentType, final Class<? extends HttpMessageConverter<?>> selectedConverterType,
			final ServerHttpRequest request, final ServerHttpResponse response) {
		try {
			String resultLog = this.objectMapper.writeValueAsString(body);
			if (resultLog.length() > 5000) {
				int size = resultLog.length() / 5000;
				if (resultLog.length() % 5000 != 0) {
					size += 1;
				}
				for (int i = 0; i < size; i++) {
					HttpResponseMessageAdvice.logger.info("part " + (i + 1) + " " + Constant.OUTBOUND_MSG_PREFIX
							+ substring(resultLog, 5000 * i, 5000 * (i + 1)));
				}
			} else {
				HttpResponseMessageAdvice.logger.info(Constant.OUTBOUND_MSG_PREFIX + resultLog);
			}
			for (final Entry<String, List<String>> headers : response.getHeaders().entrySet()) {
				final String key = headers.getKey();
				final List<String> value = headers.getValue();
				HttpResponseMessageAdvice.logger.info(key + Constant.SYMBOL_COLON + value);
			}
		} catch (Exception e) {
			HttpResponseMessageAdvice.logger.error("Record Outbound Message encounter error", e);
		}
		return body;
	}

	@Override
	public boolean supports(final MethodParameter returnType,
			final Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	public static String substring(String str, int f, int t) {
		if (f > str.length()) {
			return null;
		}
		if (t > str.length()) {
			return str.substring(f, str.length());
		} else {
			return str.substring(f, t);
		}
	}

}
