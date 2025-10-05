package com.hhhh.group.secwealth.mktdata.common.controller;

import com.google.common.io.Resources;
import com.hhhh.group.secwealth.mktdata.common.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.SwaggerResource;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("swaggerController")
@RequestMapping(value = "/swagger/hase-mds", method = RequestMethod.GET)
@ConditionalOnProperty(name = "swagger.disabled", havingValue = "false", matchIfMissing = true)
public class SwaggerController {

    @GetMapping("/swagger-resources")
    public String resources() throws Exception {
        SwaggerResource usResource = new SwaggerResource();
        usResource.setName("swagger-us");
        usResource.setSwaggerVersion("2.0");
        usResource.setLocation("/swagger-us.yaml");

        SwaggerResource hkResource = new SwaggerResource();
        hkResource.setName("swagger-hk");
        hkResource.setSwaggerVersion("2.0");
        hkResource.setLocation("/swagger-hk.yaml");

        SwaggerResource cnResource = new SwaggerResource();
        cnResource.setName("swagger-cn");
        cnResource.setSwaggerVersion("2.0");
        cnResource.setLocation("/swagger-cn.yaml");

        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(usResource);
        resources.add(hkResource);
        resources.add(cnResource);
        return JacksonUtil.beanToJson(resources);
    }

    @GetMapping("/swagger-resources/configuration/security")
    public String configurationSecurity() {
        return "{}";
    }

    @GetMapping("/swagger-resources/configuration/ui")
    public String configurationUi() {
        return "{\"deepLinking\":true,\"displayOperationId\":false,\"defaultModelsExpandDepth\":1,\"defaultModelExpandDepth\":1,\"defaultModelRendering\":\"example\",\"displayRequestDuration\":false,\"docExpansion\":\"none\",\"filter\":false,\"operationsSorter\":\"alpha\",\"showExtensions\":false,\"tagsSorter\":\"alpha\",\"validatorUrl\":\"\",\"apisSorter\":\"alpha\",\"jsonEditor\":false,\"showRequestHeaders\":false,\"supportedSubmitMethods\":[\"get\",\"put\",\"post\",\"delete\",\"options\",\"head\",\"patch\",\"trace\"]}";
    }

    @GetMapping("/swagger-us.yaml")
    public String swaggerUS() {
        try {
            URL url = Resources.getResource("swagger-hk.yaml");
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.warn("load swagger file error", e);
            return "";
        }
    }

    @GetMapping("/swagger-hk.yaml")
    public String swaggerHK() {
        try {
            URL url = Resources.getResource("swagger-hk.yaml");
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.warn("load swagger file error", e);
            return "";
        }
    }

    @GetMapping("/swagger-cn.yaml")
    public String swaggerCN() {
        try {
            URL url = Resources.getResource("swagger-cn.yaml");
            return Resources.toString(url, StandardCharsets.UTF_8);
        } catch (Exception e){
            log.warn("load swagger file error", e);
            return "";
        }
    }

}
