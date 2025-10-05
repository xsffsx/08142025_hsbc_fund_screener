package com.dummy.wpb.product.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "termsheet")
@Data
public class TermsheetConfiguration {
    
    private String sidUrl;
    
    private Map<String, String> docTypeMap;
    
    @Value("${termsheet.transfer.local.path}")
    private String localPath;

    @Value("${termsheet.transfer.remote-s3.region}")
    private String s3region;

    @Value("${termsheet.transfer.remote-s3.url}")
    private String s3url;

    @Value("${termsheet.transfer.remote-s3.bucket}")
    private String s3bucket;

    @Value("${termsheet.transfer.remote-s3.path}")
    private String s3path;
}
