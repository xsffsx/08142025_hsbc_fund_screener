package com.dummy.wpb.wpc.utils.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dummy.wpb.wpc.utils.CommonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class HealthCheckService {

    public static final String CHECK_LIST = "checkList";
    public static final String CONNECTION_LIST = "connectionList";
    public static final String SERVICE_ID = "serviceId";
    public static final String RESPONSE = "response";
    public static final String REQUEST = "request";
    public static final String URL = "url";
    public static final String REQUEST_TEMPLATE_FILE = "requestTemplateFile";
    public static final String STATUS = "status";
    public static final String ERROR_CODE = "errorCode";
    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String DATA_TYPE = "dataType";
    @Value("${health-check.folder}")
    private String folder;

    @Autowired
    private Map<String, RestTemplate> restTemplateMap;

    public Document healthCheckScopes() {
        Document cfg = readConfig();
        Document output = new Document();
        output.put(CHECK_LIST, cfg.get(CHECK_LIST));
        output.put(CONNECTION_LIST, cfg.get(CONNECTION_LIST));
        return output;
    }

    @SneakyThrows
    public Document healthCheck(String serviceId, Map<String, String> parameters, boolean needWriteLog) {
        Document serviceConfig = readServiceConfig(serviceId);
        String fileContent = readTemplate(serviceConfig, parameters);
        String method = serviceConfig.getString("method");
        String url = serviceConfig.getString(URL);
        RestTemplate restTemplate = restTemplateMap.get(serviceConfig.get(SERVICE_ID) + "-restTemplate");
        Document output = new Document();
        output.put(SERVICE_ID, serviceConfig.get(SERVICE_ID));
        output.put(URL, url);
        ResponseEntity<String> responseEntity;
        try {
            if ("GET".equals(method)) {
                Map<String, String> queryParam = parameters;
                if (StringUtils.isNotBlank(fileContent)) {
                    queryParam = new LinkedHashMap<>();
                    JsonNode jsonNode = new ObjectMapper().readTree(fileContent);
                    for (Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields(); it.hasNext(); ) {
                        Map.Entry<String, JsonNode> entry = it.next();
                        queryParam.put(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
                output.put(REQUEST, queryParam);
                responseEntity = restTemplate.getForEntity(url, String.class, queryParam);
            } else {
                Object body = parameters;
                if (StringUtils.isNotBlank(fileContent)) {
                    body = fileContent;
                }
                HttpEntity<Object> requestEntity = new HttpEntity<>(body, getHttpHeaders(serviceConfig));
                output.put(REQUEST, body);
                responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);
            }
            if (responseEntity.getStatusCodeValue() == 200) {
                output.put(RESPONSE, responseEntity.getBody());
                output.put(STATUS, UP);
            } else {
                output.put(RESPONSE, responseEntity.getBody());
                output.put(STATUS, DOWN);
                output.put(ERROR_CODE, serviceConfig.get(ERROR_CODE));
            }
        } catch (HttpStatusCodeException e) {
            log.error("HttpStatusCodeException occur when health check:", e);
            output.put(RESPONSE, e.getResponseBodyAsString());
            output.put(STATUS, DOWN);
            output.put(ERROR_CODE, serviceConfig.get(ERROR_CODE));
        } catch (Exception e) {
            log.error("Unknown exception occur when health check:", e);
            output.put(RESPONSE, e.getMessage());
            output.put(STATUS, DOWN);
            output.put(ERROR_CODE, serviceConfig.get(ERROR_CODE));
        }
        writeLog(needWriteLog, output);
        return output;
    }

    public List<Document> healthCheckAll() {
        List<Document> checkResults = new ArrayList<>();
        Document cfg = readConfig();
        List<Map<String, ?>> checkList = (List<Map<String, ?>>) cfg.get(CHECK_LIST);
        for (Map<String, ?> checkMap : checkList) {
            String service = (String) checkMap.get(SERVICE_ID);
            Map<String, String> parameters = new LinkedHashMap<>();
            List<Map<String, String>> paramList = (List<Map<String, String>>) checkMap.get("parameters");
            if (paramList != null) {
                paramList.forEach(param -> parameters.put(param.get("key"), param.get("value")));
            }
            Document output = healthCheck(service, parameters, true);
            checkResults.add(output);
        }
        return checkResults;
    }

    private Document readConfig() {
        String cfgJson = CommonUtils.readResource("/healthCheck/" + folder + "/configuration.json");
        return Document.parse(cfgJson);
    }

    private Document readServiceConfig(String service) {
        Document cfg = readConfig();
        List<Map<String, Object>> checkList = (List<Map<String, Object>>) cfg.get(CHECK_LIST);
        Document checkDoc;
        Optional<Map<String, Object>> optional = checkList.stream().filter(e -> e.get(SERVICE_ID).equals(service)).findFirst();
        if (optional.isPresent()) {
            Map<String, Object> checkMap = optional.get();
            checkDoc = new Document(checkMap);
        } else {
            throw new IllegalArgumentException("servie " + service + " can not be found.");
        }
        return checkDoc;
    }

    private static HttpHeaders getHttpHeaders(Document serviceConfig) {
        HttpHeaders headers = new HttpHeaders();
        if ("json".equals(serviceConfig.get(DATA_TYPE))) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        } else if ("xml".equals(serviceConfig.get(DATA_TYPE))) {
            headers.setContentType(MediaType.APPLICATION_XML);
        }
        return headers;
    }

    private String readTemplate(Document serviceConfig, Map<String, String> parameters) {
        String requestTemplateFile = serviceConfig.getString(REQUEST_TEMPLATE_FILE);
        if (StringUtils.isNotBlank(requestTemplateFile)) {
            String content = CommonUtils.readResource("/healthCheck/" + folder + "/" + requestTemplateFile);
            if (parameters != null) {
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    content = content.replace("${" + entry.getKey() + "}", entry.getValue());
                }
            }
            return content;
        }
        return "";
    }

    private void writeLog(boolean needWriteLog, Document output) {
        if (needWriteLog) {
            if (DOWN.equals(output.getString(STATUS))) {
                log.error("{}: Health check error, Service: {} is down, request is: {}, response is: {}.",
                        output.get(ERROR_CODE),
                        output.get(SERVICE_ID),
                        output.get(REQUEST),
                        output.get(RESPONSE));
            } else {
                log.info("Health check success, Service: {} is up.", output.get(SERVICE_ID));
            }
        }
    }

}
