package com.dummy.wpb.product.mapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
@ConfigurationProperties(prefix = "document")
public class DocumentTypeMapper {

    private Map<String, String> type;

    public void setType(Map<String, String> docTypeMap) {
        this.type = docTypeMap;
    }

    public String getDocType(String docTypeKey) {
        return type.getOrDefault(docTypeKey, null);
    }

}
