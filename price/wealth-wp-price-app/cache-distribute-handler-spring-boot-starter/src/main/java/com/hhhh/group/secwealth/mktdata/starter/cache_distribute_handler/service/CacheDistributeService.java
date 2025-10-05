/*
 */
package com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.service;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import com.hhhh.group.secwealth.mktdata.starter.cache_distribute_handler.bean.CacheDistributeResponse;

import lombok.Setter;

/**
 * <p>
 * <b> Asynchronous access the Cache Distribute. </b>
 * </p>
 */
public class CacheDistributeService {

	@Setter
	private RestTemplate restTemplate;

//	@Setter
//	private String uri;

	@Setter
	private String awsUri;

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheDistributeService.class);

	@Async("cacheDistributeExecutor")
	public CompletableFuture<CacheDistributeResponse> retrieveCacheRecord(final HttpServletRequest request) {
		String userId = request.getHeader(CacheDistributeEnum.X_hhhh_USER_ID.getValue());
		String correlationId = request.getHeader(CacheDistributeEnum.X_hhhh_SESSION_CORRELATION_ID.getValue());
		String saml = request.getHeader(CacheDistributeEnum.X_hhhh_SAML.getValue());
		String saml3 = request.getHeader(CacheDistributeEnum.X_hhhh_SAML3.getValue());
		String jwtToken = request.getHeader(CacheDistributeEnum.X_hhhh_E2E_TRUST_TOKEN.getValue());
		String systemId = request.getHeader(CacheDistributeEnum.X_hhhh_SOURCE_SYSTEM_ID.getValue());
		String appCode = request.getHeader(CacheDistributeEnum.X_hhhh_APP_CODE.getValue());
		if (StringUtils.isAnyEmpty(userId, correlationId) || StringUtils.isAllEmpty(saml, saml3, jwtToken)) {
			String trace = String.join(System.lineSeparator(),
					CacheDistributeEnum.X_hhhh_USER_ID.getValue() + "[" + userId + "]",
					CacheDistributeEnum.X_hhhh_SESSION_CORRELATION_ID.getValue() + "[" + correlationId + "]",
					CacheDistributeEnum.X_hhhh_SAML.getValue() + "[" + saml + "]",
					CacheDistributeEnum.X_hhhh_SAML3.getValue() + "[" + saml3 + "]",
					CacheDistributeEnum.X_hhhh_E2E_TRUST_TOKEN.getValue() + "[" + jwtToken + "]");
			CacheDistributeService.LOGGER.error(trace);
			String message = CacheDistributeEnum.X_hhhh_USER_ID.getValue() + " and "
					+ CacheDistributeEnum.X_hhhh_SESSION_CORRELATION_ID.getValue() + " are mandatory. "
					+ CacheDistributeEnum.X_hhhh_SAML.getValue() + " and " + CacheDistributeEnum.X_hhhh_SAML3.getValue()
					+ " and " + CacheDistributeEnum.X_hhhh_E2E_TRUST_TOKEN.getValue()
					+ " at least one exists.";
			throw new IllegalArgumentException(message);
		} else {
			if("PIB".equals(appCode)){
				if(("stb-9307263").equals(systemId)){
					correlationId = systemId+correlationId;
				}else{
					String message = CacheDistributeEnum.X_hhhh_SOURCE_SYSTEM_ID.getValue()+" income error. ";
					throw new IllegalArgumentException(message);
				}
			}
			String encrypted = DigestUtils.sha256Hex(userId + correlationId);
			String key = String.join("~~~", encrypted, correlationId, "marketData");

			String samlType, samlValue;
			if (StringUtils.isNotEmpty(saml)) {
				samlType = CacheDistributeEnum.X_hhhh_SAML.getValue();
				samlValue = saml;
			} else if (StringUtils.isNotEmpty(saml3)) {
				samlType = CacheDistributeEnum.X_hhhh_SAML3.getValue();
				samlValue = saml3;
			}else if (StringUtils.isNotEmpty(jwtToken)) {
				samlType = CacheDistributeEnum.X_hhhh_E2E_TRUST_TOKEN.getValue();
				samlValue = jwtToken;
			} else {
				throw new IllegalArgumentException("Unreachable");
			}

			return retrieve(samlType, samlValue, key);
		}
	}

	private CompletableFuture<CacheDistributeResponse> retrieve(final String samlType, final String samlValue,
			final String key) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(samlType, samlValue);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        // First check the pcf cache service
//        try {
//            result = this.restTemplate.exchange(this.uri + key, HttpMethod.GET,
//                    entity, CacheDistributeResponse.class);
//        } catch (Exception e) {
//            CacheDistributeService.LOGGER.info("error to get PFC cache service, error message:" + e.getMessage(), e);
//        }
        //Then check aws cache service
		ResponseEntity<CacheDistributeResponse> awsresult = this.restTemplate.exchange(this.awsUri + key, HttpMethod.GET,
                    entity, CacheDistributeResponse.class);

		return CompletableFuture.completedFuture(awsresult.getBody());

        //Priority returns the results of aws cache service
//        if (awsresult != null) {
//            if (awsresult.getBody().getCacheExistingFlag().equals("Y")) {
//            }
//			else {
//                if (result != null) {
//                    if (result.getBody().getCacheExistingFlag().equals("Y")) {
//                        return CompletableFuture.completedFuture(result.getBody());
//                    } else {
//                        return CompletableFuture.completedFuture(awsresult.getBody());
//                    }
//                } else {
//                    return CompletableFuture.completedFuture(awsresult.getBody());
//                }
//            }
//        } else {
//            return CompletableFuture.completedFuture(result.getBody());
//        }
    }

}
