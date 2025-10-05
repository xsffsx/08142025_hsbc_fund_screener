package com.dummy.wpb.product.jobs.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


@Slf4j
@Component
public class ProductRegistrationUrlHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${smart.globalIdUrl}")
    private String getGlobalIdUrl;
    @Value("${smart.tokenUrl}")
    private String getAmTokenUrl;

    @Value("${smart.password}")
    private String password;

    @Value("${smart.username}")
    private String username;

    @Value("${smart.tokenId:}")
    private String tokenId;

    public Map<String, Object> getGlobalId(List<Map<String, Object>> requestBody) {
        try {
            if(StringUtils.isBlank(tokenId)) {
                getAmToken();
            }
            HttpHeaders header = new HttpHeaders();
            header.add("X-dummy-E2E-Trust-Token", tokenId);
            header.add("Content-Type", "application/json");

            HttpEntity<List<Map<String, Object>>> entity = new HttpEntity<>(requestBody, header);
            log.info("start call smart API to get globalId,request prod count:{},request header:{}", requestBody.size(),
                    objectMapper.writeValueAsString(header));
            ResponseEntity<Map<String,Object>> response = restTemplate.exchange(getGlobalIdUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
            return response.getBody();
        } catch (Exception e) {
            if (e instanceof HttpClientErrorException
                    && (StringUtils.contains(e.getMessage(), "Token Validation Exception") || StringUtils.contains(e.getMessage(), "Token has expired"))) {
                getAmToken();
                return getGlobalId(requestBody);
            }else {
                log.error("Call smart API error.Error message: {}", e.getMessage(), e);
                return Collections.emptyMap();
            }
        }
    }

    private void getAmToken() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String,  Object> inputTokenState = new HashMap<>();
        inputTokenState.put("token_type", "CREDENTIAL");
        inputTokenState.put("username", username);
        inputTokenState.put("password", password);

        Map<String,  Object> outputTokenState = new HashMap<>();
        outputTokenState.put("token_type","JWT");

        Map<String, Object> tokenBody = new HashMap<>();
        tokenBody.put("input_token_state",inputTokenState);
        tokenBody.put("output_token_state",outputTokenState);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(tokenBody, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(getAmTokenUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String,  Object> body = response.getBody();
        tokenId = body != null ? body.get("issued_token").toString() : null;

        log.info("amstokenId:[{}]", tokenId);
    }
}
